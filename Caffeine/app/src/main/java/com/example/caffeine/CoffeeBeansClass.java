package com.example.caffeine;

public class CoffeeBeansClass {
    public int getBeanId() {
        return beanId;
    }

    public void setBeanId(int beanId) {
        this.beanId = beanId;
    }

    public CoffeeBeansClass(int beanId, String name, int price, String bean_type, String acidity,
                            String roast, String flavor, String origin, String key, String imgUrl) {
        this.beanId = beanId;
        this.name = name;
        this.price = price;
        this.bean_type = bean_type;
        this.acidity = acidity;
        this.roast = roast;
        this.flavor = flavor;
        this.origin = origin;
        this.key = key;
        this.imgUrl = imgUrl;
    }

    public CoffeeBeansClass() {
    }

    private int beanId;
    private String name;
    private int price;
    private String bean_type;
    private String acidity;
    private String roast;
    private String flavor;
    private String origin;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String imgUrl;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBean_type() {
        return bean_type;
    }

    public void setBean_type(String bean_type) {
        this.bean_type = bean_type;
    }

    public String getAcidity() {
        return acidity;
    }

    public void setAcidity(String acidity) {
        this.acidity = acidity;
    }

    public String getRoast() {
        return roast;
    }

    public void setRoast(String roast) {
        this.roast = roast;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


}
