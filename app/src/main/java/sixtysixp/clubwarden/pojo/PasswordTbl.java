package sixtysixp.clubwarden.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hassan on 7/14/2017.
 */

public class PasswordTbl {

    @SerializedName("passwordID")
    @Expose
    private Integer passwordID;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("userTypeID")
    @Expose
    private Integer userTypeID;
    @SerializedName("UserType")
    @Expose
    private UserType userType;

    public Integer getPasswordID() {
        return passwordID;
    }

    public void setPasswordID(Integer passwordID) {
        this.passwordID = passwordID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(Integer userTypeID) {
        this.userTypeID = userTypeID;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
