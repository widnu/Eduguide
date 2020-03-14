package org.study.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ProgramDetail")
public class ProgramDetailEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int programInformationId;

    private String name;

    private String programOverview;

    private Double tuitionFeeDomestic;

    private Double tuitionFeeInter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgramInformationId() {
        return programInformationId;
    }

    public void setProgramInformationId(int programInformationId) {
        this.programInformationId = programInformationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgramOverview() {
        return programOverview;
    }

    public void setProgramOverview(String programOverview) {
        this.programOverview = programOverview;
    }

    public Double getTuitionFeeDomestic() {
        return tuitionFeeDomestic;
    }

    public void setTuitionFeeDomestic(Double tuitionFeeDomestic) {
        this.tuitionFeeDomestic = tuitionFeeDomestic;
    }

    public Double getTuitionFeeInter() {
        return tuitionFeeInter;
    }

    public void setTuitionFeeInter(Double tuitionFeeInter) {
        this.tuitionFeeInter = tuitionFeeInter;
    }

    @Override
    public String toString() {
        return "ProgramDetailEntity{" +
                "id=" + id +
                ", programInformationId=" + programInformationId +
                ", name='" + name + '\'' +
                ", programOverview='" + programOverview + '\'' +
                ", tuitionFeeDomestic=" + tuitionFeeDomestic +
                ", tuitionFeeInter=" + tuitionFeeInter +
                '}';
    }
}
