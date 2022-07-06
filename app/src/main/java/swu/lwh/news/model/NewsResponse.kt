package swu.lwh.news.model

data class NewsResponse(
    val reason:String,
    val result: NewsResult,
    val error_code:Int)