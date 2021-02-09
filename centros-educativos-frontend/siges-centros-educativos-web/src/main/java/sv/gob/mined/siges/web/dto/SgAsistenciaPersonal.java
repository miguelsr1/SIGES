/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoInasistenciaPersonal;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "apePk", scope = SgAsistenciaPersonal.class)
public class SgAsistenciaPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long apePk;
    
    private SgControlAsistenciaPersonalCabezal apeControl;
    
    private SgPersonalSedeEducativa apePersonal;
    
    private SgCargo apeCargo;
   
    private Boolean apeInasistencia;
    
    private SgMotivoInasistenciaPersonal apeMotivoInasistencia;
    
    private String apeObservacion;
    
    private LocalDateTime apeUltModFecha;
    
    private String apeUltModUsuario;
    
    private Integer apeVersion;

    public SgAsistenciaPersonal() {
        this.apeInasistencia = Boolean.FALSE;
    }

    public SgAsistenciaPersonal(Long apePk) {
        this.apePk = apePk;
    }

    public Long getApePk() {
        return apePk;
    }

    public void setApePk(Long apePk) {
        this.apePk = apePk;
    }

    public SgControlAsistenciaPersonalCabezal getApeControl() {
        return apeControl;
    }

    public void setApeControl(SgControlAsistenciaPersonalCabezal apeControl) {
        this.apeControl = apeControl;
    }

    public SgPersonalSedeEducativa getApePersonal() {
        return apePersonal;
    }

    public void setApePersonal(SgPersonalSedeEducativa apePersonal) {
        this.apePersonal = apePersonal;
    }

    public SgCargo getApeCargo() {
        return apeCargo;
    }

    public void setApeCargo(SgCargo apeCargo) {
        this.apeCargo = apeCargo;
    }

    public Boolean getApeInasistencia() {
        return apeInasistencia;
    }

    public void setApeInasistencia(Boolean apeInasistencia) {
        this.apeInasistencia = apeInasistencia;
    }

    public SgMotivoInasistenciaPersonal getApeMotivoInasistencia() {
        return apeMotivoInasistencia;
    }

    public void setApeMotivoInasistencia(SgMotivoInasistenciaPersonal apeMotivoInasistencia) {
        this.apeMotivoInasistencia = apeMotivoInasistencia;
    }

    public String getApeObservacion() {
        return apeObservacion;
    }

    public void setApeObservacion(String apeObservacion) {
        this.apeObservacion = apeObservacion;
    }

    public LocalDateTime getApeUltModFecha() {
        return apeUltModFecha;
    }

    public void setApeUltModFecha(LocalDateTime apeUltModFecha) {
        this.apeUltModFecha = apeUltModFecha;
    }

    public String getApeUltModUsuario() {
        return apeUltModUsuario;
    }

    public void setApeUltModUsuario(String apeUltModUsuario) {
        this.apeUltModUsuario = apeUltModUsuario;
    }

    public Integer getApeVersion() {
        return apeVersion;
    }

    public void setApeVersion(Integer apeVersion) {
        this.apeVersion = apeVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (apePk != null ? apePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAsistenciaPersonal)) {
            return false;
        }
        SgAsistenciaPersonal other = (SgAsistenciaPersonal) object;
        if ((this.apePk == null && other.apePk != null) || (this.apePk != null && !this.apePk.equals(other.apePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAsistenciaPersonal[ apePk=" + apePk + " ]";
    }
    
}
