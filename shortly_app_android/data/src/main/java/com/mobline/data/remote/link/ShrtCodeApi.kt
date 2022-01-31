package com.mobline.data.remote.link

import com.mobline.data.remote.link.model.ShortenLinkResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ShrtCodeApi {

    @GET("/v2/shorten")
    suspend fun shortenLink(
        @Query("url") url: String
    ): ShortenLinkResponseModel
}