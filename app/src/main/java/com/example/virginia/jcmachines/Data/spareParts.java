package com.example.virginia.jcmachines.Data;

public class spareParts {
    int id;
    String description;
    String name;
    String Section;

    public spareParts(int mid,String mdescription, String mname, String mSection){
        this.id =mid;
        this.description =mdescription;
        this.name =mname;
        this.Section =mSection;
    }

    public int getId() {
        return this.id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getSection() {
        return this.Section;
    }

    public void setSection(String section) {
        this.Section = section;
    }
}
