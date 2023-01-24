package icu.fyi.webmon.shared.cache

import icu.fyi.webmon.shared.entity.WebsiteEntry
import icu.fyi.webmon.shared.entity.UserEntry

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllWebsites()
        }
    }

    internal fun getAllWebsites(): List<WebsiteEntry> {
        return dbQuery.selectAllWebsites(::mapWebsiteSelecting).executeAsList()
    }

    internal fun getUsers(): List<UserEntry> {
        return dbQuery.selectUsers(::mapUser).executeAsList()
    }

    private fun mapWebsiteSelecting(
        id: String,
        name: String,
        url: String,
        live: Boolean?,
        type: String,
        checkedAt: String
    ): WebsiteEntry {
        return WebsiteEntry(
            id=id,
            name=name,
            url=url,
            live=live,
            type=type,
            checkedAt=checkedAt
            )
    }

    private fun mapUser(
        uuid: String,
        name: String,
        email: String,
        latitude: Double?,
        longitude: Double?,
    ): UserEntry {
        return UserEntry(
            uuid=uuid,
            name=name,
            email = email,
            latitude = latitude,
            longitude = longitude
        )
    }

    internal fun createWebsite(launches: List<WebsiteEntry>) {
        dbQuery.transaction {
            launches.forEach { launch ->
                insertWebsite(launch)
            }
        }
    }

    internal fun insertWebsite(website: WebsiteEntry) {
        dbQuery.insertWebsite(
            id = website.id,
            name = website.name,
            url = website.url,
            live = website.live,
            type = website.type,
            checkedAt = website.checkedAt
        )
    }

    internal fun insertUser(user: UserEntry) {
        dbQuery.insertUser(
            uuid = user.uuid,
            name = user.name,
            email = user.email,
            latitude = user.latitude,
            longitude = user.longitude
        )
    }

    internal fun updateWebsite(website: WebsiteEntry){
        dbQuery.updateWebsite(
            name = website.name,
            url = website.url,
            live = website.live,
            type = website.type,
            id = website.id,
            checkedAt=website.checkedAt
        )
    }

    internal fun deleteWebsite(id: String){
        dbQuery.deleteWebsite(id=id)
    }

    internal fun findById(id: String): WebsiteEntry{
        val query = dbQuery.findById(id=id).executeAsOne()
        return WebsiteEntry(
            id = query.id,
            name = query.name,
            url = query.url,
            live = query.live,
            type = query.type,
            checkedAt=query.checkedAt
        )
    }

    fun clearUsers() {
        dbQuery.deleteUsers()
    }
}