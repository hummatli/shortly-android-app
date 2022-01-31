package com.mobline.data.repository

import com.mobline.data.mapper.toDomain
import com.mobline.data.remote.link.ShrtCodeApi
import com.mobline.domain.model.link.Link
import com.mobline.domain.repository.LinkRemoteRepository

class LinkRemoteRepositoryImpl(
    private val api: ShrtCodeApi
) : LinkRemoteRepository {

    override suspend fun shortenLink(url: String): Link? {
        val res = api.shortenLink(url)
        if(res.ok){
            return res.result.toDomain()
        } else {
            return null
        }
    }
}