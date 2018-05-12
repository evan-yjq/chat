package com.evan.chat.welcome

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.evan.chat.Injection
import com.evan.chat.R
import com.evan.chat.util.ActivityUtils

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:23
 */
@SuppressLint("Registered")
open class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_act)

//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        var welcomeFragment: WelcomeFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as? WelcomeFragment

        if (welcomeFragment == null) {
            welcomeFragment = WelcomeFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, welcomeFragment, R.id.contentFrame)
        }
        WelcomePresenter(welcomeFragment,
                Injection.provideGetAutoUser(applicationContext),
                Injection.provideUseCaseHandler(),
                Injection.provideSignInUser(applicationContext),
                Injection.provideGetSetting(applicationContext),
                applicationContext)
    }
}
