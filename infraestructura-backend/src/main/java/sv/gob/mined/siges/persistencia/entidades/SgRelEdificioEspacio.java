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
@Table(name = "sg_rel_edificio_espacio", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reePk", scope = SgRelEdificioEspacio.class)
@Audited
public class SgRelEdificioEspacio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ree_pk", nullable = false)
    private Long reePk;  

    @Column(name = "ree_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime reeUltModFecha;

    @Size(max = 45)
    @Column(name = "ree_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String reeUltModUsuario;

    @Column(name = "ree_version")
    @Version
    private Integer reeVersion;
    
    @JoinColumn(name = "ree_edificio_fk", referencedColumnName = "edi_pk")
    @ManyToOne
    private SgEdificio reeEdificio;
    
    @JoinColumn(name = "ree_espacio_comun_fk", referencedColumnName = "eco_pk")
    @ManyToOne
    private SgEspacioComun reeEspacioComun;
    
    @Column(name = "ree_bueno")
    private Integer reeBueno;
    
    @Column(name = "ree_regular")
    private Integer reeRegular;
    
    @Column(name = "ree_malo")
    private Integer reeMalo;
    
    @Column(name = "ree_descripcion")
    private String reeDescripcion;

    public SgRelEdificioEspacio() {
    }


    public Long getReePk() {
        return reePk;
    }

    public void setReePk(Long reePk) {
        this.reePk = reePk;
    }

    public SgEdificio getReeEdificio() {
        return reeEdificio;
    }

    public void setReeEdificio(SgEdificio reeEdificio) {
        this.reeEdificio = reeEdificio;
    }

    public SgEspacioComun getReeEspacioComun() {
        return reeEspacioComun;
    }

    public void setReeEspacioComun(SgEspacioComun reeEspacioComun) {
        this.reeEspacioComun = reeEspacioComun;
    }

    public Integer getReeBueno() {
        return reeBueno;
    }

    public void setReeBueno(Integer reeBueno) {
        this.reeBueno = reeBueno;
    }

    public Integer getReeRegular() {
        return reeRegular;
    }

    public void setReeRegular(Integer reeRegular) {
        this.reeRegular = reeRegular;
    }

    public Integer getReeMalo() {
        return reeMalo;
    }

    public void setReeMalo(Integer reeMalo) {
        this.reeMalo = reeMalo;
    }

    public String getReeDescripcion() {
        return reeDescripcion;
    }

    public void setReeDescripcion(String reeDescripcion) {
        this.reeDescripcion = reeDescripcion;
    }

    




    public LocalDateTime getReeUltModFecha() {
        return reeUltModFecha;
    }

    public void setReeUltModFecha(LocalDateTime reeUltModFecha) {
        this.reeUltModFecha = reeUltModFecha;
    }

    public String getReeUltModUsuario() {
        return reeUltModUsuario;
    }

    public void setReeUltModUsuario(String reeUltModUsuario) {
        this.reeUltModUsuario = reeUltModUsuario;
    }

    public Integer getReeVersion() {
        return reeVersion;
    }

    public void setReeVersion(Integer reeVersion) {
        this.reeVersion = reeVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.reePk);
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
        final SgRelEdificioEspacio other = (SgRelEdificioEspacio) obj;
        if (!Objects.equals(this.reePk, other.reePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelEdificioEspacio{" + "reePk=" + reePk + ", reeUltModFecha=" + reeUltModFecha + ", reeUltModUsuario=" + reeUltModUsuario + ", reeVersion=" + reeVersion + '}';
    }
    
    

}
