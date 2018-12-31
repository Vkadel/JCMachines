package com.example.virginia.jcmachines.Data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = machine.class, version = 2, exportSchema = false)
@TypeConverters({converterFeatures.class,converterInstructionalVids.class,converterTechnicalBulletins.class,converterSpareParts.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract machineDAO machineDAO();
    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(Context context) {
        if (AppDatabase.INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (AppDatabase.INSTANCE == null) {
                    AppDatabase.INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "machine_database").addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return AppDatabase.INSTANCE;
    }
    //adding Migration for add an additional field
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE machine_table "
                    + " ADD COLUMN isWidget INTEGER");
        }
    };
}
