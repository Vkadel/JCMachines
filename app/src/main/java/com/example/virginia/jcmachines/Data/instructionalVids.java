package com.example.virginia.jcmachines.Data;

public class instructionalVids {
    int id;
    String tittle;
    String description;
    String VideoLink;
    public instructionalVids(int mid,String mTittle,String mDescription,String mVideoLink){
        this.id =mid;
        this.tittle =mTittle;
        this.description =mDescription;
        this.VideoLink =mVideoLink;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return this.tittle;
    }

    public String getVideoLink() {
        return this.VideoLink;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setVideoLink(String videoLink) {
        this.VideoLink = videoLink;
    }
}
