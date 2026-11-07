import 'dart:async';
import 'dart:math' as math;
import 'package:flutter/material.dart';
import 'storefront_widgets.dart';
export 'storefront_widgets.dart' show ProductImage;
import 'auth.dart';
import 'shop_api.dart';

class Shop extends StatefulWidget {
  const Shop({super.key, this.auth, this.api});
  final AuthService? auth;
  final ShopApi? api;
  @override
  State<Shop> createState() => _ShopState();
}

class _ShopState extends State<Shop> {
  late final AuthService auth =
      widget.auth ?? widget.api?.auth ?? AuthService();
  late final ShopApi api = widget.api ?? ShopApi(auth);
  final search = TextEditingController();
  int tab = 0, page = 1, _generation = 0, _cartGeneration = 0;
  String? occasion, error, cartError;
  bool loading = false, cartLoading = false, updating = false;
  ProductPage? catalog;
  String query = '', category = 'All flowers', sorting = 'Default sorting';
  List<Product> get flowers => catalog?.items ?? [];
  List<Occasion> occasions = [];
  List<CartLine> cart = [];
  int get count => cart.fold(0, (sum, line) => sum + line.quantity);
  double get total => cart.fold(0, (sum, line) => sum + line.total);
  @override
  void initState() {
    super.initState();
    slideTimer = Timer.periodic(const Duration(seconds: 6), (_) {
      if (mounted && tab == 0 && !paused) {
        setState(() => slide = (slide + 1) % 3);
      }
    });
    if (auth.isLoggedIn) {
      loadProducts();
      loadCart();
      loadOccasions();
    }
  }

  @override
  void dispose() {
    search.dispose();
    scroll.dispose();
    slideTimer?.cancel();
    for (final c in delivery.values) {
      c.dispose();
    }
    if (widget.api == null) api.dispose();
    if (widget.auth == null && widget.api == null) auth.dispose();
    super.dispose();
  }

  String message(Object e) =>
      e is ShopException ? e.message : 'Có lỗi xảy ra. Vui lòng thử lại.';
  void expire() {
    if (!auth.isLoggedIn) {
      cart = [];
      catalog = null;
      occasions = [];
    }
  }

  Future<void> account() async {
    final previous = auth.accessToken;
    await Navigator.of(
      context,
    ).push<void>(MaterialPageRoute(builder: (_) => AuthScreen(auth: auth)));
    if (!mounted || previous == auth.accessToken) return;
    _generation++;
    _cartGeneration++;
    setState(() {
      cart = [];
      catalog = null;
      occasions = [];
      error = null;
      cartError = null;
      loading = false;
      cartLoading = false;
      page = 1;
      occasion = null;
      search.clear();
      query = '';
      category = 'All flowers';
      sorting = 'Default sorting';
    });
    if (auth.isLoggedIn) {
      loadProducts();
      loadCart();
      loadOccasions();
    }
  }

  Future<void> loadOccasions() async {
    final token = auth.accessToken;
    try {
      final result = await api.occasions();
      if (mounted && token == auth.accessToken) {
        setState(() => occasions = result);
      }
    } catch (_) {
      if (mounted) setState(expire);
    }
  }

  Future<void> loadProducts() async {
    if (!auth.isLoggedIn) return;
    final generation = ++_generation;
    setState(() {
      loading = true;
      error = null;
    });
    try {
      final items = await api.allProducts();
      final result = ProductPage(items, 1, items.length);
      if (mounted && generation == _generation) {
        setState(() => catalog = result);
      }
    } catch (e) {
      if (mounted && generation == _generation) {
        setState(() {
          error = message(e);
          expire();
        });
      }
    } finally {
      if (mounted && generation == _generation) setState(() => loading = false);
    }
  }

  Future<void> loadCart() async {
    if (!auth.isLoggedIn || cartLoading || updating) return;
    final generation = ++_cartGeneration;
    setState(() {
      cartLoading = true;
      cartError = null;
    });
    try {
      final result = await api.cart();
      if (mounted && generation == _cartGeneration) {
        setState(() => cart = result);
      }
    } catch (e) {
      if (mounted && generation == _cartGeneration) {
        setState(() {
          cartError = message(e);
          expire();
        });
      }
    } finally {
      if (mounted && generation == _cartGeneration) {
        setState(() => cartLoading = false);
      }
    }
  }

