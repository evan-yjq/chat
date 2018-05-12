package com.evan.chat.welcome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import com.evan.chat.PublicData
import com.evan.chat.UseCase
import com.evan.chat.UseCaseHandler
import com.evan.chat.data.source.model.User
import com.evan.chat.domain.usecase.Setting.GetSetting
import com.evan.chat.domain.usecase.User.SignInUser
import com.evan.chat.face.FaceActivity
import com.evan.chat.face.FaceFragment
import com.evan.chat.friends.FriendsActivity
import com.evan.chat.logreg.LogRegActivity
import com.evan.chat.util.AppExecutors
import com.evan.chat.util.PropertiesUtils
import com.evan.chat.domain.usecase.User.GetAutoUser
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.BitmapCallback
import okhttp3.Call

import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

import com.evan.chat.settings.domain.SettingKey.AUTO_LOGIN_OPEN_ID
import com.evan.chat.settings.domain.SettingKey.FACE_LOGIN_OPEN_ID
import com.evan.chat.util.Objects.checkNotNull

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:34
 */
class WelcomePresenter internal constructor(view: WelcomeContract.View, getAutoUser: GetAutoUser,
                                            useCaseHandler: UseCaseHandler, signInUser: SignInUser,
                                            getSetting: GetSetting, context: Context) : WelcomeContract.Presenter {

    private val view: WelcomeContract.View = checkNotNull(view, "view cannot be null!")
    private val getAutoUser: GetAutoUser = checkNotNull(getAutoUser, "getAutoUser cannot be null!")
    private val getSetting: GetSetting = checkNotNull(getSetting, "getSetting cannot be null!")
    private val signInUser: SignInUser = checkNotNull(signInUser, "signInUser cannot be null!")
    private val useCaseHandler: UseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null!")
    private val context: Context = checkNotNull(context, "context cannot be null!")

    private var mAutoSignLock = true
    private var autoUser: User? = null

    private var appExecutors: AppExecutors? = null

    @Volatile
    private var timerStop = false
    private var timer: Timer? = null

    init {
        view.setPresenter(this)
    }

    override fun start() {
        appExecutors = AppExecutors()
        getWelcome(PropertiesUtils.getInstance().getProperty("welcome_background_url", false))
        appExecutors!!.networkIO().execute {
            try {
                CountDown3()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        loadSetModule()
    }

    private fun loadSetModule(){
        setAutoUser()
    }

    //获取欢迎界面图片
    private fun getWelcome(url: String) {
        appExecutors!!.networkIO().execute {
            OkHttpUtils.get().url(url)
                    .build().execute(object : BitmapCallback() {
                        override fun onError(call: Call, e: Exception, id: Int) {
                            view.showMessage("当前网络不可用，请检查您的网络设置")
                        }

                        override fun onResponse(response: Bitmap, id: Int) {
                            view.setWelcomeIV(response)
                        }
                    })
        }
    }

    private fun isAutoLogin() {
        useCaseHandler.execute(getSetting, GetSetting.RequestValues(AUTO_LOGIN_OPEN_ID),
                object : UseCase.UseCaseCallback<GetSetting.ResponseValue> {
                    override fun onSuccess(response: GetSetting.ResponseValue) {
                        if (java.lang.Boolean.parseBoolean(response.setting.value)) {
                            signInUser()
                        } else {
                            autoUser = null
                            mAutoSignLock = false
                        }
                    }

                    override fun onError() {
                        autoUser = null
                        mAutoSignLock = false
                    }
                })
    }

    private fun signInUser() {
        useCaseHandler.execute(signInUser, SignInUser.RequestValues(autoUser!!.account, autoUser!!.password, view.file),
                object : UseCase.UseCaseCallback<SignInUser.ResponseValue> {
                    override fun onSuccess(response: SignInUser.ResponseValue) {
                        autoUser = response.user
                        PublicData.user = autoUser
                        mAutoSignLock = false
                    }

                    override fun onError() {
                        autoUser = null
                        mAutoSignLock = false
                    }
                })
    }

    private fun setAutoUser() {
        useCaseHandler.execute(getAutoUser, GetAutoUser.RequestValues(),
                object : UseCase.UseCaseCallback<GetAutoUser.ResponseValue> {
                    override fun onSuccess(response: GetAutoUser.ResponseValue) {
                        autoUser = response.user
                        isAutoLogin()
                    }

                    override fun onError() {
                        autoUser = null
                        mAutoSignLock = false
                    }
                })
    }

    private fun isFaceLogin() {
        useCaseHandler.execute(getSetting, GetSetting.RequestValues(FACE_LOGIN_OPEN_ID),
                object : UseCase.UseCaseCallback<GetSetting.ResponseValue> {
                    override fun onSuccess(response: GetSetting.ResponseValue) {
                        if (view.isActive) {
                            if (java.lang.Boolean.parseBoolean(response.setting.value)) {
                                val intent = Intent(context, FaceActivity::class.java)
                                intent.putExtra(FaceActivity.EXTRA_FACE_VIEW, FaceFragment.DIST)
                                intent.putExtra(FaceActivity.EXTRA_IS_RESULT, true)
                                (view as Fragment).startActivityForResult(intent, FaceActivity.REQUEST_FACE_DISK)
                            } else {
                                view.showNextView(Intent(context, FriendsActivity::class.java))
                            }
                        }
                    }

                    override fun onError() {
                        if (view.isActive)
                            view.showNextView(Intent(context, FriendsActivity::class.java))
                    }
                })
    }

    @Throws(InterruptedException::class)
    private fun CountDown3() {
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                //可以添加每秒做的事
            }
        }, 0, 1000)
        TimeUnit.SECONDS.sleep(3)
        if (!timerStop) timeStop()
    }

    @Throws(InterruptedException::class)
    override fun timeStop() {
        timerStop = true
        while (mAutoSignLock)
            Thread.sleep(300)
        timer!!.cancel()
        getNextView()
    }

    override fun result(requestCode: Int, resultCode: Int) {
        if (FaceActivity.REQUEST_FACE_DISK == requestCode)
            if (view.isActive)
                if (Activity.RESULT_OK == resultCode)
                    view.showNextView(Intent(context, FriendsActivity::class.java))
                else
                    view.showNextView(Intent(context, LogRegActivity::class.java))
    }

    override fun getNextView() {
        if (autoUser == null) {
            if (view.isActive) {
                view.showNextView(Intent(context, LogRegActivity::class.java))
            }
        } else {
            isFaceLogin()
        }
    }
}
