package com.salvame.cardcontact.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "classes")
public class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    private int c_id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="email")
    private String email;

    @ColumnInfo(name="phone_number")
    private String phone_number;

    @ColumnInfo(name="whatsapp")
    private String whatsapp;

    @ColumnInfo(name="line")
    private String line;

    @ColumnInfo(name="company")
    private String company;

    @ColumnInfo(name="webpage")
    private String webpage;

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

}