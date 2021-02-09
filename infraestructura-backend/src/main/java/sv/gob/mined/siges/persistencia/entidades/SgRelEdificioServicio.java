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
@Table(name = "sg_rel_edificio_servicio", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "resPk", scope = SgRelEdificioServicio.class)
@Audited
public class SgRelEdificioServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "res_pk", nullable = false)
    private Long resPk;
  

    @Column(name = "res_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime resUltModFecha;

    @Size(max = 45)
    @Column(name = "res_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String resUltModUsuario;

    @Column(name = "res_version")
    @Version
    private Integer resVersion;
    
    @JoinColumn(name = "res_edificio_fk", referencedColumnName = "edi_pk")
    @ManyToOne
    private SgEdificio resEdificio;
    
    @JoinColumn(name = "res_servicio_fk", referencedColumnName = "sba_pk")
    @ManyToOne
    private SgServicioBasico resServicio;

    public SgRelEdificioServicio() {
    }



    public Long getResPk() {
        return resPk;
    }

    public void setResPk(Long resPk) {
        this.resPk = resPk;
    }
   
    public LocalDateTime getResUltModFecha() {
        return resUltModFecha;
    }

    public void setResUltModFecha(LocalDateTime resUltModFecha) {
        this.resUltModFecha = resUltModFecha;
    }

    public String getResUltModUsuario() {
        return resUltModUsuario;
    }

    public void setResUltModUsuario(String resUltModUsuario) {
        this.resUltModUsuario = resUltModUsuario;
    }

    public Integer getResVersion() {
        return resVersion;
    }

    public void setResVersion(Integer resVersion) {
        this.resVersion = resVersion;
    }

    public SgEdificio getResEdificio() {
        return resEdificio;
    }

    public void setResEdificio(SgEdificio resEdificio) {
        this.resEdificio = resEdificio;
    }

    public SgServicioBasico getResServicio() {
        return resServicio;
    }

    public void setResServicio(SgServicioBasico resServicio) {
        this.resServicio = resServicio;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.resPk);
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
        final SgRelEdificioServicio other = (SgRelEdificioServicio) obj;
        if (!Objects.equals(this.resPk, other.resPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelEdificioServicio{" + "resPk=" + resPk + ", resUltModFecha=" + resUltModFecha + ", resUltModUsuario=" + resUltModUsuario + ", resVersion=" + resVersion + '}';
    }
    
    

}
