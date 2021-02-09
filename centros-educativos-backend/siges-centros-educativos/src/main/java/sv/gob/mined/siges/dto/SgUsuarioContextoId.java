/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "uscPk", scope = SgUsuarioContextoId.class)
public class SgUsuarioContextoId implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long uscPk;
   
    private EnumAmbito uscAmbito;
    
    private Long uscContextoId;
    
    private String uscCodigoRol;
    
    private List<SgUsuario> uscUsuarios;

    public Long getUscPk() {
        return uscPk;
    }

    public void setUscPk(Long uscPk) {
        this.uscPk = uscPk;
    }

    public List<SgUsuario> getUscUsuarios() {
        return uscUsuarios;
    }

    public void setUscUsuarios(List<SgUsuario> uscUsuarios) {
        this.uscUsuarios = uscUsuarios;
    }

   
    public EnumAmbito getUscAmbito() {
        return uscAmbito;
    }

    public void setUscAmbito(EnumAmbito uscAmbito) {
        this.uscAmbito = uscAmbito;
    }

    public Long getUscContextoId() {
        return uscContextoId;
    }

    public void setUscContextoId(Long uscContextoId) {
        this.uscContextoId = uscContextoId;
    }

    public String getUscCodigoRol() {
        return uscCodigoRol;
    }

    public void setUscCodigoRol(String uscCodigoRol) {
        this.uscCodigoRol = uscCodigoRol;
    }
  
    
}
