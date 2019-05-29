package com.example.virginia.jcmachines.Data;



public class effcalculation {
    /**
     * Generates class for calculation of the efficiency
     *
     *
     * @param compid company Id.
     * @param userid Authenticated UserId.
     * @param mid id for this Item
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
     *                    Convertions: STANDARD / METRIC
     * 1 inch /25.4 mm
     * 1 foot /0.3048 m
     * 1 lb /0.4536 kg
     * 1 in/min 0.000423 m/s
     *
     * @return effcalculation calculation object.
     */
    private String compid;
    private String userid;
    private String mid;
    private Boolean imp=true;
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
    private String date;
    double vd;


    public  effcalculation(){

    }

    public effcalculation clear(){
        effcalculation myeff;
        myeff=new effcalculation();
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
        myeff.setMid("");
       return myeff;
    }

    public void convertToMetric(){
        imp=false;
        if(c!=0){
            c=c*0.0254;
        }
        if(w!=0){
            w=w*0.0254;
        }
        if(l!=0){
            l=l*0.0254;
        }
        if(v!=0){
            v=v*0.000423;
        }
        if(ax!=0){
            ax=ax*0.00064516;
        }
        if(n!=0){
            n=n/60;
        }
    }

    public void convertToImp(){
        imp=true;
        if(c!=0){
            c=c/0.0254;
        }
        if(w!=0){
            w=w/0.0254;
        }
        if(l!=0){
            l=l/0.0254;
        }
        if(v!=0){
            v=v/0.000423;
        }
        if(ax!=0){
            ax=ax/0.00064516;
        }
        if(n!=0){
            n=n*60;
        }
    }

    public double calculateEf(){
        int myconstant=60;//this is a time conversion to change the cuts per sec to per min
        eff=Math.round(((v*ax)/(n*vd))*100);
        return eff;
    }

    public double calculateColumnSpeed(){
        //Calculate product section if ax not known

            double number=r/100;
            v=c*l*(1-number);

        return v;
    }

    public double calculateProductSection(){
        double number=z/100;
        //Calculate produ

            ax=w*h*(1-(number));

        return ax;
    }

    public String getCompid() {
        return compid;
    }

    public boolean getImp() {
        return imp;
    }

    public void setImp(boolean unit) {
        this.imp = unit;
    }

    public void setCompid(String compid) {
        this.compid = compid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getEff() {
        return eff;
    }

    public boolean isHasc() {
        return hasc;
    }

    public String getApptype() {
        return apptype;
    }

    public double getAx() {
        return ax;
    }

    public double getC() {
        return c;
    }

    public double getH() {
        return h;
    }

    public double getL() {
        return l;
    }

    public double getMs() {
        return ms;
    }

    public double getN() {
        return n;
    }

    public double getR() {
        return r;
    }

    public double getV() {
        return v;
    }

    public double getW() {
        return w;
    }

    public double getZ() {
        return z;
    }

    public String getMid() {
        return mid;
    }

    public String getUserid() {
        return userid;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setEff(double eff) {
        this.eff = eff;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setHasc(boolean hasc) {
        this.hasc = hasc;
    }

    public void setL(double l) {
        this.l = l;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setMs(double ms) {
        this.ms = ms;
    }

    public void setN(double n) {
        this.n = n;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setW(double w) {
        this.w = w;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setImp(Boolean imp) {
        this.imp = imp;
    }

    public double getVd() {
        return vd;
    }

    public void setVd(double vd) {
        this.vd = vd;
    }

    //Converter Classes for binding
    public String getEffString() {
        return String.valueOf(eff);
    }

    public void setEffString(String effString) {
        this.eff = Double.valueOf(effString);
    }

    public String getCompidString() {
        return compid;
    }

    public void setCompidString(String compidString) {
        if(!compidString.equals(compid)){
        this.compid = compidString;}
    }

    public String getVString() {
        if(v==0){
            return "";}
        else{ return String.valueOf(v);
        }
    }

    public void setVString(String VString) {
        //Makes sure the user doesnt continue deleting beyong empty
        //and sets a proper value to the variable
        if(VString.isEmpty()){
            this.v=0;
            return;
        }
        //Checks the value is different from the new
        //one before setting it
        if(VString!=null||!VString.equals("")){
            if(Double.parseDouble(VString)!=v)
            this.v = Double.parseDouble(VString);
        }
    }

    public void setCString(String CString) {

        if(CString.isEmpty()){
            this.c=0;
            return;
        }
        //Checks the value is different from the new
        //one before setting it
        if(CString!=null||!CString.equals("")){
            if(Double.parseDouble(CString)!=c)
                this.c = Double.parseDouble(CString);
        }
    }

    public String getCString() {
        if(c==0){
            return "";}
        else{ return String.valueOf(c);
        }
    }


    public void setLString(String LString) {
        if(LString.isEmpty()){
            this.l=0;
            return;
        }
        if(LString!=null||!LString.equals("")){
            if(Double.parseDouble(LString)!=l)
                this.l = Double.parseDouble(LString);

        }
    }

    public String getLString() {
        if(l==0){
            return "";}
        else{ return String.valueOf(l);
        }
    }

    public void setRString(String RString) {
        if(RString.isEmpty()){
            this.r=0;
            return;
        }
        if(RString!=null||!RString.equals("")){
            if(Double.parseDouble(RString)!=r)
                this.r = Double.parseDouble(RString);

        }
    }

    public String getRString() {
        if(r==0){
            return "";}
        else{ return String.valueOf(r);
        }
    }

    public void setAxString(String axString) {
        if(axString.isEmpty()){
            this.ax=0;
            return;
        }
        if(axString!=null||!axString.equals("")){
            if(Double.parseDouble(axString)!=ax)
                this.ax = Double.parseDouble(axString);

        }
    }

    public String getAxString() {
        if(ax==0){
            return "";}
        else{ return String.valueOf(ax);
        }
    }

    public void setWString(String WString) {
        if(WString.isEmpty()){
            this.w=0;
            return;
        }
        if(WString!=null||!WString.equals("")){
            if(Double.parseDouble(WString)!=w)
                this.w = Double.parseDouble(WString);

        }
    }

    public String getWString() {
        if(w==0){
            return "";}
        else{ return String.valueOf(w);
        }

    }

    public String getHString() {
        if(h==0){
            return "";}
        else{ return String.valueOf(h);
        }
    }

    public void setHString(String HString) {
        if(HString.isEmpty()){
            this.h=0;
            return;
        }
        if(HString!=null||!HString.equals("")){
            if(Double.parseDouble(HString)!=h)
                this.h = Double.parseDouble(HString);

        }
    }

    public String getZString() {
        if(z==0){
            return "";}
        else{ return String.valueOf(z);
        }
    }

    public void setZString(String ZString) {
        if(ZString.isEmpty()){
            this.z=0;
            return;
        }
        if(ZString!=null||!ZString.equals("")){
            if(Double.parseDouble(ZString)!=z)
                this.z = Double.parseDouble(ZString);

        }
    }

    public String getNString() {
        if(n==0){
            return "";}
        else{ return String.valueOf(n);
        }
    }

    public void setNString(String NString) {
        if(NString.isEmpty()){
            this.n=0;
            return;
        }
        if(NString!=null||!NString.equals("")){
            if(Double.parseDouble(NString)!=n)
                this.n = Double.parseDouble(NString);

        }
    }

}


