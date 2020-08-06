package com.firefly.sunrise;

public class Order_header {
    private int order_id;
    private String cus_email;
    private String order_date;
    private String status_changed_date;
    private int deliverycost;
    private  int item_total;
    private int net_total;
    private String status;

    private String customer_type;
    private String reciver_name;
    private String reciver_contact_number;
    private String reciver_address;
    private String reciver_city;
    private String reciver_location_type;
    private String delivery_date;
    private String delivery_time;
    private String description;
    private String sender_name;
    private String sender_message;

    public Order_header() {
    }

    public String getStatus_changed_date() {
        return status_changed_date;
    }

    public void setStatus_changed_date(String status_changed_date) {
        this.status_changed_date = status_changed_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getCus_email() {
        return cus_email;
    }

    public void setCus_email(String cus_email) {
        this.cus_email = cus_email;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getDeliverycost() {
        return deliverycost;
    }

    public void setDeliverycost(int deliverycost) {
        this.deliverycost = deliverycost;
    }

    public int getItem_total() {
        return item_total;
    }

    public void setItem_total(int item_total) {
        this.item_total = item_total;
    }

    public int getNet_total() {
        return net_total;
    }

    public void setNet_total(int net_total) {
        this.net_total = net_total;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public String getReciver_name() {
        return reciver_name;
    }

    public void setReciver_name(String reciver_name) {
        this.reciver_name = reciver_name;
    }

    public String getReciver_contact_number() {
        return reciver_contact_number;
    }

    public void setReciver_contact_number(String reciver_contact_number) {
        this.reciver_contact_number = reciver_contact_number;
    }

    public String getReciver_address() {
        return reciver_address;
    }

    public void setReciver_address(String reciver_address) {
        this.reciver_address = reciver_address;
    }

    public String getReciver_city() {
        return reciver_city;
    }

    public void setReciver_city(String reciver_city) {
        this.reciver_city = reciver_city;
    }

    public String getReciver_location_type() {
        return reciver_location_type;
    }

    public void setReciver_location_type(String reciver_location_type) {
        this.reciver_location_type = reciver_location_type;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_message() {
        return sender_message;
    }

    public void setSender_message(String sender_message) {
        this.sender_message = sender_message;
    }
}
