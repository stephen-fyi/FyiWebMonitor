package icu.fyi.webmon.android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import icu.fyi.webmon.shared.WebsiteSDK
import icu.fyi.webmon.shared.cache.DatabaseDriverFactory
import icu.fyi.webmon.shared.entity.WebsiteEntry
import kotlinx.coroutines.MainScope

class EditSiteActivity: AppCompatActivity() {
    private val mainScope = MainScope()

    private val sdk = WebsiteSDK(DatabaseDriverFactory(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val siteId=intent.getStringExtra("Id")

        title = "Edit Site"
        setContentView(R.layout.activity_edit_site)
        println("edit site $siteId")
        val website: WebsiteEntry = sdk.findById(siteId.toString())

        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(website.type,"API", "WWW")

        // access the listView from xml file
        val siteTypeSpin = findViewById<Spinner>(R.id.siteTypeSpin)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, users)
        siteTypeSpin.adapter = arrayAdapter

        val siteNameText = findViewById<EditText>(R.id.siteNameText)
        val siteUrlText = findViewById<EditText>(R.id.siteUrlText)
        siteNameText.setText(website.name)
        siteUrlText.setText(website.url)

        val deleteSiteBtn = findViewById<Button>(R.id.deleteSiteBtn)
        deleteSiteBtn.setOnClickListener {
            sdk.deleteWebsite(website.id)
            mainActivity()
        }

        val updateSiteBtn = findViewById<Button>(R.id.updateSiteBtn)
        updateSiteBtn.setOnClickListener {
            sdk.updateWebsite(
                WebsiteEntry(
                    id = siteId.toString(),
                    name = siteNameText.text.toString(),
                    url = siteUrlText.text.toString(),
                    live = false,
                    type= siteTypeSpin.selectedItem.toString(),
                    checkedAt = "Updated"
                )
            )
            mainActivity()
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
                val intent = Intent(this@EditSiteActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mainActivity() {
        val intent = Intent(this@EditSiteActivity, MainActivity::class.java)
        startActivity(intent)
    }
}