package com.example.virginia.jcmachines.Data;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface machineDAO {
    @Query("SELECT * FROM machine_table")
    public abstract DataSource.Factory<Integer, machine> getAll();
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

