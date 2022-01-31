package com.mobline.domain.model.link

data class Link(
    val code: String,
    val shortLink: String,
    val longLink: String,
    var isCopied: Boolean = false
)
