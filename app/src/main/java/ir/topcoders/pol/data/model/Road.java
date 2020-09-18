package ir.topcoders.pol.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Roads", indices = {@Index(value = "code", unique = true)})
public class Road implements Serializable {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "road_id")
    @SerializedName("road_id")
    public String roadId;

    @ColumnInfo(name = "registration_date")
    @SerializedName("registration_date")
    public String registrationDate;

    @ColumnInfo(name = "province")
    @SerializedName("province")
    public int province;

    @ColumnInfo(name = "county")
    @SerializedName("county")
    public int county;

    @ColumnInfo(name = "start_name")
    @SerializedName("start_name")
    public String startName;

    @ColumnInfo(name = "stop_name")
    @SerializedName("stop_name")
    public String stopName;

    @ColumnInfo(name = "start_gps_x")
    @SerializedName("start_gps_x")
    public double startGpsX;

    @ColumnInfo(name = "start_gps_y")
    @SerializedName("start_gps_y")
    public double startGpsY;

    @ColumnInfo(name = "code")
    @SerializedName("code")
    public String code;

    @ColumnInfo(name = "direction")
    @SerializedName("direction")
    public int direction;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    public int type;

    public transient boolean isNew;
    public transient boolean isSelected;
}
