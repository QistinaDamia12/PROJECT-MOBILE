package com.example.facility_bookuitm.remote;

import com.example.facility_bookuitm.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    // LOGIN
    @FormUrlEncoded
    @POST("users/login")
    Call<User> login(
            @Field("username") String username,
            @Field("password") String password
    );

    // GET LOGGED IN USER INFO
    @GET("users/me")
    Call<User> getLoggedInUser(
            @Header("api-key") String token
    );

    // UPDATE USER (including changing password)
    @PUT("users/{id}")
    Call<Void> updateUser(
            @Header("api-key") String token,
            @Path("id") int userId,
            @Body User user
    );
}




