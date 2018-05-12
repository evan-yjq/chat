package com.evan.chat.settings

import android.os.Build
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.evan.chat.Injection
import com.evan.chat.R
import com.evan.chat.util.ActivityUtils

import java.util.Objects

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/28
 * Time: 下午5:17
 */
class SettingsActivity : AppCompatActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_act)

        //设置虚拟按键颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark,theme)
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = resources.getColor(R.color.colorPrimaryDark)
        }

        //设置toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        Objects.requireNonNull<ActionBar>(ab).setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab.setTitle(R.string.settings)

        var settingsFragment: SettingsFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as? SettingsFragment
        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, settingsFragment!!, R.id.contentFrame)
        }

        SettingsPresenter(
                settingsFragment,
                Injection.provideEditSetting(applicationContext),
                Injection.provideGetSettings(applicationContext),
                Injection.provideUseCaseHandler())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
