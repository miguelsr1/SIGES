/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_clasificacion_bienes", uniqueConstraints = {
    @UniqueConstraint(name = "cbi_codigo_uk", columnNames = {"cbi_codigo"})
    ,
    @UniqueConstraint(name = "cbi_nombre_uk", columnNames = {"cbi_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cbiPk", scope = SgAfClasificacionBienes.class)
public class SgAfClasificacionBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cbi_pk", nullable = false)
    private Long cbiPk;

    @Size(max = 4)
    @Column(name = "cbi_codigo", length = 4)
    @AtributoCodigo
    private String cbiCodigo;

    @Size(max = 255)
    @Column(name = "cbi_nombre", length = 255)
    @AtributoNormalizable
    private String cbiNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cbi_nombre_busqueda", length = 255)
    private String cbiNombreBusqueda;

    @Column(name = "cbi_habilitado")
    @AtributoHabilitado
    private Boolean cbiHabilitado;

    @Column(name = "cbi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cbiUltModFecha;

    @Size(max = 45)
    @Column(name = "cbi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cbiUltModUsuario;

    @Column(name = "cbi_version")
    @Version
    private Integer cbiVersion;

    public SgAfClasificacionBienes() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cbiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cbiNombre);
    }

    public Long getCbiPk() {
        return cbiPk;
    }

    public void setCbiPk(Long cbiPk) {
        this.cbiPk = cbiPk;
    }

    public String getCbiCodigo() {
        return cbiCodigo;
    }

    public void setCbiCodigo(String cbiCodigo) {
        this.cbiCodigo = cbiCodigo;
    }

    public String getCbiNombre() {
        return cbiNombre;
    }

    public void setCbiNombre(String cbiNombre) {
        this.cbiNombre = cbiNombre;
    }

    public String getCbiNombreBusqueda() {
        return cbiNombreBusqueda;
    }

    public void setCbiNombreBusqueda(String cbiNombreBusqueda) {
        this.cbiNombreBusqueda = cbiNombreBusqueda;
    }

    public Boolean getCbiHabilitado() {
        return cbiHabilitado;
    }

    public void setCbiHabilitado(Boolean cbiHabilitado) {
        this.cbiHabilitado = cbiHabilitado;
    }

    public LocalDateTime getCbiUltModFecha() {
        return cbiUltModFecha;
    }

    public void setCbiUltModFecha(LocalDateTime cbiUltModFecha) {
        this.cbiUltModFecha = cbiUltModFecha;
    }

    public String getCbiUltModUsuario() {
        return cbiUltModUsuario;
    }

    public void setCbiUltModUsuario(String cbiUltModUsuario) {
        this.cbiUltModUsuario = cbiUltModUsuario;
    }

    public Integer getCbiVersion() {
        return cbiVersion;
    }

    public void setCbiVersion(Integer cbiVersion) {
        this.cbiVersion = cbiVersion;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgFormaAdquisicion[ cbiPk=" + cbiPk + " ]";
    }

}
