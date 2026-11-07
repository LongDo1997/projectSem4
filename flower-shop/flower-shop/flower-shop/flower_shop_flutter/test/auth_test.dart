import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';
import 'package:flower_shop_flutter/auth.dart';
import 'package:flower_shop_flutter/main.dart';

void main() {
  test(
    'Login uses form userName and keeps returned token until logout',
    () async {
      final auth = AuthService(
        baseUrl: 'http://localhost:9003',
        client: MockClient((request) async {
          expect(request.url.path, '/api/v1/auth/login');
          expect(
            request.headers['content-type'],
            contains('application/x-www-form-urlencoded'),
          );
          expect(Uri.splitQueryString(request.body), {
            'userName': 'customer@example.com',
            'password': 'abc&12345',
          });
          return http.Response(
            jsonEncode({'accessToken': 'jwt-token', 'tokenType': 'Bearer '}),
            200,
          );
        }),
      );
      addTearDown(auth.dispose);
      await auth.login(' Customer@Example.com ', 'abc&12345');
      expect(auth.authorization, 'Bearer jwt-token');
      auth.logout();
      expect(auth.isLoggedIn, isFalse);
      expect(auth.email, isNull);
    },
  );
  test(
    'Registration uses customer endpoint and surfaces duplicate email',
    () async {
      final auth = AuthService(
        client: MockClient((request) async {
          expect(request.url.path, '/api/v1/auth/register/customer');
          expect(Uri.splitQueryString(request.body)['firstName'], 'Lan');
          return http.Response(
            'Email đã được đăng ký.',
            409,
            headers: {'content-type': 'text/plain; charset=utf-8'},
          );
        }),
      );
      addTearDown(auth.dispose);
      await expectLater(
        auth.register({'firstName': 'Lan'}),
        throwsA(
          isA<AuthException>().having(
            (e) => e.message,
            'message',
            'Email đã được đăng ký.',
          ),
        ),
      );
    },
  );
  test('Invalid login response never creates a session', () async {
    final auth = AuthService(
      client: MockClient((_) async => http.Response('{}', 200)),
    );
    addTearDown(auth.dispose);
    await expectLater(
      auth.login('a@b.com', 'password'),
      throwsA(isA<AuthException>()),
    );
    expect(auth.isLoggedIn, isFalse);
  });
  testWidgets('Account button opens login and registration', (tester) async {
    await tester.pumpWidget(const FlowerApp());
    await tester.tap(find.byTooltip('Đăng nhập'));
    await tester.pumpAndSettle();
    expect(find.text('ĐĂNG NHẬP'), findsOneWidget);
    await tester.tap(find.text('ĐĂNG NHẬP'));
    await tester.pumpAndSettle();
    expect(find.text('Vui lòng nhập Email'), findsOneWidget);
    await tester.tap(find.text('Chưa có tài khoản? Đăng ký'));
    await tester.pumpAndSettle();
    expect(
      find.widgetWithText(TextFormField, 'Xác nhận mật khẩu'),
      findsOneWidget,
    );
  });
}
