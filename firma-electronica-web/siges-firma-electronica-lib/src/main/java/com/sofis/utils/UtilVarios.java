/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofis.utils;

import javax.activation.DataHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
public class UtilVarios {
    
    /**
     * Metodo auxiliar que transforma un Integer en un Long
     *
     * @param value
     * @return
     */
    public static Long toLong(Integer value) {
        if (value == null) {
            return null;
        }
        Long hastaL = null;
        try {
            hastaL = new Long(value);
        } catch (Exception w) {
        }

        return hastaL;
    }

    /**
     * Metodo auxiliar que transforma un String que representa una clase en una
     * tipo Class
     *
     * @param className
     * @return
     * @throws DAOGeneralException
     */
    public static Class toClass(String className) {
        Class class_ = null;
        try {
            class_ = Class.forName(className);
        } catch (Exception ex) {

        }
        return class_;
    }
    
    /**
     * Metodo para sumarle 10 dias a la fecha actual
     * funcional para registrar documentos con 10 dias de vigencia
     * @param fecha
     * @param dias
     * @return 
     */
    public static Date sumarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }
    
    /**
     * Para convertir la lista de DataHnadle de Documento de Aplicativo a Bytes Array
     * @param data
     * @return
     * @throws IOException 
     */
    public static List<byte[]> getBytesFromDataHandler(final List<DataHandler> data) throws IOException {

        List<byte[]> outList = new ArrayList<>();
        for (DataHandler dh : data) {
            InputStream input = dh.getInputStream();
            if (input != null) {
                byte[] out = new byte[input.available()];
                input.read(out);
                input.close();
                outList.add(out);
            }
        }

        return outList;
    }
    
    /**
     * Agrega firmado al nombre del archivo
     * @param str
     * @return 
     */
    public static String addCharFirmado(String str) {
        
        String[] x = str.split("\\.");
        String s = x[0]+".firmado."+x[1];
        
        return s;
    }
    
    public static String addIdTransaccion(String urlRetornoNavegacion, String idTransaccion) {
        if(StringUtils.isBlank(urlRetornoNavegacion)) {
            return urlRetornoNavegacion;
        }
        if(urlRetornoNavegacion.contains("?")) {
            urlRetornoNavegacion = urlRetornoNavegacion+"&";
        }else {
            urlRetornoNavegacion = urlRetornoNavegacion+"?";
        }
        return urlRetornoNavegacion + "id_transaccion="+idTransaccion;
    }
    
}
