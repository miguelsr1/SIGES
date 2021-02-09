package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.enumerados.EnumAmbito;

/**
 *
 * @author fabricio
 */
public class SgObtenerOperacionesSeguridad implements Serializable {
    
    private String operacion;
    private String codigoUsuario;
    private EnumAmbito ambito;

    public SgObtenerOperacionesSeguridad() {
    }
    
    
    public SgObtenerOperacionesSeguridad(String operacion, String codigoUsuario) {
        this.operacion = operacion;
        this.codigoUsuario = codigoUsuario;
    }
    

    public SgObtenerOperacionesSeguridad(String operacion, String codigoUsuario, EnumAmbito ambito) {
        this.operacion = operacion;
        this.codigoUsuario = codigoUsuario;
        this.ambito = ambito;
    }
    
    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    
    public EnumAmbito getAmbito() {
        return ambito;
    }

    public void setAmbito(EnumAmbito ambito) {
        this.ambito = ambito;
    }
  
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.operacion);
        hash = 67 * hash + Objects.hashCode(this.codigoUsuario);
        hash = 67 * hash + Objects.hashCode(this.ambito);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgObtenerOperacionesSeguridad other = (SgObtenerOperacionesSeguridad) obj;
        if (!Objects.equals(this.operacion, other.operacion)) {
            return false;
        }
        if (!Objects.equals(this.codigoUsuario, other.codigoUsuario)) {
            return false;
        }
        if (this.ambito != other.ambito) {
            return false;
        }
        return true;
    }

    
    
}
