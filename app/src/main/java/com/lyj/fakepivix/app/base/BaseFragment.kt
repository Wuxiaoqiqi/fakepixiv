package com.lyj.fakepivix.app.base

import android.arch.lifecycle.LifecycleObserver
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import com.gyf.barlibrary.ImmersionBar
import com.gyf.barlibrary.ImmersionFragment
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App
import me.yokeyword.fragmentation.ISupportFragment
import kotlin.properties.Delegates


/**
 * @author greensun
 *
 * @date 2019/3/19
 *
 * @desc
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<out IModel?>?> : ImmersionFragment() {

    protected lateinit var mBinding: V
    abstract var mViewModel: VM
    protected var mToolbar: Toolbar? = null
    private var mImmersionBar: ImmersionBar? = null

    protected var keyboardOpen = false

    var initializer: (() -> Unit)? = null

    protected var onCreated = false
    protected var lazyCreated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, bindLayout(), container, false)
        mViewModel?.let {
            lifecycle.addObserver(mViewModel as LifecycleObserver)
            mBinding.setVariable(bindViewModel(), mViewModel)
        }
        mToolbar = mBinding.root.findViewById(bindToolbar())
        mBinding.root.isClickable = true
        onCreated = true
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(savedInstanceState)
        initializer?.invoke()
    }

    protected fun lazyInit() {
        lazyCreated = true
        mViewModel?.lazyCreated = true
    }

    abstract fun init(savedInstanceState: Bundle?)

    override fun initImmersionBar() {
        var immersionBar = ImmersionBar.with(this)
        mToolbar?.let {
            it.overflowIcon = ContextCompat.getDrawable(App.context, R.drawable.ic_more)
            immersionBar = immersionBar.titleBar(mToolbar)
        }
        immersionBar.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    .setOnKeyboardListener{isOpen, height ->
                        keyboardOpen = isOpen
                        onKeyboardChanged(isOpen, height)
                    }
                    .init()
    }


    /**
     * 软键盘监听
     */
    open fun onKeyboardChanged(isOpen: Boolean, height: Int) {
        keyboardOpen = isOpen
    }

    @LayoutRes
    abstract fun bindLayout() : Int

    open fun bindViewModel() : Int = BR.vm

    open fun bindToolbar() : Int = R.id.toolbar

}