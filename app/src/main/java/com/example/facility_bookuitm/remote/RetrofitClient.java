package com.example.facility_bookuitm.remote;


public class RetrofitClient
{
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String URL) {

        // first API call, no retrofit instance yet?
        if (retrofit == null) {
            // initialize retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        // return instance of retrofit
        return retrofit;
    }
}
