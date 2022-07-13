package com.example.loijaapp.utils;

import android.content.SharedPreferences;

import com.example.loijaapp.enums.Roles;
import com.example.loijaapp.model.MyUser;

import java.util.Set;

public class CurrentSession {
    private String username;
    private String password;
    private Set<String> roles;
    private String firstname;
    private String surname;
    private String cookie;

    public CurrentSession(String username, String password, Set<String> roles, String firstname, String surname, String cookie) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.firstname = firstname;
        this.surname = surname;
        this.cookie = cookie;
    }

    public static CurrentSession build(SharedPreferences preferences) {
        String username, password, firstname, surname, cookie;
        Set<String> roles;
        username = preferences.getString(Tags.USERNAME, null);
        password = preferences.getString(Tags.PASSWORD, null);
        roles = preferences.getStringSet(Tags.USER_ROLES, null);
        firstname = preferences.getString(Tags.FIRSTNAME, null);
        surname = preferences.getString(Tags.SURNAME, null);
        cookie = preferences.getString(Tags.COOKIE, null);

        if (username == null | password == null | roles == null |
                firstname == null | surname == null | cookie == null)
            return null;
        else
            return new CurrentSession(username, password, roles, firstname, surname, cookie);
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
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

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public boolean isAdmin() {
        for (String rol : roles) {
            if (rol.equals(Roles.ROLE_ADMIN.name()))
                return true;
        }
        return false;
    }

    public boolean isManager() {
        for (String rol : roles) {
            if (rol.equals(Roles.ROLE_MANAGER.name()))
                return true;
        }
        return false;
    }

}

