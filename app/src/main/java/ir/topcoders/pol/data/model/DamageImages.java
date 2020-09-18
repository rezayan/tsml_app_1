package ir.topcoders.pol.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Damage_Images")
public class DamageImages implements Serializable {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "image_id")
    @SerializedName("image_id")
    public String imageId = "";

    @ColumnInfo(name = "damage_id")
    @SerializedName("damage_id")
    public String damageId = "";

    @ColumnInfo(name = "damage_image_filename")
    @SerializedName("damage_image_filename")
    public String damageImageFilename = "";

    @ColumnInfo(name = "gps_x")
    @SerializedName("gps_x")
    public Double gpsX;

    @ColumnInfo(name = "gps_y")
    @SerializedName("gps_y")
    public Double gpsY;

    @ColumnInfo(name = "azimuth")
    @SerializedName("azimuth")
    public Float azimuth;

    @ColumnInfo(name = "capture_date")
    @SerializedName("capture_date")
    public String captureDate = "";
}
