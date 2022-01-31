package com.mobline.data.mapper

import com.mobline.data.local.link.model.Link

fun Link.toDomain() = com.mobline.domain.model.link.Link(
    code = code,
    shortLink = shortLink,
    longLink = longLink
)
