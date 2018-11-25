package com.example.virginia.jcmachines.Data;

public class keyFeatures {

    int id;

    String name;

    String description;

    public keyFeatures(int mid, String mName, String mdescription){
        id=mid;
        name=mName;
        description=mdescription;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
