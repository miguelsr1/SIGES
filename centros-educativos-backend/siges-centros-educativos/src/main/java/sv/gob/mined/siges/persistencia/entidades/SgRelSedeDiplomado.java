/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_sede_diplomado",schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rsdPk", scope = SgRelSedeDiplomado.class)
@Audited
public class SgRelSedeDiplomado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rsd_pk", nullable = false)
    private Long rsdPk;
    
    @JoinColumn(name = "rsd_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede rsdSedeFk;

    @JoinColumn(name = "rsd_diplomado_fk", referencedColumnName = "dip_pk")
    @ManyToOne
    private SgDiplomado rsdDiplomadoFk;
    
    @Column(name = "rsd_habilitado")
    @AtributoHabilitado
    private Boolean rsdHabilitado;
    
    @Size(max = 20)
    @Column(name = "rsd_numero_tramite", length = 20)
    private String rsdNumeroTramite;

    @Column(name = "rsd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rsdUltModFecha;

    @Size(max = 45)
    @Column(name = "rsd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rsdUltModUsuario;

    @Column(name = "rsd_version")
    @Version
    private Integer rsdVersion;

    public SgRelSedeDiplomado() {
    }


    public Long getRsdPk() {
        return rsdPk;
    }

    public void setRsdPk(Long rsdPk) {
        this.rsdPk = rsdPk;
    }
  public Boolean getRsdHabilitado() {
        return rsdHabilitado;
    }

    public void setRsdHabilitado(Boolean rsdHabilitado) {
        this.rsdHabilitado = rsdHabilitado;
    }

    public LocalDateTime getRsdUltModFecha() {
        return rsdUltModFecha;
    }

    public void setRsdUltModFecha(LocalDateTime rsdUltModFecha) {
        this.rsdUltModFecha = rsdUltModFecha;
    }

    public String getRsdUltModUsuario() {
        return rsdUltModUsuario;
    }

    public void setRsdUltModUsuario(String rsdUltModUsuario) {
        this.rsdUltModUsuario = rsdUltModUsuario;
    }

    public Integer getRsdVersion() {
        return rsdVersion;
    }

    public void setRsdVersion(Integer rsdVersion) {
        this.rsdVersion = rsdVersion;
    }

    public SgSede getRsdSedeFk() {
        return rsdSedeFk;
    }

    public void setRsdSedeFk(SgSede rsdSedeFk) {
        this.rsdSedeFk = rsdSedeFk;
    }

    public SgDiplomado getRsdDiplomadoFk() {
        return rsdDiplomadoFk;
    }

    public void setRsdDiplomadoFk(SgDiplomado rsdDiplomadoFk) {
        this.rsdDiplomadoFk = rsdDiplomadoFk;
    }

    public String getRsdNumeroTramite() {
        return rsdNumeroTramite;
    }

    public void setRsdNumeroTramite(String rsdNumeroTramite) {
        this.rsdNumeroTramite = rsdNumeroTramite;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rsdPk);
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
        final SgRelSedeDiplomado other = (SgRelSedeDiplomado) obj;
        if (!Objects.equals(this.rsdPk, other.rsdPk)) {
            return false;
        }
        return true;
    }

 

}
