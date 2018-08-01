package com.rungenes.firebasefirestore;

public class Note {

    String title;
    String description;
    String number;

    public Note(){

        //no-arg constructor needed
    }

    public Note(String title, String description,String number) {
        this.title = title;
        this.description = description;
        this.number= number;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNumber() {
        return number;
    }
}
