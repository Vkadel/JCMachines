package com.example.virginia.jcmachines.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "machine_table")
@TypeConverters({converterFeatures.class})
public class machine {
    @PrimaryKey
    int id;
    @ColumnInfo(name = "series")
    String series;
    @ColumnInfo(name = "machineFullName")
    String machineFullName;
    @ColumnInfo(name = "ThumbnailImage")
    String ThumbnailImage;
    @ColumnInfo(name = "LargeImageOne")
    String LargeImageOne;
    @ColumnInfo(name = "LargeImageTwo")
    String LargeImageTwo;
    @ColumnInfo(name = "Description")
    String Description;
    @ColumnInfo(name = "InlIneInstallImage")
    String InlIneInstallImage;
    @ColumnInfo(name = "DimensionsSpecsImage")
    String DimensionsSpecsImage;
    @ColumnInfo(name = "AngleInstallImage")
    String AngleInstallImage;
    @ColumnInfo(name = "DatasheetLink")
    String DatasheetLink;
    @ColumnInfo(name = "LubricationChartLink")
    String LubricationChartLink;
    @ColumnInfo(name = "KeyFeatures")
    List<keyFeatures> KeyFeatures;
    @ColumnInfo(name = "InstructionalVids")
    List<instructionalVids> InstructionalVids;
    @ColumnInfo(name = "TechnicalBulletins")
    List<technicalBulletins> TechnicalBulletins;
    @ColumnInfo(name = "SpareParts")
    List<spareParts> SpareParts;

    public machine(int mid,String mseries,String mmachineFullName,String mThumbnailImage,
                   String mLargeImageOne,String mLargeImageTwo,String mInlIneInstallImage,String mDescription,
                   String mDimensionsSpecsImage,String mAngleInstallImage,String mDatasheetLink,String mLubricationChartLink,
                   List<keyFeatures> mKeyFeatures,List<instructionalVids> mInstructionalVids,
                   List<technicalBulletins> mTechnicalBulletins,List<spareParts> mSpareParts){

        id=mid;
        series=mseries;
        machineFullName=mmachineFullName;
        Description=mDescription;
        ThumbnailImage=mThumbnailImage;
        LargeImageOne=mLargeImageOne;
        LargeImageTwo=mLargeImageTwo;
        InlIneInstallImage=mInlIneInstallImage;
        DimensionsSpecsImage=mDimensionsSpecsImage;
        AngleInstallImage=mAngleInstallImage;
        DatasheetLink=mDatasheetLink;
        LubricationChartLink=mLubricationChartLink;
        KeyFeatures=mKeyFeatures;
        InstructionalVids=mInstructionalVids;
        TechnicalBulletins=mTechnicalBulletins;
        SpareParts=mSpareParts;
    }
    public machine(){

    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }



    public String getAngleInstallImage() {
        return AngleInstallImage;
    }

    public List<keyFeatures> getKeyFeatures() {
        return KeyFeatures;
    }

    public String getDatasheetLink() {
        return DatasheetLink;
    }

    public String getDimensionsSpecsImage() {
        return DimensionsSpecsImage;
    }

    public List<technicalBulletins> getTechnicalBulletins() {
        return TechnicalBulletins;
    }

    public List<spareParts> getSpareParts() {
        return SpareParts;
    }
    public List<instructionalVids> getInstructionalVids() {
        return InstructionalVids;
    }
    public void setInstructionalVids(List<instructionalVids> instructionalVids) {
        InstructionalVids = instructionalVids;
    }


    public void setSpareParts(List<spareParts> spareParts) {
        SpareParts = spareParts;
    }

    public void setTechnicalBulletins(List<technicalBulletins> technicalBulletins) {
        TechnicalBulletins = technicalBulletins;
    }

    public String getInlIneInstallImage() {
        return InlIneInstallImage;
    }


    public String getLargeImageOne() {
        return LargeImageOne;
    }

    public String getLargeImageTwo() {
        return LargeImageTwo;
    }

    public String getLubricationChartLink() {
        return LubricationChartLink;
    }

    public String getMachineFullName() {
        return machineFullName;
    }

    public String getSeries() {
        return series;
    }

    public String getThumbnailImage() {
        return ThumbnailImage;
    }

    public void setAngleInstallImage(String angleInstallImage) {
        AngleInstallImage = angleInstallImage;
    }

    public void setDatasheetLink(String datasheetLink) {
        DatasheetLink = datasheetLink;
    }

    public void setDimensionsSpecsImage(String dimensionsSpecsImage) {
        DimensionsSpecsImage = dimensionsSpecsImage;
    }

    public void setInlIneInstallImage(String inlIneInstallImage) {
        InlIneInstallImage = inlIneInstallImage;
    }



    public void setKeyFeatures(List<keyFeatures> keyFeatures) {
        KeyFeatures = keyFeatures;
    }

    public void setLargeImageOne(String largeImageOne) {
        LargeImageOne = largeImageOne;
    }

    public void setLargeImageTwo(String largeImageTwo) {
        LargeImageTwo = largeImageTwo;
    }

    public void setLubricationChartLink(String lubricationChartLink) {
        LubricationChartLink = lubricationChartLink;
    }

    public void setMachineFullName(String machineFullName) {
        this.machineFullName = machineFullName;
    }

    public void setSeries(String series) {
        this.series = series;
    }


    public void setThumbnailImage(String thumbnailImage) {
        ThumbnailImage = thumbnailImage;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}

