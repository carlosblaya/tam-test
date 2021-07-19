package com.carlosblaya.theagilemonkeystest.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.carlosblaya.theagilemonkeystest.R

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Activity?.showMessage(message: String) {
    if (this != null) {
        Snackbar
            .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(R.color.red))
            .show()
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.slideToBottom() {
    val animate = TranslateAnimation(
        0F,  // fromXDelta
        0F,  // toXDelta
        0F,  // fromYDelta
        this.height.toFloat()) // toYDelta
    animate.duration = 300
    animate.fillAfter = false
    this.startAnimation(animate)
}

fun View.slideToTop() {
    this.visibility = View.VISIBLE
    val animate = TranslateAnimation(
        0F,  // fromXDelta
        0F,  // toXDelta
        this.height.toFloat(),  // fromYDelta
        0F) // toYDelta
    animate.duration = 300
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideToRight() {
    this.visibility = View.VISIBLE
    val animate = TranslateAnimation(
        (-this.width).toFloat(),  // fromXDelta
        0F,  // toXDelta
        0F,  // fromYDelta
        0F) // toYDelta
    animate.duration = 500
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideToLeft() {
    this.visibility = View.VISIBLE
    val animate = TranslateAnimation(
        (this.width).toFloat(),  // fromXDelta
        0F,  // toXDelta
        0F,  // fromYDelta
        0F) // toYDelta
    animate.duration = 500
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.startAnimation(AlphaAnimation(0F, 1F).apply {
        duration = 500
        fillAfter = true
    })
}

fun View.fadeOut() {
    this.startAnimation(AlphaAnimation(1F, 0F).apply {
        duration = 200
        fillAfter = true
    })
    this.visibility = View.GONE
}

fun ImageView.fromUrl(url: String) {
    if (url.isNotEmpty())
        Glide.with(this).load(url).placeholder(R.drawable.placeholder).into(this)
    else
        Glide.with(this).load(R.drawable.placeholder).into(this)
}

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

inline fun <reified T: Activity> Context.createIntent() =
    Intent(this, T::class.java)

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }


