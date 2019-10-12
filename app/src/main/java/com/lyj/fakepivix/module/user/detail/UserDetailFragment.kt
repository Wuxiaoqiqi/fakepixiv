package com.lyj.fakepivix.module.user.detail

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.app.utils.bindState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.FragmentUserDetailBinding
import com.lyj.fakepivix.databinding.ItemWorkspaceBinding
import com.lyj.fakepivix.databinding.ItemWorkspaceBindingImpl
import com.lyj.fakepivix.module.common.adapter.IllustAdapter
import com.lyj.fakepivix.module.illust.bookmark.BookmarkFragment
import com.lyj.fakepivix.module.illust.works.WorksFragment
import com.lyj.fakepivix.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.item_works.*
import kotlin.math.abs

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class UserDetailFragment : BackFragment<FragmentUserDetailBinding, UserDetailViewModel>() {

    override var mViewModel: UserDetailViewModel = UserDetailViewModel()

    private var userId: String = ""

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newInstance(userId: String): UserDetailFragment {
            return UserDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_USER_ID, userId)
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            userId = it.getString(EXTRA_USER_ID, "")
            mViewModel.userId = userId
        }
        initBar()
        initUser()
        initIllust()
        initComic()
        initNovel()
        initBookMark()
        mViewModel.loadUserInfo()
    }

    private fun initBar() {
        mBinding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            val range = appBarLayout.totalScrollRange
            val distance = abs(offset)
            mViewModel.collapsed.set(distance >= range)
            var scaleRatio = (range - distance) * 1f / range
            var alphaRatio = (range / 2f - distance) / (range / 2f)
            if (alphaRatio < 0) alphaRatio = 0f
            mBinding.avatar.scaleX = scaleRatio
            mBinding.avatar.scaleY = scaleRatio
            mBinding.avatar.alpha = alphaRatio
        })
        mToolbar?.inflateMenu(R.menu.menu_detail_user)
    }

    private fun initUser() {
        with(mBinding) {
            val adapter = BaseBindingAdapter<Pair<String, String>, ViewDataBinding>(R.layout.item_workspace, mViewModel.workspace, BR.data)
            val layoutManager = LinearLayoutManager(mActivity)
            userContainer.rvWorkspace.layoutManager = layoutManager
            adapter.bindToRecyclerView(userContainer.rvWorkspace)
        }
    }

    private fun initIllust() {
        with(mBinding) {
            illustWorks.more.setOnClickListener {
                goWorks(IllustCategory.ILLUST)
            }
            val adapter = IllustAdapter(mViewModel.illustWorks).apply {
                addItemType(Illust.TYPE_ILLUST, R.layout.item_illust_related, BR.illust)
            }
            val layoutManager = GridLayoutManager(mActivity, 3)
            illustWorks.recyclerView.layoutManager = layoutManager
            adapter.apply {
                bindToRecyclerView(illustWorks.recyclerView)
                bindState(mViewModel.illustWorksState, errorRes = R.layout.layout_error_small) {
                    mViewModel.loadIllustWorks()
                }
            }
            illustWorks.recyclerView.addItemDecoration(CommonItemDecoration.Builder().dividerWidth(1.dp2px(), 1.dp2px()).draw(false).build())
        }
    }

    private fun initComic() {
        with(mBinding) {
            comicWorks.more.setOnClickListener {
                goWorks(IllustCategory.COMIC)
            }
            val adapter = IllustAdapter(mViewModel.comicWorks, false).apply {
                addItemType(Illust.TYPE_COMIC, R.layout.item_home_comic, BR.data)
                addItemType(Illust.TYPE_ILLUST, R.layout.item_home_comic, BR.data)
            }
            val layoutManager = GridLayoutManager(mActivity, 2)
            comicWorks.recyclerView.layoutManager = layoutManager
            adapter.apply {
                bindToRecyclerView(comicWorks.recyclerView)
                bindState(mViewModel.comicWorksState, errorRes = R.layout.layout_error_small) {
                    mViewModel.loadComicWorks()
                }
            }
            comicWorks.recyclerView.addItemDecoration(CommonItemDecoration.Builder().dividerWidth(5.dp2px(), 5.dp2px()).draw(false).build())
        }
    }

    private fun initNovel() {
        with(mBinding) {
            novelWorks.more.setOnClickListener {
                goWorks(IllustCategory.NOVEL)
            }
            val adapter = IllustAdapter(mViewModel.novelWorks, false).apply {
                addItemType(Illust.TYPE_NOVEL, R.layout.item_home_novel, BR.data)
                addItemType(Illust.TYPE_ILLUST, R.layout.item_home_novel, BR.data)
            }
            val layoutManager = LinearLayoutManager(mActivity)
            novelWorks.recyclerView.layoutManager = layoutManager
            adapter.apply {
                bindToRecyclerView(novelWorks.recyclerView)
                bindState(mViewModel.novelWorksState, errorRes = R.layout.layout_error_small) {
                    mViewModel.loadNovelWorks()
                }
            }
            novelWorks.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    /**
     * 插画漫画 小说收藏
     */
    private fun initBookMark() {
        with(mBinding) {
            illustBookmarks.more.setOnClickListener {
                start(BookmarkFragment.newInstance(mViewModel.userId))
            }
            val adapter = IllustAdapter(mViewModel.illustBookmarks).apply {
                addItemType(Illust.TYPE_ILLUST, R.layout.item_illust_related, BR.illust)
                addItemType(Illust.TYPE_COMIC, R.layout.item_illust_related, BR.illust)
            }
            val layoutManager = GridLayoutManager(mActivity, 3)
            illustBookmarks.recyclerView.layoutManager = layoutManager
            adapter.apply {
                bindToRecyclerView(illustBookmarks.recyclerView)
                bindState(mViewModel.illustBookmarksState, errorRes = R.layout.layout_error_small) {
                    mViewModel.loadIllustBookmarks()
                }
            }
            illustBookmarks.recyclerView.addItemDecoration(CommonItemDecoration.Builder().dividerWidth(1.dp2px(), 1.dp2px()).draw(false).build())


            novelBookmarks.more.setOnClickListener {
                start(BookmarkFragment.newInstance(mViewModel.userId, IllustCategory.NOVEL))
            }
            val novelAdapter = IllustAdapter(mViewModel.novelBookmarks, false).apply {
                addItemType(Illust.TYPE_NOVEL, R.layout.item_home_novel, BR.data)
                addItemType(Illust.TYPE_ILLUST, R.layout.item_home_novel, BR.data)
            }
            novelBookmarks.recyclerView.layoutManager = LinearLayoutManager(mActivity)
            novelAdapter.apply {
                bindToRecyclerView(novelBookmarks.recyclerView)
                bindState(mViewModel.novelBookmarksState, errorRes = R.layout.layout_error_small) {
                    mViewModel.loadNovelBookmarks()
                }
            }
            novelBookmarks.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun goWorks(@IllustCategory category: String) {
        start(WorksFragment.newInstance(mViewModel.userInfo.profile.total_illusts, mViewModel.userInfo.profile.total_manga, mViewModel.userInfo.profile.total_novels, mViewModel.userId, category))
    }

    override fun initImmersionBar() {
//        ImmersionBar.with(this)
//                .titleBar(mBinding.toolbar)
//                .statusBarDarkFont(true)
//                .transparentStatusBar()
//                .init()

        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.toolbar)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }

    override fun onDestroyView() {
        if (!diffOrientation) {
            UserRepository.instance - userId
        }
        super.onDestroyView()
    }

    override fun bindLayout(): Int = R.layout.fragment_user_detail
}