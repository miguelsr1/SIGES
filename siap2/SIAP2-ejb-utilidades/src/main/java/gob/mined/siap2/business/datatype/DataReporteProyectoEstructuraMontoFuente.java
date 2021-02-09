/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Monto por fuente en la estructura del proyecto.
 *
 * @author Sofis Solutions
 */
public class DataReporteProyectoEstructuraMontoFuente {

    String numero = "";
    String nombre = "";
    String fuente = "";
    String importe = "";

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

      // </editor-fold>
}
