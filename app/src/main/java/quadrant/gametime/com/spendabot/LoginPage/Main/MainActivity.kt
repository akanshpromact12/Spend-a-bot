package quadrant.gametime.com.spendabot.LoginPage.Main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.widget.TextView
import quadrant.gametime.com.spendabot.LoginPage.Login.LoginActivity
import quadrant.gametime.com.spendabot.LoginPage.Register.RegisterActivity
import quadrant.gametime.com.spendabot.R

class MainActivity : AppCompatActivity() {
    private lateinit var login: AppCompatButton
    private lateinit var register: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login = findViewById(R.id.loginButton)
        register = findViewById(R.id.registerText)
        login.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
        register.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }
    }
}
