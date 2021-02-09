/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.utils;

import java.io.Serializable;

public class DiaUtils implements Serializable {

    public static String transformacionDia(String dia) {
        if (dia.length() > 2) {
            return "";
        }
        String[] arr = dia.split("");
        String unidad = dia.length() > 1 ? arr[1] : arr[0];
        String decena = dia.length() > 1 ? arr[0] : "-1";
        String resultado = "";
        if(Integer.parseInt(dia)>10 && Integer.parseInt(dia)<14){
            switch(Integer.parseInt(dia)){
                case 11: resultado= "undécimo";
                break;
                case 12: resultado= "duodécimo";
                break;
                case 13: resultado= "décimotercero";
                break;
            }
            return resultado;
          
        }
        
        
        switch (decena) {
            case "1":
                resultado = "décimo ";
                break;
            case "2":
                resultado = "vigésimo ";
                break;
            case "3":
                resultado = "trigésimo ";
                break;    
            default:
        }

        switch (unidad) {
            case "1":
                resultado += "primero";
                break;
            case "2":
                resultado += "segundo";
                break;
            case "3":
                resultado += "tercero";
                break;
            case "4":
                resultado += "cuarto";
                break;
            case "5":
                resultado += "quinto";
                break;
            case "6":
                resultado += "sexto";
                break;
            case "7":
                resultado += "séptimo";
                break;
            case "8":
                resultado += "octavo";
                break;
            case "9":
                resultado += "noveno";
                break;

        }
        return resultado;
    }
}
