package `in`.trentweet.milestone.presenter

import `in`.trentweet.milestone.utils.LoggerUtils
import `in`.trentweet.milestone.utils.Widget
import `in`.trentweet.milestone.view.MainActivity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class MainActivityPresenter(
    private val mainActivity: MainActivity,
    private val mAuth: FirebaseAuth?,
    applicationContext: Context
) {

    val widget = Widget(applicationContext)

    fun signUpUser(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(mainActivity) { task ->
                if (task.isSuccessful) {
                    LoggerUtils().error("Sign Up Success")
                    widget.showToast("Sign Up Success -> Login now")
                    mainActivity.signUpPassed()
                } else {
                    LoggerUtils().error("Sign Up Failed -> ${task.exception}")
                    widget.showToast("Sign Up Failed -> ${task.exception}")
                }
            }
    }

    fun signInUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            widget.showToast("Please Enter Email and Password")
        } else {
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mainActivity) { task ->
                    if (task.isSuccessful) {
                        mainActivity.loginPassed()
                    } else {
                        // If sign in fails, display a message to the user.
                        LoggerUtils().error("signInWithEmail:failure " + task.exception)
                        widget.showToast("Login Failed -> ${task.exception}")
                    }
                }
        }

    }

}