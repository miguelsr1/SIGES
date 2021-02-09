/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.ComboValorSeguimiento;
import gob.mined.siap2.entities.data.impl.MetaIndicador;
import gob.mined.siap2.entities.data.impl.ValoresDeIndicador;
import gob.mined.siap2.entities.data.impl.ValoresSeguimiento;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class TipoSeguimientoUtils {
    
  
    public static List<ComboValorSeguimiento>  generateValoresDelProducto(TipoSeguimiento seguimiento, List<Integer> anios){

        int size =12;
        int multiplicador =1;
        
        if (TipoSeguimiento.TRIMESTRAL == seguimiento){
            size = size/3;//4
            multiplicador = 3;
        
        }else if (TipoSeguimiento.CUATRIMESTRAL == seguimiento){
            size = size/4;//3
            multiplicador = 4;
            
        }else if (TipoSeguimiento.SEMESTRAL == seguimiento){
            size = size/6;//2
            multiplicador = 6;
            
        }else if (TipoSeguimiento.ANUAL == seguimiento){
            size = size/12;//1
            multiplicador = 12;
        }
        
        
        List<ComboValorSeguimiento> l= new LinkedList();

        for (Integer anio :anios){
            ComboValorSeguimiento c = new ComboValorSeguimiento();
            c.setTipoSeguimiento(seguimiento);
            c.setAnio(anio);
            c.setValores(new LinkedList());


            for (int i=1; i<= size; i++){
                ValoresSeguimiento valor = new ValoresSeguimiento();
                valor.setPosicion(i * multiplicador);

                valor.setTipoValorSeguimiento(c);
                c.getValores().add(valor);
            }
            l.add(c);
        }
        
        return l;        
        
    }
    
    
    public static MetaIndicador  cargarValoresMeta(MetaIndicador meta){
         //semestral 12
        int size =12;
        int multiplicador =1;
        
        if (TipoSeguimiento.TRIMESTRAL == meta.getTipoSeguimiento()){
            size = size/3;//4
            multiplicador = 3;
        
        }else if (TipoSeguimiento.CUATRIMESTRAL == meta.getTipoSeguimiento()){
            size = size/4;//3
            multiplicador = 4;
            
        }else if (TipoSeguimiento.SEMESTRAL == meta.getTipoSeguimiento()){
            size = size/6;//2
            multiplicador = 6;
            
        }else if (TipoSeguimiento.ANUAL == meta.getTipoSeguimiento()){
            size = size/12;//1
            multiplicador = 12;
        }
        
        

       
        meta.setValores(new LinkedList());

        for (int i=1; i<= size; i++){
            ValoresDeIndicador valor = new ValoresDeIndicador();
           // valor.setPosicionjpa(i * multiplicador);
            valor.setMetaIndicador(meta);
            meta.getValores().add(valor);
        }
        
        return meta; 
    }
    
    
    
    
    
    public static int getMultiplicador (TipoSeguimiento seguimiento){
        if (TipoSeguimiento.MENSUAL == seguimiento){
            return 1;
        }else if (TipoSeguimiento.TRIMESTRAL == seguimiento){
            return 3;
        }if (TipoSeguimiento.CUATRIMESTRAL == seguimiento){
            return 4;
        }if (TipoSeguimiento.SEMESTRAL == seguimiento){
            return 6;
        }
        return 12;
    }
    
     
    public static List<String> getListName(TipoSeguimiento seguimiento){
        if (TipoSeguimiento.MENSUAL == seguimiento){
            return listaMensual;
        }else if (TipoSeguimiento.TRIMESTRAL == seguimiento){
            return listaTrimestral;
        }if (TipoSeguimiento.CUATRIMESTRAL == seguimiento){
            return listaCuatrimestral;
        }if (TipoSeguimiento.SEMESTRAL == seguimiento){
            return listaSemestral;
        }
        return listaAnual;
    }
    
    
       
    private static List<String> listaMensual = new LinkedList<>();
    static {
        listaMensual.add("ENE");
        listaMensual.add("FEB");
        listaMensual.add("MAR");
        listaMensual.add("ABR");
        listaMensual.add("MAY");
        listaMensual.add("JUN");
        listaMensual.add("JUL");
        listaMensual.add("AGO");
        listaMensual.add("SEPT");
        listaMensual.add("OCT");
        listaMensual.add("NOV");
        listaMensual.add("DIC");
    }
    
           
    private static List<String> listaTrimestral = new LinkedList<>();
    static {
        listaTrimestral.add("TRIM_1");
        listaTrimestral.add("TRIM_2");
        listaTrimestral.add("TRIM_3");
        listaTrimestral.add("TRIM_4");
    }
    
    
               
    private static List<String> listaCuatrimestral = new LinkedList<>();
    static {
        listaCuatrimestral.add("CUAT_1");
        listaCuatrimestral.add("CUAT_2");
        listaCuatrimestral.add("CUAT_3");
    }
    
                   
    private static List<String> listaSemestral = new LinkedList<>();
    static {
        listaSemestral.add("SEM_1");
        listaSemestral.add("SEM_2");
    }
    
    private static List<String> listaAnual = new LinkedList<>();
    static {
        listaAnual.add("ANIO");
    }
}
