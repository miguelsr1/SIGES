/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCargo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoInasistenciaPersonal;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_asistencias_personal", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "apePk", scope = SgAsistenciaPersonal.class)
public class SgAsistenciaPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ape_pk")
    private Long apePk;
    
    @JoinColumn(name = "ape_control_fk", referencedColumnName = "cpc_pk")
    @ManyToOne
    private SgControlAsistenciaPersonalCabezal apeControl;
    
    @JoinColumn(name = "ape_personal_fk", referencedColumnName = "pse_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPersonalSedeEducativa apePersonal;
    
    @JoinColumn(name = "ape_cargo_fk", referencedColumnName = "car_pk")
    @ManyToOne
    private SgCargo apeCargo;
   
    @Column(name = "ape_inasistencia")
    private Boolean apeInasistencia;
    
    @JoinColumn(name = "ape_motivo_inasistencia_fk", referencedColumnName = "mip_pk", nullable = true)
    @ManyToOne
    private SgMotivoInasistenciaPersonal apeMotivoInasistencia;
    
    @Size(max = 100)
    @Column(name = "ape_observacion", length = 100)
    private String apeObservacion;
    
    @Column(name = "ape_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime apeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ape_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String apeUltModUsuario;
    
    @Column(name = "ape_version")
    @Version
    private Integer apeVersion;

    public SgAsistenciaPersonal() {
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
