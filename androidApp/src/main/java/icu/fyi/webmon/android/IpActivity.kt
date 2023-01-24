package icu.fyi.webmon.android

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import icu.fyi.webmon.shared.network.ServicesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class IpActivity : AppCompatActivity() {
    private val mainScope = MainScope()
    private val api = ServicesApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "IP Address"
        setContentView(R.layout.activity_ip)
        mainScope.launch {
            kotlin.runCatching {
                val ip = api.getIp()
                val ipText = findViewById<TextView>(R.id.ipText)
                ipText.text = ip

                ipText.setOnClickListener {
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip: ClipData = ClipData.newPlainText("IP Address", ip)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(this@IpActivity, "Copied", Toast.LENGTH_SHORT).show()
                }

            }.onSuccess {
                Toast.makeText(this@IpActivity, "Got IP", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(this@IpActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
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
                val intent = Intent(this@IpActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}