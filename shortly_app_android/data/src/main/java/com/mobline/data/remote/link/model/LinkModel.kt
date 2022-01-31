package com.mobline.data.remote.link.model

import kotlinx.serialization.Serializable

@Serializable
class LinkModel(
    val code: String,
    val short_link: String,
    val full_short_link: String,
    val short_link2: String,
    val full_short_link2: String,
    val share_link: String,
    val full_share_link: String,
    val original_link: String
)