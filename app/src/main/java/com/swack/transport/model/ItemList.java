package com.swack.transport.model;

public class ItemList {

    private String ser_part_id;
    private String ser_part_name;

    public ItemList(String ser_part_id, String ser_part_name) {
        this.ser_part_id = ser_part_id;
        this.ser_part_name = ser_part_name;
    }

    public String getSer_part_id() {
        return ser_part_id;
    }

    public void setSer_part_id(String ser_part_id) {
        this.ser_part_id = ser_part_id;
    }

    public String getSer_part_name() {
        return ser_part_name;
    }

    public void setSer_part_name(String ser_part_name) {
        this.ser_part_name = ser_part_name;
    }
}
