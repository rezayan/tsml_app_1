package ir.topcoders.pol.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Bridges", indices = {@Index(value = "bridge_code", unique = true)})
public class Bridge implements Serializable {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "bridge_id")
    @SerializedName("bridge_id")
    public String bridgeId = "";

    @ColumnInfo(name = "road_id")
    @SerializedName("road_id")
    public String roadId = "";

    @ColumnInfo(name = "registration_date")
    @SerializedName("registration_date")
    public String registrationDate;

    @ColumnInfo(name = "bridge_code")
    @SerializedName("bridge_code")
    public String bridgeCode;

    @ColumnInfo(name = "station")
    @SerializedName("station")
    public double station;

    @ColumnInfo(name = "bridge_name")
    @SerializedName("bridge_name")
    public String bridgeName;

    @ColumnInfo(name = "construction_year")
    @SerializedName("construction_year")
    public String constructionYear;

    @ColumnInfo(name = "gps_x")
    @SerializedName("gps_x")
    public double gpsX;

    @ColumnInfo(name = "gps_y")
    @SerializedName("gps_y")
    public double gpsY;

    @ColumnInfo(name = "structural_system")
    @SerializedName("structural_system")
    public int structuralSystem;

    @ColumnInfo(name = "crossover_damage_type")
    @SerializedName("crossover_damage_type")
    public int crossoverDamageType;

    @ColumnInfo(name = "crater_count")
    @SerializedName("crater_count")
    public long craterCount;

    @ColumnInfo(name = "widest_crater_length")
    @SerializedName("widest_crater_length")
    public double widestCraterLength;

    @ColumnInfo(name = "width")
    @SerializedName("width")
    public double width;

    @ColumnInfo(name = "height")
    @SerializedName("height")
    public double height;

    @ColumnInfo(name = "lane_count")
    @SerializedName("lane_count")
    public long laneCount;

    @ColumnInfo(name = "material_type")
    @SerializedName("material_type")
    public int materialType;

    @ColumnInfo(name = "free_height")
    @SerializedName("free_height")
    public double freeHeight;

    @ColumnInfo(name = "has_alternative_route")
    @SerializedName("has_alternative_route")
    public boolean hasAlternativeRoute;

    @ColumnInfo(name = "satellite_view_image_filename")
    @SerializedName("satellite_view_image_filename")
    public String satelliteViewImageFilename;

    @ColumnInfo(name = "overview_view_image_filename")
    @SerializedName("overview_view_image_filename")
    public String overviewViewImageFilename;

    @ColumnInfo(name = "street_view_image_filename")
    @SerializedName("street_view_image_filename")
    public String streetViewImageFilename;

    @ColumnInfo(name = "crossover_view_image_filename")
    @SerializedName("crossover_view_image_filename")
    public String crossoverViewImageFilename;

    @ColumnInfo(name = "baladast_view_image_filename")
    @SerializedName("baladast_view_image_filename")
    public String baladastViewImageFilename;

    @ColumnInfo(name = "paeandast_view_image_filename")
    @SerializedName("paeandast_view_image_filename")
    public String paeandastViewImageFilename;

    @ColumnInfo(name = "operator_cell_number")
    @SerializedName("operator_cell_number")
    public String operatorCellNumber;

    @ColumnInfo(name = "operator_name")
    @SerializedName("operator_name")
    public String operatorName;

    @ColumnInfo(name = "rank")
    @SerializedName("rank")
    public int rank;

    public transient boolean isNew;
}
