package com.fwc.cosmetic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    String id;
    String name;
    String image;
    Long price;
    String brand;
    String description;
    String madeIn;

    public Product() {

    }

    public Product(String id, String name, String image, Long price, String brand, String description, String madeIn) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.madeIn = madeIn;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        price = in.readLong();
        brand = in.readString();
        description = in.readString();
        madeIn = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() { return price; }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public void setMadeIn(String madeIn) {
        this.madeIn = madeIn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeLong(price);
        parcel.writeString(brand);
        parcel.writeString(description);
        parcel.writeString(madeIn);
    }
}