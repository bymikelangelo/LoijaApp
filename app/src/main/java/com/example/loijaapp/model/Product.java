package com.example.loijaapp.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private TypeProduct type;
    private String name;

    public Product() {
        super();
    }

    public Product(TypeProduct type, String name) {
        super();
        this.type = type;
        this.name = name;
    }

    public Product(int id, TypeProduct type, String name) {
        super();
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeProduct getType() {
        return type;
    }

    public void setType(TypeProduct type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}