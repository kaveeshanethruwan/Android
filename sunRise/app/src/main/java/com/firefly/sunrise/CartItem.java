package com.firefly.sunrise;

public class CartItem {

    String caketype;
    String cakename;
    String cakeprice;
    String qty;
    String cakeimg;
    String amount;


    public CartItem(String caketype, String cakename, String cakeprice, byte[] cakeimg) {
       // this.caketype = caketype;
      //  this.cakename = cakename;
      //  this.cakeprice = cakeprice;
       // this.cakeimg = cakeimg;
    }

    public CartItem() {

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public String getCakeimg() {
        return cakeimg;
    }

    public void setCakeimg(String cakeimg) {
        this.cakeimg = cakeimg;
    }
}
