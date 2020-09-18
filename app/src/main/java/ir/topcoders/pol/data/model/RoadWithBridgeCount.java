package ir.topcoders.pol.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoadWithBridgeCount extends Road implements Serializable {

    @ColumnInfo(name = "bridgeCount")
    @SerializedName("bridgeCount")
    public int bridgeCount;
}
