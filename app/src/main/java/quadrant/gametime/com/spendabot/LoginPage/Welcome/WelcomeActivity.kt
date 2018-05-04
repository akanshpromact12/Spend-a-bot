package quadrant.gametime.com.spendabot.LoginPage.Welcome

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import quadrant.gametime.com.spendabot.R
import android.view.ViewGroup
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.support.v4.view.PagerAdapter
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import quadrant.gametime.com.spendabot.LoginPage.Utils.PrefManager
import android.text.Html
import android.view.WindowManager
import quadrant.gametime.com.spendabot.LoginPage.Main.MainActivity


class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: MutableList<TextView>
    private lateinit var layouts: IntArray
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefManager = PrefManager(this@WelcomeActivity)
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen()
            finish()
        }

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_welcome)

        viewPager = findViewById(R.id.view_pager)
        dotsLayout = findViewById(R.id.layoutDots)
        btnSkip = findViewById(R.id.btn_skip)
        btnNext = findViewById(R.id.btn_next)

        layouts = intArrayOf(R.layout.slide_1, R.layout.slide_2, R.layout.slide_3)
        addBottomDots(0)

        changeStatusBarColor()

        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager.adapter = myViewPagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                addBottomDots(position)

                if (position == layouts.size - 1) {
                    btnNext.text = getString(R.string.start)
                    btnSkip.visibility = View.GONE
                } else {
                    btnNext.text = getString(R.string.next)
                    btnSkip.visibility = View.VISIBLE
                }
            }
        })

        btnSkip.setOnClickListener {
            launchHomeScreen()
        }

        btnNext.setOnClickListener {
            val current = getItem(+1)
            if (current < layouts.size) {
                viewPager.setCurrentItem(current)
            } else {
                launchHomeScreen()
            }
        }
    }

    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun addBottomDots(currentPage: Int) {
        dots = ArrayList()

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        dotsLayout.removeAllViews()
        for (i in 0 until dots.size) {
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].textSize = 35F
            dots[i].setTextColor(colorsInactive[currentPage])
            dotsLayout.addView(dots[i])
        }

        if (dots.size > 0) {
            dots[currentPage].setTextColor(colorsActive[currentPage])
        }
    }

    private fun launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false)
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}
