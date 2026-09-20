### Day structure
Each folder is a separate project.
## Flow like phim

Cả hai API nhận `POST` với body `{"movieId":"<UUID của phim>","userId":"<UUID của user>"}`. Phim và user phải có sẵn trong database. Like lại cùng phim sẽ trả số like hiện tại mà không tăng thêm.

| Cách | Endpoint | Flow khi có nhiều request |
| --- | --- | --- |
| Pessimistic lock | `/api/like/movies/pessimistic` | Trong một transaction, khóa hàng `movie`, kiểm tra user đã like chưa, lưu like và tăng `likeCount`; request khác cùng phim chờ khóa được thả. |
| Optimistic lock | `/api/like/movies/optimistic` | Trong một transaction, đọc phim cùng `version`, kiểm tra và lưu like, tăng `likeCount`. Nếu transaction khác đã cập nhật phim trước, API trả `409 Conflict`; client gửi lại request để đọc version mới và thử lại. |

Bảng `movie_likes` có unique key `(user_id, movie_id)` để ngăn like trùng khi request chạy đồng thời. Cả bản ghi like và `likeCount` được cập nhật trong cùng transaction. Response thành công có dạng `{"movieId":"<UUID>","likeCount":1}`.

Cả hai flow đều cập nhật và flush hàng `movie` trước khi insert vào `movie_likes`, để các request lấy khóa theo cùng thứ tự và tránh vòng chờ giữa hai cách like.

Nếu database đã có dữ liệu trước khi thêm `@Version`, cần gán `version = 0` cho các hàng `movie` cũ trước khi chạy flow optimistic.

## Chạy JUnit test

Trong thư mục `Baitap-Day1`, chạy `./mvnw.cmd test` trên Windows. Test dùng H2 riêng trong `src/test/resources/application.yaml`, không cần khởi động PostgreSQL hoặc ứng dụng. Xem kết quả tại `target/surefire-reports/org.example.baitap.MovieLikeServiceTests.txt`.

`MovieLikeServiceTests` kiểm tra hai user like đồng thời bằng pessimistic lock (`likeCount = 2`, hai bản ghi like, `version = 2`), like trùng không tăng, hai user like tuần tự bằng optimistic lock, và entity giữ version cũ bị từ chối khi cập nhật.
