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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_traslados_detalle", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tdePk", scope = SgAfTrasladosDetalle.class)
public class SgAfTrasladosDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tde_pk")
    private Long tdePk;
    
    @Column(name = "tde_rechazado")
    private Boolean tdeRechazado;
    
    @Size(max = 50)
    @Column(name = "tde_observacion", length = 50)
    private String tdeObservacion;
    
    @JoinColumn(name = "tde_bienes_depreciables_fk", referencedColumnName = "bde_pk")
    @ManyToOne(optional = false)
    private SgAfBienDepreciable tdeBienesDepreciablesFk;
    
    @JoinColumn(name = "tde_traslado_fk", referencedColumnName = "tra_pk")
    @ManyToOne(optional = false)
    private SgAfTraslado tdeTrasladoFk;

    @Column(name = "tde_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tdeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tde_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tdeUltModUsuario;
    
    @Column(name = "tde_version")
    @Version
    private Integer tdeVersion;
    
    public SgAfTrasladosDetalle() {
    }

    public SgAfTrasladosDetalle(Long tdePk) {
        this.tdePk = tdePk;
    }

    public Long getTdePk() {
        return tdePk;
    }

    public void setTdePk(Long tdePk) {
        this.tdePk = tdePk;
    }

    public LocalDateTime getTdeUltModFecha() {
        return tdeUltModFecha;
    }

    public void setTdeUltModFecha(LocalDateTime tdeUltModFecha) {
        this.tdeUltModFecha = tdeUltModFecha;
    }

    public String getTdeUltModUsuario() {
        return tdeUltModUsuario;
    }

    public void setTdeUltModUsuario(String tdeUltModUsuario) {
        this.tdeUltModUsuario = tdeUltModUsuario;
    }

    public Integer getTdeVersion() {
        return tdeVersion;
    }

    public void setTdeVersion(Integer tdeVersion) {
        this.tdeVersion = tdeVersion;
    }

    public SgAfBienDepreciable getTdeBienesDepreciablesFk() {
        return tdeBienesDepreciablesFk;
    }

    public void setTdeBienesDepreciablesFk(SgAfBienDepreciable tdeBienesDepreciablesFk) {
        this.tdeBienesDepreciablesFk = tdeBienesDepreciablesFk;
    }

    public SgAfTraslado getTdeTrasladoFk() {
        return tdeTrasladoFk;
    }

    public void setTdeTrasladoFk(SgAfTraslado tdeTrasladoFk) {
        this.tdeTrasladoFk = tdeTrasladoFk;
    }

    public Boolean getTdeRechazado() {
        return tdeRechazado;
    }

    public void setTdeRechazado(Boolean tdeRechazado) {
        this.tdeRechazado = tdeRechazado;
    }

    public String getTdeObservacion() {
        return tdeObservacion;
    }

    public void setTdeObservacion(String tdeObservacion) {
        this.tdeObservacion = tdeObservacion;
    } 

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tdePk != null ? tdePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfTrasladosDetalle)) {
            return false;
        }
        SgAfTrasladosDetalle other = (SgAfTrasladosDetalle) object;
        if ((this.tdePk == null && other.tdePk != null) || (this.tdePk != null && !this.tdePk.equals(other.tdePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfTrasladosDetalle[ tdePk=" + tdePk + " ]";
    }
    
}
