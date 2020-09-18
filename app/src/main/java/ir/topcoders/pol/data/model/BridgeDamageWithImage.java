package ir.topcoders.pol.data.model;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BridgeDamageWithImage extends BridgeDamage implements Serializable {

    @ColumnInfo(name = "road_id")
    @SerializedName("road_id")
    public String roadId = "";

    @ColumnInfo(name = "image_file_name")
    @SerializedName("image_file_name")
    public String imageFileName = "";
}
