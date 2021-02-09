/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoInstitucionPaga;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_experiencia_laboral", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "elaPk", scope = SgExperienciaLaboral.class)
@Audited
public class SgExperienciaLaboral implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ela_pk")
    private Long elaPk;

    @JoinColumn(name = "ela_dato_empleado_fk", referencedColumnName = "dem_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgDatoEmpleadoLite elaDatoEmpleado;

    @Size(max = 255)
    @Column(name = "ela_institucion", length = 255)
    private String elaInstitucion;

    @JoinColumn(name = "ela_tipo_institucion", referencedColumnName = "tip_pk")
    @ManyToOne
    private SgTipoInstitucionPaga elaTipoInstitucion;

    @Size(max = 255)
    @Column(name = "ela_direccion", length = 255)
    private String elaDireccion;

    @Size(max = 100)
    @Column(name = "ela_cargo", length = 100)
    private String elaCargo;

    @Column(name = "ela_desde")
    private LocalDate elaDesde;

    @Column(name = "ela_hasta")
    private LocalDate elaHasta;
    
    @Column(name = "ela_validada")
    private Boolean elaValidada;
    
    @Column(name = "ela_ult_val_fecha")
    private LocalDateTime elaUltValidacionFecha;

    @Column(name = "ela_ult_val_usuario")
    private String elaUltValidacionUsuario;


    @Column(name = "ela_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime elaUltModFecha;

    @Size(max = 45)
    @Column(name = "ela_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String elaUltModUsuario;

    @Column(name = "ela_version")
    @Version
    private Integer elaVersion;

    public SgExperienciaLaboral() {
        this.elaValidada = Boolean.FALSE;
    }

    public SgExperienciaLaboral(Long elaPk) {
        this.elaPk = elaPk;
    }

    public Long getElaPk() {
        return elaPk;
    }

    public void setElaPk(Long elaPk) {
        this.elaPk = elaPk;
    }

    public String getElaInstitucion() {
        return elaInstitucion;
    }

    public void setElaInstitucion(String elaInstitucion) {
        this.elaInstitucion = elaInstitucion;
    }

    public SgTipoInstitucionPaga getElaTipoInstitucion() {
        return elaTipoInstitucion;
    }

    public void setElaTipoInstitucion(SgTipoInstitucionPaga elaTipoInstitucion) {
        this.elaTipoInstitucion = elaTipoInstitucion;
    }

    public String getElaDireccion() {
        return elaDireccion;
    }

    public void setElaDireccion(String elaDireccion) {
        this.elaDireccion = elaDireccion;
    }

    public String getElaCargo() {
        return elaCargo;
    }

    public void setElaCargo(String elaCargo) {
        this.elaCargo = elaCargo;
    }

    public LocalDate getElaDesde() {
        return elaDesde;
    }

    public void setElaDesde(LocalDate elaDesde) {
        this.elaDesde = elaDesde;
    }

    public LocalDate getElaHasta() {
        return elaHasta;
    }

    public void setElaHasta(LocalDate elaHasta) {
        this.elaHasta = elaHasta;
    }

    public LocalDateTime getElaUltModFecha() {
        return elaUltModFecha;
    }

    public void setElaUltModFecha(LocalDateTime elaUltModFecha) {
        this.elaUltModFecha = elaUltModFecha;
    }

    public String getElaUltModUsuario() {
        return elaUltModUsuario;
    }

    public void setElaUltModUsuario(String elaUltModUsuario) {
        this.elaUltModUsuario = elaUltModUsuario;
    }

    public Integer getElaVersion() {
        return elaVersion;
    }

    public void setElaVersion(Integer elaVersion) {
        this.elaVersion = elaVersion;
    }

    public SgDatoEmpleadoLite getElaDatoEmpleado() {
        return elaDatoEmpleado;
    }

    public void setElaDatoEmpleado(SgDatoEmpleadoLite elaDatoEmpleado) {
        this.elaDatoEmpleado = elaDatoEmpleado;
    }

    public Boolean getElaValidada() {
        return elaValidada;
    }

    public void setElaValidada(Boolean elaValidada) {
        this.elaValidada = elaValidada;
    }

    public LocalDateTime getElaUltValidacionFecha() {
        return elaUltValidacionFecha;
    }

    public void setElaUltValidacionFecha(LocalDateTime elaUltValidacionFecha) {
        this.elaUltValidacionFecha = elaUltValidacionFecha;
    }

    public String getElaUltValidacionUsuario() {
        return elaUltValidacionUsuario;
    }

    public void setElaUltValidacionUsuario(String elaUltValidacionUsuario) {
        this.elaUltValidacionUsuario = elaUltValidacionUsuario;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (elaPk != null ? elaPk.hashCode() : 0);
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
        final SgExperienciaLaboral other = (SgExperienciaLaboral) obj;
        if (!Objects.equals(this.elaPk, other.elaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgExperienciaLaboral{" + "elaPk=" + elaPk + ", elaInstitucion=" + elaInstitucion + ", elaTipoInstitucion=" + elaTipoInstitucion + ", elaDireccion=" + elaDireccion + ", elaCargo=" + elaCargo + ", elaDesde=" + elaDesde + ", elaHasta=" + elaHasta + ", elaValidada=" + elaValidada + ", elaVersion=" + elaVersion + '}';
    }

    

   

}
