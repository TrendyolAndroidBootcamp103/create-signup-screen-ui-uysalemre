package school.cactus.succulentshop

import android.app.Application
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper


class SucculentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        buildApp()
    }

    private fun buildApp() {
        SucculentNetworkHelper.buildRetrofit()
        SharedPrefHelper.buildPreferences(this.applicationContext)
    }
}