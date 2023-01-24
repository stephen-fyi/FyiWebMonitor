package icu.fyi.webmon.android

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

class MonitorActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Monitor Sites"
        setContentView(R.layout.activity_monitor)

        val startMonitorBtn = findViewById<Button>(R.id.startMonitoringBtn)
        startMonitorBtn.setOnClickListener {
           /* val intent = Intent()
            applicationContext.startForegroundService(intent)*/
            /*val monitorWorkRequest: WorkRequest =
                OneTimeWorkRequestBuilder<MonitorWorker>().build()
            WorkManager.getInstance(this).enqueue(monitorWorkRequest)*/
            val monitorWorkRequest =
                PeriodicWorkRequestBuilder<MonitorWorker>(15, TimeUnit.MINUTES).build()
            WorkManager.getInstance(this).enqueue(monitorWorkRequest)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                val intent = Intent(this@MonitorActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}