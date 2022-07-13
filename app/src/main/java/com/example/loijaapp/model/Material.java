package com.example.loijaapp.model;

import java.io.Serializable;
import java.util.Set;

public class Material implements Serializable {

	private int id;
	private String name;
	private Product product;
	private Set<Manufacture> manufactures;
	
	public Material() {
		super();
	}

	public Material(String name, Product product, Set<Manufacture> manufactures) {
		super();
		this.name = name;
		this.product = product;
		this.manufactures = manufactures;
	}
	
	public Material(int id, String name, Product product, Set<Manufacture> manufactures) {
		super();
		this.id = id;
		this.name = name;
		this.product = product;
		this.manufactures = manufactures;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Set<Manufacture> getManufactures() {
		return manufactures;
	}

	public void setManufactures(Set<Manufacture> manufactures) {
		this.manufactures = manufactures;
	}
}
