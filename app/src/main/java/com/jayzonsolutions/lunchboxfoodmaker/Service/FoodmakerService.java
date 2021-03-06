package com.jayzonsolutions.lunchboxfoodmaker.Service;

import com.jayzonsolutions.lunchboxfoodmaker.model.ApiResponse;
import com.jayzonsolutions.lunchboxfoodmaker.model.Dish;
import com.jayzonsolutions.lunchboxfoodmaker.model.Foodmaker;
import com.jayzonsolutions.lunchboxfoodmaker.model.FoodmakerDishes;
import com.jayzonsolutions.lunchboxfoodmaker.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FoodmakerService {

    @POST("foodmaker/login")
    @FormUrlEncoded
    Call<Foodmaker> foodmakerLogin(@Field("userName") String userName, @Field("password") String password,@Field("token") String token);

    @POST("foodmaker/signup")
    Call<ApiResponse> foodmakerSignup(@Body Foodmaker foodmaker);

    @POST("foodmaker/set-status")
    @FormUrlEncoded
    Call<String> updateFoodmakerStatus(@Field("foodmakerId") Integer foodmakerId,@Field("status") Integer status);



    @GET("foodmaker/foodmakers-list")
    Call<List<Foodmaker>> getFoodmakerList();

    @GET("foodmaker/foodmakers-nearBy-list")
    Call<List<Foodmaker>> getFoodmakerListNearBy(@Query("miles") Integer miles, @Query("lat") Double lat, @Query("longt") Double longt);

    @GET("foodmaker_dishes/foodmakersdishes-list-byfoodmakerid")
    Call<List<FoodmakerDishes>> getDishesByFoodmakerId(@Query("foodmakerId") Integer foodmakerId);


    @GET("foodmaker/get-orderByFoodmakerId")
    Call<List<Order>> getOrdersByFoodmakerId(@Query("foodmakerId") Integer foodmakerId);

    @GET("foodmaker/get-ack-orderByFoodmakerId")
    Call<List<Order>> getAckOrdersByFoodmakerId(@Query("foodmakerId") Integer foodmakerId);

    @GET("foodmaker/get-done-orderByFoodmakerId")
    Call<List<Order>> getDoneOrdersByFoodmakerId(@Query("foodmakerId") Integer foodmakerId);

    @GET("foodmaker_dishes/get-done-orderByFoodmakerId")
    Call<List<Dish>> getdishesByorderId(@Query("orderId") int orderId);
}
