package com.lyj.fakepivix.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepivix.app.constant.COMIC
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc 主页漫画resp
 */
class HomeComicRepository private constructor() {

    companion object {
        val instance by lazy { HomeComicRepository() }
    }

    var nextUrl = ""

    private val illustList: ArrayMap<String, Illust> = ArrayMap()

    fun loadRecommend(): Observable<IllustListResp> {
        return RetrofitManager.instance
                .apiService
                .getRecommendIllust(COMIC)
                .doOnNext {
                    with(it) {
                        nextUrl = next_url
                        illusts.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                        ranking_illusts.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                    }
                }
                .schedulerTransform()
    }

    fun loadMore(): Observable<IllustListResp> {
        return RetrofitManager.instance
                .apiService
                .getMoreIllust(nextUrl)
                .doOnNext {
                    with(it) {
                        nextUrl = next_url
                        illusts.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                    }
                }
                .schedulerTransform()
    }

    fun clear() {
        illustList.clear()
    }
}