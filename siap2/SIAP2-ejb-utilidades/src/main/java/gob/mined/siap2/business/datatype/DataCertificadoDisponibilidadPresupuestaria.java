/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Esta clase clae se usa para e paginador del certificado de diponibilidad presupuestaria
 * 
 *
 * @note Tipo de dato que corresponde a una fila del reporte global PEP.
 * @author Sofis Solutions
 */
public class DataCertificadoDisponibilidadPresupuestaria {

    private Integer idUnidadTecnica;
    private Integer idProcesoAdquisicion;
    private String nombreUnidadTecnica;
    private String nombreProcesoAdquisicion;
    private Integer anioProceso;


    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
   

    public Integer getIdUnidadTecnica() {
        return idUnidadTecnica;
    }

    public void setIdUnidadTecnica(Integer idUnidadTecnica) {
        this.idUnidadTecnica = idUnidadTecnica;
    }

    public Integer getIdProcesoAdquisicion() {
        return idProcesoAdquisicion;
    }

    public void setIdProcesoAdquisicion(Integer idProcesoAdquisicion) {
        this.idProcesoAdquisicion = idProcesoAdquisicion;
    }

    public String getNombreUnidadTecnica() {
        return nombreUnidadTecnica;
    }

    public void setNombreUnidadTecnica(String nombreUnidadTecnica) {
        this.nombreUnidadTecnica = nombreUnidadTecnica;
    }

    public String getNombreProcesoAdquisicion() {
        return nombreProcesoAdquisicion;
    }

    public void setNombreProcesoAdquisicion(String nombreProcesoAdquisicion) {
        this.nombreProcesoAdquisicion = nombreProcesoAdquisicion;
    }

    public Integer getAnioProceso() {
        return anioProceso;
    }

    public void setAnioProceso(Integer anioProceso) {
        this.anioProceso = anioProceso;
    }
    
    

    
    
    
     // </editor-fold>

}
