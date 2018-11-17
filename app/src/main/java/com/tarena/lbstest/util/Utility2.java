package com.tarena.lbstest.util;

import com.google.gson.Gson;
import com.tarena.lbstest.gson.Weather;


public class Utility2 {
    public static Weather handleInfoResponse(String response){
        try {
            return new Gson().fromJson(response, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
