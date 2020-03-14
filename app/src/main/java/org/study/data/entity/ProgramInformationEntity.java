package org.study.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Arrays;

@Entity(tableName = "ProgramInformation")
public class ProgramInformationEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int programCategoryId;

    private String qualificationName;

    private String icon;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] iconBlob;

    // 120, 240, 360
    private int credit;

    // 6, 7, 8, 9
    private int level;

    // One year full-time, Full-time for 16 weeks
    private String duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgramCategoryId() {
        return programCategoryId;
    }

    public void setProgramCategoryId(int programCategoryId) {
        this.programCategoryId = programCategoryId;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public byte[] getIconBlob() {
        return iconBlob;
    }

    public void setIconBlob(byte[] iconBlob) {
        this.iconBlob = iconBlob;
    }

    @Override
    public String toString() {
        return "ProgramInformationEntity{" +
                "id=" + id +
                ", programCategoryId=" + programCategoryId +
                ", qualificationName='" + qualificationName + '\'' +
                ", icon='" + icon + '\'' +
                ", iconBlob=" + Arrays.toString(iconBlob) +
                ", credit=" + credit +
                ", level=" + level +
                ", duration='" + duration + '\'' +
                '}';
    }
}