package com.example.a09_pushnotification

enum class NotificationType(val title : String, val id : Int){
    NOMAL("일반 알림",0),
    EXPANDABLE("확장형 알림",1),
    CUSTOM("커스텀 알림",3)
}