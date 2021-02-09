/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgServicioInfraestructura;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_rel_sede_servicio_infra", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rssPk", scope = SgRelSedeServicioInfra.class)
@Audited
public class SgRelSedeServicioInfra implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rss_pk")
    private Long rssPk;
    
    @JoinColumn(name = "rss_sede_fk")
    @ManyToOne
    private SgSede rssSede;
    
    @JoinColumn(name = "rss_servicio_infra_fk")
    @ManyToOne
    private SgServicioInfraestructura rssServicioInfra;
    
    @Column(name = "rss_tiene_servicio")
    private Boolean rssTieneServicio;
    
    @Column(name = "rss_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rssUltModFecha;
    
    @Size(max = 45)
    @Column(name = "rss_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String rssUltModUsuario;
    
    @Column(name = "rss_version")
    @Version
    private Integer rssVersion;
 

    public SgRelSedeServicioInfra() {
    }

    public SgRelSedeServicioInfra(Long rssPk) {
        this.rssPk = rssPk;
    }

    public Long getRssPk() {
        return rssPk;
    }

    public void setRssPk(Long rssPk) {
        this.rssPk = rssPk;
    }

    public SgSede getRssSede() {
        return rssSede;
    }

    public void setRssSede(SgSede rssSede) {
        this.rssSede = rssSede;
    }

    public SgServicioInfraestructura getRssServicioInfra() {
        return rssServicioInfra;
    }

    public void setRssServicioInfra(SgServicioInfraestructura rssServicioInfra) {
        this.rssServicioInfra = rssServicioInfra;
    }

    public Boolean getRssTieneServicio() {
        return rssTieneServicio;
    }

    public void setRssTieneServicio(Boolean rssTieneServicio) {
        this.rssTieneServicio = rssTieneServicio;
    }

    public LocalDateTime getRssUltModFecha() {
        return rssUltModFecha;
    }

    public void setRssUltModFecha(LocalDateTime rssUltModFecha) {
        this.rssUltModFecha = rssUltModFecha;
    }

    public String getRssUltModUsuario() {
        return rssUltModUsuario;
    }

    public void setRssUltModUsuario(String rssUltModUsuario) {
        this.rssUltModUsuario = rssUltModUsuario;
    }

    public Integer getRssVersion() {
        return rssVersion;
    }

    public void setRssVersion(Integer rssVersion) {
        this.rssVersion = rssVersion;
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
        final SgRelSedeServicioInfra other = (SgRelSedeServicioInfra) obj;
        if (!Objects.equals(this.rssPk, other.rssPk)) {
            return false;
        }
        return true;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rssPk != null ? rssPk.hashCode() : 0);
        return hash;
    }

   

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelSedeServicioInfra[ rssPk=" + rssPk + " ]";
    }
    
}
