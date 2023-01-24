package icu.fyi.webmon.shared

import icu.fyi.webmon.shared.cache.Database
import icu.fyi.webmon.shared.cache.DatabaseDriverFactory
import icu.fyi.webmon.shared.entity.WebsiteEntry
import icu.fyi.webmon.shared.network.WebsiteApi
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

class WebsiteSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = WebsiteApi()

    @Throws(Exception::class)
    fun getWebsites(forceReload: Boolean): List<WebsiteEntry> {
        return database.getAllWebsites()
    }

    suspend fun checkAllSites(): Boolean {
        var passed = true;
        val sites = database.getAllWebsites()
        for (site in sites) {
            println("type ${site.type}")
            if (site.type == "API")
                site.live = api.getApiSys(site.url)
            else {
                site.live = api.getWww(site.url)
            }
            println("site live? ${site.live}")
            val currentDateTime: LocalDateTime = LocalDateTime.now()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime: String = currentDateTime.format(formatter)
            site.checkedAt = formattedDateTime
            updateWebsite(site)
            if(!site.live!!)
                passed = false
        }
        return passed
    }

    fun addWebsite(websiteEntry: WebsiteEntry) {
        database.insertWebsite(websiteEntry)
    }

    fun deleteWebsite(id: String) {
        database.deleteWebsite(id)
    }

    fun findById(id: String): WebsiteEntry {
        return database.findById(id)
    }

    fun updateWebsite(websiteEntry: WebsiteEntry){
        database.updateWebsite(websiteEntry)
    }
}