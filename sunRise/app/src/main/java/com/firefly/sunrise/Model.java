package com.firefly.sunrise;

public class Model {
    String name;
    String imageUrl;
    Integer price;
    Integer discount;
    String description;


    public Model(){

    }

    public Model(String mname,Integer mprice,Integer mdiscount, String mdescription,String mimageUrl) {


        name=mname;
        price=mprice;
        discount=mdiscount;
        description=mdescription;
        //String mprice,String mdiscount, String mdescription,
        imageUrl=mimageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
   }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
