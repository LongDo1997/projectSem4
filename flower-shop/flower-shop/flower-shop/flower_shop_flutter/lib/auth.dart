import 'dart:async';
import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class AuthException implements Exception {
  const AuthException(this.message);
  final String message;
}

class AuthService {
  AuthService({http.Client? client, String? baseUrl})
    : _client = client ?? http.Client(),
      baseUrl = baseUrl ?? const String.fromEnvironment('API_BASE_URL');
  final http.Client _client;
  final String baseUrl;
  String? accessToken;
  String? email;
  bool get isLoggedIn => accessToken != null;
  String? get authorization =>
      accessToken == null ? null : 'Bearer $accessToken';
  String get apiBaseUrl => baseUrl.isNotEmpty
      ? baseUrl.replaceFirst(RegExp(r'/$'), '')
      : !kIsWeb && defaultTargetPlatform == TargetPlatform.android
      ? 'http://10.0.2.2:9003'
      : 'http://localhost:9003';

  Future<http.Response> _post(String path, Map<String, String> fields) async {
    try {
      final response = await _client
          .post(Uri.parse('$apiBaseUrl/api/v1/auth/$path'), body: fields)
          .timeout(const Duration(seconds: 15));
      if (response.statusCode >= 200 && response.statusCode < 300) {
        return response;
      }
      if (response.statusCode == 401 || response.statusCode == 403) {
        throw const AuthException('Email hoặc mật khẩu không đúng.');
      }
      if (response.statusCode >= 500) {
        throw const AuthException('Máy chủ gặp lỗi. Vui lòng thử lại sau.');
      }
      String message = utf8
          .decode(response.bodyBytes, allowMalformed: true)
          .trim();
      try {
        final data = jsonDecode(message);
        message = data is Map
            ? (data['message'] ?? data['error'] ?? '').toString()
            : '';
      } on FormatException {
        if (message.startsWith('<')) message = '';
      }
      if (message.isEmpty) {
        message = response.statusCode == 409
            ? 'Email đã được đăng ký.'
            : 'Vui lòng kiểm tra lại thông tin.';
      }
      throw AuthException(message);
    } on TimeoutException {
      throw const AuthException('Kết nối quá thời gian. Vui lòng thử lại.');
    } on http.ClientException {
      throw const AuthException(
        'Không thể kết nối máy chủ. Vui lòng kiểm tra mạng và backend.',
      );
    }
  }

  Future<void> login(String email, String password) async {
    final normalized = email.trim().toLowerCase();
    final response = await _post('login', {
      'userName': normalized,
      'password': password,
    });
    try {
      final data = jsonDecode(utf8.decode(response.bodyBytes));
      final token = data is Map ? data['accessToken'] : null;
      if (token is! String || token.trim().isEmpty) {
        throw const FormatException();
      }
      accessToken = token;
      this.email = normalized;
    } on FormatException {
      throw const AuthException('Phản hồi đăng nhập không hợp lệ.');
    }
  }

  Future<void> register(Map<String, String> fields) async {
    await _post('register/customer', fields);
  }

  void logout() {
    accessToken = null;
    email = null;
  }

  void dispose() => _client.close();
}

