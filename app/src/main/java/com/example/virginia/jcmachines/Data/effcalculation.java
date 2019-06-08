package com.example.virginia.jcmachines.Data;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;


public class effcalculation extends BaseObservable {
    /**
     * Generates class for calculation of the efficiency
     *
     * @param setCalcid this is the unic itemID for firebase
     * @param compid company Id.
     * @param userid Authenticated UserId.
     * @param mid id for the machine associated to this item
     * @param mids id name for the machine associated to this item
     * @param imp whether this calculation is imperial
     * @param ms Motor speed
     * @param hasc whether this setup
     * @param apptype application type:0: pelletizing , 1: Structural/clay
     * @param v v=c(l*r) = column speed (to be provided if user does not have cutter)
     * @param eff %
     * @param c cut length in
     * @param l cut rate in
     * @param r estimated waste or return length
     * @param ax product section =w*h(1- void%) in^3/sec of mat
     * @param w width of brick
     * @param h height of the brick
     * @param z void % customer provided
     * @param n auger speed customer provided rpm
     * @param date time at saving
     * @param vd theoretical volume displacement (in^3) metric and m^3
     * Convertions: STANDARD / METRIC
     * 1 inch /25.4 mm
     * 1 foot /0.3048 m
     * 1 lb /0.4536 kg
     * 1 in/min 0.000423 m/s
     * @return effcalculation calculation object.
     */
    private String compid;
    private String userid;
    private int mid;
    private String mids;
    private String calcid;
    private Boolean imp = true;
    private double ms;
    private boolean hasc;
    private String apptype;
    private double eff;
    private double v;
    private double c;
    private double l;
    private double r;
    private double ax;
    private double w;
    private double h;
    private double z;
    private double n;
    private long date;
    private String dateS;
    private Boolean isActive;
    double vd;


    public effcalculation() {

    }

    public effcalculation clear() {
        effcalculation myeff;
        myeff = new effcalculation();
        myeff.setCompid("");
        myeff.setC(0);
        myeff.setN(0);
        myeff.setZ(0);
        myeff.setH(0);
        myeff.setW(0);
        myeff.setR(0);
        myeff.setL(0);
        myeff.setApptype("");
        myeff.setUserid("");
        myeff.setMid(0);
        return myeff;
    }

    public void convertToMetric() {
        imp = false;
        if (c != 0) {
            setC(c * 0.0254);
        }
        if (w != 0) {
            setW(w * 0.0254);
        }
        if (l != 0) {
            setL(l * 0.0254);
        }
        if (v != 0) {
            setV(v * 0.000423);
        }
        if (ax != 0) {
            setAx(ax * 0.00064516);
        }
        if (n != 0) {
            setN(n / 60);
        }
    }

    public void convertToImp() {
        imp = true;
        if (c != 0) {
            setC(c / 0.0254);
        }
        if (w != 0) {
            setW(w / 0.0254);
        }
        if (l != 0) {
            setL(l / 0.0254);
        }
        if (v != 0) {
            setV(v / 0.000423);
        }
        if (ax != 0) {
            setAx(ax / 0.00064516);
        }
        if (n != 0) {
            setN(n * 60);
        }
    }

    public double calculateEf() {
        int myconstant = 60;//this is a time conversion to change the cuts per sec to per min
        setEff(Math.round(((v * ax) / (n * vd)) * 100));
        return eff;
    }

    public double calculateColumnSpeed() {
        //Calculate product section if ax not known
        double number = r / 100;
        setV(c * l * (1 - number));
        return v;
    }

    public double calculateProductSection() {
        double number = z / 100;
        //Calculate produ
        setAx(w * h * (1 - (number)));
        return ax;
    }

    private boolean checkIfEffCanBecalculated() {
        return ax != 0 && v != 0 && vd != 0 && n != 0;
    }

    private boolean checkIfColumnSpeedCanBecalculated() {
        return l != 0 && c != 0 && r != 0;
    }

