package com.ai.egg.microtalk;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Utils {

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }
   public static List<String> JsonListFromArrayName(String JsonString, String name) throws JSONException {
       List<String> Items = new ArrayList<>();
       Items.add("Selectionner");
       JSONObject  JsonOBJ = new JSONObject(JsonString);
       JSONArray cast = JsonOBJ.getJSONArray(name);
       for (int i=0; i<cast.length(); i++) {
           Items.add(cast.getString(i));
       }
       return Items;
   }
   public static String[] getDate(Long s){
       DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
       Calendar calendar = Calendar.getInstance();
       calendar.setTimeInMillis(s);
       String date = formatter.format(calendar.getTime());
       int month = Integer.parseInt(date.substring(3,5));
       String[] Months = {"Janvier",
               "Février",
               "Mars",
               "Avril",
               "Mai",
               "Juin",
               "Juillet",
               "Août",
               "Septembre",
               "Octobre",
               "Novembre",
               "Décembre"};
       int a=11;
       if (date.charAt(11)=='0'){
           a=12;
       }
       String[] data = {Months[month]+" à "+date.substring(a,date.length()),date};
       return data;
   }
   public static List<House> GetInfo(){
        List<House> Items = new ArrayList<>();
        for(int i=0;i<BackendApp.Profile.size();i++){
            if (!BackendApp.Profile.get(i).getOwner().equals(BackendApp.OwnerForProfile)){
                continue;
            }
            else{
                Items.add(BackendApp.Profile.get(i));
            }
        }
        return Items;
   }
}