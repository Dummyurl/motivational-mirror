package com.steadtech.motivationmirror.views;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.steadtech.motivationmirror.modals.QuotesAssests;
import com.steadtech.motivationmirror.utils.Container;
import com.steadtech.motivationmirror.utils.Prefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity {
    ArrayList<QuotesAssests> list;

    public ArrayList<QuotesAssests> parseJSONQuotes() {

        list = new ArrayList<QuotesAssests>();

        String json = null;
        try {
            InputStream is = getAssets().open("quotes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            // Parse Json quotes from assest folder
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonQuotes = jsonObject.getJSONObject("quotes");
            JSONArray jsonArray = jsonQuotes.getJSONArray("cd");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonInner = jsonArray.getJSONObject(i);
                QuotesAssests quote = new QuotesAssests();
                quote.setId(Integer.parseInt(jsonInner.getString("id")));
                quote.setAuthor(jsonInner.getString("author"));
                quote.setQuotes(jsonInner.getString("text"));
                list.add(quote);
            }

            Container.setQuote_list(list);

            //Set InApp Purchase Prefrences to TRUE For Remove InApp Purchase in the App.
            Prefrence.setInAppFromPref(this, true);

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


}
