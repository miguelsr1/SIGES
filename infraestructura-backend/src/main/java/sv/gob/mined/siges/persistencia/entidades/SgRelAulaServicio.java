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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgServicioBasico;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_aula_servicio", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rasPk", scope = SgRelAulaServicio.class)
@Audited
public class SgRelAulaServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ras_pk", nullable = false)
    private Long rasPk;

    @Column(name = "ras_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rasUltModFecha;

    @Size(max = 45)
    @Column(name = "ras_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rasUltModUsuario;

    @Column(name = "ras_version")
    @Version
    private Integer rasVersion;
    
    @JoinColumn(name = "ras_aula_fk", referencedColumnName = "aul_pk")
    @ManyToOne
    private SgAula rasAula;
    
    @JoinColumn(name = "ras_servicio_fk", referencedColumnName = "sba_pk")
    @ManyToOne
    private SgServicioBasico rasServicio;

    public SgRelAulaServicio() {
    }

    public Long getRasPk() {
        return rasPk;
    }

    public void setRasPk(Long rasPk) {
        this.rasPk = rasPk;
    }   

    public LocalDateTime getRasUltModFecha() {
        return rasUltModFecha;
    }

    public void setRasUltModFecha(LocalDateTime rasUltModFecha) {
        this.rasUltModFecha = rasUltModFecha;
    }

    public String getRasUltModUsuario() {
        return rasUltModUsuario;
    }

    public void setRasUltModUsuario(String rasUltModUsuario) {
        this.rasUltModUsuario = rasUltModUsuario;
    }

    public Integer getRasVersion() {
        return rasVersion;
    }

    public void setRasVersion(Integer rasVersion) {
        this.rasVersion = rasVersion;
    }

    public SgAula getRasAula() {
        return rasAula;
    }

    public void setRasAula(SgAula rasAula) {
        this.rasAula = rasAula;
    }

    public SgServicioBasico getRasServicio() {
        return rasServicio;
    }

    public void setRasServicio(SgServicioBasico rasServicio) {
        this.rasServicio = rasServicio;
    }  
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rasPk);
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
        final SgRelAulaServicio other = (SgRelAulaServicio) obj;
        if (!Objects.equals(this.rasPk, other.rasPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelAulaServicio{" + "rasPk=" + rasPk + ", rasUltModFecha=" + rasUltModFecha + ", rasUltModUsuario=" + rasUltModUsuario + ", rasVersion=" + rasVersion + '}';
    }
    
    

}
