/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_notificaciones_cumplimiento", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ncuId", scope = SgAfNotificacionCumplimiento.class)
public class SgAfNotificacionCumplimiento implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ncu_id")
    private Long ncuId;
    
    @Column(name = "ncu_leida")
    private Boolean ncuLeida;
    
    @Basic(optional = false)
    @Column(name = "ncu_numero_notificacion")
    private short ncuNumeroNotificacion;
    
    @JoinColumn(name = "ncu_documento")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo ncuDocumento;
    
    @Basic(optional = false)
    @Column(name = "ncu_anio")
    private Integer ncuAnio;

    @JoinColumn(name = "ncu_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede ncuSedeFk;

    @JoinColumn(name = "ncu_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas ncuUnidadAdministrativaFk;
    
    @Column(name = "ncu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ncuUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ncu_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ncuUltModUsuario;
    
    @Column(name = "ncu_version")
    @Version
    private Integer ncuVersion;

    public SgAfNotificacionCumplimiento() {
    }

    public Long getNcuId() {
        return ncuId;
    }

    public void setNcuId(Long ncuId) {
        this.ncuId = ncuId;
    }

    public Boolean getNcuLeida() {
        return ncuLeida;
    }

    public void setNcuLeida(Boolean ncuLeida) {
        this.ncuLeida = ncuLeida;
    }

    public short getNcuNumeroNotificacion() {
        return ncuNumeroNotificacion;
    }

    public void setNcuNumeroNotificacion(short ncuNumeroNotificacion) {
        this.ncuNumeroNotificacion = ncuNumeroNotificacion;
    }
    
    
    public Integer getNcuAnio() {
        return ncuAnio;
    }

    public void setNcuAnio(Integer ncuAnio) {
        this.ncuAnio = ncuAnio;
    }

    public SgSede getNcuSedeFk() {
        return ncuSedeFk;
    }

    public void setNcuSedeFk(SgSede ncuSedeFk) {
        this.ncuSedeFk = ncuSedeFk;
    }

    public SgAfUnidadesAdministrativas getNcuUnidadAdministrativaFk() {
        return ncuUnidadAdministrativaFk;
    }

    public void setNcuUnidadAdministrativaFk(SgAfUnidadesAdministrativas ncuUnidadAdministrativaFk) {
        this.ncuUnidadAdministrativaFk = ncuUnidadAdministrativaFk;
    }

    public LocalDateTime getNcuUltModFecha() {
        return ncuUltModFecha;
    }

    public void setNcuUltModFecha(LocalDateTime ncuUltModFecha) {
        this.ncuUltModFecha = ncuUltModFecha;
    }

    public String getNcuUltModUsuario() {
        return ncuUltModUsuario;
    }

    public void setNcuUltModUsuario(String ncuUltModUsuario) {
        this.ncuUltModUsuario = ncuUltModUsuario;
    }

    public Integer getNcuVersion() {
        return ncuVersion;
    }

    public void setNcuVersion(Integer ncuVersion) {
        this.ncuVersion = ncuVersion;
    }

    public SgArchivo getNcuDocumento() {
        return ncuDocumento;
    }

    public void setNcuDocumento(SgArchivo ncuDocumento) {
        this.ncuDocumento = ncuDocumento;
    }

    @Override
    public String securityAmbitCreate() {
        if(ncuSedeFk != null) {
            return "ncuSedeFk";
        } else if(ncuUnidadAdministrativaFk != null) {
            return "ncuUnidadAdministrativaFk";
        } else {
            return null;
        }
    } 
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ncuSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ncuUnidadAdministrativaFk.uadUnidadActivoFijoFk.uafDepartamento.depPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ncuSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.UNIDAD_ADMINISTRATIVA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ncuUnidadAdministrativaFk.uadPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ncuId", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ncuId != null ? ncuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfNotificacionCumplimiento)) {
            return false;
        }
        SgAfNotificacionCumplimiento other = (SgAfNotificacionCumplimiento) object;
        if ((this.ncuId == null && other.ncuId != null) || (this.ncuId != null && !this.ncuId.equals(other.ncuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfNotificacionesCumplimiento[ ncuId=" + ncuId + " ]";
    }
    
}
