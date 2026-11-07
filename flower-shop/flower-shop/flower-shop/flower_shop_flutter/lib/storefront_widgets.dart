import 'package:flutter/material.dart';
import 'shop_api.dart';

const ink = Color(0xff33372f);
const pink = Color(0xffc58f87);
const paper = Color(0xfffaf8f5);
const line = Color(0xffe9e4df);

Widget serif(String text, {double size = 46, TextAlign? align}) => Text(
  text,
  textAlign: align,
  style: TextStyle(
    fontFamily: 'Georgia',
    letterSpacing: 0,
    fontWeight: FontWeight.w400,
    fontSize: size,
    height: 1.2,
    color: ink,
  ),
);
Widget eyebrow(String text) => Text(
  text.toUpperCase(),
  style: const TextStyle(fontSize: 11, letterSpacing: 4, color: pink),
);
Widget paragraph(String text, {TextAlign? align, double size = 15}) => Text(
  text,
  textAlign: align,
  style: TextStyle(fontSize: size, height: 1.8, color: const Color(0xff777777)),
);
Widget wrapContent(Widget child, {EdgeInsets padding = EdgeInsets.zero}) =>
    LayoutBuilder(
      builder: (_, box) => Center(
        child: Container(
          width: box.maxWidth * .9 > 1200 ? 1200 : box.maxWidth * .9,
          padding: padding,
          child: child,
        ),
      ),
    );
Widget shopButton(String label, VoidCallback? action, {bool outline = false}) =>
    OutlinedButton(
      onPressed: action,
      style: OutlinedButton.styleFrom(
        backgroundColor: outline ? Colors.transparent : ink,
        foregroundColor: outline ? ink : Colors.white,
        disabledForegroundColor: Colors.grey,
        padding: const EdgeInsets.symmetric(horizontal: 29, vertical: 15),
        side: const BorderSide(color: ink),
        shape: const RoundedRectangleBorder(),
        minimumSize: const Size(0, 52),
        textStyle: const TextStyle(fontSize: 11, letterSpacing: 2, height: 1.8),
      ),
      child: Text(label.toUpperCase(), textAlign: TextAlign.center),
    );
Widget textLink(
  String text,
  VoidCallback? action, {
  Color? color,
  double size = 13,
}) => TextButton(
  onPressed: action,
  style: TextButton.styleFrom(
    foregroundColor: color ?? ink,
    padding: EdgeInsets.zero,
    alignment: Alignment.centerLeft,
    minimumSize: const Size(0, 32),
    textStyle: TextStyle(fontSize: size),
  ),
  child: Text(text),
);

class ProductImage extends StatelessWidget {
  const ProductImage({super.key, required this.api, required this.source});
  final ShopApi api;
  final String source;
  @override
  Widget build(BuildContext context) {
    final url = api.imageUrl(source);
    final fallback = Container(
      color: paper,
      alignment: Alignment.center,
      child: const Icon(Icons.local_florist_outlined, size: 42, color: pink),
    );
    return url.isEmpty
        ? fallback
        : Image.network(
            url,
            width: double.infinity,
            fit: BoxFit.cover,
            errorBuilder: (_, error, trace) => fallback,
            loadingBuilder: (_, child, progress) => progress == null
                ? child
                : Container(
                    color: paper,
                    alignment: Alignment.center,
                    child: const CircularProgressIndicator(),
                  ),
          );
  }
}

class ProductTile extends StatefulWidget {
  const ProductTile({
    super.key,
    required this.api,
    required this.product,
    required this.onDetail,
    required this.onAdd,
    this.collection = false,
    this.bestseller = false,
  });
  final ShopApi api;
  final Product product;
  final VoidCallback onDetail;
  final VoidCallback? onAdd;
  final bool collection, bestseller;
  @override
  State<ProductTile> createState() => _ProductTileState();
}

