package com.example.facility_bookuitm.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * initialize Retrofit with logging
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;

    /**
     * Return instance of retrofit
     * @param URL REST API URL
     * @return retrofit instance
     */
    public static Retrofit getClient(String URL) {

        if (retrofit == null) {

            // Create logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // log request & response body

            // Add interceptor to OkHttp client
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // initialize retrofit with logging client
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}

