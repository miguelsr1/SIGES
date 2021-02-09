/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_impresoras", uniqueConstraints = {
    @UniqueConstraint(name = "impresoras_codigo_uk", columnNames = {"imp_codigo"})
    ,
    @UniqueConstraint(name = "impresoras_nombre_uk", columnNames = {"imp_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "impPk", scope = SgImpresora.class)
public class SgImpresora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "imp_pk")
    private Long impPk;
    
    @Size(max = 4)
    @Column(name = "imp_codigo", length = 4)
    @AtributoCodigo
    private String impCodigo;
    
    @Column(name = "imp_habilitado")
    @AtributoHabilitado
    private Boolean impHabilitado;
    
    @Size(max = 100)
    @Column(name = "imp_nombre", length = 100)
    @AtributoNormalizable
    private String impNombre;
    
    @Size(max = 100)
    @Column(name = "imp_nombre_busqueda", length = 100)
    @AtributoNombre
    private String impNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "imp_descripcion", length = 255)
    @AtributoDescripcion
    private String impDescripcion;
    
    @Column(name = "imp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime impUltModFecha;
    
    @Size(max = 45)
    @Column(name = "imp_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String impUltModUsuario;
    
    @Column(name = "imp_version")
    @Version
    private Integer impVersion;

    public SgImpresora() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.impNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.impNombre);
    }

    public SgImpresora(Long mpePk) {
        this.impPk = mpePk;
    }

    public Long getImpPk() {
        return impPk;
    }

    public void setImpPk(Long impPk) {
        this.impPk = impPk;
    }

    public String getImpCodigo() {
        return impCodigo;
    }

    public void setImpCodigo(String impCodigo) {
        this.impCodigo = impCodigo;
    }

    public Boolean getImpHabilitado() {
        return impHabilitado;
    }

    public void setImpHabilitado(Boolean impHabilitado) {
        this.impHabilitado = impHabilitado;
    }

    public String getImpNombre() {
        return impNombre;
    }

    public void setImpNombre(String impNombre) {
        this.impNombre = impNombre;
    }

    public String getImpNombreBusqueda() {
        return impNombreBusqueda;
    }

    public void setImpNombreBusqueda(String impNombreBusqueda) {
        this.impNombreBusqueda = impNombreBusqueda;
    }

    public String getImpDescripcion() {
        return impDescripcion;
    }

    public void setImpDescripcion(String impDescripcion) {
        this.impDescripcion = impDescripcion;
    }

    public LocalDateTime getImpUltModFecha() {
        return impUltModFecha;
    }

    public void setImpUltModFecha(LocalDateTime impUltModFecha) {
        this.impUltModFecha = impUltModFecha;
    }

    public String getImpUltModUsuario() {
        return impUltModUsuario;
    }

    public void setImpUltModUsuario(String impUltModUsuario) {
        this.impUltModUsuario = impUltModUsuario;
    }

    public Integer getImpVersion() {
        return impVersion;
    }

    public void setImpVersion(Integer impVersion) {
        this.impVersion = impVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.impPk);
        return hash;
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
        final SgImpresora other = (SgImpresora) obj;
        if (!Objects.equals(this.impPk, other.impPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgImpresora{" + "impPk=" + impPk + '}';
    }

    
}

