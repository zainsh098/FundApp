package com.example.fundapp.extensions

import android.view.View

/**
 * Extension function to set the visibility of a [View].
 *
 * This function simplifies setting the visibility of a view to either [View.VISIBLE] or [View.GONE]
 * based on the provided boolean value.
 *
 * @param visible A boolean value that determines the visibility of the view.
 *                If `true`, the view will be set to [View.VISIBLE].
 *                If `false`, the view will be set to [View.GONE].
 */
fun View.visibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
