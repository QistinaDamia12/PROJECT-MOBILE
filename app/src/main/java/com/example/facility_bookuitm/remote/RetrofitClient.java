package com.example.facility_bookuitm.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

        private static Retrofit retrofit = null;

        public static Retrofit getClient(String URL) {

            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://aptitude.my/2025966025/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

            }
            return retrofit;
        }
    }

