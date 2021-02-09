/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;


/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reaPk", scope = SgRelModEdModAten.class)
public class SgRelModEdModAten implements Serializable {

    private static final long serialVersionUID = 1L;
   
    private Long reaPk;
    
    private SgModalidad reaModalidadEducativa;
    
    private SgModalidadAtencion reaModalidadAtencion;
    
    private SgSubModalidadAtencion reaSubModalidadAtencion;
       
    private Integer reaVersion;
    

    public SgRelModEdModAten() {
    }

    public SgRelModEdModAten(Long reaPk) {
        this.reaPk = reaPk;
    }

    public Long getReaPk() {
        return reaPk;
    }

    public void setReaPk(Long reaPk) {
        this.reaPk = reaPk;
    }

    public SgModalidad getReaModalidadEducativa() {
        return reaModalidadEducativa;
    }

    public void setReaModalidadEducativa(SgModalidad reaModalidadEducativa) {
        this.reaModalidadEducativa = reaModalidadEducativa;
    }

    public SgModalidadAtencion getReaModalidadAtencion() {
        return reaModalidadAtencion;
    }

    public void setReaModalidadAtencion(SgModalidadAtencion reaModalidadAtencion) {
        this.reaModalidadAtencion = reaModalidadAtencion;
    }

    public SgSubModalidadAtencion getReaSubModalidadAtencion() {
        return reaSubModalidadAtencion;
    }

    public void setReaSubModalidadAtencion(SgSubModalidadAtencion reaSubModalidadAtencion) {
        this.reaSubModalidadAtencion = reaSubModalidadAtencion;
    }

    public Integer getReaVersion() {
        return reaVersion;
    }

    public void setReaVersion(Integer reaVersion) {
        this.reaVersion = reaVersion;
    }

    
    public String getReaModalidadAtencionNombre(){
        return this.reaModalidadAtencion != null ? this.reaModalidadAtencion.getMatNombre() : "";
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reaPk != null ? reaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRelModEdModAten)) {
            return false;
        }
        SgRelModEdModAten other = (SgRelModEdModAten) object;
        if ((this.reaPk == null && other.reaPk != null) || (this.reaPk != null && !this.reaPk.equals(other.reaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelModEdModAten[ reaPk=" + reaPk + " ]";
    }
    
}
