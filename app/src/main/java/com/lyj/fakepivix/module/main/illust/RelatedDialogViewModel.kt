package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.User
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户item
 */
class RelatedDialogViewModel(val detailViewModel: IllustDetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var data = ObservableArrayList<Illust>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    init {

    }

    fun load() {

    }
}