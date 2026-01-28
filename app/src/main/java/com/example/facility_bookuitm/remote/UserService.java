package com.example.facility_bookuitm.remote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.example.facility_bookuitm.model.User;

public interface UserService {

    @FormUrlEncoded
    @POST("users/login")
    Call<User> login(
            @Field("userEmail") String email,
            @Field("userPassword") String password
    );

}