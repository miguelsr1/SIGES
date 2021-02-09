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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfTipoManejoDesechos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_inmueble_manejo_desechos", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "imdPk", scope = SgRelInmuebleManejoDesechos.class)
@Audited
public class SgRelInmuebleManejoDesechos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "imd_pk", nullable = false)
    private Long imdPk;  

    @Column(name = "imd_bueno")
    private Integer imdBueno;

    @Column(name = "imd_malo")
    private Integer imdMalo;

    @Column(name = "imd_regular")
    private Integer imdRegular;

    @Column(name = "imd_descripcion")
    private String imdDescripcion;

    @Column(name = "imd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime imdUltModFecha;

    @Size(max = 45)
    @Column(name = "imd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String imdUltModUsuario;

    @Column(name = "imd_version")
    @Version
    private Integer imdVersion;

    @JoinColumn(name = "imd_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes imdInmuebleSede;
    
    @JoinColumn(name = "imd_manejo_desechos", referencedColumnName = "tmd_pk")
    @ManyToOne
    private SgInfTipoManejoDesechos imdManejoDesechos;

    public SgRelInmuebleManejoDesechos() {
    }

 

    public Long getImdPk() {
        return imdPk;
    }

    public void setImdPk(Long imdPk) {
        this.imdPk = imdPk;
    }

    public Integer getImdBueno() {
        return imdBueno;
    }

    public void setImdBueno(Integer imdBueno) {
        this.imdBueno = imdBueno;
    }

    public Integer getImdMalo() {
        return imdMalo;
    }

    public void setImdMalo(Integer imdMalo) {
        this.imdMalo = imdMalo;
    }

    public Integer getImdRegular() {
        return imdRegular;
    }

    public void setImdRegular(Integer imdRegular) {
        this.imdRegular = imdRegular;
    }

    public String getImdDescripcion() {
        return imdDescripcion;
    }

    public void setImdDescripcion(String imdDescripcion) {
        this.imdDescripcion = imdDescripcion;
    }

    public LocalDateTime getImdUltModFecha() {
        return imdUltModFecha;
    }

    public void setImdUltModFecha(LocalDateTime imdUltModFecha) {
        this.imdUltModFecha = imdUltModFecha;
    }

    public String getImdUltModUsuario() {
        return imdUltModUsuario;
    }

    public void setImdUltModUsuario(String imdUltModUsuario) {
        this.imdUltModUsuario = imdUltModUsuario;
    }

    public Integer getImdVersion() {
        return imdVersion;
    }

    public void setImdVersion(Integer imdVersion) {
        this.imdVersion = imdVersion;
    }

    public SgInmueblesSedes getImdInmuebleSede() {
        return imdInmuebleSede;
    }

    public void setImdInmuebleSede(SgInmueblesSedes imdInmuebleSede) {
        this.imdInmuebleSede = imdInmuebleSede;
    }

    public SgInfTipoManejoDesechos getImdManejoDesechos() {
        return imdManejoDesechos;
    }

    public void setImdManejoDesechos(SgInfTipoManejoDesechos imdManejoDesechos) {
        this.imdManejoDesechos = imdManejoDesechos;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.imdPk);
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
        final SgRelInmuebleManejoDesechos other = (SgRelInmuebleManejoDesechos) obj;
        if (!Objects.equals(this.imdPk, other.imdPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleManejoDesechos{" + "imdPk=" + imdPk + ", imdBueno=" + imdBueno + ", imdMalo=" + imdMalo + ", imdRegular=" + imdRegular + ", imdDescripcion=" + imdDescripcion + '}';
    }

   
    

}
