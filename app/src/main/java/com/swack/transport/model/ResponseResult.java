package com.swack.transport.model;

import java.util.ArrayList;

public class ResponseResult {

    String response;
    private ArrayList<SliderList> slider_list;
    private ArrayList<Login> login;
    private ArrayList<Login> TraProfileList;
    private ArrayList<SupportListModel> SupportList;
    private ArrayList<TransportList> transport_detail_list;
    public ArrayList<OLPending> OrderListPend;
    public ArrayList<TrasnportPending> transport_detail_list_pend;
    public ArrayList<TrasnportPending> transport_detail_list_pro;
    public ArrayList<OLPending> OrderListProcess;
    public ArrayList<FinalListModel> JobItemFinalList;
    private ArrayList<VehicleDetailsList> VehicleList;
    private ArrayList<VehicleCatList> VehicleCatList;
    private ArrayList<TyerList> TyerList;
    private ArrayList<LoadList> LoadList;
    private ArrayList<ModalYearList> ModalYearList;

    public ArrayList<com.swack.transport.model.ModalYearList> getModalYearList() {
        return ModalYearList;
    }

    public void setModalYearList(ArrayList<com.swack.transport.model.ModalYearList> modalYearList) {
        ModalYearList = modalYearList;
    }

    public ArrayList<com.swack.transport.model.LoadList> getLoadList() {
        return LoadList;
    }

    public void setLoadList(ArrayList<com.swack.transport.model.LoadList> loadList) {
        LoadList = loadList;
    }

    public ArrayList<com.swack.transport.model.TyerList> getTyerList() {
        return TyerList;
    }

    public void setTyerList(ArrayList<com.swack.transport.model.TyerList> tyerList) {
        TyerList = tyerList;
    }

    public ArrayList<VehicleDetailsList> getVehicleList() {
        return VehicleList;
    }

    public void setVehicleList(ArrayList<VehicleDetailsList> vehicleList) {
        VehicleList = vehicleList;
    }

    public ArrayList<com.swack.transport.model.VehicleCatList> getVehicleCatList() {
        return VehicleCatList;
    }

    public void setVehicleCatList(ArrayList<com.swack.transport.model.VehicleCatList> vehicleCatList) {
        VehicleCatList = vehicleCatList;
    }

    public ArrayList<FinalListModel> getJobItemFinalList() {
        return JobItemFinalList;
    }

    public void setJobItemFinalList(ArrayList<FinalListModel> jobItemFinalList) {
        JobItemFinalList = jobItemFinalList;
    }

    public ArrayList<OLPending> getOrderListPend() {
        return OrderListPend;
    }

    public void setOrderListPend(ArrayList<OLPending> orderListPend) {
        OrderListPend = orderListPend;
    }

    public ArrayList<TrasnportPending> getTransport_detail_list_pend() {
        return transport_detail_list_pend;
    }

    public void setTransport_detail_list_pend(ArrayList<TrasnportPending> transport_detail_list_pend) {
        this.transport_detail_list_pend = transport_detail_list_pend;
    }

    public ArrayList<TrasnportPending> getTransport_detail_list_pro() {
        return transport_detail_list_pro;
    }

    public void setTransport_detail_list_pro(ArrayList<TrasnportPending> transport_detail_list_pro) {
        this.transport_detail_list_pro = transport_detail_list_pro;
    }

    public ArrayList<OLPending> getOrderListProcess() {
        return OrderListProcess;
    }

    public void setOrderListProcess(ArrayList<OLPending> orderListProcess) {
        OrderListProcess = orderListProcess;
    }

    private String userMsgList;

    public String getUserMsgList() {
        return userMsgList;
    }

    public void setUserMsgList(String userMsgList) {
        this.userMsgList = userMsgList;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<SliderList> getSlider_list() {
        return slider_list;
    }

    public void setSlider_list(ArrayList<SliderList> slider_list) {
        this.slider_list = slider_list;
    }

    public ArrayList<Login> getLogin() {
        return login;
    }

    public void setLogin(ArrayList<Login> login) {
        this.login = login;
    }

    public ArrayList<Login> getTraProfileList() {
        return TraProfileList;
    }

    public void setTraProfileList(ArrayList<Login> traProfileList) {
        TraProfileList = traProfileList;
    }

    public ArrayList<SupportListModel> getSupportList() {
        return SupportList;
    }

    public void setSupportList(ArrayList<SupportListModel> supportList) {
        SupportList = supportList;
    }

    public ArrayList<TransportList> getTransport_detail_list() {
        return transport_detail_list;
    }

    public void setTransport_detail_list(ArrayList<TransportList> transport_detail_list) {
        this.transport_detail_list = transport_detail_list;
    }
}
