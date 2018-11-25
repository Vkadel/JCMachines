package com.example.virginia.jcmachines.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {machine.class}, version = 1,exportSchema = false)
@TypeConverters({converterFeatures.class,converterInstructionalVids.class,converterTechnicalBulletins.class,converterSpareParts.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract machineDAO machineDAO();
    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "machine_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}