package com.tarena.lbstest.util;

import com.google.gson.Gson;
import com.tarena.lbstest.gson.Info;

public class Utility1 {
    public static Info handleInfoResponse(String response){
        try {
            return new Gson().fromJson(response, Info.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
