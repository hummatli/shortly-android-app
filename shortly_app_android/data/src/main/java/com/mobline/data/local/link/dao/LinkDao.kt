package com.mobline.data.local.link.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobline.data.local.link.model.Link
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkDao {
    @Query("SELECT * FROM link ORDER by id DESC")
    fun getAll(): Flow<List<Link>>

    @Query("SELECT * FROM link where code = :code")
    suspend fun findLinkByCode(code: String): List<Link>

    @Insert
    suspend fun insert(link: Link)

    @Query("DELETE FROM link WHERE code = :code")
    suspend fun deleteByCode(code: String)
}