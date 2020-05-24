package com.example.onmyway.Utils;

import android.content.Context;
import android.content.Intent;

import com.example.onmyway.Models.CustomFirebase;
import com.example.onmyway.general.Login;

public final class CheckLogin {

    public static boolean toLogin(final Context context) {
        if (CustomFirebase.getCurrentUser() == null) {
            context.startActivity(new Intent(context, Login.class));
            return true;
        }
        return false;
    }
}
