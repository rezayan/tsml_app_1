package ir.topcoders.pol.data.model;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DamageImagesWithFullInfo extends DamageImages implements Serializable {

    @ColumnInfo(name = "road_id")
    @SerializedName("road_id")
    public String roadId = "";

    @ColumnInfo(name = "bridge_id")
    @SerializedName("bridge_id")
    public String bridgeId = "";
}
