package quadrant.gametime.com.spendabot.LoginPage.Home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import quadrant.gametime.com.spendabot.LoginPage.Profile.ProfileFragment
import quadrant.gametime.com.spendabot.LoginPage.Utils.PrefManager
import quadrant.gametime.com.spendabot.LoginPage.Utils.ReplaceFragments
import quadrant.gametime.com.spendabot.R

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ReplaceFragments {
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var frame: FrameLayout
    private lateinit var fragment: Fragment
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        frame = findViewById(R.id.frameHome)
        prefManager = PrefManager(this@HomeActivity)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        if (prefManager.isFirstTimeLogin()) {
            fragment = ProfileFragment()
        } else {
            fragment = HomeFragment()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameHome, fragment, fragment.javaClass.simpleName)
                .addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameHome, fragment, fragment.javaClass.simpleName)
                        .addToBackStack(null).commit()
            }
            R.id.nav_profile -> {
                toolbar.title = getString(R.string.profile_string)
                fragment = ProfileFragment()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frameHome, fragment, fragment.javaClass.simpleName)
                        .addToBackStack(null).commit()
            }
            R.id.nav_budget_and_expenses -> {
                toolbar.title = getString(R.string.budget_and_exp)
            }
            R.id.nav_analyze -> {
                toolbar.title = getString(R.string.analyze)
            }
            R.id.nav_settings -> {
                toolbar.title = getString(R.string.settings)
            }
            R.id.nav_accounts -> {
                toolbar.title = getString(R.string.accounts)
            }
            R.id.nav_share -> {

            }
            R.id.nav_contact_us -> {

            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun replaceFrags() {
        toolbar.title = getString(R.string.profile_string)
        fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameHome, fragment, fragment.javaClass.simpleName)
                .addToBackStack(null).commit()
    }
}
