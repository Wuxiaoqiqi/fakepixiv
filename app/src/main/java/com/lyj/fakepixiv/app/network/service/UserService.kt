package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.LoginResp
import com.lyj.fakepixiv.app.data.model.response.UserInfo
import com.lyj.fakepixiv.app.data.model.response.UserPreviewListResp
import com.lyj.fakepixiv.app.network.retrofit.interceptors.SwitchBaseUrlInterceptor
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author green sun
 *
 * @date 2019/10/16
 *
 * @desc
 */
interface UserService {

    /**
     * 登录
     * header 用于切换baseUrl [SwitchBaseUrlInterceptor]
     * [grantType] 登录方式，账号密码或refreshToken登录二选一
     * @Field("client_id") clientId: String = "MOBrBDS8blbauoSck0ZfDbtuzpyT",
     * @Field("client_secret")clientSecret: String = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj",
     */
    @Headers("SWITCH-HEADER:TAG_AUTH")
    @POST("/auth/token")
    @FormUrlEncoded
    fun login(@Field("client_id") clientId: String = "",
              @Field("client_secret")clientSecret: String = "",
              @Field("get_secure_url")get_secure_url: Boolean = true,
              @Field("include_policy")include_policy: Boolean = true,
              @Field("grant_type")grantType: String = Constant.Net.GRANT_TYPE_PWD,
              @Field("username")userName: String = "",
              @Field("password")password: String = "",
              @Field("device_token")deviceToken: String = "",
              @Field("refresh_token")refreshToken: String = ""): Observable<LoginResp>

    /**
     * 最新-推荐用户
     */
    @GET("/v1/user/recommended")
    suspend fun getUserRecommend(): UserPreviewListResp


    /**
     * 粉丝
     */
    @GET("/v1/user/follower")
    suspend fun getFollower(@Query("user_id") userId: String): UserPreviewListResp

    /**
     * 好P友
     */
    @GET("/v1/user/mypixiv")
    suspend fun getMypixiv(@Query("user_id") userId: String): UserPreviewListResp

    /**
     * 关注的人
     */
    @GET("/v1/user/following")
    suspend fun getFollowing(@Query("user_id") userId: String, @Query("restrict")restrict: String): UserPreviewListResp

    /**
     * 获取用户详情
     */
    @GET("/v1/user/detail")
    suspend fun getUserDetail(@Query("user_id")userId: String): UserInfo


    /**
     * 获取相关用户
     */
    @GET("/v1/user/related")
    fun getRelatedUsers(@Query("seed_user_id")userId: String): Observable<UserPreviewListResp>

    /**
     * 搜索用户
     */
    @GET("/v1/search/user")
    suspend fun searchUser(@Query("word")keyword: String): UserPreviewListResp

    /**
     * 关注
     */
    @POST("/v1/user/follow/add")
    @FormUrlEncoded
    fun followUser(@Field("user_id")userId: String,
                   @Restrict @Field("restrict") restrict: String = Restrict.PUBLIC): Observable<Any>

    @POST("/v1/user/follow/delete")
    @FormUrlEncoded
    fun unFollowUser(@Field("user_id")userId: String): Observable<Any>



    /**
     * 更多用户
     */
    @GET
    suspend fun getMoreUser(@Url nextUrl: String): UserPreviewListResp
}