/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Tipo de dato que incluye la disponibilidad de de una fuente de recurso.
 * @note Disponibilidad de una fuente de recurso.
 * @author Sofis Solutions
 */
public class DataFondoDisponibilidad {
    
    private String nombreFuente;
    private String monto;

    /**
     * Nombre de la fuente de recurso
     * @return 
     */
    public String getNombreFuente() {
        return nombreFuente;
    }

    /**
     * Actualizacion del nombre de la fuente de recurso.
     * @param nombreFuente 
     */
    public void setNombreFuente(String nombreFuente) {
        this.nombreFuente = nombreFuente;
    }

    /**
     * Monto asociado a la fuente de recurso.
     * @return 
     */
    public String getMonto() {
        return monto;
    }

    /**
     * Setea el monto de la fuente de recurso.
     * @param monto 
     */
    public void setMonto(String monto) {
        this.monto = monto;
    }
    
    
    
}
