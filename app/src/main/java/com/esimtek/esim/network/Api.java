package com.esimtek.esim.network;

import com.esimtek.esim.model.BadNoBean;
import com.esimtek.esim.model.ForceBean;
import com.esimtek.esim.model.LocationBean;
import com.esimtek.esim.model.LoggedBean;
import com.esimtek.esim.model.LoginBean;
import com.esimtek.esim.model.NewLocationBean;
import com.esimtek.esim.model.NewRelateBean;
import com.esimtek.esim.model.NewTransferBean;
import com.esimtek.esim.model.OrderBean;
import com.esimtek.esim.model.RelateBean;
import com.esimtek.esim.model.ResultBean;
import com.esimtek.esim.model.TokenBean;
import com.esimtek.esim.model.TransferBean;
import com.esimtek.esim.model.UserBean;
import com.esimtek.esim.model.UserManagerBean;
import com.esimtek.esim.model.workNumberBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 网络接口类
 *
 * @author wang
 * @date 2018/12/12
 */

public interface Api {

    /**
     * 获取token
     *
     * @param staffId 识别码
     * @return token
     */
    @GET("GetToken")
    Call<TokenBean> getToken(@Query("staffId") String staffId);

    /**
     * 根据ESL条码获取订单信息
     *
     * @param eslCode ESL条码
     * @return 订单信息
     */
    @GET("ESIM/pm/get/orderByESLCode")
    Call<OrderBean> orderByESLCode(@Query("eslCode") String eslCode);

    /**
     * 根据纸质条码获取订单信息
     *
     * @param plCode 纸质条码
     * @return 订单信息
     */
    @GET("ESIM/pm/get/orderByPLCode")
    Call<OrderBean> orderByPLCode(@Query("plCode") String plCode);

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @GET("ESIM/pm/get/GetUserData")
    Call<UserBean> userList();

    /**
     * 退出登录
     *
     * @return 退出结果
     */
    @GET("ESIM/pm/get/LogoutUser")
    Call<ResultBean> logout();

    /**
     * 登录
     *
     * @param loginBean 登录信息 包含用户名、密码
     * @return 用户id和权限
     */
    @POST("ESIM/pm/post/retUserJurisdiction")
    Call<LoggedBean> login(@Body LoginBean loginBean);

    /**
     * 绑定ESL和纸质标签
     *
     * @param relateBean ESL条码、纸质标签条码
     * @return 操作结果
     */
    @POST("ESIM/pm/post/relateESLAndPL")
    Call<ResultBean> relateESLAndPL(@Body RelateBean relateBean);

    /**
     * 解绑ESL和纸质标签
     *
     * @param relateBean ESL条码、纸质标签条码
     * @return 操作结果
     */
    @POST("ESIM/pm/post/deleteESL")
    Call<ResultBean> deleteESL(@Body RelateBean relateBean);

    /**
     * 提交坏卡
     *
     * @param badNoBean ESL条码、坏卡数量、提交类型
     * @return 操作结果
     */
    @POST("ESIM/pm/post/badCardNo")
    Call<ResultBean> badCardNo(@Body BadNoBean badNoBean);

    /**
     * 批量转移ESL
     *
     * @param transferBean 转移前ESL条码、转移后ESL条码
     * @return 操作结果
     */
    @POST("ESIM/pm/post/replaceRFIDLable")
    Call<ResultBean> transferESL(@Body TransferBean transferBean);

    /**
     * 清空ESL
     *
     * @param list ESL条码(RFID EPC)列表
     * @return 操作结果
     */
    @POST("ESIM/pm/post/clearESL")
    Call<ResultBean> clearESL(@Body List<String> list);

    /**
     * 更新定位
     *
     * @param locationBean 位置、ESL条码(RFID EPC)列表
     * @return 操作结果
     */
    @POST("ESIM/pm/post/updateLocation")
    Call<ResultBean> updateLocation(@Body LocationBean locationBean);

    /**
     * 人工更新定位
     *
     * @param bean 位置、工单号、ESL条码(RFID EPC)数量、ESL条码(RFID EPC)列表
     * @return 操作结果
     */
    @POST("ESIM/pm/post/mandatoryUpload")
    Call<ResultBean> mandatoryUpload(@Body ForceBean bean);

    /**
     * 人员管理
     *
     * @param bean 用户名、密码、请求类型
     *             请求类型 1 添加用户
     *             请求类型 2 更新密码
     *             请求类型 3 删除用户 密码字段为空
     * @return 操作结果
     */
    @POST("ESIM/pm/post/SendUserRequest")
    Call<ResultBean> userManager(@Body UserManagerBean bean);


    //流程修改

    /**
     * 批量转移ESL 替换原有
     *
     * @param newTransferBean 转移前ESL条码、工单号
     * @return 操作结果
     */
    @POST("ESIM/pm/post/replaceRFIDLable")
    Call<ResultBean> transferESL(@Body NewTransferBean newTransferBean);


    /**
     * 更新定位  新增功能
     *
     * @param newLocationBean 位置、工单号
     * @return 操作结果
     */
    @POST("ESIM/pm/post/updateLocationByWorkNumber")
    Call<ResultBean> updateLocation(@Body NewLocationBean newLocationBean);

    /**
     * 清空ESL  新增功能
     *
     * @param workNumber 工单号
     * @return 操作结果
     */
    @POST("ESIM/pm/post/clearESLForWorkNumber")
    Call<ResultBean> clearESL(@Query("workNumber") String workNumber);

    /**
     * 绑定ESL和纸质标签 替换原有
     *
     * @param newRelateBean ESL条码、工单号
     * @return 操作结果
     */
    @POST("ESIM/pm/post/relateESLAndPL")
    Call<ResultBean> relateESLAndPL(@Body NewRelateBean newRelateBean);

    /**
     * 根据ESL条码或者plCode 纸质条码获取工单信息
     *
     * @param plCodeOrESL ESL条码或者plCode 纸质条码
     * @return 工单信息
     */
    @GET("ESIM/pm/get/GetWorkNumberByBarcodeOrESLcode")
    Call<workNumberBean> getWrokNumByPlCodeOrESL(@Query("plCodeOrESL") String plCodeOrESL);
}