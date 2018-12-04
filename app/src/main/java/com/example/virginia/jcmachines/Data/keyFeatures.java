package com.example.virginia.jcmachines.Data;

public class keyFeatures {

    int id;

    String name;

    String description;

    public keyFeatures(int mid, String mName, String mdescription){
        this.id =mid;
        this.name =mName;
        this.description =mdescription;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
