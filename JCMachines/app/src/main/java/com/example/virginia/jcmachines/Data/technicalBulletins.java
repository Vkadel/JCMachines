package com.example.virginia.jcmachines.Data;

public class technicalBulletins {
    int id;
    String tittle;
    String description;
    String pdflink;

    public technicalBulletins(int mId,String mTittle,String mdescription,String mpdflink ){
        id=mId;
        tittle=mTittle;
        description=mdescription;
        pdflink=mpdflink;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPdflink(String pdflink) {
        this.pdflink = pdflink;
    }

    public String getPdflink() {
        return pdflink;
    }
}
