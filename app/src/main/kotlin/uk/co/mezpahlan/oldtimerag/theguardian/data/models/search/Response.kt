package uk.co.mezpahlan.oldtimerag.theguardian.data.models.search

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(val currentPage: Int = 0,
                    val pageSize: Int = 0,
                    val pages: Int = 0,
                    val total: Int = 0,
                    val userTier: String? = null,
                    val startIndex: Int = 0,
                    val results: List<Result> = emptyList(),
                    val status: String? = null,
                    val orderBy: String? = null
)