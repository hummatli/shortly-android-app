package com.mobline.data.local.link.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Link(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "short_link") val shortLink: String,
    @ColumnInfo(name = "long_link") val longLink: String
)
