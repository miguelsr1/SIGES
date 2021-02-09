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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfItemObraExterior;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_inmueble_item_obra_exterior", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rixPk", scope = SgRelInmuebleItemObraExterior.class)
@Audited
public class SgRelInmuebleItemObraExterior implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rix_pk", nullable = false)
    private Long rixPk;  

    @Column(name = "rix_bueno")
    private Integer rixBueno;

    @Column(name = "rix_malo")
    private Integer rixMalo;

    @Column(name = "rix_regular")
    private Integer rixRegular;

    @Column(name = "rix_descripcion")
    private String rixDescripcion;

    @Column(name = "rix_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rixUltModFecha;

    @Size(max = 45)
    @Column(name = "rix_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rixUltModUsuario;

    @Column(name = "rix_version")
    @Version
    private Integer rixVersion;

    @JoinColumn(name = "rix_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes rixInmuebleSede;
    
    @JoinColumn(name = "rix_item_obra_exterior_fk", referencedColumnName = "ioe_pk")
    @ManyToOne
    private SgInfItemObraExterior rixItemObraExterior;

    public SgRelInmuebleItemObraExterior() {
    }

 

    public Long getRixPk() {
        return rixPk;
    }

    public void setRixPk(Long rixPk) {
        this.rixPk = rixPk;
    }

    public Integer getRixBueno() {
        return rixBueno;
    }

    public void setRixBueno(Integer rixBueno) {
        this.rixBueno = rixBueno;
    }

    public Integer getRixMalo() {
        return rixMalo;
    }

    public void setRixMalo(Integer rixMalo) {
        this.rixMalo = rixMalo;
    }

    public Integer getRixRegular() {
        return rixRegular;
    }

    public void setRixRegular(Integer rixRegular) {
        this.rixRegular = rixRegular;
    }

    public String getRixDescripcion() {
        return rixDescripcion;
    }

    public void setRixDescripcion(String rixDescripcion) {
        this.rixDescripcion = rixDescripcion;
    }

    public LocalDateTime getRixUltModFecha() {
        return rixUltModFecha;
    }

    public void setRixUltModFecha(LocalDateTime rixUltModFecha) {
        this.rixUltModFecha = rixUltModFecha;
    }

    public String getRixUltModUsuario() {
        return rixUltModUsuario;
    }

    public void setRixUltModUsuario(String rixUltModUsuario) {
        this.rixUltModUsuario = rixUltModUsuario;
    }

    public Integer getRixVersion() {
        return rixVersion;
    }

    public void setRixVersion(Integer rixVersion) {
        this.rixVersion = rixVersion;
    }

    public SgInmueblesSedes getRixInmuebleSede() {
        return rixInmuebleSede;
    }

    public void setRixInmuebleSede(SgInmueblesSedes rixInmuebleSede) {
        this.rixInmuebleSede = rixInmuebleSede;
    }

    public SgInfItemObraExterior getRixItemObraExterior() {
        return rixItemObraExterior;
    }

    public void setRixItemObraExterior(SgInfItemObraExterior rixItemObraExterior) {
        this.rixItemObraExterior = rixItemObraExterior;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rixPk);
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
        final SgRelInmuebleItemObraExterior other = (SgRelInmuebleItemObraExterior) obj;
        if (!Objects.equals(this.rixPk, other.rixPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleItemObraExterior{" + "rixPk=" + rixPk + ", rixBueno=" + rixBueno + ", rixMalo=" + rixMalo + ", rixRegular=" + rixRegular + ", rixDescripcion=" + rixDescripcion + ", rixVersion=" + rixVersion + ", rixInmuebleSede=" + rixInmuebleSede + '}';
    }


    

}
