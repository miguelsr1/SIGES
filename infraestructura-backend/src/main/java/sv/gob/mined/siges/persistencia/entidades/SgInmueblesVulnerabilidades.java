/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.entidades.catalogo.SgVulnerabilidades;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "sg_inmuebles_vulnerabilidades",  schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "ivuPk", scope = SgInmueblesVulnerabilidades.class)
@Audited
public class SgInmueblesVulnerabilidades implements Serializable {
 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ivu_pk")
    private Long ivuPk;
    
    @JoinColumn(name = "ivu_inmuebles_sedes_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes ivuInmueblesSedesFK;

    @JoinColumn(name = "ivu_vulnerabilidad_fk", referencedColumnName = "vul_pk")
    @ManyToOne
    private SgVulnerabilidades ivuVulnerabilidadesFk;
    
    @Size(max = 45)
    @Column(name = "ivu_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String ivuUltModUsuario;

    @Column(name = "ivu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ivuUltModFecha;
    
    @Column(name = "ivu_version")
    @Version
    private Integer ivuVersion;
    
    public Long getIvuPk() {
        return ivuPk;
    }

    public void setIvuPk(Long ivuPk) {
        this.ivuPk = ivuPk;
    }

    public SgInmueblesSedes getIvuInmueblesSedesFK() {
        return ivuInmueblesSedesFK;
    }

    public void setIvuInmueblesSedesFK(SgInmueblesSedes ivuInmueblesSedesFK) {
        this.ivuInmueblesSedesFK = ivuInmueblesSedesFK;
    }

    public SgVulnerabilidades getIvuVulnerabilidadesFk() {
        return ivuVulnerabilidadesFk;
    }

    public void setIvuVulnerabilidadesFk(SgVulnerabilidades ivuVulnerabilidadesFk) {
        this.ivuVulnerabilidadesFk = ivuVulnerabilidadesFk;
    }

    public String getIvuUltModUsuario() {
        return ivuUltModUsuario;
    }

    public void setIvuUltModUsuario(String ivuUltModUsuario) {
        this.ivuUltModUsuario = ivuUltModUsuario;
    }

    public LocalDateTime getIvuUltModFecha() {
        return ivuUltModFecha;
    }

    public void setIvuUltModFecha(LocalDateTime ivuUltModFecha) {
        this.ivuUltModFecha = ivuUltModFecha;
    }

    public Integer getIvuVersion() {
        return ivuVersion;
    }

    public void setIvuVersion(Integer ivuVersion) {
        this.ivuVersion = ivuVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.ivuPk);
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
        final SgInmueblesVulnerabilidades other = (SgInmueblesVulnerabilidades) obj;
        if (!Objects.equals(this.ivuPk, other.ivuPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInmueblesVulnerabilidades[ ivuPk=" + ivuPk + " ]";
    }

  
    
}

