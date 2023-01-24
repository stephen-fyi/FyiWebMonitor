package icu.fyi.webmon.shared

import icu.fyi.webmon.shared.cache.Database
import icu.fyi.webmon.shared.cache.DatabaseDriverFactory
import icu.fyi.webmon.shared.network.LocationApi
import icu.fyi.webmon.shared.entity.UserEntry

class LocationSrv(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = LocationApi()

    suspend fun getUser(): UserEntry {
        val users = database.getUsers()
        println("Found Users ${users.size}")
        return if (users.isEmpty()){
            val user = api.createUser()
            println("Got User $user")
            database.insertUser(user)
            println("Return User")
            user
        }else {
            users[0]
        }
    }

    fun insertUser(userEntry: UserEntry) {
        database.insertUser(userEntry)
    }

    fun deleteUsers() {
        database.clearUsers()
    }
}