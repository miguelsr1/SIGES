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
@Table(name = "sg_est_categorias_indicadores", uniqueConstraints = {
    @UniqueConstraint(name = "cin_codigo_uk", columnNames = {"cin_codigo"})
    ,
    @UniqueConstraint(name = "cin_nombre_uk", columnNames = {"cin_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cinPk", scope = SgEstCategoriaIndicador.class)
@Audited
public class SgEstCategoriaIndicador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cin_pk", nullable = false)
    private Long cinPk;

    @Size(max = 45)
    @Column(name = "cin_codigo", length = 45)
    @AtributoCodigo
    private String cinCodigo;

    @Size(max = 255)
    @Column(name = "cin_nombre", length = 255)
    @AtributoNormalizable
    private String cinNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cin_nombre_busqueda", length = 255)
    private String cinNombreBusqueda;

    @Column(name = "cin_habilitado")
    @AtributoHabilitado
    private Boolean cinHabilitado;

    @Column(name = "cin_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cinUltModFecha;

    @Size(max = 45)
    @Column(name = "cin_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cinUltModUsuario;

    @Column(name = "cin_version")
    @Version
    private Integer cinVersion;

    public SgEstCategoriaIndicador() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cinNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cinNombre);
    }

    public Long getCinPk() {
        return cinPk;
    }

    public void setCinPk(Long cinPk) {
        this.cinPk = cinPk;
    }

    public String getCinCodigo() {
        return cinCodigo;
    }

    public void setCinCodigo(String cinCodigo) {
        this.cinCodigo = cinCodigo;
    }

    public String getCinNombre() {
        return cinNombre;
    }

    public void setCinNombre(String cinNombre) {
        this.cinNombre = cinNombre;
    }

    public String getCinNombreBusqueda() {
        return cinNombreBusqueda;
    }

    public void setCinNombreBusqueda(String cinNombreBusqueda) {
        this.cinNombreBusqueda = cinNombreBusqueda;
    }

    public Boolean getCinHabilitado() {
        return cinHabilitado;
    }

    public void setCinHabilitado(Boolean cinHabilitado) {
        this.cinHabilitado = cinHabilitado;
    }

    public LocalDateTime getCinUltModFecha() {
        return cinUltModFecha;
    }

    public void setCinUltModFecha(LocalDateTime cinUltModFecha) {
        this.cinUltModFecha = cinUltModFecha;
    }

    public String getCinUltModUsuario() {
        return cinUltModUsuario;
    }

    public void setCinUltModUsuario(String cinUltModUsuario) {
        this.cinUltModUsuario = cinUltModUsuario;
    }

    public Integer getCinVersion() {
        return cinVersion;
    }

    public void setCinVersion(Integer cinVersion) {
        this.cinVersion = cinVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cinPk);
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
        final SgEstCategoriaIndicador other = (SgEstCategoriaIndicador) obj;
        if (!Objects.equals(this.cinPk, other.cinPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEstCategoriaIndicador{" + "cinPk=" + cinPk + ", cinCodigo=" + cinCodigo + ", cinNombre=" + cinNombre + ", cinNombreBusqueda=" + cinNombreBusqueda + ", cinHabilitado=" + cinHabilitado + ", cinUltModFecha=" + cinUltModFecha + ", cinUltModUsuario=" + cinUltModUsuario + ", cinVersion=" + cinVersion + '}';
    }
    
    

}
