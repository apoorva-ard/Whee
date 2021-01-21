package com.example.whee4;

public class UploadFinfo {
    public String imageName;
    public String Fplace;
    public String Fdate;
    public String Fdetails;
    public String imageURL;
    public UploadFinfo(){}

    public UploadFinfo(String name, String place,String date,String Details,String url) {
        this.imageName = name;
        this.Fplace=place;
        this.Fdate=date;
        this.Fdetails=Details;
        this.imageURL = url;
    }

    public String getImageName() {
        return imageName;
    }
    public String getImageURL() {
        return imageURL;
    }
}
