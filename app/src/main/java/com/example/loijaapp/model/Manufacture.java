package com.example.loijaapp.model;

import com.example.loijaapp.enums.Manufactures;

import java.io.Serializable;

public class Manufacture implements Serializable {

	private int id;
	private Manufactures name;
	
	public Manufacture(Manufactures name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Manufactures getName() {
		return name;
	}

	public void setName(Manufactures name) {
		this.name = name;
	}
}
