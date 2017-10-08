package sixtysixp.clubwarden;

import sixtysixp.clubwarden.pojo.BookingTable;
import sixtysixp.clubwarden.pojo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author: Ather Iltifat
 */

public interface APIInterface {

    @GET("Booking/getBookingTblDataByDateFrmApi")
    Call<List<BookingTable>> getRegTblDataByDateFrmInterface(@Header("authorization") String authHeader,
                                                             @Query("requestedDate") String requestedDate,
                                                             @Query("clubID") int clubID);


    @POST("Booking/bookCourt")
    Call<BookingTable> bookCourtFrmInterface(@Header("authorization") String authHeader,
                                             @Query("userID") int userID,
                                             @Query("clubID") int clubID,
                                             @Query("courtTypeID") int courtTypeID,
                                             @Query("bookingDate") String bookingDate,
                                             @Query("courtNumber") int courtNumber,
                                             @Query("timeSlot") String timeSlot,
                                             @Query("dateTimeOfBooking") String dateTimeOfBooking);

    @DELETE("Booking/unbookCourtByCoach")
    Call<List<BookingTable>> unbookCourtByCoachFrmInterface(@Header("authorization") String authHeader,
                                                      @Query("coachID") int coachID,
                                                      @Query("coachClubID") int coachClubID,
                                                      @Query("bookingTblID") int bookingTblID,
                                                      @Query("requestedDate") String requestedDate);


    @DELETE("Booking/unbookCourtByUser")
    Call<List<BookingTable>> unbookCourtByUserFrmInterface(@Header("authorization") String authHeader,
                                                     @Query("userID") int userID,
                                                     @Query("clubID") int clubID,
                                                     @Query("bookingTblID") int bookingTblID,
                                                     @Query("requestedDate") String requestedDate);

    @POST("Password/verifyPassAndAddUser")
    Call<User>  verifyPassAndAddUserFrmApi(@Header("authorization") String authHeader,
                                           @Query("firstName") String firstName,
                                           @Query("lastName") String lastName,
                                           @Query("phoneNo") String phoneNo,
                                           @Query("joinDate") String joinDate,
                                           @Query("password") String password);

    @GET("User/getUserByIdFromController")
    Call<User> getUserByIdFromInterface(@Header("authorization") String authHeader, @Query("userId") int userId);

    @GET("User/getUserByNamePhoneAndDate")
    Call<User> getUserByNamePhoneDateFrmInterface(@Header("authorization") String authHeader,
                                                  @Query("fname") String fname,
                                                  @Query("lname") String lname,
                                                  @Query("phone") String phone,
                                                  @Query("joinDate") String joinDate);
}
