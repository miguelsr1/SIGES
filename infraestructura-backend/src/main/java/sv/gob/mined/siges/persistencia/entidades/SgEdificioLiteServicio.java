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
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_edificios", schema=Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ediPk", scope = SgEdificioLiteServicio.class)
@Audited
public class SgEdificioLiteServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "edi_pk", nullable = false)
    private Long ediPk;

    @Column(name = "edi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ediUltModFecha;

    @Size(max = 45)
    @Column(name = "edi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ediUltModUsuario;

    @Column(name = "edi_version")
    @Version
    private Integer ediVersion;

    @OneToMany(mappedBy = "resEdificio", cascade = CascadeType.ALL, orphanRemoval = true)  
    private List<SgRelEdificioServicio> ediRelEdificioServicio;
    
    @OneToMany(mappedBy = "retEdificio", cascade = CascadeType.ALL, orphanRemoval = true)  
    private List<SgRelEdificioServicioSanitario> ediRelEdificioServicioSanitario;
    

    public SgEdificioLiteServicio() {
    }


    public Long getEdiPk() {
        return ediPk;
    }

    public void setEdiPk(Long ediPk) {
        this.ediPk = ediPk;
    }

 
    public LocalDateTime getEdiUltModFecha() {
        return ediUltModFecha;
    }

    public void setEdiUltModFecha(LocalDateTime ediUltModFecha) {
        this.ediUltModFecha = ediUltModFecha;
    }

    public String getEdiUltModUsuario() {
        return ediUltModUsuario;
    }

    public void setEdiUltModUsuario(String ediUltModUsuario) {
        this.ediUltModUsuario = ediUltModUsuario;
    }

    public Integer getEdiVersion() {
        return ediVersion;
    }

    public void setEdiVersion(Integer ediVersion) {
        this.ediVersion = ediVersion;
    }


    public List<SgRelEdificioServicio> getEdiRelEdificioServicio() {
        return ediRelEdificioServicio;
    }

    public void setEdiRelEdificioServicio(List<SgRelEdificioServicio> ediRelEdificioServicio) {
        this.ediRelEdificioServicio = ediRelEdificioServicio;
    }

    public List<SgRelEdificioServicioSanitario> getEdiRelEdificioServicioSanitario() {
        return ediRelEdificioServicioSanitario;
    }

    public void setEdiRelEdificioServicioSanitario(List<SgRelEdificioServicioSanitario> ediRelEdificioServicioSanitario) {
        this.ediRelEdificioServicioSanitario = ediRelEdificioServicioSanitario;
    }
    
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ediPk);
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
        final SgEdificioLiteServicio other = (SgEdificioLiteServicio) obj;
        if (!Objects.equals(this.ediPk, other.ediPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEdificio{" + "ediPk=" + ediPk + ediUltModFecha + ", ediUltModUsuario=" + ediUltModUsuario + ", ediVersion=" + ediVersion + '}';
    }
    
    

}
