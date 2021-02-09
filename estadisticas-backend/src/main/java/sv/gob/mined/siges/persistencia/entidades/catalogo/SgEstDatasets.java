/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_est_datasets", uniqueConstraints = {
    @UniqueConstraint(name = "dat_codigo_uk", columnNames = {"dat_codigo"})
    ,
    @UniqueConstraint(name = "dat_nombre_uk", columnNames = {"dat_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "datPk", scope = SgEstDatasets.class)
@Audited
public class SgEstDatasets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dat_pk", nullable = false)
    private Long datPk;

    @Size(max = 45)
    @Column(name = "dat_codigo", length = 45)
    @AtributoCodigo
    private String datCodigo;

    @Size(max = 255)
    @Column(name = "dat_nombre", length = 255)
    @AtributoNormalizable
    private String datNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "dat_nombre_busqueda", length = 255)
    private String datNombreBusqueda;

    @Column(name = "dat_habilitado")
    @AtributoHabilitado
    private Boolean datHabilitado;

    @Column(name = "dat_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime datUltModFecha;

    @Size(max = 45)
    @Column(name = "dat_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String datUltModUsuario;

    @Column(name = "dat_version")
    @Version
    private Integer datVersion;

    public SgEstDatasets() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.datNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.datNombre);
    }

    public Long getDatPk() {
        return datPk;
    }

    public void setDatPk(Long datPk) {
        this.datPk = datPk;
    }

    public String getDatCodigo() {
        return datCodigo;
    }

    public void setDatCodigo(String datCodigo) {
        this.datCodigo = datCodigo;
    }

    public String getDatNombre() {
        return datNombre;
    }

    public void setDatNombre(String datNombre) {
        this.datNombre = datNombre;
    }

    public String getDatNombreBusqueda() {
        return datNombreBusqueda;
    }

    public void setDatNombreBusqueda(String datNombreBusqueda) {
        this.datNombreBusqueda = datNombreBusqueda;
    }

    public Boolean getDatHabilitado() {
        return datHabilitado;
    }

    public void setDatHabilitado(Boolean datHabilitado) {
        this.datHabilitado = datHabilitado;
    }

    public LocalDateTime getDatUltModFecha() {
        return datUltModFecha;
    }

    public void setDatUltModFecha(LocalDateTime datUltModFecha) {
        this.datUltModFecha = datUltModFecha;
    }

    public String getDatUltModUsuario() {
        return datUltModUsuario;
    }

    public void setDatUltModUsuario(String datUltModUsuario) {
        this.datUltModUsuario = datUltModUsuario;
    }

    public Integer getDatVersion() {
        return datVersion;
    }

    public void setDatVersion(Integer datVersion) {
        this.datVersion = datVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.datPk);
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
        final SgEstDatasets other = (SgEstDatasets) obj;
        if (!Objects.equals(this.datPk, other.datPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDatasets{" + "datPk=" + datPk + ", datCodigo=" + datCodigo + ", datNombre=" + datNombre + ", datNombreBusqueda=" + datNombreBusqueda + ", datHabilitado=" + datHabilitado + ", datUltModFecha=" + datUltModFecha + ", datUltModUsuario=" + datUltModUsuario + ", datVersion=" + datVersion + '}';
    }
    
    

}
