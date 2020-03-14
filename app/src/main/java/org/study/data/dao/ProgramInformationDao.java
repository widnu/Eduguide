package org.study.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.study.data.entity.ProgramInformationEntity;

import java.util.List;

@Dao
public interface ProgramInformationDao {
    @Query("SELECT * FROM ProgramInformation")
    List<ProgramInformationEntity> loadAll();

    @Query("SELECT * FROM ProgramInformation where programCategoryId = :programCategoryId")
    List<ProgramInformationEntity> loadByProgramCategoryId(int programCategoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProgramInformationEntity> programCategories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ProgramInformationEntity programInformation);
}
