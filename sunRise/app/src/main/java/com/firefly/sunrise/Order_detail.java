package com.firefly.sunrise;

public class Order_detail {

    private int order_id;
    private int item_id;
    private String caketype;
    private String cakename;
    private String cakeprice;
    private String cakediscount;
    private String qty;
    private String weight;
    private String cakemessage;
    private String amount;

    public Order_detail() {
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getCaketype() {
        return caketype;
    }

    public void setCaketype(String caketype) {
        this.caketype = caketype;
    }

    public String getCakename() {
        return cakename;
    }

    public void setCakename(String cakename) {
        this.cakename = cakename;
    }

    public String getCakeprice() {
        return cakeprice;
    }

    public void setCakeprice(String cakeprice) {
        this.cakeprice = cakeprice;
    }

    public String getCakediscount() {
        return cakediscount;
    }

    public void setCakediscount(String cakediscount) {
        this.cakediscount = cakediscount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCakemessage() {
        return cakemessage;
    }

    public void setCakemessage(String cakemessage) {
        this.cakemessage = cakemessage;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
