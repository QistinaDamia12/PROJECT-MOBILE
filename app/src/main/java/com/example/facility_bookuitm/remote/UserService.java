package com.example.facility_bookuitm.remote;

import com.example.facility_bookuitm.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

        @FormUrlEncoded  //login username
        @POST("user/login")
        Call<User> login(@Field("username") String username, @Field("password") String password);

        //login email
        @FormUrlEncoded
        @POST("user/login")
        Call<User> loginEmail(@Field("email") String username, @Field("password") String password);
    }

