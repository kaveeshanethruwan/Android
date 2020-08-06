package com.acare.animalcare;

public class Model {

    private String title;
    private String address;
    private String image;
    private String description;

    public Model(String title,String address,String image,String description) {
        this.title=title;
        this.address=address;
        this.image=image;
        this.description=description;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
