package com.example.loijaapp.model;

import com.example.loijaapp.enums.Roles;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MyUser implements Serializable{

	private int id;
	private String firstname;
	private String surname;
	private String username;
	private String password;
	private Set<Rol> roles;
	
	public MyUser() {
		
	}
	
	public MyUser(String firstname, String surname, String username, String password) {
		this.firstname = firstname;
		this.surname = surname;
		this.username = username;
		this.password = password;
	}
	
	public MyUser(String firstname, String surname, String username, String password, Set<Rol> roles) {
		super();
		this.firstname = firstname;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
	public MyUser(int id, String firstname, String surname, String username, String password, Set<Rol> roles) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public Set<String> getRolNames() {
		Set<String> rolNames = new HashSet<>();
		for (Rol rol : roles) {
			rolNames.add(rol.getName().name());
		}
		return rolNames;
	}

	@Override
	public String toString() {
		return "MyUser [id=" + id + ", firstname=" + firstname + ", surname=" + surname + ", username=" + username
				+ ", password=" + password + "]";
	}
	
	
	
}
