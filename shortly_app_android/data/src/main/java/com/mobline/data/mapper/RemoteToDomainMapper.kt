package com.mobline.data.mapper

import com.mobline.data.remote.link.model.LinkModel
import com.mobline.domain.model.link.Link

fun LinkModel.toDomain() = Link(
    code = code,
    shortLink = full_short_link,
    longLink = original_link
)
