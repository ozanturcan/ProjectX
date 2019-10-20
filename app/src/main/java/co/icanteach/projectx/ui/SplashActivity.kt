package co.icanteach.projectx.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat
import androidx.core.view.ViewPropertyAnimatorListener
import co.icanteach.projectx.R
import co.icanteach.projectx.RemoteConfigUtil
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit


class SplashActivity : AppCompatActivity() {
    private var compositeDisposable = CompositeDisposable()
    private val animationStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setTheme(R.style.AppTheme)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        supportActionBar?.hide()

        val initRemoteConfigDisposable = Completable.mergeArrayDelayError(
            RemoteConfigUtil.init().subscribeOn(Schedulers.io()),
            initSplashString().subscribeOn(Schedulers.computation())
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(
                TIMEOUT_FOR_FETCHING_REMOTE_CONFIG_PARAMS_IN_MILLISECONDS,
                TimeUnit.MILLISECONDS
            )
            .subscribe({
                println("success")
            }, {
                Toast.makeText(this, "Remote config fetch failed", Toast.LENGTH_SHORT).show()
            })

        compositeDisposable.add(initRemoteConfigDisposable)
    }

    private fun initSplashString(): Completable {
        return Completable.create { emitter ->
            runOnUiThread {
                tvSplashText.text = FirebaseRemoteConfig.getInstance()
                    .getString(RemoteConfigUtil.SPLASH_SCREEN_STRING_KEY)
            }
            emitter.onComplete()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {

        if (!hasFocus || animationStarted) {
            return
        }

        animate()

        super.onWindowFocusChanged(hasFocus)
    }

    private fun animate() {
        val container = findViewById<View>(R.id.container) as ViewGroup


        for (i in 0 until container.childCount) {
            val v = container.getChildAt(i)
            val viewAnimator: ViewPropertyAnimatorCompat = ViewCompat.animate(v)
                .setStartDelay(STARTUP_DELAY.toLong())
                .alphaBy(0f)
                .alpha(1f)
                .setDuration(ANIM_ITEM_DURATION.toLong()).setInterpolator(
                    DecelerateInterpolator(1.2f)
                )
            viewAnimator.setInterpolator(DecelerateInterpolator())
                .setListener(object : ViewPropertyAnimatorListener {
                    override fun onAnimationEnd(view: View?) {
                        this@SplashActivity.moveForward()
                    }

                    override fun onAnimationCancel(view: View?) {
                    }

                    override fun onAnimationStart(view: View?) {
                    }

                }).start()
        }
    }

    companion object {
        const val STARTUP_DELAY = 0
        const val ANIM_ITEM_DURATION = 250
        private const val TIMEOUT_FOR_FETCHING_REMOTE_CONFIG_PARAMS_IN_MILLISECONDS = 2500L
    }

    private fun moveForward() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}