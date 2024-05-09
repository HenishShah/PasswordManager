package com.example.passwordmanager.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.notifyUser(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}


//for logs
fun logD(tag: String = "PasswordManagerApp", message: String) {
    Log.d(tag, message)
}

fun logE(tag: String = "PasswordManagerApp", message: String) {
    Log.e(tag, message)
}

fun logI(tag: String = "PasswordManagerApp", message: String = "") {
    Log.i(tag, message)
}