package com.steadtech.motivationmirror.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.steadtech.motivationmirror.modals.QuotesAssests;

import java.io.IOException;
import java.util.ArrayList;


public class Prefrence {
    public static SharedPreferences sharedpreferences;
    public static String PREF_NAME = "Image_pref";
    public static String IMAGE_PATH = "path";

    public static String PREF_QUOTE = "Quote_Pref";
    public static String QUOTE_ID = "quote_id";

    public static String PREF_DATE = "Date_Pref";
    public static String DATE = "date";

    public static String PREF_FAVOURITE = "Favourite_Pref";
    public static String FAVOURITE_LIST = "Favourite_List";

    public static String PREF_BACK_STACK = "BackStack_pref";
    public static String BACK_STACK = "Back_Stack";

    public static String PREF_INAPP_PURCHASE = "InApp_pref";
    public static String IS_INAPP = "is_InApp";

    public static String PREF_FAV_SELF = "Favourite_Pref_Self";
    public static String FAV_SELF_LIST = "Fav_Self_List";


    public static void setImagePathFromPref(Context ctx, String path) {
        sharedpreferences = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(IMAGE_PATH, path);
        editor.commit();
    }

    public static String getImagePathFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        return sharedpreferences.getString(IMAGE_PATH, null);
    }

    public static void setQuoteIdFromPref(Context ctx, Integer id) {
        sharedpreferences = ctx.getSharedPreferences(PREF_QUOTE, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(QUOTE_ID, id);
        editor.commit();
    }

    public static int getQuoteIdFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_QUOTE, ctx.MODE_PRIVATE);
        return sharedpreferences.getInt(QUOTE_ID, -1);
    }


    public static void setDateFromPref(Context ctx, String date) {
        sharedpreferences = ctx.getSharedPreferences(PREF_DATE, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DATE, date);
        editor.commit();
    }

    public static String getDateFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_DATE, ctx.MODE_PRIVATE);
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
        editor.commit();
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
        sharedpreferences = ctx.getSharedPreferences(PREF_BACK_STACK, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(BACK_STACK, backStack);
        editor.commit();
    }

    public static Boolean getBackStackFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_BACK_STACK, ctx.MODE_PRIVATE);
        return sharedpreferences.getBoolean(BACK_STACK, false);
    }

    // Here Set back Stack True Or False When Select Quote From Favourite List And Set Text According To the Selection Of the Id In The Array List
    public static void setInAppFromPref(Context ctx, Boolean backStack) {
        sharedpreferences = ctx.getSharedPreferences(PREF_INAPP_PURCHASE, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(IS_INAPP, backStack);
        editor.commit();
    }

    public static Boolean getInAppFromPref(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(PREF_INAPP_PURCHASE, ctx.MODE_PRIVATE);
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
        editor.commit();
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
