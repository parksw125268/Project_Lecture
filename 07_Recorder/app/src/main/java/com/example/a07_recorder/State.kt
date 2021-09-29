package com.example.a07_recorder

enum class State {//녹음 상태값에 따라 버튼을 바꿔주려고함
    BEFORE_RECORDING,       //미리 상태들을 정의해놓음.
    ON_RECORDING,
    AFTER_RECORDING,
    ON_PLAYING
}