package com.example.caffeine;

public class cartClass {



    public cartClass(String productName,
                     String userUid, String productUid, String date,
                     String status, Integer piece, Integer price,String imgUrl) {
        this.productName = productName;
        this.userUid = userUid;
        this.productUid = productUid;
        this.date = date;
        this.status = status;
        this.piece = piece;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public cartClass() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getProductUid() {
        return productUid;
    }

    public void setProductUid(String productUid) {
        this.productUid = productUid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    String productName;
    String userUid;
    String productUid;
    String date;
    String status;
    Integer piece;
    Integer price;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    String imgUrl;
}
