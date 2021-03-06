package quadrant.gametime.com.spendabot.LoginPage.Home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import quadrant.gametime.com.spendabot.LoginPage.Utils.PrefManager

import quadrant.gametime.com.spendabot.R
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var monthName: TextView
    private lateinit var noBudget: TextView
    private lateinit var currDate: String
    private lateinit var progress: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        monthName = rootView.findViewById(R.id.monthName)
        noBudget = rootView.findViewById(R.id.noBudget)
        progress = rootView.findViewById(R.id.expProgress)

        currDate = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        monthName.text = currDate
        if (progress.progress == 0) {
            noBudget.visibility = View.VISIBLE
        } else {
            noBudget.visibility = View.GONE
        }

        return rootView
    }
}// Required empty public constructor
