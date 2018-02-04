package com.example.kkkk.helloworld.http;



import com.baidu.platform.comapi.map.C;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * author：Anumbrella
 * Date：16/5/29 下午3:52
 */

public interface HttpInterface {
    /**
     * 登录
     *
     * @param requestBody
     * @return
     */
    //192.168.2.251:50000/expand/login
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("user/login")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    /**
     * 查询明星员工
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("user/superStar")
    Call<ResponseBody> StarStaffList();

    /**
     * 紧急通知
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("note/urgency/list")
    Call<ResponseBody> WarringMsg();

    /**
     * 通知公告列表
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("note/list")
    Call<ResponseBody> ReadList(@Header("USER-TOKEN") String token,@Query("isRead") String isRead);

    /**
     * 通知公告详情
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("note/info/{uuid}")
    Call<ResponseBody> ReadDetail(@Header("USER-TOKEN") String token,@Path("uuid") String uuid);

    /**
     * 上传图片
     *
     * @return
     */

    //@Headers({"Content-Type:application/json", "Accept:application/json"})
    @Multipart
    @POST("note/create")
    Call<ResponseBody> LoadImage(@Header("USER-TOKEN") String token, @PartMap Map<String, RequestBody> map, @Part("images") List<MultipartBody.Part> parts);


    /**
     * 任务列表
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("task/cur")
    Call<ResponseBody> TaskList(@Header("USER-TOKEN") String token,@Query("page") String page,@Query("size") String size);

    /**
     * 任务详情
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("task/info/{uuid}")
    Call<ResponseBody> TaskDetail(@Path("uuid") String uuid);

    /**
     * 做任务
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @Multipart
    @POST("task/do")
    Call<ResponseBody> DoTask(@Header("USER-TOKEN") String token,@PartMap Map<String, RequestBody> map,@Part("images") List<MultipartBody.Part> parts);

    /**
     * 上报位置信息
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("user/gps/collect")
    Call<ResponseBody> uploadAddr(@Header("USER-TOKEN") String token,@Body RequestBody requestBody);

    /*
    * 商户位置采集
    *
    */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("merchant/collect/{uuid}")
    Call<ResponseBody> merchantCollect(@Path("uuid") String uuid,@Body RequestBody requestBody);


}
