package com.tikeii.nghienchat.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatroomModel {
    String roomID;
    List<String> userID;
    Timestamp lastTextSentTime;
    String lastTextSenderID;
    String lastText;

    public ChatroomModel() {
    }

    public ChatroomModel(String roomID, List<String> userID, Timestamp lastTextSentTime, String lastTextSenderID) {
        this.roomID = roomID;
        this.userID = userID;
        this.lastTextSentTime = lastTextSentTime;
        this.lastTextSenderID = lastTextSenderID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public List<String> getUserID() {
        return userID;
    }

    public void setUserID(List<String> userID) {
        this.userID = userID;
    }

    public Timestamp getLastTextSentTime() {
        return lastTextSentTime;
    }

    public void setLastTextSentTime(Timestamp lastTextSentTime) {
        this.lastTextSentTime = lastTextSentTime;
    }

    public String getLastTextSenderID() {
        return lastTextSenderID;
    }

    public void setLastTextSenderID(String lastTextSenderID) {
        this.lastTextSenderID = lastTextSenderID;
    }

    public String getLastText() {
        return lastText;
    }

    public void setLastText(String lastText) {
        this.lastText = lastText;
    }
}