  Future<bool> updateCart(
    Product product,
    int quantity, {
    bool add = false,
  }) async {
    if (updating) return false;
    if (!auth.isLoggedIn) {
      await account();
      if (!auth.isLoggedIn) return false;
    }
    if (!mounted) return false;
    setState(() {
      updating = true;
      _cartGeneration++;
      cartLoading = false;
    });
    bool saved = false;
    try {
      var target = quantity;
      if (add) {
        final current = await api.cart();
        for (final line in current) {
          if (line.product.id == product.id) target += line.quantity;
        }
      }
      await api.setQuantity(product.id, target);
      saved = true;
      final result = await api.cart();
      if (mounted) {
        setState(() {
          cart = result;
          cartError = null;
        });
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          expire();
          cartError = saved
              ? 'Đã lưu thay đổi. Vui lòng tải lại giỏ hàng.'
              : message(e);
        });
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text(cartError!)));
      }
    } finally {
      if (mounted) setState(() => updating = false);
    }
    return saved;
  }

  double screenWidth = 1200;
  final scroll = ScrollController();
  final storyKey = GlobalKey(), contactKey = GlobalKey();
  final checkoutKey = GlobalKey<FormState>(),
      newsletterKey = GlobalKey<FormState>();
  final delivery = {
    for (final key in ['name', 'phone', 'email', 'city', 'address', 'note'])
      key: TextEditingController(),
  };
  Timer? slideTimer;
  int slide = 0, detailGeneration = 0, detailQuantity = 1;
  bool menu = false, paused = false, care = false, consent = false;
  double priceLimit = 1000000, appliedPrice = 1000000, demoTotal = 0;
  Product? selectedProduct;
  String? detailError;
  bool detailLoading = false;
  void selectTab(int value) {
    setState(() {
      tab = value;
      menu = false;
    });
    if (scroll.hasClients) scroll.jumpTo(0);
    if (value == 2) loadCart();
  }

  void anchor(GlobalKey key) {
    if (key == storyKey && tab != 0) selectTab(0);
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (key.currentContext != null) {
        Scrollable.ensureVisible(
          key.currentContext!,
          duration: const Duration(milliseconds: 400),
        );
      }
    });
  }

  Future<bool> add(Product p, [int quantity = 1]) async {
    final saved = await updateCart(p, quantity, add: true);
    if (saved && mounted) toast('Flowers added to your cart');
    return saved;
  }

  void toast(String text) => ScaffoldMessenger.of(context).showSnackBar(
    SnackBar(content: Text(text), duration: const Duration(seconds: 3)),
  );
  Future<void> detail(Product p) async {
    final generation = ++detailGeneration;
    setState(() {
      selectedProduct = p;
      detailQuantity = 1;
      care = false;
      detailLoading = true;
      detailError = null;
    });
    selectTab(3);
    try {
      final result = await api.detail(p.id);
      if (mounted && generation == detailGeneration) {
        setState(() => selectedProduct = result);
      }
    } catch (e) {
      if (mounted && generation == detailGeneration) {
        setState(() {
          detailError = message(e);
          expire();
        });
      }
    } finally {
      if (mounted && generation == detailGeneration) {
        setState(() => detailLoading = false);
      }
    }
  }

  Widget stateMessage(String text, VoidCallback retry) => Padding(
    padding: const EdgeInsets.all(32),
    child: Column(
      children: [
        paragraph(text, align: TextAlign.center),
        const SizedBox(height: 16),
        shopButton('Try again', retry, outline: true),
      ],
    ),
  );
  Widget loginPrompt() => Padding(
    padding: const EdgeInsets.all(40),
    child: Column(
      children: [
        paragraph(
          'Please sign in to view flowers and your cart.',
          align: TextAlign.center,
        ),
        const SizedBox(height: 18),
        shopButton('Sign in', account),
      ],
    ),
  );
  Widget? catalogState() {
    if (!auth.isLoggedIn) return loginPrompt();
    if (loading) {
      return const Padding(
        padding: EdgeInsets.all(48),
        child: Center(child: CircularProgressIndicator()),
      );
    }
    if (error != null) return stateMessage(error!, loadProducts);
    return null;
  }

  Widget pageTitle(String title) => Container(
    width: double.infinity,
    color: paper,
    padding: const EdgeInsets.symmetric(vertical: 60, horizontal: 20),
    child: Column(
      children: [
        Text(
          'Home / $title',
          style: const TextStyle(fontSize: 12, color: Color(0xff999999)),
        ),
        const SizedBox(height: 12),
        serif(
          title,
          size: screenWidth <= 600 ? 36 : 46,
          align: TextAlign.center,
        ),
      ],
    ),
  );
  Widget header() => LayoutBuilder(
    builder: (context, box) {
      final mobile = box.maxWidth <= 600;
      final links = [
        nav('HOME', () => selectTab(0), tab == 0),
        nav('COLLECTION', () => selectTab(1), tab == 1),
        nav('OUR STORY', () => anchor(storyKey)),
        nav('CONTACT', () => anchor(contactKey)),
        nav('CART ($count)', () => selectTab(2), tab == 2),
      ];
      return Column(
        children: [
          Container(
            color: paper,
            width: double.infinity,
            padding: EdgeInsets.symmetric(
              horizontal: box.maxWidth * .05,
              vertical: 7,
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Flexible(
                  child: Text(
                    'SEND FLOWERS & SHARE THE LOVE',
                    style: TextStyle(fontSize: 11, letterSpacing: 1),
                  ),
                ),
                if (!mobile)
                  const Text(
                    'Fresh flowers every day / Rosebud Flower Studio',
                    style: TextStyle(fontSize: 11, letterSpacing: 1),
                  ),
              ],
            ),
          ),
          wrapContent(
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 20),
              child: Column(
                children: [
                  Row(
                    children: [
                      InkWell(
                        onTap: () => selectTab(0),
                        child: Column(
                          children: [
                            Text(
                              'ROSEBUD',
                              style: TextStyle(
                                fontFamily: 'Georgia',
                                fontSize: mobile ? 27 : 34,
                                letterSpacing: 5,
                              ),
                            ),
                            const Text(
                              'FLOWER STUDIO',
                              style: TextStyle(fontSize: 9, letterSpacing: 5),
                            ),
                          ],
                        ),
                      ),
                      if (!mobile)
                        Expanded(
                          child: FittedBox(
                            fit: BoxFit.scaleDown,
                            alignment: Alignment.centerRight,
                            child: Row(children: links),
                          ),
                        )
                      else
                        const Spacer(),
                      IconButton(
                        tooltip: auth.isLoggedIn ? 'Tài khoản' : 'Đăng nhập',
                        onPressed: updating ? null : account,
                        icon: const Icon(Icons.person_outline, size: 21),
                      ),
                      if (mobile)
                        IconButton(
                          tooltip: 'Open menu',
                          onPressed: () => setState(() => menu = !menu),
                          icon: Icon(menu ? Icons.close : Icons.menu),
                        ),
                    ],
                  ),
                  if (mobile && menu)
                    Padding(
                      padding: const EdgeInsets.only(top: 15),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: links,
                      ),
                    ),
                ],
              ),
            ),
          ),
        ],
      );
    },
  );
  Widget nav(String label, VoidCallback action, [bool active = false]) =>
      Padding(
        padding: const EdgeInsets.symmetric(horizontal: 12),
        child: TextButton(
          onPressed: action,
          style: TextButton.styleFrom(
            foregroundColor: active ? pink : ink,
            textStyle: const TextStyle(fontSize: 11, letterSpacing: 2),
          ),
          child: Text(label),
        ),
      );
  Widget productGrid(List<Product> items, {bool collection = false}) =>
      LayoutBuilder(
        builder: (context, box) {
          final screen = screenWidth;
          final columns = collection
              ? (screen > 800 ? 3 : 2)
              : (screen > 900 ? 4 : 2);
          final gap = screen <= 600
              ? 15.0
              : collection
              ? 25.0
              : 30.0;
          final width = (box.maxWidth - gap * (columns - 1)) / columns;
          return Wrap(
            spacing: gap,
            runSpacing: collection ? 42 : 30,
            children: [
              for (final p in items)
                SizedBox(
                  width: width,
                  child: ProductTile(
                    api: api,
                    product: p,
                    collection: collection,
                    bestseller: flowers.isNotEmpty && p.id == flowers.first.id,
                    onDetail: () => detail(p),
                    onAdd: updating ? null : () => add(p),
                  ),
                ),
            ],
          );
        },
      );
  Widget home() {
    final mobile = screenWidth <= 600;
    final slides = [
      (
        'h1-img-1.jpg',
        'Let flowers\ndo the talking.',
        'One bouquet, a thousand feelings. Send a little tenderness to someone you love.',
      ),
      (
        'h3-img-3-1.jpg',
        'A little grace\nin every petal.',
        'Natural colors, beautifully brought together with a thoughtful touch.',
      ),
      (
        'h2-img-1.jpg',
        'For your most\nspecial days.',
        'From everyday joys to the moments you will always remember.',
      ),
    ];
    final current = slides[slide];
    final heroCopy = Padding(
      padding: EdgeInsets.fromLTRB(
        mobile ? 25 : math.max(screenWidth * .07, 30),
        mobile ? 35 : 60,
        mobile ? 25 : math.max(screenWidth * .07, 30),
        mobile ? 85 : 60,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          eyebrow('Rosebud · Flowers & feelings'),
          const SizedBox(height: 20),
          serif(
            current.$2,
            size: mobile ? 43 : (screenWidth * .06).clamp(42, 82),
          ),
          const SizedBox(height: 24),
          paragraph(current.$3),
          const SizedBox(height: 32),
          shopButton('Explore the collection', () => selectTab(1)),
        ],
      ),
    );
    final heroImage = Image.asset(
      'assets/images/${current.$1}',
      height: mobile ? 290 : 620,
      width: double.infinity,
      fit: BoxFit.cover,
    );
    return Column(
      children: [
        Container(
          color: const Color(0xffeee8df),
          child: Stack(
            children: [
              if (mobile)
                Column(children: [heroImage, heroCopy])
              else
                Row(
                  children: [
                    Expanded(child: heroCopy),
                    Expanded(child: heroImage),
                  ],
                ),
              Positioned(
                left: mobile ? 25 : screenWidth * .07,
                bottom: 28,
                child: Row(
                  children: [
                    for (int i = 0; i < 3; i++)
                      Padding(
                        padding: const EdgeInsets.only(right: 10),
                        child: InkWell(
                          onTap: () => setState(() => slide = i),
                          child: Semantics(
                            button: true,
                            label: 'Slide ${i + 1}',
                            child: Container(
                              width: 26,
                              height: 4,
                              color: slide == i ? ink : Colors.grey,
                            ),
                          ),
                        ),
                      ),
                  ],
                ),
              ),
              Positioned(
                right: 25,
                bottom: 20,
                child: Row(
                  children: [
                    Material(
                      color: Colors.white,
                      child: IconButton(
                        tooltip: 'Previous slide',
                        onPressed: () =>
                            setState(() => slide = (slide + 2) % 3),
                        icon: const Icon(Icons.arrow_back, size: 18),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Material(
                      color: Colors.white,
                      child: IconButton(
                        tooltip: 'Next slide',
                        onPressed: () =>
                            setState(() => slide = (slide + 1) % 3),
                        icon: const Icon(Icons.arrow_forward, size: 18),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Material(
                      color: Colors.white,
                      child: TextButton(
                        onPressed: () => setState(() => paused = !paused),
                        child: Text(
                          paused ? 'Play' : 'Pause',
                          style: const TextStyle(color: ink, fontSize: 11),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
        wrapContent(
          Container(
            padding: const EdgeInsets.symmetric(vertical: 45),
            decoration: const BoxDecoration(
              border: Border(bottom: BorderSide(color: line)),
            ),
            child: LayoutBuilder(
              builder: (context, box) {
                final services = [
                  for (final (i, title, text) in [
                    (
                      1,
                      'Handpicked fresh flowers',
                      'Every petal, carefully chosen.',
                    ),
                    (2, 'Wrapped with love', 'Thoughtfulness in every detail.'),
                    (3, 'For every occasion', 'A gift that means something.'),
                  ])
                    Row(
                      children: [
                        Image.asset(
                          'assets/images/custom-icon-$i.png',
                          width: 45,
                        ),
                        const SizedBox(width: 20),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              serif(title, size: 20),
                              paragraph(text, size: 13),
                            ],
                          ),
                        ),
                      ],
                    ),
                ];
                return mobile
                    ? Column(
                        children: [
                          for (final service in services)
                            Padding(
                              padding: const EdgeInsets.symmetric(vertical: 10),
                              child: service,
                            ),
                        ],
                      )
                    : Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          for (final service in services)
                            Expanded(
                              child: Padding(
                                padding: const EdgeInsets.only(right: 20),
                                child: service,
                              ),
                            ),
                        ],
                      );
              },
            ),
          ),
        ),
        flowerSection(
          'Made with love',
          'Our most-loved flowers',
          flowers.take(4).toList(),
          description: 'Bring a little beauty into your life, in your own way.',
          viewAll: true,
        ),
        Container(
          key: storyKey,
          color: paper,
          child: LayoutBuilder(
            builder: (_, box) {
              final image = Image.asset(
                'assets/images/h1-img-1.jpg',
                height: mobile ? 320 : 540,
                width: double.infinity,
                fit: BoxFit.cover,
              );
              final text = Padding(
                padding: EdgeInsets.all(mobile ? 30 : 65),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    eyebrow('Our flower story'),
                    const SizedBox(height: 12),
                    serif(
                      'Where beautiful\nmoments begin',
                      size: mobile ? 32 : 46,
                    ),
                    const SizedBox(height: 22),
                    paragraph(
                      'We believe every flower has a story. From warm pink petals to fresh green foliage, Rosebud brings natural beauty to the feelings we share.',
                    ),
                    const SizedBox(height: 15),
                    paragraph('Every bouquet is a message written with care.'),
                    const SizedBox(height: 24),
                    shopButton('Find your bouquet', () => selectTab(1)),
                  ],
                ),
              );
              return mobile
                  ? Column(children: [image, text])
                  : Row(
                      children: [
                        Expanded(child: image),
                        Expanded(child: text),
                      ],
                    );
            },
          ),
        ),
        Container(
          width: double.infinity,
          color: const Color(0xfff2e9e5),
          padding: const EdgeInsets.symmetric(vertical: 80, horizontal: 20),
          child: Column(
            children: [
              eyebrow('Say it with flowers'),
              const SizedBox(height: 12),
              ConstrainedBox(
                constraints: const BoxConstraints(maxWidth: 780),
                child: const Text(
                  '“Sometimes happiness is simply a small bouquet, given at just the right moment.”',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontFamily: 'Georgia',
                    fontStyle: FontStyle.italic,
                    fontSize: 27,
                    height: 1.6,
                  ),
                ),
              ),
              const SizedBox(height: 25),
              eyebrow('The Rosebud philosophy'),
            ],
          ),
        ),
        flowerSection(
          'Flowers for every occasion',
          'For days to remember',
          flowers.skip(4).take(4).toList(),
        ),
        LayoutBuilder(
          builder: (_, box) => Wrap(
            children: [
              for (int i = 1; i <= 4; i++)
                SizedBox(
                  width: box.maxWidth / (mobile ? 2 : 4),
                  child: InkWell(
                    onTap: () => selectTab(1),
                    child: AspectRatio(
                      aspectRatio: 1,
                      child: Image.asset(
                        'assets/images/gallery-img-$i.jpg',
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                ),
            ],
          ),
        ),
      ],
    );
  }

  Widget flowerSection(
    String tag,
    String title,
    List<Product> items, {
    String? description,
    bool viewAll = false,
  }) => wrapContent(
    Padding(
      padding: const EdgeInsets.symmetric(vertical: 80),
      child: Column(
        children: [
          eyebrow(tag),
          const SizedBox(height: 12),
          serif(title, align: TextAlign.center),
          const SizedBox(height: 22),
          if (description != null) ...[
            paragraph(description, align: TextAlign.center),
            const SizedBox(height: 30),
          ],
          catalogState() ?? productGrid(items),
          if (viewAll) ...[
            const SizedBox(height: 40),
            shopButton('View all products', () => selectTab(1), outline: true),
          ],
        ],
      ),
    ),
  );
  Widget collection() {
    final mobile = screenWidth <= 600;
    final categories = [
      'All flowers',
      ...{
        for (final p in flowers)
          if (p.occasion.isNotEmpty) p.occasion,
      },
    ];
    final filtered = flowers
        .where(
          (p) =>
              (category == 'All flowers' || p.occasion == category) &&
              p.price <= appliedPrice &&
              p.name.toLowerCase().contains(query.toLowerCase()),
        )
        .toList();
    if (sorting == 'Price: low to high') {
      filtered.sort((a, b) => a.price.compareTo(b.price));
    }
    if (sorting == 'Price: high to low') {
      filtered.sort((a, b) => b.price.compareTo(a.price));
    }
    if (sorting == 'Product name') {
      filtered.sort((a, b) => a.name.compareTo(b.name));
    }
    final pages = math.max(1, (filtered.length / 6).ceil());
    final currentPage = page.clamp(1, pages);
    final offset = (currentPage - 1) * 6;
    final sortPanel = Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        sidebarTitle('Sort products'),
        DropdownButtonFormField<String>(
          initialValue: sorting,
          key: ValueKey(sorting),
          isExpanded: true,
          style: const TextStyle(fontSize: 12, color: ink),
          items: [
            'Default sorting',
            'Price: low to high',
            'Price: high to low',
            'Product name',
          ].map((s) => DropdownMenuItem(value: s, child: Text(s))).toList(),
          onChanged: (value) => setState(() {
            sorting = value!;
            page = 1;
          }),
        ),
      ],
    );
    final pricePanel = Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        sidebarTitle('Filter by price'),
        Slider(
          value: priceLimit,
          min: 0,
          max: math.max(
            1000000,
            flowers.fold<double>(0, (v, p) => math.max(v, p.price)),
          ),
          activeColor: ink,
          onChanged: (v) => setState(() => priceLimit = v),
        ),
        Wrap(
          alignment: WrapAlignment.spaceBetween,
          crossAxisAlignment: WrapCrossAlignment.center,
          children: [
            Text(
              'Price: ${money(0)} – ${money(priceLimit)}',
              style: const TextStyle(fontSize: 11, color: Colors.grey),
            ),
            textLink(
              'FILTER',
              () => setState(() {
                appliedPrice = priceLimit;
                page = 1;
              }),
              size: 10,
            ),
          ],
        ),
      ],
    );
    final categoryPanel = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        sidebarTitle('Categories'),
        for (final c in categories)
          textLink(
            c,
            () => setState(() {
              category = c;
              page = 1;
            }),
            color: category == c ? ink : Colors.grey,
            size: 14,
          ),
      ],
    );
    final sidebar = Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        TextField(
          controller: search,
          onChanged: (value) => setState(() {
            query = value;
            page = 1;
          }),
          decoration: const InputDecoration(
            hintText: 'Search products...',
            suffixIcon: Icon(Icons.search, size: 19),
          ),
        ),

        const SizedBox(height: 30),
        if (mobile)
          Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Expanded(child: sortPanel),
              const SizedBox(width: 20),
              Expanded(child: pricePanel),
            ],
          )
        else ...[
          sortPanel,
          const SizedBox(height: 38),
          pricePanel,
        ],
        const SizedBox(height: 30),
        categoryPanel,
        const SizedBox(height: 20),
        textLink(
          'Reset filters',
          () => setState(() {
            category = 'All flowers';
            sorting = 'Default sorting';
            query = '';
            search.clear();
            priceLimit = appliedPrice = 1000000;
            page = 1;
          }),
          color: Colors.grey,
          size: 12,
        ),
      ],
    );
    final results = Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Padding(
          padding: const EdgeInsets.only(top: 15, bottom: 30),
          child: Text(
            filtered.isEmpty
                ? 'No results'
                : 'Showing ${offset + 1}–${math.min(offset + 6, filtered.length)} of ${filtered.length} results',
            style: const TextStyle(fontSize: 13, color: Colors.grey),
          ),
        ),
        catalogState() ??
            (filtered.isEmpty
                ? Padding(
                    padding: const EdgeInsets.symmetric(vertical: 55),
                    child: Column(
                      children: [
                        serif('No products found', size: 24),
                        paragraph('Try another search or reset your filters.'),
                      ],
                    ),
                  )
                : productGrid(
                    filtered.skip(offset).take(6).toList(),
                    collection: true,
                  )),
        if (pages > 1)
          Padding(
            padding: const EdgeInsets.only(top: 50),
            child: Wrap(
              alignment: WrapAlignment.center,
              spacing: 10,
              runSpacing: 10,
              children: [
                paginationButton(
                  '←',
                  currentPage > 1
                      ? () => setState(() => page = currentPage - 1)
                      : null,
                ),
                for (int n = 1; n <= pages; n++)
                  paginationButton(
                    '$n',
                    () => setState(() => page = n),
                    selected: currentPage == n,
                  ),
                paginationButton(
                  '→',
                  currentPage < pages
                      ? () => setState(() => page = currentPage + 1)
                      : null,
                ),
              ],
            ),
          ),
      ],
    );
    return Column(
      children: [
        pageTitle('Flower collection'),
        wrapContent(
          Padding(
            padding: EdgeInsets.symmetric(vertical: mobile ? 35 : 55),
            child: mobile
                ? Column(
                    children: [sidebar, const SizedBox(height: 30), results],
                  )
                : Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      SizedBox(
                        width: screenWidth > 1050 ? 270 : 210,
                        child: sidebar,
                      ),
                      const SizedBox(width: 40),
                      Expanded(child: results),
                    ],
                  ),
          ),
        ),
      ],
    );
  }

  Widget sidebarTitle(String text) => Padding(
    padding: const EdgeInsets.only(bottom: 22),
    child: Text(
      text.toUpperCase(),
      style: const TextStyle(
        fontFamily: 'Georgia',
        fontSize: 16,
        letterSpacing: 1.5,
      ),
    ),
  );
  Widget paginationButton(
    String label,
    VoidCallback? action, {
    bool selected = false,
  }) => SizedBox(
    width: 40,
    height: 40,
    child: OutlinedButton(
      style: OutlinedButton.styleFrom(
        padding: EdgeInsets.zero,
        backgroundColor: selected ? ink : Colors.white,
        foregroundColor: selected ? Colors.white : ink,
        side: const BorderSide(color: line),
        shape: const RoundedRectangleBorder(),
      ),
      onPressed: action,
      child: Text(label),
    ),
  );
  Widget detailView() {
    final p = selectedProduct!;
    final mobile = screenWidth <= 600;
    final photo = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        AspectRatio(
          aspectRatio: 800 / 970,
          child: ProductImage(api: api, source: p.imageUrl),
        ),
        const SizedBox(height: 15),
        Container(
          width: 80,
          height: 97,
          decoration: BoxDecoration(border: Border.all(color: line)),
          child: ProductImage(api: api, source: p.imageUrl),
        ),
      ],
    );
    final copy = Padding(
      padding: const EdgeInsets.only(top: 25),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          eyebrow('The Rosebud collection'),
          const SizedBox(height: 20),
          serif(p.name, size: mobile ? 38 : 48),
          const SizedBox(height: 20),
          Text(
            money(p.price),
            style: const TextStyle(fontSize: 24, color: pink),
          ),
          const SizedBox(height: 20),
          paragraph(p.description),
          const SizedBox(height: 30),
          Wrap(
            spacing: 12,
            runSpacing: 12,
            crossAxisAlignment: WrapCrossAlignment.center,
            children: [
              QuantityBox(
                key: ValueKey('detail-${p.id}'),
                quantity: detailQuantity,
                onCommit: (v) => setState(() => detailQuantity = v),
              ),
              shopButton(
                'Add to cart',
                updating ? null : () => add(p, detailQuantity),
              ),
            ],
          ),
          const SizedBox(height: 24),
          textLink('View cart →', () => selectTab(2), size: 15),
          const SizedBox(height: 20),
          Container(
            width: double.infinity,
            padding: const EdgeInsets.only(top: 20),
            decoration: const BoxDecoration(
              border: Border(top: BorderSide(color: line)),
            ),
            child: paragraph(
              'SKU: RB-${p.id.padLeft(3, '0')}\nCategory: ${p.occasion}\nReference imagery from the Rosebud collection.',
              size: 13,
            ),
          ),
          const SizedBox(height: 50),
          Container(
            decoration: const BoxDecoration(
              border: Border(top: BorderSide(color: line)),
            ),
            padding: const EdgeInsets.only(top: 25),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Wrap(
                  spacing: 20,
                  children: [
                    for (final (label, selected) in [
                      ('Description', false),
                      ('Flower care', true),
                    ])
                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            bottom: BorderSide(
                              color: care == selected
                                  ? pink
                                  : Colors.transparent,
                              width: 2,
                            ),
                          ),
                        ),
                        child: textLink(
                          label,
                          () => setState(() => care = selected),
                          size: 15,
                        ),
                      ),
                  ],
                ),
                const SizedBox(height: 16),
                paragraph(
                  care
                      ? 'Trim stems at an angle, change the water daily, and keep flowers in a cool, airy place away from direct sunlight and heat.'
                      : p.description,
                ),
              ],
            ),
          ),
        ],
      ),
    );
    return Column(
      children: [
        pageTitle(p.name),
        if (detailLoading) const LinearProgressIndicator(),
        if (detailError != null)
          stateMessage(detailError!, () => detail(p))
        else
          wrapContent(
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 90),
              child: Column(
                children: [
                  if (mobile) ...[
                    photo,
                    const SizedBox(height: 30),
                    copy,
                  ] else
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(child: photo),
                        const SizedBox(width: 65),
                        Expanded(child: copy),
                      ],
                    ),
                  const SizedBox(height: 90),
                  eyebrow('You may also love'),
                  const SizedBox(height: 12),
                  serif('You may also like', align: TextAlign.center),
                  const SizedBox(height: 30),
                  productGrid(
                    flowers.where((other) => other.id != p.id).take(4).toList(),
                  ),
                ],
              ),
            ),
          ),
      ],
    );
  }

  Widget emptyCart() => Padding(
    padding: const EdgeInsets.symmetric(vertical: 60, horizontal: 20),
    child: Column(
      children: [
        eyebrow('A little beauty awaits'),
        const SizedBox(height: 12),
        serif('Your cart is empty', align: TextAlign.center),
        const SizedBox(height: 20),
        paragraph('Choose a little beauty for your day.'),
        const SizedBox(height: 30),
        shopButton('Explore the collection', () => selectTab(1)),
      ],
    ),
  );
  Widget summary(String label, String value, {bool bold = false}) => Container(
    padding: const EdgeInsets.symmetric(vertical: 12),
    decoration: const BoxDecoration(
      border: Border(bottom: BorderSide(color: line)),
    ),
    child: Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Expanded(
          child: Text(
            label,
            style: TextStyle(
              height: 1.8,
              fontWeight: bold ? FontWeight.bold : FontWeight.normal,
            ),
          ),
        ),
        const SizedBox(width: 20),
        Flexible(
          child: Text(
            value,
            textAlign: TextAlign.end,
            style: TextStyle(
              height: 1.8,
              fontWeight: bold ? FontWeight.bold : FontWeight.normal,
            ),
          ),
        ),
      ],
    ),
  );
  Widget basket() => Column(
    children: [
      pageTitle('Cart'),
      wrapContent(
        Padding(
          padding: const EdgeInsets.symmetric(vertical: 90),
          child: !auth.isLoggedIn
              ? loginPrompt()
              : cartLoading
              ? const Center(child: CircularProgressIndicator())
              : cartError != null
              ? stateMessage(cartError!, loadCart)
              : cart.isEmpty
              ? emptyCart()
              : LayoutBuilder(
                  builder: (context, box) {
                    final mobile = box.maxWidth < 600;
                    Widget quantity(CartLine item) => QuantityBox(
                      key: ValueKey('cart-${item.product.id}'),
                      label: 'Quantity ${item.product.name}',
                      quantity: item.quantity,
                      enabled: !updating,
                      onCommit: (v) => updateCart(item.product, v),
                    );
                    return Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        if (!mobile)
                          Padding(
                            padding: const EdgeInsets.symmetric(vertical: 20),
                            child: Row(
                              children: [
                                const SizedBox(width: 110),
                                const Expanded(child: Text('Product')),
                                for (final heading in [
                                  'Unit price',
                                  'Quantity',
                                  'Subtotal',
                                ])
                                  SizedBox(
                                    width: box.maxWidth > 1000 ? 140 : 105,
                                    child: Text(heading),
                                  ),
                                const SizedBox(width: 40),
                              ],
                            ),
                          ),
                        for (final item in cart)
                          Container(
                            padding: const EdgeInsets.symmetric(vertical: 20),
                            decoration: const BoxDecoration(
                              border: Border(bottom: BorderSide(color: line)),
                            ),
                            child: Row(
                              crossAxisAlignment: mobile
                                  ? CrossAxisAlignment.start
                                  : CrossAxisAlignment.center,
                              children: [
                                InkWell(
                                  onTap: () => detail(item.product),
                                  child: SizedBox(
                                    width: mobile ? 60 : 75,
                                    height: mobile ? 75 : 90,
                                    child: ProductImage(
                                      api: api,
                                      source: item.product.imageUrl,
                                    ),
                                  ),
                                ),
                                SizedBox(width: mobile ? 12 : 35),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      textLink(
                                        item.product.name,
                                        () => detail(item.product),
                                        size: 15,
                                      ),
                                      if (mobile) ...[
                                        const SizedBox(height: 10),
                                        quantity(item),
                                        const SizedBox(height: 10),
                                        Text(money(item.total)),
                                      ],
                                    ],
                                  ),
                                ),
                                if (!mobile) ...[
                                  SizedBox(
                                    width: box.maxWidth > 1000 ? 140 : 105,
                                    child: Text(money(item.product.price)),
                                  ),
                                  SizedBox(
                                    width: box.maxWidth > 1000 ? 140 : 105,
                                    child: Align(
                                      alignment: Alignment.centerLeft,
                                      child: quantity(item),
                                    ),
                                  ),
                                  SizedBox(
                                    width: box.maxWidth > 1000 ? 140 : 105,
                                    child: Text(money(item.total)),
                                  ),
                                ],
                                SizedBox(
                                  width: 40,
                                  child: IconButton(
                                    tooltip: 'Remove ${item.product.name}',
                                    onPressed: updating
                                        ? null
                                        : () => updateCart(item.product, 0),
                                    icon: const Icon(Icons.close, size: 21),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        if (updating) const LinearProgressIndicator(),
                        const SizedBox(height: 25),
                        Align(
                          alignment: Alignment.centerLeft,
                          child: shopButton(
                            '← Continue shopping',
                            () => selectTab(1),
                            outline: true,
                          ),
                        ),
                        const SizedBox(height: 35),
                        Align(
                          alignment: Alignment.centerRight,
                          child: Container(
                            width: 450,
                            color: paper,
                            padding: const EdgeInsets.all(30),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.stretch,
                              children: [
                                serif('Cart totals', size: 24),
                                const SizedBox(height: 20),
                                summary('Subtotal', money(total)),
                                summary('Delivery', 'Free (demo)'),
                                summary('Total', money(total), bold: true),
                                const SizedBox(height: 20),
                                shopButton(
                                  'Proceed to checkout →',
                                  updating ? null : () => selectTab(4),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ],
                    );
                  },
                ),
        ),
      ),
    ],
  );
  Widget checkout() {
    final mobile = screenWidth <= 600;
    Widget field(String key, String label, {TextInputType? type}) => Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('$label *', style: const TextStyle(fontSize: 13)),
        const SizedBox(height: 7),
        TextFormField(
          controller: delivery[key],
          keyboardType: type,
          decoration: InputDecoration(hintText: label),
          validator: (value) {
            if (value == null || value.trim().isEmpty) {
              return 'Please enter $label';
            }
            if (key == 'email' &&
                !RegExp(r'^[^\s@]+@[^\s@]+\.[^\s@]+$').hasMatch(value.trim())) {
              return 'Enter a valid email';
            }
            if (key == 'phone' &&
                !RegExp(r'^[+0-9 ()-]{9,20}$').hasMatch(value.trim())) {
              return 'Enter a valid phone number';
            }
            return null;
          },
        ),
      ],
    );
    final fields = Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        eyebrow('Your details'),
        const SizedBox(height: 12),
        serif('Delivery details', size: mobile ? 32 : 46),
        const SizedBox(height: 30),
        LayoutBuilder(
          builder: (_, box) {
            final width = mobile ? box.maxWidth : (box.maxWidth - 20) / 2;
            return Wrap(
              spacing: 20,
              runSpacing: 20,
              children: [
                SizedBox(width: width, child: field('name', 'Full name')),
                SizedBox(
                  width: width,
                  child: field(
                    'phone',
                    'Phone number',
                    type: TextInputType.phone,
                  ),
                ),
                SizedBox(
                  width: width,
                  child: field(
                    'email',
                    'Email',
                    type: TextInputType.emailAddress,
                  ),
                ),
                SizedBox(width: width, child: field('city', 'City / Province')),
                SizedBox(
                  width: box.maxWidth,
                  child: field('address', 'Delivery address'),
                ),
              ],
            );
          },
        ),
        const SizedBox(height: 25),
        const Text(
          'Gift message / Order notes',
          style: TextStyle(fontSize: 13),
        ),
        const SizedBox(height: 7),
        TextFormField(
          controller: delivery['note'],
          maxLines: 4,
          maxLength: 1000,
          decoration: const InputDecoration(
            hintText: 'Write a little message of love...',
            counterText: '',
          ),
        ),
      ],
    );
    final order = Container(
      color: paper,
      padding: const EdgeInsets.all(30),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          serif('Your order', size: 24),
          const SizedBox(height: 20),
          for (final item in cart)
            summary(
              '${item.product.name} × ${item.quantity}',
              money(item.total),
            ),
          summary('Delivery', 'Free (demo)'),
          summary('Total', money(total), bold: true),
          const SizedBox(height: 20),
          const Row(
            children: [
              Icon(Icons.radio_button_checked, size: 18),
              SizedBox(width: 8),
              Expanded(child: Text('Cash on delivery (demo)')),
            ],
          ),
          const SizedBox(height: 20),
          Container(
            color: Colors.white,
            padding: const EdgeInsets.all(15),
            child: paragraph(
              'This is a demo. Your information is processed only in your browser. No order is sent to the studio, and no payment or delivery takes place.',
              size: 12,
            ),
          ),
          const SizedBox(height: 16),
          FormField<bool>(
            initialValue: consent,
            validator: (_) =>
                consent ? null : 'Please confirm this is a demo order.',
            builder: (field) => Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Checkbox(
                      value: consent,
                      onChanged: (v) {
                        setState(() => consent = v ?? false);
                        field.didChange(consent);
                      },
                    ),
                    const Expanded(
                      child: Text('I understand this is a demo order.'),
                    ),
                  ],
                ),
                if (field.hasError)
                  Text(
                    field.errorText!,
                    style: TextStyle(
                      color: Theme.of(context).colorScheme.error,
                    ),
                  ),
              ],
            ),
          ),
          const SizedBox(height: 25),
          shopButton('Place demo order', () {
            if (!checkoutKey.currentState!.validate()) return;
            FocusScope.of(context).unfocus();
            demoTotal = total;
            selectTab(5);
          }),
          const SizedBox(height: 15),
          textLink('← Back to cart', () => selectTab(2)),
        ],
      ),
    );
    return Column(
      children: [
        pageTitle('Checkout'),
        wrapContent(
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 90),
            child: !auth.isLoggedIn
                ? loginPrompt()
                : cart.isEmpty
                ? emptyCart()
                : Form(
                    key: checkoutKey,
                    child: mobile
                        ? Column(
                            children: [
                              fields,
                              const SizedBox(height: 30),
                              order,
                            ],
                          )
                        : Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Expanded(flex: 5, child: fields),
                              const SizedBox(width: 65),
                              Expanded(flex: 4, child: order),
                            ],
                          ),
                  ),
          ),
        ),
      ],
    );
  }

  Widget success() => Column(
    children: [
      pageTitle('Order complete'),
      Padding(
        padding: const EdgeInsets.symmetric(vertical: 60, horizontal: 20),
        child: Center(
          child: Container(
            constraints: const BoxConstraints(maxWidth: 700),
            color: paper,
            padding: const EdgeInsets.all(50),
            child: Column(
              children: [
                eyebrow('Thank you'),
                const SizedBox(height: 12),
                serif('Your demo order is complete!', align: TextAlign.center),
                const SizedBox(height: 22),
                paragraph(
                  'Order total: ${money(demoTotal)}. This is a demo confirmation. No order has been sent to the studio and no payment has been made.',
                  align: TextAlign.center,
                ),
                const SizedBox(height: 30),
                shopButton('Continue shopping', () => selectTab(1)),
              ],
            ),
          ),
        ),
      ),
    ],
  );
  Widget footer() {
    final mobile = screenWidth <= 600;
    final subscribe = Form(
      key: newsletterKey,
      child: SizedBox(
        width: mobile ? double.infinity : 450,
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              child: TextFormField(
                decoration: const InputDecoration(
                  hintText: 'Your email address',
                ),
                keyboardType: TextInputType.emailAddress,
                validator: (v) =>
                    v != null &&
                        RegExp(r'^[^\s@]+@[^\s@]+\.[^\s@]+$').hasMatch(v.trim())
                    ? null
                    : 'Enter a valid email',
              ),
            ),
            shopButton('Subscribe', () {
              if (newsletterKey.currentState!.validate()) {
                toast('This is a demo; your email has not been submitted.');
              }
            }),
          ],
        ),
      ),
    );
    final news = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        eyebrow('A little flower, a little joy'),
        const SizedBox(height: 8),
        serif('Let your day bloom', size: 24),
      ],
    );
    final about = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'ROSEBUD',
          style: TextStyle(
            fontFamily: 'Georgia',
            letterSpacing: 5,
            fontSize: 30,
            color: Color(0xffdddddd),
          ),
        ),
        const SizedBox(height: 15),
        paragraph(
          'A little tenderness from nature. Thoughtfully arranged flowers to make every expression of love feel special.',
          size: 13,
        ),
      ],
    );
    Widget footerHeading(String text) => Text(
      text,
      style: const TextStyle(
        fontFamily: 'Georgia',
        fontSize: 24,
        color: Color(0xffdddddd),
      ),
    );
    final explore = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        footerHeading('Explore'),
        const SizedBox(height: 15),
        textLink('All products', () => selectTab(1), color: Colors.white70),
        textLink(
          'The Rosebud story',
          () => anchor(storyKey),
          color: Colors.white70,
        ),
        textLink('Your cart', () => selectTab(2), color: Colors.white70),
      ],
    );
    final studio = Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        footerHeading('Flower Studio'),
        const SizedBox(height: 15),
        paragraph(
          'Choose your flowers, write a note, and send a little love.',
          size: 13,
        ),
        textLink(
          'Flowers for special days →',
          () => selectTab(1),
          color: Colors.white70,
        ),
      ],
    );
    return Column(
      key: contactKey,
      children: [
        wrapContent(
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 55),
            child: mobile
                ? Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [news, const SizedBox(height: 30), subscribe],
                  )
                : Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Expanded(child: news),
                      const SizedBox(width: 30),
                      Flexible(child: subscribe),
                    ],
                  ),
          ),
        ),
        Container(
          width: double.infinity,
          color: const Color(0xff252923),
          child: wrapContent(
            Padding(
              padding: const EdgeInsets.only(top: 65, bottom: 20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  if (mobile) ...[
                    about,
                    const SizedBox(height: 25),
                    explore,
                    const SizedBox(height: 25),
                    studio,
                  ] else
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(flex: 2, child: about),
                        const SizedBox(width: 70),
                        Expanded(child: explore),
                        const SizedBox(width: 70),
                        Expanded(child: studio),
                      ],
                    ),
                  const SizedBox(height: 45),
                  const Divider(color: Color(0xff444444)),
                  const SizedBox(height: 20),
                  const Text(
                    '© 2026 Rosebud Flower Studio · Inspired by the Rosebud template.',
                    style: TextStyle(fontSize: 11, color: Color(0xffaaaaaa)),
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) => LayoutBuilder(
    builder: (context, box) {
      screenWidth = box.maxWidth;
      return Scaffold(
        backgroundColor: Colors.white,
        body: SafeArea(
          child: Stack(
            children: [
              SingleChildScrollView(
                controller: scroll,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    header(),
                    if (tab == 0)
                      home()
                    else if (tab == 1)
                      collection()
                    else if (tab == 2)
                      basket()
                    else if (tab == 3)
                      detailView()
                    else if (tab == 4)
                      checkout()
                    else
                      success(),
                    footer(),
                  ],
                ),
              ),
              Positioned(
                bottom: 25,
                right: 20,
                child: Material(
                  color: Colors.white,
                  shape: const RoundedRectangleBorder(
                    side: BorderSide(color: line),
                  ),
                  child: IconButton(
                    tooltip: 'Back to top',
                    onPressed: () {
                      if (scroll.hasClients) {
                        scroll.animateTo(
                          0,
                          duration: const Duration(milliseconds: 350),
                          curve: Curves.easeOut,
                        );
                      }
                    },
                    icon: const Icon(Icons.arrow_upward, size: 19),
                  ),
                ),
              ),
            ],
          ),
        ),
      );
    },
  );
}
