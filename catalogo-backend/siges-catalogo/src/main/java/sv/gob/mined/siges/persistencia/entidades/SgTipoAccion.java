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
@Table(name = "sg_tipos_accion", uniqueConstraints = {
    @UniqueConstraint(name = "tac_codigo_uk", columnNames = {"tac_codigo"})
    ,
    @UniqueConstraint(name = "tac_nombre_uk", columnNames = {"tac_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tacPk", scope = SgTipoAccion.class)
@Audited
public class SgTipoAccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tac_pk", nullable = false)
    private Long tacPk;

    @Size(max = 45)
    @Column(name = "tac_codigo", length = 45)
    @AtributoCodigo
    private String tacCodigo;

    @Size(max = 255)
    @Column(name = "tac_nombre", length = 255)
    @AtributoNormalizable
    private String tacNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tac_nombre_busqueda", length = 255)
    private String tacNombreBusqueda;

    @Column(name = "tac_habilitado")
    @AtributoHabilitado
    private Boolean tacHabilitado;

    @Column(name = "tac_necesita_descripcion")
    private Boolean tacNecesitaDescripcion;

    @Column(name = "tac_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tacUltModFecha;

    @Size(max = 45)
    @Column(name = "tac_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tacUltModUsuario;

    @Column(name = "tac_version")
    @Version
    private Integer tacVersion;

    public SgTipoAccion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tacNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tacNombre);
    }

    public Long getTacPk() {
        return tacPk;
    }

    public void setTacPk(Long tacPk) {
        this.tacPk = tacPk;
    }

    public String getTacCodigo() {
        return tacCodigo;
    }

    public void setTacCodigo(String tacCodigo) {
        this.tacCodigo = tacCodigo;
    }

    public String getTacNombre() {
        return tacNombre;
    }

    public void setTacNombre(String tacNombre) {
        this.tacNombre = tacNombre;
    }

    public String getTacNombreBusqueda() {
        return tacNombreBusqueda;
    }

    public void setTacNombreBusqueda(String tacNombreBusqueda) {
        this.tacNombreBusqueda = tacNombreBusqueda;
    }

    public Boolean getTacHabilitado() {
        return tacHabilitado;
    }

    public void setTacHabilitado(Boolean tacHabilitado) {
        this.tacHabilitado = tacHabilitado;
    }

    public Boolean getTacNecesitaDescripcion() {
        return tacNecesitaDescripcion;
    }

    public void setTacNecesitaDescripcion(Boolean tacNecesitaDescripcion) {
        this.tacNecesitaDescripcion = tacNecesitaDescripcion;
    }

    public LocalDateTime getTacUltModFecha() {
        return tacUltModFecha;
    }

    public void setTacUltModFecha(LocalDateTime tacUltModFecha) {
        this.tacUltModFecha = tacUltModFecha;
    }

    public String getTacUltModUsuario() {
        return tacUltModUsuario;
    }

    public void setTacUltModUsuario(String tacUltModUsuario) {
        this.tacUltModUsuario = tacUltModUsuario;
    }

    public Integer getTacVersion() {
        return tacVersion;
    }

    public void setTacVersion(Integer tacVersion) {
        this.tacVersion = tacVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tacPk);
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
        final SgTipoAccion other = (SgTipoAccion) obj;
        if (!Objects.equals(this.tacPk, other.tacPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoAccion{" + "tacPk=" + tacPk + ", tacCodigo=" + tacCodigo + ", tacNombre=" + tacNombre + ", tacNombreBusqueda=" + tacNombreBusqueda + ", tacHabilitado=" + tacHabilitado + ", tacUltModFecha=" + tacUltModFecha + ", tacUltModUsuario=" + tacUltModUsuario + ", tacVersion=" + tacVersion + '}';
    }
    
    

}
