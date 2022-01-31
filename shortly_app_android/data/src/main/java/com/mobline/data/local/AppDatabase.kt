package com.mobline.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobline.data.local.link.dao.LinkDao
import com.mobline.data.local.link.model.Link

@Database(entities = [Link::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        val DB_NAME = "local_database"
    }

    abstract fun linkDao(): LinkDao
}