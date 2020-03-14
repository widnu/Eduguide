package org.study.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.study.data.entity.ProgramCategoryEntity;

import java.util.List;

@Dao
public interface ProgramCategoryDao {

    @Query("SELECT * FROM ProgramCategory")
    List<ProgramCategoryEntity> loadAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProgramCategoryEntity> programCategories);
}
