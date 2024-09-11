package com.example.fundapp.extensions

fun Int.percentOf(percent: Int): Int {
    return this * percent / 100
}