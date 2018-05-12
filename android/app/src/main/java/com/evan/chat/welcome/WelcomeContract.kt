package com.evan.chat.welcome

import android.content.Intent
import android.graphics.Bitmap
import com.evan.chat.BasePresenter
import com.evan.chat.BaseView

import java.io.File

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:29
 */
interface WelcomeContract {

    interface View : BaseView<Presenter> {

        val file: File

        fun setTitle(title: String)

        fun setWelcomeIV(bitmap: Bitmap)

        fun showMessage(msg: String)

        fun showNextView(intent: Intent)
    }

    interface Presenter : BasePresenter {

        fun result(requestCode: Int, resultCode: Int)

        fun getNextView()

        @Throws(InterruptedException::class)
        fun timeStop()
    }
}
