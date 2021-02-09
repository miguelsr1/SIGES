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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEspacioComun;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_aula_espacio", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "raePk", scope = SgRelAulaEspacio.class)
@Audited
public class SgRelAulaEspacio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rae_pk", nullable = false)
    private Long raePk;
    
    @Column(name = "rae_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime raeUltModFecha;

    @Size(max = 45)
    @Column(name = "rae_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String raeUltModUsuario;

    @Column(name = "rae_version")
    @Version
    private Integer raeVersion;
    
    @JoinColumn(name = "rae_aula_fk", referencedColumnName = "aul_pk")
    @ManyToOne
    private SgAula raeAula;
    
    @JoinColumn(name = "rae_espacio_comun_fk", referencedColumnName = "eco_pk")
    @ManyToOne
    private SgEspacioComun raeEspacioComun;
    
    @Column(name = "rae_bueno")
    private Integer raeBueno;
    
    @Column(name = "rae_regular")
    private Integer raeRegular;
    
    @Column(name = "rae_malo")
    private Integer raeMalo;
    
    @Column(name = "rae_descripcion")
    private String raeDescripcion;

    public SgRelAulaEspacio() {
    }

 

    public Long getRaePk() {
        return raePk;
    }

    public void setRaePk(Long raePk) {
        this.raePk = raePk;
    }

    public LocalDateTime getRaeUltModFecha() {
        return raeUltModFecha;
    }

    public void setRaeUltModFecha(LocalDateTime raeUltModFecha) {
        this.raeUltModFecha = raeUltModFecha;
    }

    public String getRaeUltModUsuario() {
        return raeUltModUsuario;
    }

    public void setRaeUltModUsuario(String raeUltModUsuario) {
        this.raeUltModUsuario = raeUltModUsuario;
    }

    public Integer getRaeVersion() {
        return raeVersion;
    }

    public void setRaeVersion(Integer raeVersion) {
        this.raeVersion = raeVersion;
    }

    public SgEspacioComun getRaeEspacioComun() {
        return raeEspacioComun;
    }

    public void setRaeEspacioComun(SgEspacioComun raeEspacioComun) {
        this.raeEspacioComun = raeEspacioComun;
    }

    public SgAula getRaeAula() {
        return raeAula;
    }

    public void setRaeAula(SgAula raeAula) {
        this.raeAula = raeAula;
    }

    public Integer getRaeBueno() {
        return raeBueno;
    }

    public void setRaeBueno(Integer raeBueno) {
        this.raeBueno = raeBueno;
    }

    public Integer getRaeRegular() {
        return raeRegular;
    }

    public void setRaeRegular(Integer raeRegular) {
        this.raeRegular = raeRegular;
    }

    public Integer getRaeMalo() {
        return raeMalo;
    }

    public void setRaeMalo(Integer raeMalo) {
        this.raeMalo = raeMalo;
    }

    public String getRaeDescripcion() {
        return raeDescripcion;
    }

    public void setRaeDescripcion(String raeDescripcion) {
        this.raeDescripcion = raeDescripcion;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.raePk);
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
        final SgRelAulaEspacio other = (SgRelAulaEspacio) obj;
        if (!Objects.equals(this.raePk, other.raePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelAulaEspacio{" + "raePk=" + raePk + ", raeUltModFecha=" + raeUltModFecha + ", raeUltModUsuario=" + raeUltModUsuario + ", raeVersion=" + raeVersion + '}';
    }
    
    

}
