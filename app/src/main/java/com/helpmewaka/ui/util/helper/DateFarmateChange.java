package com.helpmewaka.ui.util.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Ravindra Birla on 21,December,2018
 */
public class DateFarmateChange {
    public static String convertddMMyyyyToyyyyMMdd(String stringData)
            throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd'
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd ");
        Date data = sdf.parse(stringData);
        String formattedTime = output.format(data);
        return formattedTime;
    }

    public static String convertyyyyMMddToddMMyyyy(String stringData)
            throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//y-MM-dd'
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
        Date data = sdf.parse(stringData);
        String formattedTime = output.format(data);
        return formattedTime;
    }

    // MMM dd, yyyy HH:mm to dd MMM, yyyy HH:mm
    public static String convertMMMddyyyyToddMMMyyyy(String stringData)
            throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");//y-MM-dd'
        SimpleDateFormat output = new SimpleDateFormat("d MMM, yyyy HH:mm");
        Date data = sdf.parse(stringData);
        String formattedTime = output.format(data);
        return formattedTime;
    }

    public static String convertDateStringFormat(String strDate, String fromFormat, String toFormat){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim());
            return dateFormat2.format(sdf.parse(strDate));
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
