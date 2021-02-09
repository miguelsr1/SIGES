/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofis.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sofis3
 */
public class DatesUtils {
    public static DateFormat dfto = new SimpleDateFormat("yyyy-MM-dd");  
       
    public static String getDateInStringFormat(Date date){
        String stringDate = dfto.format(date);
        return stringDate;
    }
}
