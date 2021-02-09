/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos de indicadores para el reporte de proyectos.
 * @author Sofis Solutions
 */
public class DataReporteProyectoIndicador {
    String indicador;
    String tipoSeguimiento;
    String fintoleranciaColorVerde;
    String fintoleranciaColorAmarillo;

     // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public String getTipoSeguimiento() {
        return tipoSeguimiento;
    }

    public void setTipoSeguimiento(String tipoSeguimiento) {
        this.tipoSeguimiento = tipoSeguimiento;
    }

    public String getFintoleranciaColorVerde() {
        return fintoleranciaColorVerde;
    }

    public void setFintoleranciaColorVerde(String fintoleranciaColorVerde) {
        this.fintoleranciaColorVerde = fintoleranciaColorVerde;
    }

    public String getFintoleranciaColorAmarillo() {
        return fintoleranciaColorAmarillo;
    }

    public void setFintoleranciaColorAmarillo(String fintoleranciaColorAmarillo) {
        this.fintoleranciaColorAmarillo = fintoleranciaColorAmarillo;
    }
   
   
    // </editor-fold>
    
}