class AuthScreen extends StatefulWidget {
  const AuthScreen({super.key, required this.auth});
  final AuthService auth;
  @override
  State<AuthScreen> createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> {
  final _form = GlobalKey<FormState>();
  final _fields = {
    for (final key in [
      'lastName',
      'firstName',
      'email',
      'password',
      'confirm',
      'phone',
      'address',
    ])
      key: TextEditingController(),
  };
  bool _register = false, _busy = false, _obscure = true;
  String? _error, _notice;
  @override
  void dispose() {
    for (final controller in _fields.values) {
      controller.dispose();
    }
    super.dispose();
  }

  String value(String key) => _fields[key]!.text;
  void _switchMode() {
    setState(() {
      _register = !_register;
      _error = null;
      _notice = null;
      _fields['password']!.clear();
      _fields['confirm']!.clear();
      _form.currentState?.reset();
    });
  }

  Future<void> _submit() async {
    if (_busy || !_form.currentState!.validate()) return;
    FocusScope.of(context).unfocus();
    setState(() {
      _busy = true;
      _error = null;
      _notice = null;
    });
    try {
      if (_register) {
        await widget.auth.register({
          'firstName': value('firstName').trim(),
          'lastName': value('lastName').trim(),
          'email': value('email').trim().toLowerCase(),
          'password': value('password'),
          if (value('phone').trim().isNotEmpty) 'phone': value('phone').trim(),
          if (value('address').trim().isNotEmpty)
            'address': value('address').trim(),
        });
        if (!mounted) return;
        setState(() {
          _register = false;
          _fields['password']!.clear();
          _fields['confirm']!.clear();
          _form.currentState?.reset();
          _notice = 'Đăng ký thành công! Bạn có thể đăng nhập ngay.';
        });
      } else {
        await widget.auth.login(value('email'), value('password'));
        if (!mounted) return;
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(const SnackBar(content: Text('Đăng nhập thành công.')));
        Navigator.of(context).pop();
      }
    } on AuthException catch (e) {
      if (mounted) setState(() => _error = e.message);
    } catch (_) {
      if (mounted) setState(() => _error = 'Có lỗi xảy ra. Vui lòng thử lại.');
    } finally {
      if (mounted) setState(() => _busy = false);
    }
  }

  Widget _field(String key, String label, {bool optional = false}) {
    final secret = key == 'password' || key == 'confirm';
    return Padding(
      padding: const EdgeInsets.only(bottom: 16),
      child: TextFormField(
        controller: _fields[key],
        enabled: !_busy,
        obscureText: secret && _obscure,
        autocorrect: !secret && key != 'email',
        enableSuggestions: !secret,
        keyboardType: key == 'email'
            ? TextInputType.emailAddress
            : key == 'phone'
            ? TextInputType.phone
            : TextInputType.text,
        autofillHints: key == 'email'
            ? [AutofillHints.username]
            : key == 'password'
            ? [_register ? AutofillHints.newPassword : AutofillHints.password]
            : null,
        textInputAction: TextInputAction.next,
        decoration: InputDecoration(
          labelText: label,
          suffixIcon: secret
              ? IconButton(
                  tooltip: _obscure ? 'Hiện mật khẩu' : 'Ẩn mật khẩu',
                  onPressed: () => setState(() => _obscure = !_obscure),
                  icon: Icon(
                    _obscure
                        ? Icons.visibility_outlined
                        : Icons.visibility_off_outlined,
                  ),
                )
              : null,
        ),
        validator: (input) {
          final text = input ?? '';
          if (!optional && text.trim().isEmpty) return 'Vui lòng nhập $label';
          if (key == 'email' &&
              !RegExp(r'^[^\s@]+@[^\s@]+\.[^\s@]+$').hasMatch(text.trim())) {
            return 'Email không hợp lệ.';
          }
          if (key == 'password' && _register && text.length < 8) {
            return 'Mật khẩu phải có ít nhất 8 ký tự.';
          }
          if (key == 'confirm' && text != value('password')) {
            return 'Mật khẩu xác nhận chưa khớp.';
          }
          if (key == 'phone' &&
              text.trim().isNotEmpty &&
              !RegExp(r'^\+?[0-9\s.-]{9,15}$').hasMatch(text.trim())) {
            return 'Số điện thoại không hợp lệ.';
          }
          return null;
        },
      ),
    );
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text(
        widget.auth.isLoggedIn
            ? 'Tài khoản'
            : _register
            ? 'Đăng ký'
            : 'Đăng nhập',
      ),
    ),
    body: SafeArea(
      child: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Center(
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 460),
            child: widget.auth.isLoggedIn
                ? Column(
                    children: [
                      const Icon(Icons.account_circle_outlined, size: 72),
                      const SizedBox(height: 16),
                      Text(
                        widget.auth.email ?? '',
                        style: Theme.of(context).textTheme.titleLarge,
                      ),
                      const SizedBox(height: 24),
                      FilledButton(
                        onPressed: () {
                          widget.auth.logout();
                          Navigator.of(context).pop();
                        },
                        child: const Text('Đăng xuất'),
                      ),
                    ],
                  )
                : AutofillGroup(
                    child: Form(
                      key: _form,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          const Icon(
                            Icons.local_florist_outlined,
                            size: 48,
                            color: Color(0xffb47882),
                          ),
                          const SizedBox(height: 16),
                          const Text(
                            'ROSEBUD',
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              fontFamily: 'Georgia',
                              fontSize: 30,
                              letterSpacing: 5,
                            ),
                          ),
                          const SizedBox(height: 12),
                          Text(
                            _register
                                ? 'Tạo tài khoản để gửi ngàn lời yêu thương.'
                                : 'Chào mừng bạn trở lại!',
                            textAlign: TextAlign.center,
                          ),
                          const SizedBox(height: 28),
                          if (_notice != null)
                            Padding(
                              padding: const EdgeInsets.only(bottom: 16),
                              child: Text(_notice!),
                            ),
                          if (_register) ...[
                            _field('lastName', 'Họ'),
                            _field('firstName', 'Tên'),
                          ],
                          _field('email', 'Email'),
                          _field('password', 'Mật khẩu'),
                          if (_register) ...[
                            _field('confirm', 'Xác nhận mật khẩu'),
                            _field(
                              'phone',
                              'Số điện thoại (tùy chọn)',
                              optional: true,
                            ),
                            _field(
                              'address',
                              'Địa chỉ (tùy chọn)',
                              optional: true,
                            ),
                          ],
                          if (_error != null)
                            Padding(
                              padding: const EdgeInsets.only(bottom: 16),
                              child: Text(
                                _error!,
                                style: TextStyle(
                                  color: Theme.of(context).colorScheme.error,
                                ),
                              ),
                            ),
                          FilledButton(
                            onPressed: _busy ? null : _submit,
                            child: _busy
                                ? const SizedBox(
                                    width: 22,
                                    height: 22,
                                    child: CircularProgressIndicator(
                                      strokeWidth: 2,
                                    ),
                                  )
                                : Text(_register ? 'ĐĂNG KÝ' : 'ĐĂNG NHẬP'),
                          ),
                          const SizedBox(height: 12),
                          TextButton(
                            onPressed: _busy ? null : _switchMode,
                            child: Text(
                              _register
                                  ? 'Đã có tài khoản? Đăng nhập'
                                  : 'Chưa có tài khoản? Đăng ký',
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
          ),
        ),
      ),
    ),
  );
}
