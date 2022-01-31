package com.mobline.domain.repository

import com.mobline.domain.model.link.Link
import kotlinx.coroutines.flow.Flow


interface LinkLocalRepository {
    fun getLinks(): Flow<List<Link>>
    suspend fun insertLink(link: Link)
    suspend fun deleteLink(link: Link)
}