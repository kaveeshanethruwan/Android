package com.firefly.sunrise;

public class Upload {
  String name;
  String imageUrl;

    public Upload(String na,String url) {
    name=na;
    imageUrl=url;
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
}
