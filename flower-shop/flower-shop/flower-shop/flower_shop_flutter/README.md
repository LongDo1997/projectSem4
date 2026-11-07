# Rosebud Flower Studio — Flutter

Ứng dụng Flutter dựa trên giao diện HTML/CSS/JS và ảnh trong thư mục flower-shop.

## Chạy trong Visual Studio Code
1. Mở thư mục flower_shop_flutter trong VS Code.
2. Cài extension Flutter và Dart khi VS Code đề xuất.
3. Mở terminal và chạy: flutter pub get
4. Nhấn Ctrl+Shift+P → Flutter: Launch Emulator → chọn máy ảo Android.
5. Chọn thiết bị ở thanh trạng thái, nhấn F5 (cấu hình Rosebud Flutter).

Nếu chưa có máy ảo: Android Studio → Device Manager → Create Virtual Device → chọn điện thoại và tải system image → Finish.

Lệnh thay thế:
```
flutter doctor
flutter emulators
flutter emulators --launch <emulator_id>
flutter devices
flutter run -d <device_id>
```
Có thể chạy trên trình duyệt: flutter run -d chrome

## Chức năng
Giữ nguyên giao diện Flutter gốc: trang chủ với các mục dịch vụ và câu chuyện Rosebud, bộ sưu tập với tìm kiếm/lọc/sắp xếp giá, chi tiết có ảnh thu nhỏ, giỏ hàng với tạm tính/giao hàng/tổng cộng và nút đặt hàng mẫu. Dữ liệu sản phẩm và giỏ hàng lấy từ API. Danh mục được tải đủ các trang API để các bộ lọc và sắp xếp gốc áp dụng trên toàn bộ sản phẩm.

Đăng ký, đăng nhập, sản phẩm và giỏ hàng đã kết nối backend. Giỏ hàng lưu trong cơ sở dữ liệu theo tài khoản và được tải lại sau đăng nhập. Backend hiện yêu cầu JWT cho sản phẩm và giỏ hàng. Màn đặt hàng mẫu giữ nguyên như bản gốc; chưa có API đặt hàng/thanh toán. Hoàn tất đơn mẫu không xóa giỏ hàng thật trên server.

## Kiểm tra
```
flutter analyze
flutter test
flutter build web
```

## Cấu hình Android trên máy hiện tại
Dùng Gradle 8.13 và JDK 21 sẵn có. android/gradle.properties có org.gradle.java.home=C:/Users/huent/.jdks/ms-21.0.7; đổi hoặc xóa dòng này khi chuyển sang máy khác.
Máy ảo hiện có: Medium_Phone_API_36.1.
Nếu flutter doctor báo thiếu cmdline-tools: Android Studio → SDK Manager → SDK Tools → Android SDK Command-line Tools (latest) → Apply. Sau đó chạy flutter doctor --android-licenses và tự đọc/chấp nhận giấy phép.

Lần chạy Android đầu tiên đang thiếu NDK 27.0.12077973 và tải tự động chưa hoàn tất. Có thể cài qua Android Studio → SDK Manager → SDK Tools → Show Package Details → NDK (Side by side) → 27.0.12077973 → Apply. Cài xong chạy flutter run -d emulator-5554 (khi máy ảo đang mở).
Đã kiểm tra: flutter analyze không có vấn đề; flutter test kiểm tra xác thực và luồng sản phẩm/giỏ hàng; flutter build web thành công. Chưa xác nhận build/cài Android thành công do bước tải NDK.

## Đăng ký và đăng nhập
Bật backend D:\projectSem4\boutquetShop trên cổng 9003. Nhấn biểu tượng tài khoản trên thanh trên cùng để đăng nhập hoặc đăng ký khách hàng. Đăng ký thành công sẽ chuyển về form đăng nhập; sau đăng nhập có thể mở tài khoản để đăng xuất.

Web/Windows mặc định gọi http://localhost:9003; Android emulator gọi http://10.0.2.2:9003. Với điện thoại thật, dùng IP LAN của máy chạy backend:
```
flutter run --dart-define=API_BASE_URL=http://192.168.1.10:9003
```
Backend hiện cho phép CORS từ localhost/127.0.0.1. Token chỉ giữ trong bộ nhớ và mất khi khởi động lại; mật khẩu không lưu xuống thiết bị. Android cho phép HTTP để kết nối backend phát triển cục bộ; khi phát hành cần dùng HTTPS và bỏ usesCleartextTraffic.
## API sản phẩm và giỏ hàng
- GET /api/v1/bouquet?page=1&size=12&name=...&occasionId=...: tìm kiếm/phân trang từ 1.
- GET /api/v1/bouquet/detail/{id}: lấy chi tiết mới nhất.
- GET /api/v1/occasion/all: dịp tặng để lọc hoa.
- GET /api/v1/cart: giỏ hàng của tài khoản hiện tại.
- POST /api/v1/cart/update: gửi form bouquetId, quantity (số lượng cuối cùng).
- POST /api/v1/cart/delete: gửi form bouquetId, quantity=0.

Các yêu cầu gửi Authorization: Bearer <token>, kiểm tra cả HTTP status và code=20000 trong JSON. Hết phiên sẽ xóa dữ liệu tài khoản trên giao diện và yêu cầu đăng nhập lại. Giao diện có trạng thái tải, thử lại, danh sách trống và ảnh thay thế khi ảnh không tải được.

Ảnh lấy từ imageUrl do backend trả về. Cấu hình hiện tại dùng http://localhost:5283; cần bật dịch vụ ảnh ở cổng này hoặc sửa upload.image.rootPath trong backend về nguồn ảnh hoạt động. Trên Android/thiết bị thật, frontend thay hostname localhost trong URL ảnh bằng hostname API, giữ nguyên cổng và đường dẫn.

## Sửa lỗi SQL giỏ hàng
Database đã thiếu bảng Carts và CartItems, gây Invalid object name 'CartItems' và API trả SQL Error Syntax!. Script tạo bảng an toàn nằm ở backend: src/main/resources/db/create-shopping-cart.sql. Script chỉ tạo các bảng còn thiếu, không xóa dữ liệu; chạy script này khi thiết lập database mới.

Đã chạy kiểm tra trực tiếp trên backend cổng 9003: đăng ký/đăng nhập tài khoản kiểm thử tạm, đọc giỏ, thêm sản phẩm, đổi số lượng, xóa và đọc lại giỏ trống. Tài khoản và dữ liệu kiểm thử đã được dọn sạch sau kiểm tra.


## Giao diện theo mẫu HTML
Flutter được dựng theo index.html, list-product.html, detail-product.html, cart.html và checkout.html cùng assets/style.css, assets/app.js. Header có topbar/menu thu gọn; home có slideshow, dịch vụ, story, quote, gallery; danh sách có sidebar trái, tìm kiếm/sắp xếp/lọc giá, phân trang; chi tiết có ảnh, tab mô tả/chăm sóc; cart dùng bảng và nhập số lượng; checkout dùng form hai cột, xác nhận đơn mẫu. Footer có newsletter và ba cột thông tin. Các màn chuyển bố cục theo kích thước thiết bị. Không chỉnh các file HTML mẫu.
