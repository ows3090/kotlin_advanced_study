package ows.kotlinstudy.sns_application.home

data class ArticleModel(
    val userId: String,
    val title: String,
    val createAt: Long,
    val content: String,
    val imageUrlList: List<String>
){
    constructor(): this("","",0,"", listOf())
}
