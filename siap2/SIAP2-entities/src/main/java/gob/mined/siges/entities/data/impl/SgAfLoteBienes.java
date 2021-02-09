/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.EnumEstadosProceso;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.Size;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_lote_bienes", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@EntityListeners({DecoratorEntityListener.class})
@Cacheable(false)
public class SgAfLoteBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_af_lote_bienes_pk_seq", sequenceName = Constantes.SCHEMA_ACTIVO_FIJO + ".sg_af_lote_bienes_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_af_lote_bienes_pk_seq")
    @Column(name = "lbi_id")
    private Long lbiId;
    
    @Size(max = 20)
    @Column(name = "lbi_codigo_inventario_padre", length =20)
    private String lbiCodigoInventarioPadre;
    
    
    @Basic(optional = false)
    @Column(name = "lbi_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadosProceso lbiEstado;
    
    @Column(name = "lbi_cantidad_bienes_replicar")
    private Integer lbiCantidadBienesReplicar;
   
     //Auditoria
    @Column(name = "lbi_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "lbi_ult_mod_fecha")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;
    
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

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
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

    public String getLbiCodigoInventarioPadre() {
        return lbiCodigoInventarioPadre;
    }

    public void setLbiCodigoInventarioPadre(String lbiCodigoInventarioPadre) {
        this.lbiCodigoInventarioPadre = lbiCodigoInventarioPadre;
    }

    public Integer getLbiCantidadBienesReplicar() {
        return lbiCantidadBienesReplicar;
    }

    public void setLbiCantidadBienesReplicar(Integer lbiCantidadBienesReplicar) {
        this.lbiCantidadBienesReplicar = lbiCantidadBienesReplicar;
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
