package ows.kotlinstudy.sns_application.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
){
    // firebase realtimedatabase 넣기 위해서는 빈 생성자 필요
    constructor(): this("","")
}
