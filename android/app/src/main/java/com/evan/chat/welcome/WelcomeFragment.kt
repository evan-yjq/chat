package com.evan.chat.welcome

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.evan.chat.R

import java.io.File
import java.util.Objects

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:33
 */
class WelcomeFragment : Fragment(), WelcomeContract.View {

    override val file: File
        get() {
            val filesDir = Objects.requireNonNull<FragmentActivity>(activity).filesDir
            return if (filesDir.exists()) {
                filesDir
            } else {
                filesDir.mkdirs()
                filesDir
            }
        }

    private var presenter: WelcomeContract.Presenter? = null

    private var welcomeIV: ImageView? = null
    private var titleTV: TextView? = null

    override fun onResume() {
        super.onResume()
        presenter!!.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.welcome_frag, container, false)
        welcomeIV = root.findViewById(R.id.welcome_image)
        titleTV = root.findViewById(R.id.welcome_title)
        titleTV!!.setOnClickListener {
            try {
                presenter!!.timeStop()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return root
    }

    override fun setWelcomeIV(bitmap: Bitmap) {
        welcomeIV!!.setImageBitmap(bitmap)
    }

    override fun showMessage(msg: String) {
        Snackbar.make(Objects.requireNonNull<View>(view), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter!!.result(requestCode, resultCode)
    }

    override fun showNextView(intent: Intent) {
        startActivity(intent)
        Objects.requireNonNull<FragmentActivity>(activity).finish()
    }

    override fun setTitle(title: String) {
        titleTV!!.text = title
    }

    override fun setPresenter(presenter: WelcomeContract.Presenter) {
        this.presenter = presenter
    }

    override fun isActive(): Boolean {
        return isAdded
    }

    companion object {

        internal fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }
    }
}
