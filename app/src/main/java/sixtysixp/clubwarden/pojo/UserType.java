package sixtysixp.clubwarden.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import sixtysixp.clubwarden.pojo.PasswordTbl;
import sixtysixp.clubwarden.pojo.User;

/**
 * Created by hassan on 7/14/2017.
 */

public class UserType {

    @SerializedName("userTypeID")
    @Expose
    private Integer userTypeID;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("listPasswordTbl")
    @Expose
    private List<PasswordTbl> listPasswordTbl = null;
    @SerializedName("listUser")
    @Expose
    private List<User> listUser = null;

    public Integer getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(Integer userTypeID) {
        this.userTypeID = userTypeID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<User> getUser() {
        return listUser;
    }

    public void setUser(List<User> listUser) {
        this.listUser = listUser;
    }
}
