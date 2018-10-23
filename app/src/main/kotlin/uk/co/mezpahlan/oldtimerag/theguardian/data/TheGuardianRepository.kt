package uk.co.mezpahlan.oldtimerag.theguardian.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.mezpahlan.oldtimerag.extensions.convertDateFormat
import uk.co.mezpahlan.oldtimerag.extensions.stripHtml
import uk.co.mezpahlan.oldtimerag.extensions.wrapArticleHtml
import uk.co.mezpahlan.oldtimerag.theguardian.article.Article
import uk.co.mezpahlan.oldtimerag.theguardian.data.network.TheGuardianOpenPlatformClient
import uk.co.mezpahlan.oldtimerag.theguardian.feed.FeedItem
import uk.co.mezpahlan.oldtimerag.theguardian.feed.FeedType

/**
 * Repository for The Guardian.
 *
 * Initial implementation is naive API only.
 *
 * TODO: Use Repository pattern to return cached result from here via Room
 */
class TheGuardianRepository(private val feedService: TheGuardianOpenPlatformClient, private val articleService: TheGuardianOpenPlatformClient) {

    fun fetchFeed(feedType: FeedType): Single<List<FeedItem>> {
        return feedService.search()
                .subscribeOn(Schedulers.io())
                .map { t -> t.response.results }
                .filter { t -> t.isNotEmpty() }
                .flattenAsFlowable { t -> t }
                .filter { t ->
                    when (feedType.name) {
                        FeedType.ALL.name -> true
                        else -> t.type == feedType.value
                    }
                }
                .map { t -> FeedItem(t.id, t.fields.thumbnail, t.fields.headline.stripHtml(), t.fields.trailText.stripHtml(), t.webPublicationDate.convertDateFormat(), t.sectionName) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchArticle(id: String): Single<Article> {
        return articleService.singleItem(id)
                .subscribeOn(Schedulers.io())
                .map { t -> t.response.content }
                .map { t -> Article(t.id, t.webTitle, t.fields.body.wrapArticleHtml()) }
                .observeOn(AndroidSchedulers.mainThread())
    }
}