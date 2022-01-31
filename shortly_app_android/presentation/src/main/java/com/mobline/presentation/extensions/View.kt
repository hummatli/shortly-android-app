package com.mobline.presentation.extensions

import android.view.View
import android.widget.EditText

fun EditText.clear() {
    setText("")
}

//For view's visibility
fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}