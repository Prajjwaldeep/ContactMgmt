package com.sbdev.contactmgmt;

public class ContactModel {

    private String link, name, phone;

    public ContactModel(){}

    public ContactModel(String link, String name, String phone) {
        this.link = link;
        this.name = name;
        this.phone = phone;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
