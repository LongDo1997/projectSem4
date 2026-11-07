import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';
import 'package:flower_shop_flutter/auth.dart';
import 'package:flower_shop_flutter/shop.dart';
import 'package:flower_shop_flutter/shop_api.dart';

http.Response ok(dynamic data) => http.Response(
  jsonEncode({'code': 20000, 'data': data}),
  200,
  headers: {'content-type': 'application/json; charset=utf-8'},
);
const product = {
  'id': '7',
  'name': 'Hoa hồng',
  'price': '690000.00',
  'description': 'Hoa tươi màu hồng',
  'occasionName': 'Sinh nhật',
};
AuthService session() =>
    AuthService(baseUrl: 'http://localhost:9003')..accessToken = 'test-token';
void main() {
  test(
    'Catalog sends one-based paging, search, occasion and bearer token',
    () async {
      final auth = session();
      final api = ShopApi(
        auth,
        client: MockClient((r) async {
          expect(r.headers['Authorization'], 'Bearer test-token');
          expect(r.url.path, '/api/v1/bouquet');
          expect(r.url.queryParameters, {
            'page': '2',
            'size': '12',
            'name': 'hồng',
            'occasionId': '3',
          });
          return ok({
            'items': [product],
            'totalPages': 3,
            'total': 25,
          });
        }),
      );
      addTearDown(auth.dispose);
      addTearDown(api.dispose);
      final page = await api.products(page: 2, name: ' hồng ', occasion: '3');
      expect(page.items.single.price, 690000);
      expect(page.totalPages, 3);
      expect(page.total, 25);
      expect(money(page.items.single.price), '690.000 ₫');
    },
  );
  test(
    'Detail uses backend description and rejects application error codes',
    () async {
      final auth = session();
      final api = ShopApi(
        auth,
        client: MockClient(
          (r) async => r.url.path.endsWith('/7')
              ? ok(product)
              : http.Response(
                  '{"code":10000,"message":"Product is not exist"}',
                  200,
                ),
        ),
      );
      addTearDown(auth.dispose);
      addTearDown(api.dispose);
      expect((await api.detail('7')).description, 'Hoa tươi màu hồng');
      await expectLater(api.detail('999'), throwsA(isA<ShopException>()));
    },
  );
  test('Expired session is cleared and image host follows API host', () async {
    final auth = session();
    final api = ShopApi(
      auth,
      client: MockClient((_) async => http.Response('', 401)),
    );
    addTearDown(auth.dispose);
    addTearDown(api.dispose);
    await expectLater(api.cart(), throwsA(isA<ShopException>()));
    expect(auth.isLoggedIn, isFalse);
    final mobileAuth = AuthService(baseUrl: 'http://10.0.2.2:9003');
    final mobile = ShopApi(mobileAuth);
    addTearDown(mobileAuth.dispose);
    addTearDown(mobile.dispose);
    expect(
      mobile.imageUrl('http://localhost:5283/flowers/a.jpg'),
      'http://10.0.2.2:5283/flowers/a.jpg',
    );
  });
  testWidgets('Catalog to detail, additive cart, quantity update and delete', (
    tester,
  ) async {
    await tester.binding.setSurfaceSize(const Size(1100, 1000));
    addTearDown(() => tester.binding.setSurfaceSize(null));
    final auth = session();
    var quantity = 2;
    final updates = <int>[];
    final api = ShopApi(
      auth,
      client: MockClient((r) async {
        switch (r.url.path) {
          case '/api/v1/bouquet':
            return ok({
              'items': [product],
              'totalPages': 1,
              'total': 1,
            });
          case '/api/v1/bouquet/detail/7':
            return ok(product);
          case '/api/v1/occasion/all':
            return ok({
              'items': [
                {'id': '3', 'name': 'Sinh nhật'},
              ],
            });
          case '/api/v1/cart':
            return ok({
              'items': quantity == 0
                  ? []
                  : [
                      {
                        ...product,
                        'id': null,
                        'bouquetId': '7',
                        'name': null,
                        'bouquetName': 'Hoa hồng',
                        'quantity': '$quantity',
                      },
                    ],
            });
          case '/api/v1/cart/update':
          case '/api/v1/cart/delete':
            expect(r.method, 'POST');
            final fields = Uri.splitQueryString(r.body);
            expect(fields['bouquetId'], '7');
            quantity = int.parse(fields['quantity']!);
            updates.add(quantity);
            return ok({});
          default:
            throw StateError('Unexpected endpoint: ${r.url}');
        }
      }),
    );
    addTearDown(auth.dispose);
    addTearDown(api.dispose);
    await tester.pumpWidget(
      MaterialApp(
        home: Shop(auth: auth, api: api),
      ),
    );
    await tester.pumpAndSettle();
    await tester.tap(find.text('COLLECTION'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('HOA HỒNG'));
    await tester.pumpAndSettle();
    expect(find.text('Hoa tươi màu hồng'), findsNWidgets(2));
    await tester.tap(find.text('ADD TO CART'));
    await tester.pumpAndSettle();
    expect(quantity, 3);
    await tester.tap(find.textContaining('CART ('));
    await tester.pumpAndSettle();
    expect(find.text('2.070.000 ₫'), findsNWidgets(3));
    await tester.enterText(
      find.descendant(
        of: find.byKey(const ValueKey('cart-7')),
        matching: find.byType(TextField),
      ),
      '4',
    );
    await tester.testTextInput.receiveAction(TextInputAction.done);
    await tester.pumpAndSettle();
    expect(quantity, 4);
    await tester.enterText(
      find.descendant(
        of: find.byKey(const ValueKey('cart-7')),
        matching: find.byType(TextField),
      ),
      '3',
    );
    await tester.testTextInput.receiveAction(TextInputAction.done);
    await tester.pumpAndSettle();
    expect(quantity, 3);
    await tester.ensureVisible(find.text('PROCEED TO CHECKOUT →'));
    await tester.tap(find.text('PROCEED TO CHECKOUT →'));
    await tester.pumpAndSettle();
    expect(find.text('Delivery details'), findsOneWidget);
    await tester.ensureVisible(find.text('PLACE DEMO ORDER'));
    await tester.tap(find.text('PLACE DEMO ORDER'));
    await tester.pumpAndSettle();
    expect(find.text('Please enter Full name'), findsOneWidget);
    for (final entry in {
      'Full name': 'Test Customer',
      'Phone number': '0901234567',
      'Email': 'test@example.com',
      'City / Province': 'Hanoi',
      'Delivery address': '123 Flower Street',
    }.entries) {
      final input = find.widgetWithText(TextField, entry.key);
      await tester.ensureVisible(input);
      await tester.enterText(input, entry.value);
    }
    await tester.ensureVisible(find.byType(Checkbox));
    await tester.tap(find.byType(Checkbox));
    await tester.ensureVisible(find.text('PLACE DEMO ORDER'));
    await tester.tap(find.text('PLACE DEMO ORDER'));
    await tester.pumpAndSettle();
    expect(find.text('Your demo order is complete!'), findsOneWidget);
    expect(quantity, 3);
    await tester.tap(find.textContaining('CART ('));
    await tester.pumpAndSettle();
    await tester.tap(find.byTooltip('Remove Hoa hồng'));
    await tester.pumpAndSettle();
    expect(find.text('Your cart is empty'), findsOneWidget);
    expect(updates, [3, 4, 3, 0]);
  });
  testWidgets('API failure can be retried and an empty catalog is explicit', (
    tester,
  ) async {
    final auth = session();
    bool fail = true;
    final api = ShopApi(
      auth,
      client: MockClient((r) async {
        if (r.url.path.endsWith('bouquet')) {
          return fail
              ? http.Response('', 500)
              : ok({'items': [], 'totalPages': 0, 'total': 0});
        }
        return ok({'items': []});
      }),
    );
    addTearDown(auth.dispose);
    addTearDown(api.dispose);
    await tester.pumpWidget(
      MaterialApp(
        home: Shop(auth: auth, api: api),
      ),
    );
    await tester.pumpAndSettle();
    await tester.tap(find.text('COLLECTION'));
    await tester.pumpAndSettle();
    expect(find.text('TRY AGAIN'), findsOneWidget);
    fail = false;
    await tester.ensureVisible(find.text('TRY AGAIN'));
    await tester.tap(find.text('TRY AGAIN'));
    await tester.pumpAndSettle();
    expect(find.text('No products found'), findsOneWidget);
  });
  testWidgets('Mobile cart clears account data on logout', (tester) async {
    await tester.binding.setSurfaceSize(const Size(360, 800));
    addTearDown(() => tester.binding.setSurfaceSize(null));
    final auth = session()..email = 'customer@example.com';
    final api = ShopApi(
      auth,
      client: MockClient((r) async {
        if (r.url.path.endsWith('bouquet')) {
          return ok({
            'items': [product],
            'totalPages': 1,
            'total': 1,
          });
        }
        if (r.url.path.endsWith('cart')) {
          return ok({
            'items': [
              {
                'bouquetId': '7',
                'bouquetName': 'Hoa hồng',
                'price': '690000',
                'quantity': '2',
              },
            ],
          });
        }
        return ok({'items': []});
      }),
    );
    addTearDown(auth.dispose);
    addTearDown(api.dispose);
    await tester.pumpWidget(
      MaterialApp(
        home: Shop(auth: auth, api: api),
      ),
    );
    await tester.pumpAndSettle();
    await tester.tap(find.byTooltip('Open menu'));
    await tester.pumpAndSettle();
    await tester.tap(find.textContaining('CART ('));
    await tester.pumpAndSettle();
    expect(find.text('Hoa hồng'), findsOneWidget);

    await tester.tap(find.byTooltip('Tài khoản'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Đăng xuất'));
    await tester.pumpAndSettle();
    expect(find.text('Hoa hồng'), findsNothing);
    expect(
      find.text('Please sign in to view flowers and your cart.'),
      findsOneWidget,
    );
    expect(auth.isLoggedIn, isFalse);
  });
}
