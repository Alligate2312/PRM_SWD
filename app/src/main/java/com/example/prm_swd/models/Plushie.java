package com.example.prm_swd.models;

public class Plushie {
    public String name;
    public double price;
    public String description;

    public Plushie(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }
}
