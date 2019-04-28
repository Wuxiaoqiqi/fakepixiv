package com.lyj.fakepivix.module.main.home.novel

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.ComicRepository
import com.lyj.fakepivix.app.data.source.remote.NovelRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.module.main.home.illust.RankViewModel
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 主页小说vm
 */
class HomeNovelViewModel : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    val rankViewModel: RankViewModel = RankViewModel()

    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)


    fun lazyLoad() {
        load()
    }

    fun load() {
        val disposable = NovelRepository.instance
                .loadRecommend()
                .doOnSubscribe {
                    loadState.set(LoadState.Loading)
                    data.clear()
                }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    data.clear()
                    data.addAll(it.novels)
                    rankViewModel.onData(it.ranking_novels)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun loadMore() {
        if (loadMoreState.get() !is LoadState.Loading) {
            val disposable = NovelRepository.instance
                    .loadMore()
                    .doOnSubscribe { loadMoreState.set(LoadState.Loading) }
                    .subscribeBy(onNext = {
                        loadMoreState.set(LoadState.Succeed)
                        data.addAll(it.novels)
                    }, onError = {
                        loadMoreState.set(LoadState.Failed(it))
                    })
            addDisposable(disposable)
        }
    }

}