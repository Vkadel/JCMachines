package com.example.virginia.jcmachines.Data;

public class spareParts {
    int id;
    String description;
    String name;
    String Section;
    String ImageLink;

    public spareParts(int mid,String mdescription, String mname, String mSection,String mimageLink){
        this.id =mid;
        this.description =mdescription;
        this.name =mname;
        this.Section =mSection;
        this.ImageLink=mimageLink;

    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
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
