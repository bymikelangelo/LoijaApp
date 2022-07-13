package com.example.loijaapp.model;

import com.example.loijaapp.enums.TypesProduct;

import java.io.Serializable;

public class TypeProduct implements Serializable {

	private int id;
	private TypesProduct name;
	
	public TypeProduct() {
		super();
	}
	
	public TypeProduct(TypesProduct name) {
		super();
		this.name = name;
	}
	
	public TypeProduct(int id, TypesProduct name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TypesProduct getName() {
		return name;
	}

	public void setName(TypesProduct name) {
		this.name = name;
	}

}
