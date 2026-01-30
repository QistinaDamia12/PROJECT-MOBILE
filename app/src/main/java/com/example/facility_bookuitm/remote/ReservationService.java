package com.example.facility_bookuitm.remote;

import com.example.facility_bookuitm.model.Reservation;
import com.example.facility_bookuitm.model.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationService {

    // Get all reservations
    @GET("reservation")
    Call<List<Reservation>> getAllReservations(@Header("api-key") String token);

    // Get reservations by user_id
    @GET("reservations")
    Call<List<Reservation>> getReservationsByUser(
            @Header("api-key") String token,
            @Query("user_id") int userId
    );


    // Add a reservation
    @FormUrlEncoded
    @POST("reservation")
    Call<Reservation> addReservation(
            @Header("api-key") String token,
            @Field("reserveDate") String reserveDate,
            @Field("reserveTime") String reserveTime,
            @Field("reservePurpose") String reservePurpose,
            @Field("reserveStatus") String reserveStatus,
            @Field("facilityID") int facilityID,
            @Field("id") int userID
    );

    // Delete a reservation
    @DELETE("reservation/{reservationID}")
    Call<DeleteResponse> deleteReservation(
            @Header("api-key") String token,
            @Path("reservationID") int reservationID
    );
}
