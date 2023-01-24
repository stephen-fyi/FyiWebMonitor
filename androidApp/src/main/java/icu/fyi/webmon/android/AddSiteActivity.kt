package icu.fyi.webmon.android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import icu.fyi.webmon.shared.WebsiteSDK
import icu.fyi.webmon.shared.cache.DatabaseDriverFactory
import icu.fyi.webmon.shared.entity.WebsiteEntry
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AddSiteActivity: AppCompatActivity() {
    private val mainScope = MainScope()

    private val sdk = WebsiteSDK(DatabaseDriverFactory(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Add Site"
        setContentView(R.layout.activity_add_site)

        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf("API", "WWW")

        // access the listView from xml file
        val siteTypeSpin = findViewById<Spinner>(R.id.siteTypeSpin)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, users)
        siteTypeSpin.adapter = arrayAdapter

        val addSiteBtn = findViewById<Button>(R.id.addSiteBtn)
        addSiteBtn.setOnClickListener {
            val newSiteNameText = findViewById<EditText>(R.id.newSiteNameText).text.toString()
            val newSiteUrlText = findViewById<EditText>(R.id.newSiteUrlText).text.toString()
            addWebsite(
                WebsiteEntry(
                    name = newSiteNameText,
                    url = newSiteUrlText,
                    live = false,
                    type= siteTypeSpin.selectedItem.toString(),
                    checkedAt = "Not Checked"
                )
            )
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
                val intent = Intent(this@AddSiteActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addWebsite(websiteEntry: WebsiteEntry) {
        mainScope.launch {
            kotlin.runCatching {
                sdk.addWebsite(websiteEntry)
            }.onSuccess {
                val intent = Intent(this@AddSiteActivity, MainActivity::class.java)
                startActivity(intent)
            }.onFailure {
                Toast.makeText(this@AddSiteActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}