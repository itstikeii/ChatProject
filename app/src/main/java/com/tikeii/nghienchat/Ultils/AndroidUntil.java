package com.tikeii.nghienchat.Ultils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tikeii.nghienchat.model.UserModel;

public class AndroidUntil {

    public static void sendUserModelData(Intent intent, UserModel model) {
        intent.putExtra("USERNAME_MODEL",model.getName());
        intent.putExtra("PHONE_MODEL",model.getPhone());
        intent.putExtra("USERID_MODEL",model.getID());
        intent.putExtra("USER_TOKEN",model.getToken());
    }

    public static UserModel getDataUserfromIntent(Intent intent) {
        UserModel userModel = new UserModel();
        userModel.setName(intent.getStringExtra("USERNAME_MODEL"));
        userModel.setPhone(intent.getStringExtra("PHONE_MODEL"));
        userModel.setID(intent.getStringExtra("USERID_MODEL"));
        userModel.setToken(intent.getStringExtra("USER_TOKEN"));
        return userModel;
    }

    public static void setUserAVT(Context context, Uri uri, ImageView imageView) {
        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
