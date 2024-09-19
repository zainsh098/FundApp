package com.example.fundapp.extensions

/**
 * Calculates the percentage of an integer value.
 *
 * This extension function calculates what percentage of the current integer value
 * corresponds to the specified percentage.
 *
 * @param percent The percentage to calculate (0-100).
 * @return The calculated percentage of the integer value.
 *
 * Example usage:
 * ```kotlin
 * val value = 200
 * val tenPercent = value.percentOf(10)  // tenPercent will be 20
 * ```
 */
fun Int.percentOf(percent: Int): Int {
    return this * percent / 100
}
