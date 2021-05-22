package school.cactus.succulentshop.utils.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.text.TextUtils

class SharedPrefHelper {
    private lateinit var userPreferences: SharedPreferences

    companion object {
        private const val USER_PREFERENCES: String = "user_preferences"
        private const val KEY_JWT: String = "jwt"

        @Volatile
        private var INSTANCE: SharedPrefHelper? = null

        fun getInstance(context: Context): SharedPrefHelper {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = SharedPrefHelper()
                    INSTANCE!!.userPreferences =
                        context.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)
                }
                return INSTANCE!!
            }
        }
    }

    fun setJwt(jwt: String) = userPreferences.edit().putString(KEY_JWT, jwt).apply()

    fun getJwt() = userPreferences.getString(KEY_JWT, "")!!

    fun isJwtExist() = !TextUtils.isEmpty(getJwt())

}