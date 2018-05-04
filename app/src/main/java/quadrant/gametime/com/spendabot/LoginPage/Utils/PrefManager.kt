package quadrant.gametime.com.spendabot.LoginPage.Utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Akansh on 03-05-2018.
 */
class PrefManager {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var context: Context
    //Shared Prefs Mode
    val PRIVATE_MODE = 0

    private val PREF_NAME = "SpendaBot-Welcome"
    private val IS_FIRST_TIME_LAUNCH  = "IsFirstTimeLaunch"
    private val IS_FIRST_TIME_LOGIN  = "IsFirstTimeLogin"

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun setFirstTimeLogin(isFirstTimeLogin: Boolean) {
        editor.putBoolean(IS_FIRST_TIME_LOGIN, isFirstTimeLogin)
        editor.commit()
    }

    fun isFirstTimeLogin(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME_LOGIN, false)
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor.commit()
    }

    fun isFirstTimeLaunch(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }
}