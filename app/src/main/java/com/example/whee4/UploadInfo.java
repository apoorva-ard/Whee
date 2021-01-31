package com.example.whee4;

public class UploadInfo {
    public String itemName;
    public String userId;
    public String place;
    public String date;
    public String details;
    public String imageURL;
    public UploadInfo(){}

    public UploadInfo(String name, String userId, String place, String date, String details, String url) {
        this.itemName = name;
        this.userId = userId;
        this.place = place;
        this.date = date;
        this.details = details;
        this.imageURL = url;
    }
    public String getUserId(){ return userId;}
    public String getImageName() {
        return itemName;
    }
    public String getImageURL() {
        return imageURL;
    }
}
