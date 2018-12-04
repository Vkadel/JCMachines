package com.example.virginia.jcmachines.Data;

public class technicalBulletins {
    int id;
    String tittle;
    String description;
    String pdflink;

    public technicalBulletins(int mId,String mTittle,String mdescription,String mpdflink ){
        this.id =mId;
        this.tittle =mTittle;
        this.description =mdescription;
        this.pdflink =mpdflink;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getTittle() {
        return this.tittle;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPdflink(String pdflink) {
        this.pdflink = pdflink;
    }

    public String getPdflink() {
        return this.pdflink;
    }
}
