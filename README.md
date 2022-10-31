# Bài tập lớn OOP - Bomberman Game

## Giới thiệu
Yêu cầu của bài tập lớn Bomberman Game là viết một phiên bản Java mô phỏng lại trò chơi [Bomberman](https://www.youtube.com/watch?v=mKIOVwqgSXM) kinh điển của NES.

## Thành viên nhóm

| Họ và tên     | Mã sinh viên |
| ------------- | ------------ |
| Hoàng Phi Hùng | 21020074     |
| Nguyễn Việt Hưng  | 21020334     |

## Mô tả về các đối tượng trong trò chơi
Nếu bạn đã từng chơi Bomberman, bạn sẽ cảm thấy quen thuộc với những đối tượng này. Chúng được được chia làm hai loại chính là nhóm đối tượng động (*Bomber*, *Enemy*, *Bomb*) và nhóm đối tượng tĩnh (*Grass*, *Wall*, *Brick*, *Door*, *Item*).

- ![](res/sprites/player_down.png) *Bomber* là nhân vật chính của trò chơi. Bomber có thể di chuyển theo 4 hướng trái/phải/lên/xuống theo sự điều khiển của người chơi. 
- ![](res/sprites/balloom_left1.png) *Enemy* là các đối tượng mà Bomber phải tiêu diệt hết để có thể qua Level. Enemy có thể di chuyển ngẫu nhiên hoặc tự đuổi theo Bomber tùy theo loại Enemy. Các loại Enemy sẽ được mô tả cụ thể ở phần dưới.
- ![](res/sprites/bomb.png) *Bomb* là đối tượng mà Bomber sẽ đặt và kích hoạt tại các ô Grass. Khi đã được kích hoạt, Bomber và Enemy không thể di chuyển vào vị trí Bomb. Tuy nhiên ngay khi Bomber vừa đặt và kích hoạt Bomb tại ví trí của mình, Bomber có một lần được đi từ vị trí đặt Bomb ra vị trí bên cạnh. Sau khi kích hoạt 2s, Bomb sẽ tự nổ, các đối tượng *Flame* ![](res/sprites/explosion_horizontal.png) được tạo ra.


- ![](res/sprites/grass.png) *Grass* là đối tượng mà Bomber và Enemy có thể di chuyển xuyên qua, và cho phép đặt Bomb lên vị trí của nó
- ![](res/sprites/wall.png) *Wall* là đối tượng cố định, không thể phá hủy bằng Bomb cũng như không thể đặt Bomb lên được, Bomber và Enemy không thể di chuyển vào đối tượng này
- ![](res/sprites/brick.png) *Brick* là đối tượng được đặt lên các ô Grass, không cho phép đặt Bomb lên nhưng có thể bị phá hủy bởi Bomb được đặt gần đó. Bomber và Enemy thông thường không thể di chuyển vào vị trí Brick khi nó chưa bị phá hủy.


- ![](res/sprites/portal.png) *Portal* là đối tượng được giấu phía sau một đối tượng Brick. Khi Brick đó bị phá hủy, Portal sẽ hiện ra và nếu tất cả Enemy đã bị tiêu diệt thì người chơi có thể qua Level khác bằng cách di chuyển vào vị trí của Portal.

Các *Item* cũng được giấu phía sau Brick và chỉ hiện ra khi Brick bị phá hủy. Bomber có thể sử dụng Item bằng cách di chuyển vào vị trí của Item. Thông tin về chức năng của các Item được liệt kê như dưới đây:
- ![](res/sprites/powerup_speed.png) *SpeedItem* Khi sử dụng Item này, Bomber sẽ được tăng vận tốc di chuyển thêm một giá trị thích hợp
- ![](res/sprites/powerup_flames.png) *FlameItem* Item này giúp tăng phạm vi ảnh hưởng của Bomb khi nổ (độ dài các Flame lớn hơn)
- ![](res/sprites/powerup_bombs.png) *BombItem* Thông thường, nếu không có đối tượng Bomb nào đang trong trạng thái kích hoạt, Bomber sẽ được đặt và kích hoạt duy nhất một đối tượng Bomb. Item này giúp tăng số lượng Bomb có thể đặt thêm một.

Thông tin về các loại *Enemy* được liệt kê như dưới đây:

- ![](res/sprites/balloom_left1.png) *Balloom* là Enemy đơn giản nhất, di chuyển ngẫu nhiên với vận tốc 32 pixels / 64 khung hình, không thể đi xuyên bomb.
- ![](res/sprites/oneal_left1.png) *Oneal* có tốc độ di chuyển đuổi theo bomber gần nhất (sử dụng [thuật toán A*](https://en.wikipedia.org/wiki/A*_search_algorithm)) với vận tốc 32 pixels / 32 khung hình, không định vị được bomb và đi xuyên bomb.
- ![](res/sprites/doll_left1.png) *Doll* Là Enemy di chuyển ngẫu nhiên với vận tốc 32 pixels / 24 khung hình và có thể đi xuyên qua mọi object.
- ![](res/sprites/kondoria_left1.png) *Kondoria* Là Enemy có tốc độ di chuyển đuổi theo bomber gần nhất (sử dụng [thuật toán A*](https://en.wikipedia.org/wiki/A*_search_algorithm)) với vận tốc 32 pixels / 32 khung hình, định vị được bomb và lửa của bomb đồng thời né được bomb nếu đủ thời gian chạy.
- ![](res/sprites/minvo_left1.png) *Duplicate* Là Enemy có tốc độ di chuyển đuổi theo bomber gần nhất (sử dụng [thuật toán A*](https://en.wikipedia.org/wiki/A*_search_algorithm)) với vận tốc 32 pixels / 32 khung hình, không định vị được bomb và đi xuyên bomb. Khi chết thì Enemy này sẽ sinh ra 2 Balloom enemy ngay tại vị trí bị tiêu diệt



## Mô tả game play
### Điều khiển
- Game hoàn toàn điều khiển bằng bàn phím.
- Trong menu: sử dụng `W`, `S` để di chuyển giữa các chế độ chơi, `Enter` để chọn.
- Đối với Bomber sử dụng các phím `W`, `A`, `S`, `D` tương ứng với đi lên, trái, xuống, phải. `Space` để đặt bomb.
- Khi đang trong chế độ chơi Single, có thể sử dụng phím `P` để dừng lại trò chơi. Nhấn `Enter` để tiếp tục trò chơi

### Cơ chế game

- Bomber sẽ bị giết khi va chạm với Enemy hoặc thuộc phạm vi Bomb nổ. Lúc đấy trò chơi kết thúc.
- Enemy bị tiêu diệt khi thuộc phạm vi Bomb nổ
- Một đối tượng thuộc phạm vi Bomb nổ có nghĩa là đối tượng đó va chạm với một trong các tia lửa được tạo ra tại thời điểm một đối tượng Bomb nổ.
- Khi Bomb nổ, một Flame trung tâm![](res/sprites/bomb_exploded.png) tại vị trí Bomb nổ và bốn Flame tại bốn vị trí ô đơn vị xung quanh vị trí của Bomb xuất hiện theo bốn hướng trên![](res/sprites/explosion_vertical.png)/dưới![](res/sprites/explosion_vertical.png)/trái![](res/sprites/explosion_horizontal.png)/phải![](res/sprites/explosion_horizontal.png). Độ dài bốn Flame xung quanh mặc định là 1 đơn vị, được tăng lên khi Bomber sử dụng các FlameItem.

