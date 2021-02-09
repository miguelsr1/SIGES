/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.enumerados;

import java.time.DayOfWeek;

public enum EnumCeldaDiaHora {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado"),
    DOMINGO("Domingo");
    private final String text;

    private EnumCeldaDiaHora(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    private static final EnumCeldaDiaHora[] ENUMS = EnumCeldaDiaHora.values();
    private static final DayOfWeek[] ENUMS_DAY = DayOfWeek.values();
    
    public static EnumCeldaDiaHora dia(int dayOfWeek) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            return null;
        }
        return ENUMS[dayOfWeek - 1];
    }
    public static Integer numero(EnumCeldaDiaHora dayOfWeek) {
        switch(dayOfWeek){
            case LUNES: return 0;
            case MARTES: return 1;   
            case MIERCOLES: return 2;  
            case JUEVES: return 3;  
            case VIERNES: return 4;  
            case SABADO: return 5;  
            case DOMINGO: return 6;
            default: return -1;
            
        }
       
    }
    
    public static DayOfWeek diaSemana(EnumCeldaDiaHora dayOfWeek) {
        Integer i=numero(dayOfWeek);
        if (i.equals(-1)) {
            return null;
        }
        return ENUMS_DAY[numero(dayOfWeek)];
    }

}
