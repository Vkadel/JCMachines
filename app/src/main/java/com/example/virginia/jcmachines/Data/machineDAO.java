package com.example.virginia.jcmachines.Data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

@Dao
public interface machineDAO {
    @Query("SELECT * FROM machine_table")
    public abstract android.arch.paging.DataSource.Factory<Integer, machine> getAll();
    //LiveData<List<machine>> getAll();

    @Query("SELECT * FROM machine_table WHERE id IN (:id)")
    List<machine> loadAllbyID(int[] id);

    @Query("SELECT * FROM machine_table WHERE id IN (:id)")
    machine loadOnebyID(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOne(machine... machines);

    @Delete
    void delete(machine user);
}

