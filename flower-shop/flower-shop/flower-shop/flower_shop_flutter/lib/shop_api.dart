import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'auth.dart';

class ShopException implements Exception {
  const ShopException(this.message);
  final String message;
}

class Product {
  const Product({
    required this.id,
    required this.name,
    required this.price,
    this.description = '',
    this.imageUrl = '',
    this.occasion = '',
  });
  final String id, name, description, imageUrl, occasion;
  final double price;
  factory Product.fromJson(Map<String, dynamic> j) => Product(
    id: '${j['id'] ?? j['bouquetId'] ?? ''}',
    name: '${j['name'] ?? j['bouquetName'] ?? ''}',
    price: double.parse('${j['price'] ?? 0}'),
    description: '${j['description'] ?? ''}',
    imageUrl: '${j['imageUrl'] ?? ''}',
    occasion: '${j['occasionName'] ?? ''}',
  );
}

class CartLine {
  const CartLine(this.product, this.quantity);
  final Product product;
  final int quantity;
  factory CartLine.fromJson(Map<String, dynamic> j) =>
      CartLine(Product.fromJson(j), int.parse('${j['quantity']}'));
  double get total => product.price * quantity;
}

class ProductPage {
  const ProductPage(this.items, this.totalPages, this.total);
  final List<Product> items;
  final int totalPages, total;
}

class Occasion {
  const Occasion(this.id, this.name);
  final String id, name;
}

class ShopApi {
  ShopApi(this.auth, {http.Client? client}) : _client = client ?? http.Client();
  final AuthService auth;
  final http.Client _client;
  Future<dynamic> _request(
    String path, {
    Map<String, String>? query,
    Map<String, String>? body,
  }) async {
    if (!auth.isLoggedIn) {
      throw const ShopException('Vui lòng đăng nhập để tiếp tục.');
    }
    final token = auth.accessToken;
    try {
      final uri = Uri.parse(
        '${auth.apiBaseUrl}/api/v1/$path',
      ).replace(queryParameters: query);
      final headers = {'Authorization': auth.authorization!};
      final response =
          await (body == null
                  ? _client.get(uri, headers: headers)
                  : _client.post(uri, headers: headers, body: body))
              .timeout(const Duration(seconds: 15));
      if (token != auth.accessToken) {
        throw const ShopException('Tài khoản đã thay đổi. Vui lòng tải lại.');
      }
      if (response.statusCode == 401 || response.statusCode == 403) {
        auth.logout();
        throw const ShopException(
          'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.',
        );
      }
      if (response.statusCode < 200 || response.statusCode >= 300) {
        throw const ShopException('Không thể tải dữ liệu. Vui lòng thử lại.');
      }
      final json = jsonDecode(utf8.decode(response.bodyBytes));
      if (json is! Map || json['code'] != 20000) {
        throw ShopException(
          json is Map
              ? '${json['message'] ?? 'Yêu cầu không thành công.'}'
              : 'Phản hồi không hợp lệ.',
        );
      }
      return json['data'];
    } on TimeoutException {
      throw const ShopException('Kết nối quá thời gian. Vui lòng thử lại.');
    } on http.ClientException {
      throw const ShopException(
        'Không thể kết nối máy chủ. Vui lòng kiểm tra mạng.',
      );
    } on FormatException {
      throw const ShopException('Dữ liệu máy chủ không hợp lệ.');
    }
  }

  Future<ProductPage> products({
    int page = 1,
    String name = '',
    String? occasion,
  }) async {
    final data = await _request(
      'bouquet',
      query: {
        'page': '$page',
        'size': '12',
        if (name.trim().isNotEmpty) 'name': name.trim(),
        if (occasion != null) 'occasionId': occasion,
      },
    );
    return ProductPage(
      (data['items'] as List)
          .map((e) => Product.fromJson(Map<String, dynamic>.from(e)))
          .toList(),
      (data['totalPages'] as num?)?.toInt() ?? 1,
      (data['total'] as num?)?.toInt() ?? 0,
    );
  }

  Future<List<Product>> allProducts() async {
    final first = await products();
    final items = [...first.items];
    for (int page = 2; page <= first.totalPages; page++) {
      items.addAll((await products(page: page)).items);
    }
    return items;
  }

  Future<Product> detail(String id) async => Product.fromJson(
    Map<String, dynamic>.from(await _request('bouquet/detail/$id')),
  );
  Future<List<Occasion>> occasions() async {
    final data = await _request('occasion/all');
    return (data['items'] as List)
        .map((e) => Occasion('${e['id']}', '${e['name']}'))
        .toList();
  }

  Future<List<CartLine>> cart() async {
    final data = await _request('cart');
    return (data['items'] as List)
        .map((e) => CartLine.fromJson(Map<String, dynamic>.from(e)))
        .toList();
  }

  Future<void> setQuantity(String id, int quantity) async {
    if (quantity < 0 || quantity > 999) {
      throw const ShopException('Số lượng phải từ 0 đến 999.');
    }
    await _request(
      quantity == 0 ? 'cart/delete' : 'cart/update',
      body: {'bouquetId': id, 'quantity': '$quantity'},
    );
  }

  String imageUrl(String source) {
    final uri = Uri.tryParse(source);
    if (uri == null || source.trim().isEmpty) return '';
    final base = Uri.parse(auth.apiBaseUrl);
    if (!uri.hasScheme) return base.resolve(source).toString();
    if (uri.host == 'localhost' || uri.host == '127.0.0.1') {
      return uri.replace(host: base.host).toString();
    }
    return source;
  }

  void dispose() => _client.close();
}

String money(num value) =>
    '${value.toStringAsFixed(0).replaceAllMapped(RegExp(r'(\d)(?=(\d{3})+(?!\d))'), (m) => '${m[1]}.')} ₫';
