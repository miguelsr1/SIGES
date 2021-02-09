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
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfTipoImagen;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_inmueble_archivo", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "riaPk", scope = SgRelInmuebleArchivo.class)
@Audited
public class SgRelInmuebleArchivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ria_pk", nullable = false)
    private Long riaPk;

    @Column(name = "ria_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime riaUltModFecha;

    @Size(max = 45)
    @Column(name = "ria_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String riaUltModUsuario;

    @Column(name = "ria_version")
    @Version
    private Integer riaVersion;
    
    @JoinColumn(name = "ria_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes riaInmuebleSede;
    
    @JoinColumn(name = "ria_archivo_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    private SgArchivo riaArchivo;
    
    @Column(name = "ria_publicable")
    private Boolean riaPublicable;
    
    @JoinColumn(name = "ria_tipo_imagen_fk", referencedColumnName = "tii_pk")
    @ManyToOne
    private SgInfTipoImagen riaTipoImagen;

    public SgRelInmuebleArchivo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    
    }

    public Long getRiaPk() {
        return riaPk;
    }

    public void setRiaPk(Long riaPk) {
        this.riaPk = riaPk;
    }

    public SgInmueblesSedes getRiaInmuebleSede() {
        return riaInmuebleSede;
    }

    public void setRiaInmuebleSede(SgInmueblesSedes riaInmuebleSede) {
        this.riaInmuebleSede = riaInmuebleSede;
    }

    public SgArchivo getRiaArchivo() {
        return riaArchivo;
    }

    public void setRiaArchivo(SgArchivo riaArchivo) {
        this.riaArchivo = riaArchivo;
    }  

    public LocalDateTime getRiaUltModFecha() {
        return riaUltModFecha;
    }

    public void setRiaUltModFecha(LocalDateTime riaUltModFecha) {
        this.riaUltModFecha = riaUltModFecha;
    }

    public String getRiaUltModUsuario() {
        return riaUltModUsuario;
    }

    public void setRiaUltModUsuario(String riaUltModUsuario) {
        this.riaUltModUsuario = riaUltModUsuario;
    }

    public Integer getRiaVersion() {
        return riaVersion;
    }

    public void setRiaVersion(Integer riaVersion) {
        this.riaVersion = riaVersion;
    }

    public Boolean getRiaPublicable() {
        return riaPublicable;
    }

    public void setRiaPublicable(Boolean riaPublicable) {
        this.riaPublicable = riaPublicable;
    }

    public SgInfTipoImagen getRiaTipoImagen() {
        return riaTipoImagen;
    }

    public void setRiaTipoImagen(SgInfTipoImagen riaTipoImagen) {
        this.riaTipoImagen = riaTipoImagen;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.riaPk);
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
        final SgRelInmuebleArchivo other = (SgRelInmuebleArchivo) obj;
        if (!Objects.equals(this.riaPk, other.riaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleArchivo{" + "riaPk=" + riaPk +  ", riaUltModFecha=" + riaUltModFecha + ", riaUltModUsuario=" + riaUltModUsuario + ", riaVersion=" + riaVersion + '}';
    }
    
    

}
