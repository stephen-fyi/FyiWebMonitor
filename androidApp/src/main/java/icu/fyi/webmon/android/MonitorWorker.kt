package icu.fyi.webmon.android

import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.work.Worker
import androidx.work.WorkerParameters
import icu.fyi.webmon.shared.WebsiteSDK
import icu.fyi.webmon.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MonitorWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    private val mainScope = MainScope()
    private val sdk = WebsiteSDK(DatabaseDriverFactory(appContext))

    override fun doWork(): Result {
        mainScope.launch {
            kotlin.runCatching {
                val passed = sdk.checkAllSites()
                if(!passed) {
                    var builder = NotificationCompat.Builder(applicationContext, "FYI_MONITOR")
                        .setSmallIcon(R.drawable.ic_stat_fyi)
                        .setContentTitle("Site Failed")
                        .setContentText("One or more sites are down")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    with(NotificationManagerCompat.from(applicationContext)) {
                        notify(134542, builder.build())
                    }
                }
            }.onSuccess {
            }.onFailure {
            }
        }
        return Result.success()
    }

}