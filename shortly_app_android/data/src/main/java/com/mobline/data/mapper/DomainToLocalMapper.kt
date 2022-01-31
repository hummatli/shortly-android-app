package com.mobline.data.mapper

import com.mobline.domain.model.link.Link

fun Link.toLocal() = com.mobline.data.local.link.model.Link(
    code = code,
    shortLink = shortLink,
    longLink = longLink
)
