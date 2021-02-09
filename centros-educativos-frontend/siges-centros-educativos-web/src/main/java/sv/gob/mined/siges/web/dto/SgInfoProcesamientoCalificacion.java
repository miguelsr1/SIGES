/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ipcPk", scope = SgInfoProcesamientoCalificacion.class)
public class SgInfoProcesamientoCalificacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ipcPk;

    private String ipcError;    

    private LocalDateTime ipcUltModFecha;   

    private String ipcUltModUsuario;

    private Integer ipcVersion;
    
    
    public SgInfoProcesamientoCalificacion() {
       
    }

    public Long getIpcPk() {
        return ipcPk;
    }

    public void setIpcPk(Long ipcPk) {
        this.ipcPk = ipcPk;
    }

    public String getIpcError() {
        return ipcError;
    }

    public void setIpcError(String ipcError) {
        this.ipcError = ipcError;
    }

    

    public LocalDateTime getIpcUltModFecha() {
        return ipcUltModFecha;
    }

    public void setIpcUltModFecha(LocalDateTime ipcUltModFecha) {
        this.ipcUltModFecha = ipcUltModFecha;
    }

    public String getIpcUltModUsuario() {
        return ipcUltModUsuario;
    }

    public void setIpcUltModUsuario(String ipcUltModUsuario) {
        this.ipcUltModUsuario = ipcUltModUsuario;
    }

    public Integer getIpcVersion() {
        return ipcVersion;
    }

    public void setIpcVersion(Integer ipcVersion) {
        this.ipcVersion = ipcVersion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ipcPk != null ? ipcPk.hashCode() : 0);
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
        final SgInfoProcesamientoCalificacion other = (SgInfoProcesamientoCalificacion) obj;
        if (!Objects.equals(this.ipcPk, other.ipcPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgAcuerdo[ acuPk=" + ipcPk + " ]";
    }
    
}
