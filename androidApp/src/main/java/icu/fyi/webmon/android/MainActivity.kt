package icu.fyi.webmon.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import icu.fyi.webmon.shared.LocationSrv
import icu.fyi.webmon.shared.WebsiteSDK
import icu.fyi.webmon.shared.cache.DatabaseDriverFactory
import icu.fyi.webmon.shared.cache.User
import icu.fyi.webmon.shared.entity.UserEntry
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val mainScope = MainScope()

    private lateinit var websitesRecyclerView: RecyclerView
    private lateinit var progressBarView: FrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val sdk = WebsiteSDK(DatabaseDriverFactory(this))
    private val location = LocationSrv(DatabaseDriverFactory(this))
    private val websitesRvAdapter = WebsitesRvAdapter(listOf()) {model ->
        val intent = Intent(this@MainActivity, EditSiteActivity::class.java)
        intent.putExtra("Id",model.id)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate", "MainActivity")
        super.onCreate(savedInstanceState)
        title = "fyi.icu"
        setContentView(R.layout.activity_main)

        websitesRecyclerView = findViewById(R.id.websitesListRv)
        progressBarView = findViewById(R.id.progressBar)
        swipeRefreshLayout = findViewById(R.id.swipeContainer)

        websitesRecyclerView.adapter = websitesRvAdapter
        websitesRecyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            displayWebsites(true)
        }

        displayWebsites(false)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("FYI_MONITOR", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onResume() {
        super.onResume()
        displayWebsites(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.check -> {
                checkSites()
                true
            }
            R.id.add -> {
                menuActivity(AddSiteActivity::class.java)
                true
            }
            R.id.ip -> {
                menuActivity(IpActivity::class.java)
                true
            }
            R.id.monitor -> {
                menuActivity(MonitorActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun menuActivity(activity: Class<*>) {
        val intent = Intent(this@MainActivity, activity)
        startActivity(intent)
    }

    private fun checkSites() {
        mainScope.launch {
            kotlin.runCatching {
                sdk.checkAllSites()
            }.onSuccess {
                displayWebsites(false)
            }.onFailure {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            progressBarView.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    private fun displayWebsites(needReload: Boolean) {
        progressBarView.isVisible = true
        mainScope.launch {
            kotlin.runCatching {
                //location.deleteUsers()
                sdk.getWebsites(needReload)
            }.onSuccess {
                websitesRvAdapter.websites = it
                websitesRvAdapter.notifyDataSetChanged()
            }.onFailure {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            progressBarView.isVisible = false
        }
    }
}