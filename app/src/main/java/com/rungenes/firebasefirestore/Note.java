package com.rungenes.firebasefirestore;

import com.google.firebase.firestore.Exclude;

public class Note {

    private String title;
    private String description;
    private int priority;
    private String documentId;

    public Note(){

        //no-arg constructor needed
    }

    public Note(String title, String description,int priority) {
        this.title = title;
        this.description = description;
        this.priority= priority;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
