package quadrant.gametime.com.spendabot.LoginPage.Login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.AppCompatButton
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import quadrant.gametime.com.spendabot.LoginPage.Home.HomeActivity
import quadrant.gametime.com.spendabot.R
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: AppCompatButton
    private lateinit var auth: FirebaseAuth
    private lateinit var progress: FrameLayout
    private val validEmailAddrRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_login)
        username = findViewById(R.id.loginUsername)
        password = findViewById(R.id.loginPassword)
        login = findViewById(R.id.loginButton)
        progress = findViewById(R.id.progress)
        auth = FirebaseAuth.getInstance()

        /*if (username.text.trim() == "" || password.text.trim() == "") {
            if (username.text.trim() == "") {

            } else if (password.text.trim() == "") {

            }
        }*/

        login.setOnClickListener {
            val email = username.text.toString()
            val pwd = password.text.toString()

            /*if (username.text.toString() == "Akansh" && password.text.toString() == "1234") {
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter the valid credentials and try again!!", Toast.LENGTH_SHORT)
                        .show()
            }*/

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd) && checkEmail(email) && pwd.length >= 8) {
                progress.visibility = View.VISIBLE

                //authenticating the user
                auth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this@LoginActivity) { task ->
                            progress.visibility = View.GONE
                            if (!task.isSuccessful) {
                                if (pwd.length < 8) {
                                    password.error = getString(R.string.minimum_password_length)
                                } else {
                                    Toast.makeText(this@LoginActivity,
                                            "Either the user doesn't exist, or there was some problem....",
                                            Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                finish()
                            }
                        }
            } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd) || !checkEmail(email) || pwd.length < 8) {
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(this@LoginActivity, "Enter email address!", Toast.LENGTH_SHORT).show()
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this@LoginActivity, "Enter password!", Toast.LENGTH_SHORT).show()
                }
                if (!checkEmail(email)) {
                    Toast.makeText(this@LoginActivity, "Please enter a valid email address!!",
                            Toast.LENGTH_SHORT).show()
                }
                if (pwd.length < 8) {
                    Toast.makeText(this@LoginActivity, "Please enter a valid passwords!!",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun checkEmail(email: String): Boolean {
        val matcher = validEmailAddrRegex.matcher(email)
        return matcher.find()
    }
}
