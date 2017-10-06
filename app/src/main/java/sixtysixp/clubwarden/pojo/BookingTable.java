package sixtysixp.clubwarden.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hassan on 6/22/2017.
 */

public class BookingTable {

    @SerializedName("bookingTableID")
    @Expose
    private Integer bookingTableID;
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("clubID")
    @Expose
    private Integer clubID;
    @SerializedName("courtTypeID")
    @Expose
    private Integer courtTypeID;
    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;
    @SerializedName("dateTimeOfBooking")
    @Expose
    private String dateTimeOfBooking;
    @SerializedName("courtNumber")
    @Expose
    private Integer courtNumber;
    @SerializedName("timeSlot")
    @Expose
    private String timeSlot;

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("club")
    @Expose
    private Club club;
    @SerializedName("courtType")
    @Expose
    private CourtType courtType;

    public Integer getBookingTableID() {
        return bookingTableID;
    }

    public void setBookingTableID(Integer bookingTableID) {
        this.bookingTableID = bookingTableID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getClubID() {
        return clubID;
    }

    public void setClubID(Integer clubID) {
        this.clubID = clubID;
    }

    public Integer getCourtTypeID() {
        return courtTypeID;
    }

    public void setcourtTypeID(Integer courtTypeID) {
        this.courtTypeID = courtTypeID;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDateTimeOfBooking() {
        return dateTimeOfBooking;
    }

    public void setDateTimeOfBooking(String dateTimeOfBooking) {
        this.dateTimeOfBooking = dateTimeOfBooking;
    }

    public Integer getCourtNumber() {
        return courtNumber;
    }

    public void setBookingTime(Integer courtNumber) {
        this.courtNumber = courtNumber;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Club getClub() {
        return club;
    }

    public void setUser(Club club) {
        this.club = club;
    }

    public CourtType getCourtType() {
        return courtType;
    }

    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }
}
