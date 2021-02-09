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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfTratamientoAgua;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_inmueble_almacenamiento_agua", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ialPk", scope = SgRelInmuebleAlmacenamientoAgua.class)
@Audited
public class SgRelInmuebleAlmacenamientoAgua implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ial_pk", nullable = false)
    private Long ialPk;  

    @Column(name = "ial_bueno")
    private Integer ialBueno;

    @Column(name = "ial_malo")
    private Integer ialMalo;

    @Column(name = "ial_regular")
    private Integer ialRegular;

    @Column(name = "ial_descripcion")
    private String ialDescripcion;

    @Column(name = "ial_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ialUltModFecha;

    @Size(max = 45)
    @Column(name = "ial_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ialUltModUsuario;

    @Column(name = "ial_version")
    @Version
    private Integer ialVersion;

    @JoinColumn(name = "ial_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes ialInmuebleSede;
    
    @JoinColumn(name = "ial_almacenamiento_agua", referencedColumnName = "tra_pk")
    @ManyToOne
    private SgInfTratamientoAgua ialAlmacenamientoAgua;

    public SgRelInmuebleAlmacenamientoAgua() {
    }

 

    public Long getIalPk() {
        return ialPk;
    }

    public void setIalPk(Long ialPk) {
        this.ialPk = ialPk;
    }

    public Integer getIalBueno() {
        return ialBueno;
    }

    public void setIalBueno(Integer ialBueno) {
        this.ialBueno = ialBueno;
    }

    public Integer getIalMalo() {
        return ialMalo;
    }

    public void setIalMalo(Integer ialMalo) {
        this.ialMalo = ialMalo;
    }

    public Integer getIalRegular() {
        return ialRegular;
    }

    public void setIalRegular(Integer ialRegular) {
        this.ialRegular = ialRegular;
    }

    public String getIalDescripcion() {
        return ialDescripcion;
    }

    public void setIalDescripcion(String ialDescripcion) {
        this.ialDescripcion = ialDescripcion;
    }

    public LocalDateTime getIalUltModFecha() {
        return ialUltModFecha;
    }

    public void setIalUltModFecha(LocalDateTime ialUltModFecha) {
        this.ialUltModFecha = ialUltModFecha;
    }

    public String getIalUltModUsuario() {
        return ialUltModUsuario;
    }

    public void setIalUltModUsuario(String ialUltModUsuario) {
        this.ialUltModUsuario = ialUltModUsuario;
    }

    public Integer getIalVersion() {
        return ialVersion;
    }

    public void setIalVersion(Integer ialVersion) {
        this.ialVersion = ialVersion;
    }

    public SgInmueblesSedes getIalInmuebleSede() {
        return ialInmuebleSede;
    }

    public void setIalInmuebleSede(SgInmueblesSedes ialInmuebleSede) {
        this.ialInmuebleSede = ialInmuebleSede;
    }

    public SgInfTratamientoAgua getIalAlmacenamientoAgua() {
        return ialAlmacenamientoAgua;
    }

    public void setIalAlmacenamientoAgua(SgInfTratamientoAgua ialAlmacenamientoAgua) {
        this.ialAlmacenamientoAgua = ialAlmacenamientoAgua;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ialPk);
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
        final SgRelInmuebleAlmacenamientoAgua other = (SgRelInmuebleAlmacenamientoAgua) obj;
        if (!Objects.equals(this.ialPk, other.ialPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleAlmacenamientoAgua{" + "ialPk=" + ialPk + ", ialBueno=" + ialBueno + ", ialMalo=" + ialMalo + ", ialRegular=" + ialRegular + ", ialVersion=" + ialVersion + '}';
    }

    
    
    

}
