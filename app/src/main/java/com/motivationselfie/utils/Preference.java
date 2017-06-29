package com.motivationselfie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.motivationselfie.modals.QuotesAssests;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Preference {
    private static SharedPreferences sharedpreferences;
    private static String PREF_NAME = "Image_pref";
    private static String IMAGE_PATH = "path";

    private static String PREF_QUOTE = "Quote_Pref";
    private static String QUOTE_ID = "quote_id";

    private static String PREF_DATE = "Date_Pref";
    private static String DATE = "date";

    private static String PREF_FAVOURITE = "Favourite_Pref";
    private static String FAVOURITE_LIST = "Favourite_List";

    private static String PREF_BACK_STACK = "BackStack_pref";
    private static String BACK_STACK = "Back_Stack";

    private static String PREF_INAPP_PURCHASE = "InApp_pref";
    private static String IS_INAPP = "is_InApp";

    private static String PREF_FAV_SELF = "Favourite_Pref_Self";
    private static String FAV_SELF_LIST = "Fav_Self_List";


    public static void setImagePathFromPref(Context ctx, String path) {
        sharedpreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(IMAGE_PATH, path);
        editor.apply();
    }

    public static String getImagePathFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString(IMAGE_PATH, null);
    }

    public static void setQuoteIdFromPref(Context ctx, Integer id) {
        sharedpreferences = ctx.getSharedPreferences(PREF_QUOTE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(QUOTE_ID, id);
        editor.apply();
    }

    public static int getQuoteIdFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_QUOTE, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(QUOTE_ID, -1);
    }


    public static void setDateFromPref(Context ctx, String date) {
        sharedpreferences = ctx.getSharedPreferences(PREF_DATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DATE, date);
        editor.apply();
    }

    public static String getDateFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_DATE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(DATE, null);
    }

    // insert favourite arraylist in SharePrefrence...
    public static void insertArrayListPref(Context ctx, ArrayList<Integer> list) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_FAVOURITE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(FAVOURITE_LIST, ObjectSerializer.serialize(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Integer> getfavArrayListPref(Context ctx) {
        ArrayList<Integer> favlist = null;
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_FAVOURITE,
                Context.MODE_PRIVATE);
        try {
            favlist = (ArrayList<Integer>) ObjectSerializer
                    .deserialize(prefs.getString(FAVOURITE_LIST,
                            ObjectSerializer.serialize(new ArrayList<Integer>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return favlist;
    }

    // Here Set back Stack True Or False When Select Quote From Favourite List And Set Text According To the Selection Of the Id In The Array List
    public static void setBackStackFromPref(Context ctx, Boolean backStack) {
        sharedpreferences = ctx.getSharedPreferences(PREF_BACK_STACK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(BACK_STACK, backStack);
        editor.apply();
    }

    @NonNull
    public static Boolean getBackStackFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_BACK_STACK, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(BACK_STACK, false);
    }

    // Here Set back Stack True Or False When Select Quote From Favourite List And Set Text According To the Selection Of the Id In The Array List
    public static void setInAppFromPref(Context ctx, Boolean backStack) {
        sharedpreferences = ctx.getSharedPreferences(PREF_INAPP_PURCHASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(IS_INAPP, backStack);
        editor.apply();
    }

    @NonNull
    public static Boolean getInAppFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_INAPP_PURCHASE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(IS_INAPP, false);
    }


    // insert self favourite arraylist in SharePrefrence...
    public static void insertSelfArrayListPref(Context ctx, ArrayList<QuotesAssests> fav_list) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_FAV_SELF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(FAV_SELF_LIST, ObjectSerializer.serialize(fav_list));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<QuotesAssests> getfavSelfArrayListPref(Context ctx) {
        ArrayList<QuotesAssests> favlist = null;
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_FAV_SELF,
                Context.MODE_PRIVATE);
        try {
            favlist = (ArrayList<QuotesAssests>) ObjectSerializer
                    .deserialize(prefs.getString(FAV_SELF_LIST,
                            ObjectSerializer.serialize(new ArrayList<QuotesAssests>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return favlist;
    }
}