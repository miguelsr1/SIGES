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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_gestion_anio_lectivo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "galPk", scope = SgGestionAnioLectivo.class)
@Audited
public class SgGestionAnioLectivo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "gal_pk")
    private Long galPk;
    
    @Size(max = 4)
    @Column(name = "gal_codigo",length = 4)
    @AtributoCodigo
    private String galCodigo;
    
    @Basic(optional = false)
    @Column(name = "gal_anio")
    private Integer galAnio;
    
    @Basic(optional = false)
    @Column(name = "gal_fecha_desde")
    private LocalDate galFechaDesde;
    
    @Basic(optional = false)
    @Column(name = "gal_fecha_hasta")
    private LocalDate galFechaHasta;
    
    @Column(name = "gal_anio_actual")
    private Boolean galAnioActual;
    
    @Column(name = "gal_habilitado")
    @AtributoHabilitado
    private Boolean galHabilitado;
    
    @Column(name = "gal_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime galUltModFecha;
    
    @Size(max = 45)
    @Column(name = "gal_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String galUltModUsuario;
    
    @Column(name = "gal_version")
    @Version
    private Integer galVersion;

    public SgGestionAnioLectivo() {
    }

    public SgGestionAnioLectivo(Long galPk) {
        this.galPk = galPk;
    }

    public Long getGalPk() {
        return galPk;
    }

    public void setGalPk(Long galPk) {
        this.galPk = galPk;
    }

    public String getGalCodigo() {
        return galCodigo;
    }

    public void setGalCodigo(String galCodigo) {
        this.galCodigo = galCodigo;
    }

    public Integer getGalAnio() {
        return galAnio;
    }

    public void setGalAnio(Integer galAnio) {
        this.galAnio = galAnio;
    }

    public LocalDate getGalFechaDesde() {
        return galFechaDesde;
    }

    public void setGalFechaDesde(LocalDate galFechaDesde) {
        this.galFechaDesde = galFechaDesde;
    }

    public LocalDate getGalFechaHasta() {
        return galFechaHasta;
    }

    public void setGalFechaHasta(LocalDate galFechaHasta) {
        this.galFechaHasta = galFechaHasta;
    }

    public Boolean getGalAnioActual() {
        return galAnioActual;
    }

    public void setGalAnioActual(Boolean galAnioActual) {
        this.galAnioActual = galAnioActual;
    }

    public Boolean getGalHabilitado() {
        return galHabilitado;
    }

    public void setGalHabilitado(Boolean galHabilitado) {
        this.galHabilitado = galHabilitado;
    }

    public LocalDateTime getGalUltModFecha() {
        return galUltModFecha;
    }

    public void setGalUltModFecha(LocalDateTime galUltModFecha) {
        this.galUltModFecha = galUltModFecha;
    }

    public String getGalUltModUsuario() {
        return galUltModUsuario;
    }

    public void setGalUltModUsuario(String galUltModUsuario) {
        this.galUltModUsuario = galUltModUsuario;
    }

    public Integer getGalVersion() {
        return galVersion;
    }

    public void setGalVersion(Integer galVersion) {
        this.galVersion = galVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (galPk != null ? galPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgGestionAnioLectivo)) {
            return false;
        }
        SgGestionAnioLectivo other = (SgGestionAnioLectivo) object;
        if ((this.galPk == null && other.galPk != null) || (this.galPk != null && !this.galPk.equals(other.galPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgGestionAnioLectivo[ galPk=" + galPk + " ]";
    }
    
}