    private boolean checkIfProductSectionCanBeCalculated() {
        return h != 0 && z != 0 && w != 0;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getActive() {
        return isActive;
    }

    @Bindable
    public String getMids() {
        return mids;
    }


    public void setMids(String mids) {
        if (this.mids != mids) {
            this.mids = mids;
        }
    }

    @Bindable
    public String getCalcid() {
        return calcid;
    }

    public void setCalcid(String calcid) {
        if (this.calcid != calcid) {
            this.calcid = calcid;
        }
    }


    @Bindable
    public String getDateS() {
        return dateS;
    }

    public void setDateS(String dateS) {
        this.dateS = dateS;
    }

    @Bindable
    public String getCompid() {
        return compid;
    }

    @Bindable
    public boolean getImp() {
        return imp;
    }

    public void setImp(boolean unit) {
        this.imp = unit;
    }

    public void setCompid(String compid) {
        this.compid = compid;
    }

    @Bindable
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Bindable
    public double getEff() {
        return eff;
    }

    @Bindable
    public boolean isHasc() {
        return hasc;
    }

    @Bindable
    public String getApptype() {
        return apptype;
    }

    @Bindable
    public double getAx() {
        return ax;
    }

    @Bindable
    public double getC() {
        return c;
    }

    @Bindable
    public double getH() {
        return h;
    }

    @Bindable
    public double getL() {
        return l;
    }

    @Bindable
    public double getMs() {
        return ms;
    }

    @Bindable
    public double getN() {
        return n;
    }

    @Bindable
    public double getR() {
        return r;
    }

    @Bindable
    public double getV() {
        return v;
    }

    @Bindable
    public double getW() {
        return w;
    }

    @Bindable
    public double getZ() {
        return z;
    }

    @Bindable
    public int getMid() {
        return mid;
    }

    @Bindable
    public String getUserid() {
        return userid;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public void setAx(double ax) {
        if (this.ax != ax) {
            this.ax = ax;
        }

        if (checkIfEffCanBecalculated()) {
            setEff(calculateEf());
        }

    }

    public void setC(double c) {
        if (this.c != c) {
            this.c = c;
        }
        if (checkIfColumnSpeedCanBecalculated()) {
            calculateColumnSpeed();
        }
    }

    public void setEff(double eff) {
        if (this.eff != eff) {
            this.eff = eff;
            notifyPropertyChanged(BR.eff);
        }
    }



    public void setHasc(boolean hasc) {
        if (this.hasc != hasc) {
            this.hasc = hasc;
        }
    }

    public void setL(double l) {
        if (this.l != l) {
            this.l = l;
            if (checkIfColumnSpeedCanBecalculated()) {
                calculateColumnSpeed();
            }
        }
    }

    public void setMid(Integer mid) {
        if (this.mid != mid) {
            this.mid = mid;
        }
    }

    public void setMs(double ms) {
        if (this.ms != ms) {
            this.ms = ms;
        }
    }

    public void setN(double n) {
        if (this.n != n) {
            this.n = n;
        }
        if (checkIfEffCanBecalculated()) {
            setEff(calculateEf());
        }
    }

    public void setR(double r) {
        if (this.r != r) {
            this.r = r;
            if (checkIfColumnSpeedCanBecalculated()) {
                calculateColumnSpeed();
            }
        }
    }

    public void setV(double v) {
        if (this.v != v) {
            this.v = v;
        }
        if (checkIfEffCanBecalculated()) {
            setEff(calculateEf());
        }

    }

    public void setUserid(String userid) {
        if (this.userid != userid) {
            this.userid = userid;
        }
        this.userid = userid;
    }

    public void setW(double w) {
        if (this.w != w) {
            this.w = w;
        }
        if(checkIfProductSectionCanBeCalculated()){
            calculateProductSection();
        }
    }
    public void setH(double h) {
        if (this.h != h) {
            this.h = h;
        }
        if(checkIfProductSectionCanBeCalculated()){
            calculateProductSection();
        }
    }
    public void setZ(double z) {
        if (this.z != z) {
            this.z = z;
        }
        if(checkIfProductSectionCanBeCalculated()){
            calculateProductSection();
        }
    }

    @Bindable
    public double getVd() {
        return vd;
    }

    public void setVd(double vd) {
        if (this.vd != vd) {
            this.vd = vd;
        }
        if (checkIfEffCanBecalculated()) {
            setEff(calculateEf());
        }
    }


}


