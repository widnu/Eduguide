package org.study.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.study.data.entity.ProgramDetailEntity;
import org.study.data.entity.ProgramDetailEntity;
import org.study.data.entity.ProgramInformationEntity;

import java.util.List;

@Dao
public interface ProgramDetailDao {
    @Query("SELECT * FROM ProgramDetail")
    List<ProgramDetailEntity> loadAll();

    @Query("SELECT * FROM ProgramDetail where programInformationId = :programInformationId")
    List<ProgramDetailEntity> loadByProgramInformationId(int programInformationId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProgramDetailEntity> programDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ProgramDetailEntity programDetail);
}
