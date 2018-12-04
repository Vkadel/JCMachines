package com.example.virginia.jcmachines.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "machine_table")
@TypeConverters(converterFeatures.class)
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

        this.id =mid;
        this.series =mseries;
        this.machineFullName =mmachineFullName;
        this.Description =mDescription;
        this.ThumbnailImage =mThumbnailImage;
        this.LargeImageOne =mLargeImageOne;
        this.LargeImageTwo =mLargeImageTwo;
        this.InlIneInstallImage =mInlIneInstallImage;
        this.DimensionsSpecsImage =mDimensionsSpecsImage;
        this.AngleInstallImage =mAngleInstallImage;
        this.DatasheetLink =mDatasheetLink;
        this.LubricationChartLink =mLubricationChartLink;
        this.KeyFeatures =mKeyFeatures;
        this.InstructionalVids =mInstructionalVids;
        this.TechnicalBulletins =mTechnicalBulletins;
        this.SpareParts =mSpareParts;
    }
    public machine(){

    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }



    public String getAngleInstallImage() {
        return this.AngleInstallImage;
    }

    public List<keyFeatures> getKeyFeatures() {
        return this.KeyFeatures;
    }

    public String getDatasheetLink() {
        return this.DatasheetLink;
    }

    public String getDimensionsSpecsImage() {
        return this.DimensionsSpecsImage;
    }

    public List<technicalBulletins> getTechnicalBulletins() {
        return this.TechnicalBulletins;
    }

    public List<spareParts> getSpareParts() {
        return this.SpareParts;
    }
    public List<instructionalVids> getInstructionalVids() {
        return this.InstructionalVids;
    }
    public void setInstructionalVids(List<instructionalVids> instructionalVids) {
        this.InstructionalVids = instructionalVids;
    }


    public void setSpareParts(List<spareParts> spareParts) {
        this.SpareParts = spareParts;
    }

    public void setTechnicalBulletins(List<technicalBulletins> technicalBulletins) {
        this.TechnicalBulletins = technicalBulletins;
    }

    public String getInlIneInstallImage() {
        return this.InlIneInstallImage;
    }


    public String getLargeImageOne() {
        return this.LargeImageOne;
    }

    public String getLargeImageTwo() {
        return this.LargeImageTwo;
    }

    public String getLubricationChartLink() {
        return this.LubricationChartLink;
    }

    public String getMachineFullName() {
        return this.machineFullName;
    }

    public String getSeries() {
        return this.series;
    }

    public String getThumbnailImage() {
        return this.ThumbnailImage;
    }

    public void setAngleInstallImage(String angleInstallImage) {
        this.AngleInstallImage = angleInstallImage;
    }

    public void setDatasheetLink(String datasheetLink) {
        this.DatasheetLink = datasheetLink;
    }

    public void setDimensionsSpecsImage(String dimensionsSpecsImage) {
        this.DimensionsSpecsImage = dimensionsSpecsImage;
    }

    public void setInlIneInstallImage(String inlIneInstallImage) {
        this.InlIneInstallImage = inlIneInstallImage;
    }



    public void setKeyFeatures(List<keyFeatures> keyFeatures) {
        this.KeyFeatures = keyFeatures;
    }

    public void setLargeImageOne(String largeImageOne) {
        this.LargeImageOne = largeImageOne;
    }

    public void setLargeImageTwo(String largeImageTwo) {
        this.LargeImageTwo = largeImageTwo;
    }

    public void setLubricationChartLink(String lubricationChartLink) {
        this.LubricationChartLink = lubricationChartLink;
    }

    public void setMachineFullName(String machineFullName) {
        this.machineFullName = machineFullName;
    }

    public void setSeries(String series) {
        this.series = series;
    }


    public void setThumbnailImage(String thumbnailImage) {
        this.ThumbnailImage = thumbnailImage;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }
}

