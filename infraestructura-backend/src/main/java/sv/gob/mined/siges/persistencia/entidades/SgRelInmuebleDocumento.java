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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfTipoDocumento;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_inmueble_documento", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ridPk", scope = SgRelInmuebleDocumento.class)
@Audited
public class SgRelInmuebleDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rid_pk", nullable = false)
    private Long ridPk;

    @JoinColumn(name = "rid_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes ridInmuebleSede;

    @Size(max = 4000)
    @Column(name = "rid_descripcion", length = 4000)
    private String ridDescripcion;

    @Size(max = 40)
    @Column(name = "rid_nombre", length = 40)
    private String ridNombre;

    @JoinColumn(name = "rid_documento_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private SgArchivo ridDocumento;

    @JoinColumn(name = "rid_tipo_documento_fk", referencedColumnName = "tid_pk")
    @ManyToOne
    private SgInfTipoDocumento ridTipoDocumento;

    @Column(name = "rid_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ridUltModFecha;

    @Size(max = 45)
    @Column(name = "rid_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ridUltModUsuario;

    @Column(name = "rid_version")
    @Version
    private Integer ridVersion;

    public SgRelInmuebleDocumento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }

    public Long getRidPk() {
        return ridPk;
    }

    public void setRidPk(Long ridPk) {
        this.ridPk = ridPk;
    }

    public String getRidNombre() {
        return ridNombre;
    }

    public void setRidNombre(String ridNombre) {
        this.ridNombre = ridNombre;
    }

    public SgInmueblesSedes getRidInmuebleSede() {
        return ridInmuebleSede;
    }

    public void setRidInmuebleSede(SgInmueblesSedes ridInmuebleSede) {
        this.ridInmuebleSede = ridInmuebleSede;
    }

    public String getRidDescripcion() {
        return ridDescripcion;
    }

    public void setRidDescripcion(String ridDescripcion) {
        this.ridDescripcion = ridDescripcion;
    }

    public SgArchivo getRidDocumento() {
        return ridDocumento;
    }

    public void setRidDocumento(SgArchivo ridDocumento) {
        this.ridDocumento = ridDocumento;
    }

    public SgInfTipoDocumento getRidTipoDocumento() {
        return ridTipoDocumento;
    }

    public void setRidTipoDocumento(SgInfTipoDocumento ridTipoDocumento) {
        this.ridTipoDocumento = ridTipoDocumento;
    }

    public LocalDateTime getRidUltModFecha() {
        return ridUltModFecha;
    }

    public void setRidUltModFecha(LocalDateTime ridUltModFecha) {
        this.ridUltModFecha = ridUltModFecha;
    }

    public String getRidUltModUsuario() {
        return ridUltModUsuario;
    }

    public void setRidUltModUsuario(String ridUltModUsuario) {
        this.ridUltModUsuario = ridUltModUsuario;
    }

    public Integer getRidVersion() {
        return ridVersion;
    }

    public void setRidVersion(Integer ridVersion) {
        this.ridVersion = ridVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.ridPk);
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
        final SgRelInmuebleDocumento other = (SgRelInmuebleDocumento) obj;
        if (!Objects.equals(this.ridPk, other.ridPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleDocumento{" + "ridPk=" + ridPk + ", ridUltModFecha=" + ridUltModFecha + ", ridUltModUsuario=" + ridUltModUsuario + ", ridVersion=" + ridVersion + '}';
    }

}
