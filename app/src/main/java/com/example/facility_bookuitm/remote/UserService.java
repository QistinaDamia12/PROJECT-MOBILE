package com.example.facility_bookuitm.remote;

import android.telecom.Call;
import com.example.facility_bookuitm.model.User;
import com.google.android.gms.fitness.data.Field;

public interface UserService
{
    @FormUrlEncoded
    @POST("user/login")
    Call<User> login(@Field("userID") String username, @Field("userPassword") String password);

}
