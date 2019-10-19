package co.icanteach.projectx

import android.util.Log

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.reactivex.Completable

class RemoteConfigUtil {

    companion object {

        const val SPLASH_SCREEN_STRING_KEY = "new_splash_text"
        private const val SPLASH_SCREEN_CACHE_EXP = 60000L

        @JvmStatic
        fun init(): Completable {
            return Completable.create { emitter ->
                val remoteConfig = FirebaseRemoteConfig.getInstance()

                val configSettings = FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build()

                remoteConfig.setConfigSettingsAsync(configSettings)
                remoteConfig.setDefaultsAsync(R.xml.remote_config_default)

                emitter.onComplete()
            }.andThen(fetchRemoteConfigParameters(FirebaseRemoteConfig.getInstance()))
        }

        private fun getCacheExpiration(): Long {
            // If is developer mode, cache expiration set to 0, in order to test
            if (BuildConfig.DEBUG) {
                return 0
            }
            return SPLASH_SCREEN_CACHE_EXP
        }

        private fun fetchRemoteConfigParameters(remoteConfig: FirebaseRemoteConfig): Completable {
            return Completable.create { emitter ->


                remoteConfig.fetch(getCacheExpiration())
                    .addOnCompleteListener { task ->
                        run {
                            if (task.isSuccessful) {
                                Log.d(RemoteConfigUtil::class.java.canonicalName, "Fetched")
                                Log.d(
                                    RemoteConfigUtil::class.java.canonicalName,
                                    task.result.toString() + "Activated"
                                )
                                remoteConfig.fetchAndActivate()
                                emitter.onComplete()
                            } else {
                                Log.d(RemoteConfigUtil::class.java.canonicalName, "Fetch Failed")

                                emitter.onError(task.exception!!)
                            }
                        }
                    }
            }
        }

    }
}