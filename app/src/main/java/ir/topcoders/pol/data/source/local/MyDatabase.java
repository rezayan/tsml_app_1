package ir.topcoders.pol.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ir.topcoders.pol.data.model.Bridge;
import ir.topcoders.pol.data.model.BridgeDamage;
import ir.topcoders.pol.data.model.DamageImages;
import ir.topcoders.pol.data.model.Road;

@Database(entities = {Road.class, Bridge.class, BridgeDamage.class, DamageImages.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract RoadDao roadDao();
    public abstract BridgeDao bridgeDao();
    public abstract BridgeDamageDao bridgeDamageDao();
    public abstract DamageImageDao damageImageDao();
    public abstract RawDao rawDao();
}
