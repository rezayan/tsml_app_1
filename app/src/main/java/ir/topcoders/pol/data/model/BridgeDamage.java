package ir.topcoders.pol.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Bridge_Damages")
public class BridgeDamage implements Serializable {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "damage_id")
    @SerializedName("damage_id")
    public String damageId = "";

    @ColumnInfo(name = "bridge_id")
    @SerializedName("bridge_id")
    public String bridgeId = "";

    @ColumnInfo(name = "element_code")
    @SerializedName("element_code")
    public int elementCode;

    @ColumnInfo(name = "damage_code")
    @SerializedName("damage_code")
    public int damageCode;

    @ColumnInfo(name = "damage_level")
    @SerializedName("damage_level")
    public int damageLevel;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    public String description;

    @ColumnInfo(name = "investigation_status")
    @SerializedName("investigation_status")
    public int investigationStatus;

    @ColumnInfo(name = "investigation_problem")
    @SerializedName("investigation_problem")
    public int investigationProblem;

}
