package com.kotlin.a04_calc.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kotlin.a04_calc.model.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")//쿼리 작성
    fun getAll():List<History>

    @Insert
    fun insertHistory(history: History)//history에 있는것을 넣음.

    @Query("DELETE FROM History")//history에있는 전제 삭제
    fun deleteAll()
}