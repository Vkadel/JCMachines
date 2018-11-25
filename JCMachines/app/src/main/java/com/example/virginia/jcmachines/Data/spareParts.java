package com.example.virginia.jcmachines.Data;

public class spareParts {
    int id;
    String description;
    String name;
    String Section;

    public spareParts(int mid,String mdescription, String mname, String mSection){
        id=mid;
        description=mdescription;
        name=mname;
        Section=mSection;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }
}
