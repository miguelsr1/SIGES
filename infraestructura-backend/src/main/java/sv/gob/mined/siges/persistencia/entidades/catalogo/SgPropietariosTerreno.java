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
@Table(name = "sg_propietarios_terreno", uniqueConstraints = {
    @UniqueConstraint(name = "pdt_codigo_uk", columnNames = {"pdt_codigo"})
    ,
    @UniqueConstraint(name = "pdt_nombre_uk", columnNames = {"pdt_nombre"})},schema=Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pdtPk", scope = SgPropietariosTerreno.class)
@Audited
public class SgPropietariosTerreno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pdt_pk", nullable = false)
    private Long pdtPk;

    @Size(max = 45)
    @Column(name = "pdt_codigo", length = 45)
    @AtributoCodigo
    private String pdtCodigo;

    @Size(max = 255)
    @Column(name = "pdt_nombre", length = 255)
    @AtributoNormalizable
    private String pdtNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "pdt_nombre_busqueda", length = 255)
    private String pdtNombreBusqueda;

    @Column(name = "pdt_habilitado")
    @AtributoHabilitado
    private Boolean pdtHabilitado;

    @Column(name = "pdt_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pdtUltModFecha;

    @Size(max = 45)
    @Column(name = "pdt_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pdtUltModUsuario;

    @Column(name = "pdt_version")
    @Version
    private Integer pdtVersion;

    public SgPropietariosTerreno() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.pdtNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.pdtNombre);
    }

    public Long getPdtPk() {
        return pdtPk;
    }

    public void setPdtPk(Long pdtPk) {
        this.pdtPk = pdtPk;
    }

    public String getPdtCodigo() {
        return pdtCodigo;
    }

    public void setPdtCodigo(String pdtCodigo) {
        this.pdtCodigo = pdtCodigo;
    }

    public String getPdtNombre() {
        return pdtNombre;
    }

    public void setPdtNombre(String pdtNombre) {
        this.pdtNombre = pdtNombre;
    }

    public String getPdtNombreBusqueda() {
        return pdtNombreBusqueda;
    }

    public void setPdtNombreBusqueda(String pdtNombreBusqueda) {
        this.pdtNombreBusqueda = pdtNombreBusqueda;
    }

    public Boolean getPdtHabilitado() {
        return pdtHabilitado;
    }

    public void setPdtHabilitado(Boolean pdtHabilitado) {
        this.pdtHabilitado = pdtHabilitado;
    }

    public LocalDateTime getPdtUltModFecha() {
        return pdtUltModFecha;
    }

    public void setPdtUltModFecha(LocalDateTime pdtUltModFecha) {
        this.pdtUltModFecha = pdtUltModFecha;
    }

    public String getPdtUltModUsuario() {
        return pdtUltModUsuario;
    }

    public void setPdtUltModUsuario(String pdtUltModUsuario) {
        this.pdtUltModUsuario = pdtUltModUsuario;
    }

    public Integer getPdtVersion() {
        return pdtVersion;
    }

    public void setPdtVersion(Integer pdtVersion) {
        this.pdtVersion = pdtVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.pdtPk);
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
        final SgPropietariosTerreno other = (SgPropietariosTerreno) obj;
        if (!Objects.equals(this.pdtPk, other.pdtPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPropietariosTerreno{" + "pdtPk=" + pdtPk + ", pdtCodigo=" + pdtCodigo + ", pdtNombre=" + pdtNombre + ", pdtNombreBusqueda=" + pdtNombreBusqueda + ", pdtHabilitado=" + pdtHabilitado + ", pdtUltModFecha=" + pdtUltModFecha + ", pdtUltModUsuario=" + pdtUltModUsuario + ", pdtVersion=" + pdtVersion + '}';
    }
    
    

}