class _ProductTileState extends State<ProductTile> {
  bool hover = false;
  @override
  Widget build(BuildContext context) {
    final p = widget.product;
    final touch =
        Theme.of(context).platform == TargetPlatform.android ||
        Theme.of(context).platform == TargetPlatform.iOS;
    return MouseRegion(
      onEnter: (_) => setState(() => hover = true),
      onExit: (_) => setState(() => hover = false),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          AspectRatio(
            aspectRatio: widget.collection ? 3 / 4 : 800 / 970,
            child: Stack(
              fit: StackFit.expand,
              children: [
                InkWell(
                  onTap: widget.onDetail,
                  child: ClipRect(
                    child: AnimatedScale(
                      scale: hover ? 1.045 : 1,
                      duration: const Duration(milliseconds: 400),
                      child: ProductImage(api: widget.api, source: p.imageUrl),
                    ),
                  ),
                ),
                if (widget.bestseller)
                  Positioned(
                    top: widget.collection ? 0 : 15,
                    right: widget.collection ? 0 : null,
                    left: widget.collection ? null : 15,
                    child: Container(
                      color: widget.collection ? ink : Colors.white,
                      padding: const EdgeInsets.symmetric(
                        horizontal: 12,
                        vertical: 6,
                      ),
                      child: Text(
                        'BESTSELLER',
                        style: TextStyle(
                          fontSize: 9,
                          letterSpacing: 1,
                          color: widget.collection ? Colors.white : ink,
                        ),
                      ),
                    ),
                  ),
                if (widget.collection)
                  Positioned.fill(
                    child: IgnorePointer(
                      ignoring: !hover && !touch,
                      child: AnimatedOpacity(
                        opacity: hover || touch ? 1 : 0,
                        duration: const Duration(milliseconds: 200),
                        child: Container(
                          color: hover
                              ? Colors.black.withValues(alpha: .22)
                              : null,
                          alignment: touch
                              ? Alignment.bottomRight
                              : Alignment.center,
                          padding: const EdgeInsets.all(8),
                          child: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              _action(
                                Icons.shopping_cart_outlined,
                                'Add ${p.name} to cart',
                                widget.onAdd,
                              ),
                              const SizedBox(width: 12),
                              _action(
                                Icons.visibility_outlined,
                                'View details for ${p.name}',
                                widget.onDetail,
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
              ],
            ),
          ),
          const SizedBox(height: 18),
          InkWell(
            onTap: widget.onDetail,
            child: Text(
              widget.collection ? p.name.toUpperCase() : p.name,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: widget.collection ? 'Arial' : 'Georgia',
                fontSize: widget.collection ? 15 : 21,
                letterSpacing: widget.collection ? 1 : 0,
                height: 1.3,
              ),
            ),
          ),
          const SizedBox(height: 4),
          Text(
            money(p.price),
            textAlign: TextAlign.center,
            style: TextStyle(
              color: widget.collection ? const Color(0xff999999) : pink,
              fontSize: 14,
            ),
          ),
          if (!widget.collection) ...[
            const SizedBox(height: 12),
            TextButton(
              onPressed: widget.onAdd,
              style: TextButton.styleFrom(
                backgroundColor: paper,
                foregroundColor: ink,
                shape: const RoundedRectangleBorder(),
                padding: const EdgeInsets.all(12),
                textStyle: const TextStyle(fontSize: 11, letterSpacing: 1),
              ),
              child: const Text('ADD TO CART +'),
            ),
          ],
        ],
      ),
    );
  }

  Widget _action(IconData icon, String tooltip, VoidCallback? action) =>
      Material(
        color: Colors.white.withValues(alpha: .94),
        child: IconButton(
          tooltip: tooltip,
          onPressed: action,
          icon: Icon(icon, size: 21),
          color: ink,
        ),
      );
}

class QuantityBox extends StatefulWidget {
  const QuantityBox({
    super.key,
    required this.quantity,
    required this.onCommit,
    this.enabled = true,
    this.label = 'Quantity',
  });
  final int quantity;
  final ValueChanged<int> onCommit;
  final bool enabled;
  final String label;
  @override
  State<QuantityBox> createState() => _QuantityBoxState();
}

class _QuantityBoxState extends State<QuantityBox> {
  late final controller = TextEditingController(text: '${widget.quantity}');
  int? submitted;
  @override
  void didUpdateWidget(QuantityBox oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.quantity != widget.quantity ||
        oldWidget.enabled != widget.enabled) {
      controller.text = '${widget.quantity}';
      submitted = null;
    }
  }

  void commit() {
    final value = int.tryParse(controller.text);
    if (value == null || value < 1 || value > 99) {
      controller.text = '${widget.quantity}';
      return;
    }
    if (value != widget.quantity && value != submitted && widget.enabled) {
      submitted = value;
      widget.onCommit(value);
    }
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => SizedBox(
    width: 90,
    child: Focus(
      onFocusChange: (focused) {
        if (!focused) commit();
      },
      child: TextField(
        controller: controller,
        enabled: widget.enabled,
        keyboardType: TextInputType.number,
        textInputAction: TextInputAction.done,
        onSubmitted: (_) => commit(),
        onTapOutside: (_) {
          commit();
          FocusScope.of(context).unfocus();
        },
        decoration: InputDecoration(
          semanticCounterText: widget.label,
          hintText: widget.label,
          contentPadding: const EdgeInsets.symmetric(
            horizontal: 15,
            vertical: 13,
          ),
        ),
      ),
    ),
  );
}
