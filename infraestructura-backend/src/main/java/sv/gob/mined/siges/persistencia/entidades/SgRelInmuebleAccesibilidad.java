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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfAccesibilidad;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_inmueble_accesibilidad", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "iacPk", scope = SgRelInmuebleAccesibilidad.class)
@Audited
public class SgRelInmuebleAccesibilidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iac_pk", nullable = false)
    private Long iacPk;  

    @Column(name = "iac_bueno")
    private Integer iacBueno;

    @Column(name = "iac_malo")
    private Integer iacMalo;

    @Column(name = "iac_regular")
    private Integer iacRegular;

    @Column(name = "iac_descripcion")
    private String iacDescripcion;

    @Column(name = "iac_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime iacUltModFecha;

    @Size(max = 45)
    @Column(name = "iac_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String iacUltModUsuario;

    @Column(name = "iac_version")
    @Version
    private Integer iacVersion;
    
    @JoinColumn(name = "iac_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes iacInmuebleSede;
    
    @JoinColumn(name = "iac_accesibilidad_fk", referencedColumnName = "acc_pk")
    @ManyToOne
    private SgInfAccesibilidad iacAccesibilidad;

    public SgRelInmuebleAccesibilidad() {
    }

 

    public Long getIacPk() {
        return iacPk;
    }

    public void setIacPk(Long iacPk) {
        this.iacPk = iacPk;
    }

    public Integer getIacBueno() {
        return iacBueno;
    }

    public void setIacBueno(Integer iacBueno) {
        this.iacBueno = iacBueno;
    }

    public Integer getIacMalo() {
        return iacMalo;
    }

    public void setIacMalo(Integer iacMalo) {
        this.iacMalo = iacMalo;
    }

    public Integer getIacRegular() {
        return iacRegular;
    }

    public void setIacRegular(Integer iacRegular) {
        this.iacRegular = iacRegular;
    }

    public String getIacDescripcion() {
        return iacDescripcion;
    }

    public void setIacDescripcion(String iacDescripcion) {
        this.iacDescripcion = iacDescripcion;
    }

    public SgInmueblesSedes getIacInmuebleSede() {
        return iacInmuebleSede;
    }

    public void setIacInmuebleSede(SgInmueblesSedes iacInmuebleSede) {
        this.iacInmuebleSede = iacInmuebleSede;
    }

    
    public LocalDateTime getIacUltModFecha() {
        return iacUltModFecha;
    }

    public void setIacUltModFecha(LocalDateTime iacUltModFecha) {
        this.iacUltModFecha = iacUltModFecha;
    }

    public String getIacUltModUsuario() {
        return iacUltModUsuario;
    }

    public void setIacUltModUsuario(String iacUltModUsuario) {
        this.iacUltModUsuario = iacUltModUsuario;
    }

    public Integer getIacVersion() {
        return iacVersion;
    }

    public void setIacVersion(Integer iacVersion) {
        this.iacVersion = iacVersion;
    }

    public SgInfAccesibilidad getIacAccesibilidad() {
        return iacAccesibilidad;
    }

    public void setIacAccesibilidad(SgInfAccesibilidad iacAccesibilidad) {
        this.iacAccesibilidad = iacAccesibilidad;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.iacPk);
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
        final SgRelInmuebleAccesibilidad other = (SgRelInmuebleAccesibilidad) obj;
        if (!Objects.equals(this.iacPk, other.iacPk)) {
            return false;
        }
        return true;
    }

   
    
    

}
