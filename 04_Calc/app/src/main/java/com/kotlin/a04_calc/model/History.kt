package com.kotlin.a04_calc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey val uid: Int?,           //?는 널이 가능한 타입
    @ColumnInfo(name = "expression") val expression: String?, //name = 필드네임
    @ColumnInfo(name = "result")val result: String?
)
