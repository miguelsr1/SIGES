package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataRolUsuario {
    private Integer idUnidadTecnica;
    private Integer idSsRol;
    
    private List<String> codigoOperaciones;

    public Integer getIdUnidadTecnica() {
        return idUnidadTecnica;
    }

    public void setIdUnidadTecnica(Integer idUnidadTecnica) {
        this.idUnidadTecnica = idUnidadTecnica;
    }

    public Integer getIdSsRol() {
        return idSsRol;
    }

    public void setIdSsRol(Integer idSsRol) {
        this.idSsRol = idSsRol;
    }


    public List<String> getCodigoOperaciones() {
        return codigoOperaciones;
    }

    public void setCodigoOperaciones(List<String> codigoOperaciones) {
        this.codigoOperaciones = codigoOperaciones;
    }
    
    
    
    
    
}
