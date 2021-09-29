package com.kotlin.a04_calc

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlin.a04_calc.dao.HistoryDao
import com.kotlin.a04_calc.model.History

@Database(entities = [History::class], version = 1)//테이블 네임과 버전정보
abstract class AppDataBase : RoomDatabase(){ //RoomDatabase를 상속받음.
    abstract fun historyDao():HistoryDao
}