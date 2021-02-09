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
@Table(name = "sg_af_comision_descargo", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cdePk", scope = SgAfComisionDescargo.class)
public class SgAfComisionDescargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cde_pk")
    private Long cdePk;
    
    @Size(max = 255)
    @Column(name = "cde_nombre_representante", length = 255)
    private String cdeNombreRepresentante;
    
    @Size(max = 255)
    @Column(name = "cde_cargo_representante", length = 255)
    private String cdeCargoRepresentante;
    
    @Column(name = "cde_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cdeUltModFecha;
    
    @Column(name = "cde_habilitado")
    private Boolean cdeHabilitado;
    
    @Size(max = 45)
    @Column(name = "cde_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cdeUltModUsuario;
    
    @Column(name = "cde_version")
    @Version
    private Integer cdeVersion;
    
    @JoinColumn(name = "cde_activo_fijo_fk", referencedColumnName = "uaf_pk")
    @ManyToOne(optional = false)
    private SgAfUnidadesActivoFijo cdeActivoFijoFk;

    public SgAfComisionDescargo() {
    }

    public Long getCdePk() {
        return cdePk;
    }

    public void setCdePk(Long cdePk) {
        this.cdePk = cdePk;
    }

    public String getCdeNombreRepresentante() {
        return cdeNombreRepresentante;
    }

    public void setCdeNombreRepresentante(String cdeNombreRepresentante) {
        this.cdeNombreRepresentante = cdeNombreRepresentante;
    }

    public String getCdeCargoRepresentante() {
        return cdeCargoRepresentante;
    }

    public void setCdeCargoRepresentante(String cdeCargoRepresentante) {
        this.cdeCargoRepresentante = cdeCargoRepresentante;
    }

    public LocalDateTime getCdeUltModFecha() {
        return cdeUltModFecha;
    }

    public void setCdeUltModFecha(LocalDateTime cdeUltModFecha) {
        this.cdeUltModFecha = cdeUltModFecha;
    }

    public String getCdeUltModUsuario() {
        return cdeUltModUsuario;
    }

    public void setCdeUltModUsuario(String cdeUltModUsuario) {
        this.cdeUltModUsuario = cdeUltModUsuario;
    }

    public Integer getCdeVersion() {
        return cdeVersion;
    }

    public void setCdeVersion(Integer cdeVersion) {
        this.cdeVersion = cdeVersion;
    }

    public SgAfUnidadesActivoFijo getCdeActivoFijoFk() {
        return cdeActivoFijoFk;
    }

    public void setCdeActivoFijoFk(SgAfUnidadesActivoFijo cdeActivoFijoFk) {
        this.cdeActivoFijoFk = cdeActivoFijoFk;
    }

    public Boolean getCdeHabilitado() {
        return cdeHabilitado;
    }

    public void setCdeHabilitado(Boolean cdeHabilitado) {
        this.cdeHabilitado = cdeHabilitado;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdePk != null ? cdePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfComisionDescargo)) {
            return false;
        }
        SgAfComisionDescargo other = (SgAfComisionDescargo) object;
        if ((this.cdePk == null && other.cdePk != null) || (this.cdePk != null && !this.cdePk.equals(other.cdePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfComisionDescargo[ cdePk=" + cdePk + " ]";
    }
    
}
