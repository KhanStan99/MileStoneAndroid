package `in`.trentweet.milestone.view

import `in`.trentweet.milestone.R
import `in`.trentweet.milestone.presenter.MainActivityPresenter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_login.*
import kotlinx.android.synthetic.main.layout_sign_up.*


class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val presenter = MainActivityPresenter(this, mAuth, applicationContext)
        val currentUser = mAuth!!.currentUser
        if (currentUser != null)
            loginPassed()

        txtToSignup.setOnClickListener {
            flipView.flipTheView()
        }

        txtToLogin.setOnClickListener {
            flipView.flipTheView()
        }

        btnSignUp.setOnClickListener {
            presenter.signUpUser(etEmail.text.toString(), etPassword.text.toString())
        }

        btnLogin.setOnClickListener {
            presenter.signInUser(etEmailLogin.text.toString(), etPasswordLogin.text.toString())
        }

    }


    fun signUpPassed() {
        flipView.flipTheView()
    }

    fun loginPassed() {
        val intent = Intent(applicationContext, MileStoneActivity::class.java)
        startActivity(intent)
        finish()
    }

}
