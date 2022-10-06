package com.anahuac.awsdb;


public class ListElement {
    public String name;
    public String email;
    public int id;

    public ListElement(int id, String name, String email){
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getEmail() {return email;}

    public String getName() {
        return name;
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

}
