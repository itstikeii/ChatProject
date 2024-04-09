package com.tikeii.nghienchat.Ultils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;


public class FireBaseUltil {

    public static boolean isLoggedIN() {
        if (currentUserID()!=null) {
            return true;
        } else {
            return false;
        }
    }

    public static String currentUserID() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference getUser_withAuthUDI() {
        return FirebaseFirestore.getInstance().collection("USERS").document(currentUserID());
    }



    public static CollectionReference all() {
        return FirebaseFirestore.getInstance().collection("USERS");
    }

    public static DocumentReference getChatRoomReferance(String roomID) {
        return FirebaseFirestore.getInstance().collection("CHATROOMS").document(roomID);
    }

    public static CollectionReference getChatRoomTextReference(String roomID) {
        return FirebaseFirestore.getInstance().collection("CHATROOMS").document(roomID).collection("CHATS");
    }


    public static String getdata_ChatRoomID(String userID_A,String userID_B) {
        if (userID_A.hashCode()<userID_B.hashCode()) {
            return userID_A+"_"+userID_B;
        } else {
           return userID_B+"_"+userID_A;
        }
    }

    public static CollectionReference allChatRoomReference() {
        return FirebaseFirestore.getInstance().collection("CHATROOMS");
    }

    public static DocumentReference getFriendsFromChatRoom(List<String> userID) {
        if (userID.get(0).equals(FireBaseUltil.currentUserID())){
           return all().document(userID.get(1));
        } else {
           return all().document(userID.get(0));
        }
    }

    public static String timeStampToString(Timestamp timestamp) {
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getUserAVT() {
        return FirebaseStorage.getInstance().getReference().child("USERS_AVT")
                .child(FireBaseUltil.currentUserID());
    }

    public static StorageReference getFriendsAVT(String friendID) {
        return FirebaseStorage.getInstance().getReference().child("USERS_AVT")
                .child(friendID);
    }




}
