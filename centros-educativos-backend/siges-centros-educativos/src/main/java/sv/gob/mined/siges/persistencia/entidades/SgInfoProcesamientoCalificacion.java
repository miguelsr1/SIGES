/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "sg_info_procesamiento_calificacion", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ipcPk", scope = SgInfoProcesamientoCalificacion.class)
@Audited
public class SgInfoProcesamientoCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ipc_pk")
    private Long ipcPk;

    
    @Column(name = "ipc_error")
    private String ipcError;
    
    @Column(name = "ipc_ult_mod_fecha")
    @AtributoUltimaModificacion 
    private LocalDateTime ipcUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ipc_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String ipcUltModUsuario;
    
    @Column(name = "ipc_version")
    @Version
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
        return "sv.gob.mined.siges.persistencia.entidades.SigesCiclo[ ipcPk=" + ipcPk + " ]";
    }

  

}
