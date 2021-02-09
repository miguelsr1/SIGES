/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgSede;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfTipoBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosBienes;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_bienes_depreciables", uniqueConstraints = {
    @UniqueConstraint(name = "codigo_inventario_uk", columnNames = {"bde_codigo_inventario"})}, schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bdePk", scope = SgAfBienDepreciableLite.class)
public class SgAfBienDepreciableLite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bde_pk", nullable = false)
    private Long bdePk;
    
    @Size(max = 10)
    @Column(name = "bde_codigo_correlativo", length = 10)
    private String bdeCodigoCorrelativo;

    @Column(name = "bde_num_correlativo", length = 10)
    private Integer bdeNumCorrelativo;
    
    @Size(max = 20)
    @Column(name = "bde_codigo_inventario", length = 20)
    private String bdeCodigoInventario;
    
    @JoinColumn(name = "bde_tipo_bien_fk", referencedColumnName = "tbi_pk")
    @ManyToOne(optional = false)
    private SgAfTipoBienes bdeTipoBien;
    
    @JoinColumn(name = "bde_estado_fk", referencedColumnName = "ebi_pk")
    @ManyToOne(optional = false)
    private SgAfEstadosBienes bdeEstadosBienes;

    @JoinColumn(name = "bde_estado_solicitud_fk", referencedColumnName = "ebi_pk")
    @ManyToOne
    private SgAfEstadosBienes bdeEstadosSolicitud;
    
    @JoinColumn(name = "bde_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede bdeSede;

    @JoinColumn(name = "bde_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas bdeUnidadesAdministrativas;
    
    public SgAfBienDepreciableLite() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if(StringUtils.isNotBlank(bdeCodigoCorrelativo)) {
            this.bdeNumCorrelativo = Integer.parseInt(bdeCodigoCorrelativo);
        }
    }
    
    public Long getBdePk() {
        return bdePk;
    }

    public void setBdePk(Long bdePk) {
        this.bdePk = bdePk;
    }

    public String getBdeCodigoCorrelativo() {
        return bdeCodigoCorrelativo;
    }

    public void setBdeCodigoCorrelativo(String bdeCodigoCorrelativo) {
        this.bdeCodigoCorrelativo = bdeCodigoCorrelativo;
    }

    public String getBdeCodigoInventario() {
        return bdeCodigoInventario;
    }

    public void setBdeCodigoInventario(String bdeCodigoInventario) {
        this.bdeCodigoInventario = bdeCodigoInventario;
    }

    public Integer getBdeNumCorrelativo() {
        return bdeNumCorrelativo;
    }

    public void setBdeNumCorrelativo(Integer bdeNumCorrelativo) {
        this.bdeNumCorrelativo = bdeNumCorrelativo;
    }

    public SgAfTipoBienes getBdeTipoBien() {
        return bdeTipoBien;
    }

    public void setBdeTipoBien(SgAfTipoBienes bdeTipoBien) {
        this.bdeTipoBien = bdeTipoBien;
    }

    public SgAfEstadosBienes getBdeEstadosBienes() {
        return bdeEstadosBienes;
    }

    public void setBdeEstadosBienes(SgAfEstadosBienes bdeEstadosBienes) {
        this.bdeEstadosBienes = bdeEstadosBienes;
    }

    public SgAfEstadosBienes getBdeEstadosSolicitud() {
        return bdeEstadosSolicitud;
    }

    public void setBdeEstadosSolicitud(SgAfEstadosBienes bdeEstadosSolicitud) {
        this.bdeEstadosSolicitud = bdeEstadosSolicitud;
    }

    public SgSede getBdeSede() {
        return bdeSede;
    }

    public void setBdeSede(SgSede bdeSede) {
        this.bdeSede = bdeSede;
    }

    public SgAfUnidadesAdministrativas getBdeUnidadesAdministrativas() {
        return bdeUnidadesAdministrativas;
    }

    public void setBdeUnidadesAdministrativas(SgAfUnidadesAdministrativas bdeUnidadesAdministrativas) {
        this.bdeUnidadesAdministrativas = bdeUnidadesAdministrativas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bdePk != null ? bdePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfBienDepreciableLite)) {
            return false;
        }
        SgAfBienDepreciableLite other = (SgAfBienDepreciableLite) object;
        if ((this.bdePk == null && other.bdePk != null) || (this.bdePk != null && !this.bdePk.equals(other.bdePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciableLite{" + "bdePk=" + bdePk + '}';
    }
}
