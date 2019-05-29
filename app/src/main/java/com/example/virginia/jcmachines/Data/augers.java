package com.example.virginia.jcmachines.Data;


public class augers {
    /**
     * @param id: number to identify each in room
     * @param anam: name for this auger to display to user
     * @param tvoli theoretical volume imperial
     * @param tvolm theoretical volume metric
     *
     */
    int id;
    String anam;
    String tvoli;
    String tvolm;


    public augers(int mid,String mNamm, String mtvoli,String mtvolm){
        this.anam =mNamm;
        this.id=mid;
        this.tvoli=mtvoli;
        this.tvolm=mtvolm;
    }

    public int getId() {
        return this.id;
    }

    public String getAnam() {
        return this.anam;
    }

    public String getTvoli() {
        return this.tvoli;
    }

    public String getTvolm() {
        return this.tvolm;
    }

    public void setAnam(String anam) {
        this.anam = anam;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTvoli(String tvoli) {
        this.tvoli = tvoli;
    }

    public void setTvolm(String tvolm) {
        this.tvolm = tvolm;
    }
}
