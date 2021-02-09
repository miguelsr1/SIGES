/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos para el reporte de asignaciones no programables en lo que respecta a montos.
 * Máximo de años: 5, que es lo que corresponde a un periodo presupuestal.
 * @author Sofis Solutions
 */
public class DataReporteANPMontos {
    private String nombreActividad="";
    private String meta="";
    private String anio1="";
    private String anio2="";
    private String anio3="";
    private String anio4="";

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getAnio1() {
        return anio1;
    }

    public void setAnio1(String anio1) {
        this.anio1 = anio1;
    }

    public String getAnio2() {
        return anio2;
    }

    public void setAnio2(String anio2) {
        this.anio2 = anio2;
    }

    public String getAnio3() {
        return anio3;
    }

    public void setAnio3(String anio3) {
        this.anio3 = anio3;
    }

    public String getAnio4() {
        return anio4;
    }

    public void setAnio4(String anio4) {
        this.anio4 = anio4;
    }

   

    
    
    
    
    
    
    
    
    
}
