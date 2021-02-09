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
@Table(name = "sg_rel_inmueble_tipo_drenaje", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "itdPk", scope = SgRelInmuebleTipoDrenaje.class)
@Audited
public class SgRelInmuebleTipoDrenaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "itd_pk", nullable = false)
    private Long itdPk;  

    @Column(name = "itd_bueno")
    private Integer itdBueno;

    @Column(name = "itd_malo")
    private Integer itdMalo;

    @Column(name = "itd_regular")
    private Integer itdRegular;

    @Column(name = "itd_descripcion")
    private String itdDescripcion;

    @Column(name = "itd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime itdUltModFecha;

    @Size(max = 45)
    @Column(name = "itd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String itdUltModUsuario;

    @Column(name = "itd_version")
    @Version
    private Integer itdVersion;

    @JoinColumn(name = "itd_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes itdInmuebleSede;
    
    @JoinColumn(name = "itd_tipo_drenaje", referencedColumnName = "tra_pk")
    @ManyToOne
    private SgInfTratamientoAgua itdTipoDrenaje;

    public SgRelInmuebleTipoDrenaje() {
    }

 

    public Long getItdPk() {
        return itdPk;
    }

    public void setItdPk(Long itdPk) {
        this.itdPk = itdPk;
    }

    public Integer getItdBueno() {
        return itdBueno;
    }

    public void setItdBueno(Integer itdBueno) {
        this.itdBueno = itdBueno;
    }

    public Integer getItdMalo() {
        return itdMalo;
    }

    public void setItdMalo(Integer itdMalo) {
        this.itdMalo = itdMalo;
    }

    public Integer getItdRegular() {
        return itdRegular;
    }

    public void setItdRegular(Integer itdRegular) {
        this.itdRegular = itdRegular;
    }

    public String getItdDescripcion() {
        return itdDescripcion;
    }

    public void setItdDescripcion(String itdDescripcion) {
        this.itdDescripcion = itdDescripcion;
    }

    public LocalDateTime getItdUltModFecha() {
        return itdUltModFecha;
    }

    public void setItdUltModFecha(LocalDateTime itdUltModFecha) {
        this.itdUltModFecha = itdUltModFecha;
    }

    public String getItdUltModUsuario() {
        return itdUltModUsuario;
    }

    public void setItdUltModUsuario(String itdUltModUsuario) {
        this.itdUltModUsuario = itdUltModUsuario;
    }

    public Integer getItdVersion() {
        return itdVersion;
    }

    public void setItdVersion(Integer itdVersion) {
        this.itdVersion = itdVersion;
    }

    public SgInmueblesSedes getItdInmuebleSede() {
        return itdInmuebleSede;
    }

    public void setItdInmuebleSede(SgInmueblesSedes itdInmuebleSede) {
        this.itdInmuebleSede = itdInmuebleSede;
    }

    public SgInfTratamientoAgua getItdTipoDrenaje() {
        return itdTipoDrenaje;
    }

    public void setItdTipoDrenaje(SgInfTratamientoAgua itdTipoDrenaje) {
        this.itdTipoDrenaje = itdTipoDrenaje;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.itdPk);
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
        final SgRelInmuebleTipoDrenaje other = (SgRelInmuebleTipoDrenaje) obj;
        if (!Objects.equals(this.itdPk, other.itdPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleTipoDrenaje{" + "itdPk=" + itdPk + ", itdBueno=" + itdBueno + ", itdMalo=" + itdMalo + '}';
    }

   

}
