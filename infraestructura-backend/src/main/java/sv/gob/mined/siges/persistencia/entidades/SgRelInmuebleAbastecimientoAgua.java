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
@Table(name = "sg_rel_inmueble_abastecimiento_agua", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "iaaPk", scope = SgRelInmuebleAbastecimientoAgua.class)
@Audited
public class SgRelInmuebleAbastecimientoAgua implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iaa_pk", nullable = false)
    private Long iaaPk;  

    @Column(name = "iaa_bueno")
    private Integer iaaBueno;

    @Column(name = "iaa_malo")
    private Integer iaaMalo;

    @Column(name = "iaa_regular")
    private Integer iaaRegular;

    @Column(name = "iaa_descripcion")
    private String iaaDescripcion;

    @Column(name = "iaa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime iaaUltModFecha;

    @Size(max = 45)
    @Column(name = "iaa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String iaaUltModUsuario;

    @Column(name = "iaa_version")
    @Version
    private Integer iaaVersion;

    @JoinColumn(name = "iaa_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes iaaInmuebleSede;
    
    @JoinColumn(name = "iaa_abastecimiento_agua", referencedColumnName = "tra_pk")
    @ManyToOne
    private SgInfTratamientoAgua iaaAbastecimientoAgua;

    public SgRelInmuebleAbastecimientoAgua() {
    }

 

    public Long getIaaPk() {
        return iaaPk;
    }

    public void setIaaPk(Long iaaPk) {
        this.iaaPk = iaaPk;
    }

    public Integer getIaaBueno() {
        return iaaBueno;
    }

    public void setIaaBueno(Integer iaaBueno) {
        this.iaaBueno = iaaBueno;
    }

    public Integer getIaaMalo() {
        return iaaMalo;
    }

    public void setIaaMalo(Integer iaaMalo) {
        this.iaaMalo = iaaMalo;
    }

    public Integer getIaaRegular() {
        return iaaRegular;
    }

    public void setIaaRegular(Integer iaaRegular) {
        this.iaaRegular = iaaRegular;
    }

    public String getIaaDescripcion() {
        return iaaDescripcion;
    }

    public void setIaaDescripcion(String iaaDescripcion) {
        this.iaaDescripcion = iaaDescripcion;
    }

    public LocalDateTime getIaaUltModFecha() {
        return iaaUltModFecha;
    }

    public void setIaaUltModFecha(LocalDateTime iaaUltModFecha) {
        this.iaaUltModFecha = iaaUltModFecha;
    }

    public String getIaaUltModUsuario() {
        return iaaUltModUsuario;
    }

    public void setIaaUltModUsuario(String iaaUltModUsuario) {
        this.iaaUltModUsuario = iaaUltModUsuario;
    }

    public Integer getIaaVersion() {
        return iaaVersion;
    }

    public void setIaaVersion(Integer iaaVersion) {
        this.iaaVersion = iaaVersion;
    }

    public SgInmueblesSedes getIaaInmuebleSede() {
        return iaaInmuebleSede;
    }

    public void setIaaInmuebleSede(SgInmueblesSedes iaaInmuebleSede) {
        this.iaaInmuebleSede = iaaInmuebleSede;
    }

    public SgInfTratamientoAgua getIaaAbastecimientoAgua() {
        return iaaAbastecimientoAgua;
    }

    public void setIaaAbastecimientoAgua(SgInfTratamientoAgua iaaAbastecimientoAgua) {
        this.iaaAbastecimientoAgua = iaaAbastecimientoAgua;
    }
    
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.iaaPk);
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
        final SgRelInmuebleAbastecimientoAgua other = (SgRelInmuebleAbastecimientoAgua) obj;
        if (!Objects.equals(this.iaaPk, other.iaaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleAbastecimientoAgua{" + "iaaPk=" + iaaPk + ", iaaBueno=" + iaaBueno + ", iaaMalo=" + iaaMalo + ", iaaRegular=" + iaaRegular + ", iaaVersion=" + iaaVersion + '}';
    }

    
    
    

}
