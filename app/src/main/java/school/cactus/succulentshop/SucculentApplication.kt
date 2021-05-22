package school.cactus.succulentshop

import android.app.Application
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper


class SucculentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SucculentNetworkHelper.buildRetrofit(this.applicationContext)
    }

}