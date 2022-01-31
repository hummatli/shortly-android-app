package com.mobline.data.repository

import com.mobline.data.local.AppDatabase
import com.mobline.data.mapper.toDomain
import com.mobline.data.mapper.toLocal
import com.mobline.domain.model.link.Link
import com.mobline.domain.repository.LinkLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LinkLocalRepositoryImpl(
    private val db: AppDatabase
) : LinkLocalRepository {

    override fun getLinks(): Flow<List<Link>> {
        return db.linkDao().getAll().map { list -> list.map { it.toDomain() }}
    }

    override suspend fun deleteLink(link: Link) {
        db.linkDao().deleteByCode(link.toLocal().code)
    }

    override suspend fun insertLink(link: Link) {
        db.linkDao().insert(link.toLocal())
    }
}