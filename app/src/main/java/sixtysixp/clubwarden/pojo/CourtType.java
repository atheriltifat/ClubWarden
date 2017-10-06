package sixtysixp.clubwarden.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import sixtysixp.clubwarden.pojo.BookingTable;

/**
 * Created by hassan on 8/5/2017.
 */

public class CourtType {

    @SerializedName("courtTypeID")
    @Expose
    private Integer courtTypeID;
    @SerializedName("courtType")
    @Expose
    private String courtType;

    @SerializedName("bookingTable")
    @Expose
    private List<BookingTable> bookingTable = null;

    public Integer getBookingTableID() {
        return courtTypeID;
    }

    public void setBookingTableID(Integer courtTypeID) {
        this.courtTypeID = courtTypeID;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }

    public List<BookingTable> getBookingTable() {
        return bookingTable;
    }

    public void setBookingTable(List<BookingTable> bookingTable) {
        this.bookingTable = bookingTable;
    }
}
