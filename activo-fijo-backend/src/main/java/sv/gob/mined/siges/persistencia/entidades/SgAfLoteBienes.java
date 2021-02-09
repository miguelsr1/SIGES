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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
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
@Table(name = "sg_af_lote_bienes", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lbiId", scope = SgAfLoteBienes.class)
public class SgAfLoteBienes implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lbi_id", nullable = false)
    private Long lbiId;
    
    @Size(max = 20)
    @Column(name = "lbi_codigo_inventario_padre", length =20)
    private String lbiCodigoInventarioPadre;
    
    @Size(max = 20)
    @Column(name = "lbi_primer_cod_inventario", length =20)
    private String lbiPrimerCodInventario;
    
    @Size(max = 20)
    @Column(name = "lbi_ultimo_cod_inventario", length =20)
    private String lbiUltimoCodInventario;
    
    @Column(name = "lbi_ultimo_correlativo")
    private Integer lbiUltimoCorrelativo;
    
    @Basic(optional = false)
    @Column(name = "lbi_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadosProceso lbiEstado;
    
    @Column(name = "lbi_cantidad_bienes_replicar")
    private Integer lbiCantidadBienesReplicar;
    
    
    @Column(name = "lbi_fecha_inicio_procesamiento")
    private LocalDateTime lbiFechaInicioProcesamiento;
    
    @Column(name = "lbi_fecha_final_procesamiento")
    private LocalDateTime lbiFechaFinalProcesamiento;
    
    @JoinColumn(name = "lbi_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede lbiSede;

    @JoinColumn(name = "lbi_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas lbiUnidadesAdministrativas;
    
    
    @Column(name = "lbi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime lbiUltModFecha;
    
    @Size(max = 45)
    @Column(name = "lbi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String lbiUltModUsuario;
    
    @Column(name = "lbi_version")
    @Version
    private Integer lbiVersion;

    public SgAfLoteBienes() {
    }

    public Long getLbiId() {
        return lbiId;
    }

    public void setLbiId(Long lbiId) {
        this.lbiId = lbiId;
    }

    public String getLbiUltimoCodInventario() {
        return lbiUltimoCodInventario;
    }

    public void setLbiUltimoCodInventario(String lbiUltimoCodInventario) {
        this.lbiUltimoCodInventario = lbiUltimoCodInventario;
    }

    public LocalDateTime getLbiUltModFecha() {
        return lbiUltModFecha;
    }

    public void setLbiUltModFecha(LocalDateTime lbiUltModFecha) {
        this.lbiUltModFecha = lbiUltModFecha;
    }

    public String getLbiUltModUsuario() {
        return lbiUltModUsuario;
    }

    public void setLbiUltModUsuario(String lbiUltModUsuario) {
        this.lbiUltModUsuario = lbiUltModUsuario;
    }

    public Integer getLbiVersion() {
        return lbiVersion;
    }

    public void setLbiVersion(Integer lbiVersion) {
        this.lbiVersion = lbiVersion;
    }
    
    public EnumEstadosProceso getLbiEstado() {
        return lbiEstado;
    }

    public void setLbiEstado(EnumEstadosProceso lbiEstado) {
        this.lbiEstado = lbiEstado;
    }

    public LocalDateTime getLbiFechaInicioProcesamiento() {
        return lbiFechaInicioProcesamiento;
    }

    public void setLbiFechaInicioProcesamiento(LocalDateTime lbiFechaInicioProcesamiento) {
        this.lbiFechaInicioProcesamiento = lbiFechaInicioProcesamiento;
    }

    public LocalDateTime getLbiFechaFinalProcesamiento() {
        return lbiFechaFinalProcesamiento;
    }

    public void setLbiFechaFinalProcesamiento(LocalDateTime lbiFechaFinalProcesamiento) {
        this.lbiFechaFinalProcesamiento = lbiFechaFinalProcesamiento;
    }

    public String getLbiCodigoInventarioPadre() {
        return lbiCodigoInventarioPadre;
    }

    public void setLbiCodigoInventarioPadre(String lbiCodigoInventarioPadre) {
        this.lbiCodigoInventarioPadre = lbiCodigoInventarioPadre;
    }

    public String getLbiPrimerCodInventario() {
        return lbiPrimerCodInventario;
    }

    public void setLbiPrimerCodInventario(String lbiPrimerCodInventario) {
        this.lbiPrimerCodInventario = lbiPrimerCodInventario;
    }

    public SgSede getLbiSede() {
        return lbiSede;
    }

    public void setLbiSede(SgSede lbiSede) {
        this.lbiSede = lbiSede;
    }

    public SgAfUnidadesAdministrativas getLbiUnidadesAdministrativas() {
        return lbiUnidadesAdministrativas;
    }

    public void setLbiUnidadesAdministrativas(SgAfUnidadesAdministrativas lbiUnidadesAdministrativas) {
        this.lbiUnidadesAdministrativas = lbiUnidadesAdministrativas;
    }

    public Integer getLbiCantidadBienesReplicar() {
        return lbiCantidadBienesReplicar;
    }

    public void setLbiCantidadBienesReplicar(Integer lbiCantidadBienesReplicar) {
        this.lbiCantidadBienesReplicar = lbiCantidadBienesReplicar;
    }

    public Integer getLbiUltimoCorrelativo() {
        return lbiUltimoCorrelativo;
    }

    public void setLbiUltimoCorrelativo(Integer lbiUltimoCorrelativo) {
        this.lbiUltimoCorrelativo = lbiUltimoCorrelativo;
    }
    
    @Override
    public String securityAmbitCreate() {
        if(lbiSede != null) {
            return "lbiSede";
        } else if(lbiUnidadesAdministrativas != null) {
            return "lbiUnidadesAdministrativas";
        } else {
            return null;
        }
    } 
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lbiSede.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lbiUnidadesAdministrativas.uadUnidadActivoFijoFk.uafDepartamento.depPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lbiSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.UNIDAD_ADMINISTRATIVA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lbiUnidadesAdministrativas.uadPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lbiId", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lbiId != null ? lbiId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfLoteBienes)) {
            return false;
        }
        SgAfLoteBienes other = (SgAfLoteBienes) object;
        if ((this.lbiId == null && other.lbiId != null) || (this.lbiId != null && !this.lbiId.equals(other.lbiId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfLoteBienes[ lbiId=" + lbiId + " ]";
    }
    
}
