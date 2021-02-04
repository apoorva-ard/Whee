package com.example.whee4;

public class UploadInfo {
    public String itemName;
    public String userId;
    public String place;
    public String date;
    public String details;
    public String imageURL;
    private boolean expandable;
    public UploadInfo(){}

    public UploadInfo(String name, String userId, String place, String date, String details, String url) {
        this.itemName = name;
        this.userId = userId;
        this.place = place;
        this.date = date;
        this.details = details;
        this.imageURL = url;
        this.expandable=false;
    }
    public String getUserId(){ return userId;}
    public String getImageName() {
        return itemName;
    }
    public String getImageURL() {
        return imageURL;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "UploadInfo{" +
                "itemName='" + itemName + '\'' +
                ", place='" + place + '\'' +
                ", date='" + date + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
