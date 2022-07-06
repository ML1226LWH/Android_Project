package swu.lwh.news.model

data class Video(
    val id: String,
    val title: String,
    val userName: String,
    val userPic: String,
    val coverUrl: String,
    val playUrl: String,
    val duration: String,
    var isPlayer:Boolean
)