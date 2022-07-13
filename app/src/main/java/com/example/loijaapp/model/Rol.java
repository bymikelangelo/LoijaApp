package com.example.loijaapp.model;

import com.example.loijaapp.enums.Roles;

import java.io.Serializable;

public class Rol implements Serializable{
    private int id;
    private Roles name;
    
    public Rol() {
    	
    }

    public Rol(Roles name) {
        this.name = name;
    }
    
	public Rol(int id, Roles name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Roles getName() {
		return name;
	}

	public void setName(Roles name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name.name();
	}
	
	

}
