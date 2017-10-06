package sixtysixp.clubwarden.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hassan on 7/30/2017.
 */

public class Club {

    @SerializedName("clubID")
    @Expose
    private Integer clubID;
    @SerializedName("clubName")
    @Expose
    private String clubName;

    @SerializedName("clubCity")
    @Expose
    private String clubCity;

    @SerializedName("clubCountry")
    @Expose
    private String clubCountry;

    @SerializedName("bookingTable")
    @Expose
    private List<BookingTable> bookingTable = null;
    @SerializedName("passwordTbl")
    @Expose
    private List<PasswordTbl> passwordTbl = null;
    @SerializedName("user")
    @Expose
    private List<User> user = null;

    public Integer getClubID() {
        return clubID;
    }

    public void setClubID(Integer clubID) {
        this.clubID = clubID;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubCity() {
        return clubCity;
    }

    public void setClubCity(String clubCity) {
        this.clubCity = clubCity;
    }

    public String getClubCountry() {
        return clubCountry;
    }

    public void setClubCountry(String clubCountry) {
        this.clubCountry = clubCountry;
    }

    public List<BookingTable> getBookingTable() {
        return bookingTable;
    }

    public void setBookingTable(List<BookingTable> bookingTable) {
        this.bookingTable = bookingTable;
    }

    public List<PasswordTbl> getPasswordTbl() {
        return passwordTbl;
    }

    public void setPasswordTbl(List<PasswordTbl> passwordTbl) {
        this.passwordTbl = passwordTbl;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
