package swu.lwh.news.model

data class News(
    val title: String,
    val date: String,
    val author_name: String,
    val thumbnail_pic_s: String?,
    val thumbnail_pic_s02: String?,
    val thumbnail_pic_s03: String?,
    val url: String
)