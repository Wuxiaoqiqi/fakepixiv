package com.lyj.fakepivix.app.data.source.remote

import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.*
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.CommentListResp
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc 插画、漫画、小说
 */
class IllustRepository private constructor() {

    companion object {
        val instance by lazy { IllustRepository() }
    }

    var illustList: MutableList<Illust> = mutableListOf()

    /**
     * 获取推荐
     */
    fun loadRecommendIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
//        val ob = when(category) {
//            ILLUST, COMIC -> service.getRecommendIllust(category)
//            else -> service.getHomeNovelRecommendData()
//                    .map { IllustListResp(it.contest_exists, it.novels, it.next_url, it.privacy_policy, it.ranking_novels) }
//        }
        return service.getRecommendIllust(category).doOnNext {
//                    with(it) {
//                        nextUrl = next_url
//                        illusts.forEach {
//                            illust ->
//                            illustList[illust.id.toString()] = illust
//                        }
//                        ranking_illusts.forEach {
//                            illust ->
//                            illustList[illust.id.toString()] = illust
//                        }
//                    }
                }
                .schedulerTransform()
    }

    /**
     * 获取关注的
     * [filter] 筛选条件
     */
    fun loadFollowedIllust(@IllustCategory category: String, @Restrict filter: String = Restrict.ALL): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        var ob = when(category) {
            ILLUST, COMIC -> service.getFollowIllustData(restrict = filter)
            else -> service.getFollowNovelData(restrict = filter)
        }
        return ob.checkEmpty()
                .schedulerTransform()
    }

    /**
     * 获取最新的
     */
    fun loadNewIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        val ob = when(category) {
            ILLUST, COMIC -> service.getNewIllustData(category = category)
            else -> service.getNewNovelData()
        }
        return ob.checkEmpty()
                .schedulerTransform()
    }

    /**
     * 获取好P友
     */
    fun loadFriendIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        var ob = when(category) {
            OTHER -> service.getFriendIllustData()
            else -> service.getFriendNovelData()
        }
        return ob.checkEmpty()
                .schedulerTransform()
    }

    /**
     * 获取用户作品
     */
    fun loadUserIllust(userId: String): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .getUserIllustData(userId)
                .schedulerTransform()
    }

    /**
     * 获取相关作品
     */
    fun loadRelatedIllust(illustId: String): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .getRelatedIllustData(illustId)
                .checkEmpty()
                .schedulerTransform()
    }

    /**
     * 按顺序搜索
     */
    fun searchIllust(@IllustCategory category: String,
                     keyword: String,
                     asc: Boolean,
                     start: String = "",
                     end: String = "" ,
                     strategy: String = Constant.Request.KEY_SEARCH_PARTIAL
    ): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .searchIllust(category, keyword, if (asc) "asc" else "desc", strategy, start, end)
                .checkEmpty()
                .schedulerTransform()
    }

    /**
     * 按热门搜索
     */
    fun searchPopularIllust(@IllustCategory category: String,
                     keyword: String,
                     start: String = "",
                     end: String = "" ,
                     strategy: String = Constant.Request.KEY_SEARCH_PARTIAL
    ): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .searchPopularIllust(category, keyword, strategy, start, end)
                .checkEmpty()
                .schedulerTransform()
    }

    /**
     * 获取作品评论
     */
    fun loadIllustComment(illustId: String): Observable<CommentListResp> {
        return RetrofitManager.instance.apiService
                .getIllustComment(illustId)
                .schedulerTransform()
    }

    fun loadMoreComment(nextUrl: String): Observable<CommentListResp> {
        return RetrofitManager.instance.apiService
                .getIllustComment(nextUrl)
                .schedulerTransform()
    }

    /**
     * 收藏/取消收藏
     */
    fun star(illustId: String, star: Boolean, @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        return if (star)
            RetrofitManager.instance.apiService
                .starIllust(illustId)
                .schedulerTransform()
        else
            RetrofitManager.instance.apiService
                    .unStarIllust(illustId)
                    .schedulerTransform()

    }

    /**
     * 加载更多
     */
    fun loadMore(nextUrl: String, category: String = ILLUST): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        return service.getMoreIllust(nextUrl).doOnNext {
//                    with(it) {
//                        illusts.forEach {
//                            illust ->
//                            illustList[illust.id.toString()] = illust
//                        }
//                    }
                }
                .schedulerTransform()
    }

    fun Observable<IllustListResp>.checkEmpty(): Observable<IllustListResp> = this.map {
        if (it.illusts.isEmpty()) {
            throw ApiException(ApiException.CODE_EMPTY_DATA)
        }
        it
    }

}