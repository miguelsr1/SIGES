/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.EnumEstadoOrganismoAdministrativo;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_organismo_administracion_escolar", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@Cache(expiry = 150000)
public class SgOrganismoAdministracionEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "oae_pk", nullable = false)
    private Long oaePk;

    @JoinColumn(name = "oae_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede oaeSedeFk;

    @JoinColumn(name = "oae_tipo_organismo_administrativo_fk")
    @ManyToOne
    private SgTipoOrganismoAdministrativo oaeTipoOrganismoAdministrativo;
    
    @Size(max = 10)
    @Column(name = "oae_acta_integracion", length = 10)
    private String oaeActaIntegracion;

    @Column(name = "oae_fecha_acta_integracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date oaeFechaActaIntegracion;

    @Column(name = "oae_fecha_vencimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date oaeFechaVencimiento;

    @Column(name = "oae_miembros_vigentes")
    private Boolean oaeMiembrosVigentes;

    @Column(name = "oae_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoOrganismoAdministrativo oeaEstado;

    @Column(name = "oae_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date oaeUltModFecha;

    @Size(max = 45)
    @Column(name = "oae_ult_mod_usuario", length = 45)
    private String oaeUltModUsuario;

    @Column(name = "oae_version")
    @Version
    private Integer oaeVersion;

    public SgOrganismoAdministracionEscolar() {
    }

    public Long getOaePk() {
        return oaePk;
    }

    public void setOaePk(Long oaePk) {
        this.oaePk = oaePk;
    }

    public Date getOaeUltModFecha() {
        return oaeUltModFecha;
    }

    public void setOaeUltModFecha(Date oaeUltModFecha) {
        this.oaeUltModFecha = oaeUltModFecha;
    }

    public String getOaeUltModUsuario() {
        return oaeUltModUsuario;
    }

    public void setOaeUltModUsuario(String oaeUltModUsuario) {
        this.oaeUltModUsuario = oaeUltModUsuario;
    }

    public Integer getOaeVersion() {
        return oaeVersion;
    }

    public void setOaeVersion(Integer oaeVersion) {
        this.oaeVersion = oaeVersion;
    }

    public SgSede getOaeSedeFk() {
        return oaeSedeFk;
    }

    public void setOaeSedeFk(SgSede oaeSedeFk) {
        this.oaeSedeFk = oaeSedeFk;
    }

    public String getOaeActaIntegracion() {
        return oaeActaIntegracion;
    }

    public void setOaeActaIntegracion(String oaeActaIntegracion) {
        this.oaeActaIntegracion = oaeActaIntegracion;
    }

    public Date getOaeFechaActaIntegracion() {
        return oaeFechaActaIntegracion;
    }

    public void setOaeFechaActaIntegracion(Date oaeFechaActaIntegracion) {
        this.oaeFechaActaIntegracion = oaeFechaActaIntegracion;
    }

    public Date getOaeFechaVencimiento() {
        return oaeFechaVencimiento;
    }

    public void setOaeFechaVencimiento(Date oaeFechaVencimiento) {
        this.oaeFechaVencimiento = oaeFechaVencimiento;
    }

    public EnumEstadoOrganismoAdministrativo getOeaEstado() {
        return oeaEstado;
    }

    public void setOeaEstado(EnumEstadoOrganismoAdministrativo oeaEstado) {
        this.oeaEstado = oeaEstado;
    }

    public Boolean getOaeMiembrosVigentes() {
        return oaeMiembrosVigentes;
    }

    public void setOaeMiembrosVigentes(Boolean oaeMiembrosVigentes) {
        this.oaeMiembrosVigentes = oaeMiembrosVigentes;
    }

    public SgTipoOrganismoAdministrativo getOaeTipoOrganismoAdministrativo() {
        return oaeTipoOrganismoAdministrativo;
    }

    public void setOaeTipoOrganismoAdministrativo(SgTipoOrganismoAdministrativo oaeTipoOrganismoAdministrativo) {
        this.oaeTipoOrganismoAdministrativo = oaeTipoOrganismoAdministrativo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.oaePk);
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
        final SgOrganismoAdministracionEscolar other = (SgOrganismoAdministracionEscolar) obj;
        if (!Objects.equals(this.oaePk, other.oaePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgOrganismoAdministracionEscolar{" + "oaePk=" + oaePk + ", oaeUltModFecha=" + oaeUltModFecha + ", oaeUltModUsuario=" + oaeUltModUsuario + ", oaeVersion=" + oaeVersion + '}';
    }

}
