package com.swack.transport.model;

import java.util.ArrayList;

public class VehicleDetailsList {
    private String response;


    private ArrayList<VehicleDetailsList> VehicleDetailsList;

    private String vehicle_cat_name;

    private String vehicled_id;

    private String vehicle_name;

    private String vehicled_name;


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<VehicleDetailsList> getVehicleDetailsList() {
        return VehicleDetailsList;
    }

    public void setVehicleDetailsList(ArrayList<VehicleDetailsList> vehicleDetailsList) {
        VehicleDetailsList = vehicleDetailsList;
    }

    public String getVehicled_name() {
        return vehicled_name;
    }

    public void setVehicled_name(String vehicled_name) {
        this.vehicled_name = vehicled_name;
    }

    public String getVehicle_cat_name ()
    {
        return vehicle_cat_name;
    }

    public void setVehicle_cat_name (String vehicle_cat_name)
    {
        this.vehicle_cat_name = vehicle_cat_name;
    }

    public String getVehicled_id ()
    {
        return vehicled_id;
    }

    public void setVehicled_id (String vehicled_id)
    {
        this.vehicled_id = vehicled_id;
    }

    public String getVehicle_name ()
    {
        return vehicle_name;
    }

    public void setVehicle_name (String vehicle_name)
    {
        this.vehicle_name = vehicle_name;
    }
}
