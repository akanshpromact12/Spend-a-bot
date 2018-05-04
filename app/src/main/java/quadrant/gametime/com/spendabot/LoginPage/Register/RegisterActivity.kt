package quadrant.gametime.com.spendabot.LoginPage.Register

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.AppCompatButton
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import quadrant.gametime.com.spendabot.LoginPage.Home.HomeActivity
import quadrant.gametime.com.spendabot.LoginPage.Login.LoginActivity
import quadrant.gametime.com.spendabot.LoginPage.Utils.PrefManager
import quadrant.gametime.com.spendabot.R
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var register: AppCompatButton
    private lateinit var forgotPwd: TextView
    private lateinit var loginExists: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var progress: FrameLayout
    private val validEmailAddrRegex = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
            Pattern.CASE_INSENSITIVE)
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        username = findViewById(R.id.username)
        password = findViewById(R.id.pwd)
        register = findViewById(R.id.registerBtn)
        forgotPwd = findViewById(R.id.forgotPassword)
        loginExists = findViewById(R.id.loginExists)
        progress = findViewById(R.id.progress)
        prefManager = PrefManager(this@RegisterActivity)

        register.setOnClickListener {
            val email = username.text.toString()
            val pwd = password.text.toString()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd) && pwd.length >= 8 && checkEmail(email)) {
                progress.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this@RegisterActivity) { task ->
                            Toast.makeText(this@RegisterActivity, "createUserWithEmail:onComplete:" + task
                                    .isSuccessful, Toast.LENGTH_SHORT).show()
                            progress.visibility = View.GONE

                            if (!task.isSuccessful) {
                                Toast.makeText(this@RegisterActivity, "Authentication failed...",
                                        Toast.LENGTH_SHORT).show()
                            } else {
                                startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                                prefManager.setFirstTimeLogin(true)
                                finish()
                            }
                        }
            } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd) || pwd.length < 8 || !checkEmail(email)) {
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(this@RegisterActivity, "Enter email address!", Toast.LENGTH_SHORT).show()
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this@RegisterActivity, "Enter password!", Toast.LENGTH_SHORT).show()
                }
                if (pwd.length < 8) {
                    Toast.makeText(this@RegisterActivity, "Password too short, enter minimum 8 characters!", Toast.LENGTH_SHORT).show()
                }
                if (!checkEmail(email)) {
                    Toast.makeText(this@RegisterActivity, "Please enter a valid email address!!",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
        forgotPwd.setOnClickListener {

        }
        loginExists.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun checkEmail(email: String): Boolean {
        val matcher = validEmailAddrRegex.matcher(email)
        return matcher.find()
    }

    override fun onResume() {
        super.onResume()
        progress.visibility = View.GONE
    }
}
