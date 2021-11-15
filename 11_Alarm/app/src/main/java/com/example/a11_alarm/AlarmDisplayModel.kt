package com.example.a11_alarm

data class AlarmDisplayModel(
    val hour : Int,
    val minute : Int,
    var onOff : Boolean
){
    val tiemText : String
        get() {
            val h = "%02d".format( if(hour<12) hour else hour-12)
            val m = "%02".format(minute)

            return "$h:$m"
        }
    val ampmText : String
        get() {
            return if (hour < 12) "AM" else "PM"
        }
    fun makeDataForDB():String{
        return "$hour:$minute"
    }
}