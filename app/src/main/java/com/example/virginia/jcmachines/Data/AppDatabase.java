package com.example.virginia.jcmachines.Data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = machine.class, version = 3, exportSchema = false)
@TypeConverters({converterFeatures.class,converterInstructionalVids.class,converterTechnicalBulletins.class,converterSpareParts.class,converteraugers.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract machineDAO machineDAO();
    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(Context context) {
        if (AppDatabase.INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (AppDatabase.INSTANCE == null) {
                    AppDatabase.INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "machine_database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return AppDatabase.INSTANCE;
    }

}
