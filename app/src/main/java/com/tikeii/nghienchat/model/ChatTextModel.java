package com.tikeii.nghienchat.model;

import com.google.firebase.Timestamp;

public class ChatTextModel {
    String text;
    String senderID;
    Timestamp timestamp;

    public ChatTextModel() {
    }

    public ChatTextModel(String text, String senderID, Timestamp timestamp) {
        this.text = text;
        this.senderID = senderID;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
