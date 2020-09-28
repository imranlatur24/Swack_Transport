package com.swack.transport.model;

public class TrasnportPending
{
    private String trnreqcusid;
    private String FromDistrict_Name;
    private String ToDistrict_Name;
    private String loadrang_name;
    private String trnreqloadid;
    private String TraProductName;
    private String trnreqprice;
    private String trnreqaddress;                                                                                                                                                                                                                                                                                                       
    private String status_name;
    private String is_create;
    private String cus_id;
    private String  cus_name;
    private String  cus_mob;
    private String Distance;
    private String FromTalukaName;
    private String ToTalukaName;

    public String getToTalukaName() {
        return ToTalukaName;
    }

    public void setToTalukaName(String toTalukaName) {
        ToTalukaName = toTalukaName;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getFromTalukaName() {
        return FromTalukaName;
    }

    public void setFromTalukaName(String fromTalukaName) {
        FromTalukaName = fromTalukaName;
    }

    public String getTrnreqcusid() {
        return trnreqcusid;
    }

    public void setTrnreqcusid(String trnreqcusid) {
        this.trnreqcusid = trnreqcusid;
    }

    public String getFromDistrict_Name() {
        return FromDistrict_Name;
    }

    public void setFromDistrict_Name(String fromDistrict_Name) {
        FromDistrict_Name = fromDistrict_Name;
    }

    public String getToDistrict_Name() {
        return ToDistrict_Name;
    }

    public void setToDistrict_Name(String toDistrict_Name) {
        ToDistrict_Name = toDistrict_Name;
    }

    public String getLoadrang_name() {
        return loadrang_name;
    }

    public void setLoadrang_name(String loadrang_name) {
        this.loadrang_name = loadrang_name;
    }

    public String getTrnreqloadid() {
        return trnreqloadid;
    }

    public void setTrnreqloadid(String trnreqloadid) {
        this.trnreqloadid = trnreqloadid;
    }

    public String getTraProductName() {
        return TraProductName;
    }

    public void setTraProductName(String traProductName) {
        TraProductName = traProductName;
    }

    public String getTrnreqprice() {
        return trnreqprice;
    }

    public void setTrnreqprice(String trnreqprice) {
        this.trnreqprice = trnreqprice;
    }

    public String getTrnreqaddress() {
        return trnreqaddress;
    }

    public void setTrnreqaddress(String trnreqaddress) {
        this.trnreqaddress = trnreqaddress;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getIs_create() {
        return is_create;
    }

    public void setIs_create(String is_create) {
        this.is_create = is_create;
    }

    public String getCus_id() {
        return cus_id;
    }

    public void setCus_id(String cus_id) {
        this.cus_id = cus_id;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_mob() {
        return cus_mob;
    }

    public void setCus_mob(String cus_mob) {
        this.cus_mob = cus_mob;
    }
}
