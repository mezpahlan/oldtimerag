package uk.co.mezpahlan.oldtimerag.theguardian.data.models.search

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Fields(val thumbnail: String = "",
             val trailText: String = "",
             val headline: String = "")