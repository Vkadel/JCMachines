package com.example.virginia.jcmachines.Data;

public class instructionalVids {
    int id;
    String tittle;
    String description;
    String VideoLink;
    public instructionalVids(int mid,String mTittle,String mDescription,String mVideoLink){
        id=mid;
        tittle=mTittle;
        description=mDescription;
        VideoLink=mVideoLink;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getVideoLink() {
        return VideoLink;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setVideoLink(String videoLink) {
        VideoLink = videoLink;
    }
}
