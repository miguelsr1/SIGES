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
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "ss_anio_fiscal", uniqueConstraints = {
    @UniqueConstraint(name = "ani_anio_uk", columnNames = {"ani_anio"})},
        schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "aniPk", scope = SgAnioFiscal.class)
/**
 * Entidad correspondiente a los años fiscales
 */
public class SgAnioFiscal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ani_id")
    private Long aniPk;

    @Column(name = "ani_anio")
    private Integer aniAnio;

    @Column(name = "ani_desde")
    private LocalDateTime aniDesde;

    @Column(name = "ani_ejecucion")
    private Boolean aniEjecucion;

    @Column(name = "ani_planificacion")
    private Boolean aniPlanificacion;

    @Column(name = "ani_hasta")
    private LocalDateTime aniHasta;

    @Size(max = 255)
    @Column(name = "ani_nombre", length = 255)
    private String aniNombre;

    @Column(name = "ani_ult_mod")
    @AtributoUltimaModificacion
    private LocalDateTime aniUltModFecha;

    @Size(max = 45)
    @Column(name = "ani_ult_usuario", length = 45)
    @AtributoUltimoUsuario
    private String aniUltModUsuario;

    @Column(name = "ani_version")
    @Version
    private Integer aniVersion;

    @Column(name = "ani_cerrado")
    private Boolean aniCerrado;

    @Column(name = "ani_formulacion_ce")
    private Boolean aniFormulacionCe;

    @Column(name = "ani_ajuste_ce")
    private Boolean aniAjusteCe;

    public SgAnioFiscal() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public Long getAniPk() {
        return aniPk;
    }

    public void setAniPk(Long aniPk) {
        this.aniPk = aniPk;
    }

    public Integer getAniAnio() {
        return aniAnio;
    }

    public void setAniAnio(Integer aniAnio) {
        this.aniAnio = aniAnio;
    }

    public LocalDateTime getAniDesde() {
        return aniDesde;
    }

    public void setAniDesde(LocalDateTime aniDesde) {
        this.aniDesde = aniDesde;
    }

    public Boolean getAniEjecucion() {
        return aniEjecucion;
    }

    public void setAniEjecucion(Boolean aniEjecucion) {
        this.aniEjecucion = aniEjecucion;
    }

    public Boolean getAniPlanificacion() {
        return aniPlanificacion;
    }

    public void setAniPlanificacion(Boolean aniPlanificacion) {
        this.aniPlanificacion = aniPlanificacion;
    }

    public LocalDateTime getAniHasta() {
        return aniHasta;
    }

    public void setAniHasta(LocalDateTime aniHasta) {
        this.aniHasta = aniHasta;
    }

    public String getAniNombre() {
        return aniNombre;
    }

    public void setAniNombre(String aniNombre) {
        this.aniNombre = aniNombre;
    }

    public LocalDateTime getAniUltModFecha() {
        return aniUltModFecha;
    }

    public void setAniUltModFecha(LocalDateTime aniUltModFecha) {
        this.aniUltModFecha = aniUltModFecha;
    }

    public String getAniUltModUsuario() {
        return aniUltModUsuario;
    }

    public void setAniUltModUsuario(String aniUltModUsuario) {
        this.aniUltModUsuario = aniUltModUsuario;
    }

    public Integer getAniVersion() {
        return aniVersion;
    }

    public void setAniVersion(Integer aniVersion) {
        this.aniVersion = aniVersion;
    }

    public Boolean getAniCerrado() {
        return aniCerrado;
    }

    public void setAniCerrado(Boolean aniCerrado) {
        this.aniCerrado = aniCerrado;
    }

    public Boolean getAniFormulacionCe() {
        return aniFormulacionCe;
    }

    public void setAniFormulacionCe(Boolean aniFormulacionCe) {
        this.aniFormulacionCe = aniFormulacionCe;
    }

    public Boolean getAniAjusteCe() {
        return aniAjusteCe;
    }

    public void setAniAjusteCe(Boolean aniAjusteCe) {
        this.aniAjusteCe = aniAjusteCe;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aniPk != null ? aniPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAnioFiscal)) {
            return false;
        }
        SgAnioFiscal other = (SgAnioFiscal) object;
        if ((this.aniPk == null && other.aniPk != null) || (this.aniPk != null && !this.aniPk.equals(other.aniPk))) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAnioFiscal[ aniPk=" + aniPk + " ]";
    }
    // </editor-fold>

}
