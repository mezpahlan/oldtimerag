package uk.co.mezpahlan.oldtimerag.data.model.singleitem

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Response(val userTier: String? = null,
               val total: Int = 0,
               val content: Content? = null,
               val status: String? = null)