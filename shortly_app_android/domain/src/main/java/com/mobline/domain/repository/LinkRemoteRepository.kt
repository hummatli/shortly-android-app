package com.mobline.domain.repository

import com.mobline.domain.model.link.Link

interface LinkRemoteRepository {
    suspend fun shortenLink(url: String): Link?
}