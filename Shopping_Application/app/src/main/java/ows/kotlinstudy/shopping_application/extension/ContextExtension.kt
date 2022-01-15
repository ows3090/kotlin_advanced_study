package ows.kotlinstudy.shopping_application.extension

import android.content.Context
import android.widget.Toast


internal fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}