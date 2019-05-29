package com.example.virginia.jcmachines.Data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

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
