package com.mobline.data.remote.link.model

import kotlinx.serialization.Serializable

@Serializable
class ShortenLinkResponseModel(
    val ok: Boolean,
    val result: LinkModel,
)