package `in`.trentweet.milestone.utils

import android.content.Context
import android.widget.Toast

class Widget(val context: Context) {

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}