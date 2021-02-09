/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgServicioBasico;

@Entity
@Table(name = "sg_rel_inmueble_servicio", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "risPk", scope = SgRelInmuebleServicioBasico.class)
@Audited
public class SgRelInmuebleServicioBasico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ris_pk")
    private Long risPk;
  
    @Size(max = 45)
    @Column(name = "ris_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String risUltModUsuario;

    
    @Column(name = "ris_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime risUltModFecha;
    
    @Column(name = "ris_version")
    @Version
    private Integer risVersion;
    
    @JoinColumn(name = "ris_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes risInmuebleSede;
    
    @JoinColumn(name = "ris_servicio_fk", referencedColumnName = "sba_pk")
    @ManyToOne
    private SgServicioBasico risServicio;
 
    
    public SgRelInmuebleServicioBasico() {
    }

    public Long getRisPk() {
        return risPk;
    }

    public void setRisPk(Long risPk) {
        this.risPk = risPk;
    }

    public String getRisUltModUsuario() {
        return risUltModUsuario;
    }

    public void setRisUltModUsuario(String risUltModUsuario) {
        this.risUltModUsuario = risUltModUsuario;
    }

    public LocalDateTime getRisUltModFecha() {
        return risUltModFecha;
    }

    public void setRisUltModFecha(LocalDateTime risUltModFecha) {
        this.risUltModFecha = risUltModFecha;
    }

    public Integer getRisVersion() {
        return risVersion;
    }

    public void setRisVersion(Integer risVersion) {
        this.risVersion = risVersion;
    }

    public SgInmueblesSedes getRisInmuebleSede() {
        return risInmuebleSede;
    }

    public void setRisInmuebleSede(SgInmueblesSedes risInmuebleSede) {
        this.risInmuebleSede = risInmuebleSede;
    }

    public SgServicioBasico getRisServicio() {
        return risServicio;
    }

    public void setRisServicio(SgServicioBasico risServicio) {
        this.risServicio = risServicio;
    }


    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.risPk);
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
        final SgRelInmuebleServicioBasico other = (SgRelInmuebleServicioBasico) obj;
        if (!Objects.equals(this.risPk, other.risPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes[ tisPk=" + risPk + " ]";
    }

 
  
    
}
