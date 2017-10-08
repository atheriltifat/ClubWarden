package sixtysixp.clubwarden.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: Ather Iltifat
 */

public class User {

    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("userTypeID")
    @Expose
    private Integer userTypeID;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isBanned")
    @Expose
    private Boolean isBanned;
    @SerializedName("isApproved")
    @Expose
    private Boolean isApproved;
    @SerializedName("inActiveReason")
    @Expose
    private String inActiveReason;
    @SerializedName("joinDate")
    @Expose
    private String joinDate;
    @SerializedName("listBookingTable")
    @Expose
    private List<BookingTable> listBookingTable = null;
    @SerializedName("userType")
    @Expose
    private UserType userType;
    @SerializedName("club")
    @Expose
    private Club club;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(Integer memberTypeID) {
        this.userTypeID = memberTypeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String getInActiveReason() {
        return inActiveReason;
    }

    public void setInActiveReason(String inActiveReason) {
        this.inActiveReason = inActiveReason;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }


    public List<BookingTable> getBookingTable() {
        return listBookingTable;
    }

    public void setBookingTable(List<BookingTable> listBookingTable) {
        this.listBookingTable = listBookingTable;
    }
    public Club getClub() {
    return club;
}

    public void setClub(Club club) {
        this.club = club;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
