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
@Table(name = "sg_inf_tipo_abastecimiento", uniqueConstraints = {
    @UniqueConstraint(name = "tab_codigo_uk", columnNames = {"tab_codigo"})
    ,
    @UniqueConstraint(name = "tab_nombre_uk", columnNames = {"tab_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tabPk", scope = SgInfTipoAbastecimiento.class)
@Audited
public class SgInfTipoAbastecimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tab_pk", nullable = false)
    private Long tabPk;

    @Size(max = 45)
    @Column(name = "tab_codigo", length = 45)
    @AtributoCodigo
    private String tabCodigo;

    @Size(max = 255)
    @Column(name = "tab_nombre", length = 255)
    @AtributoNormalizable
    private String tabNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tab_nombre_busqueda", length = 255)
    private String tabNombreBusqueda;

    @Column(name = "tab_habilitado")
    @AtributoHabilitado
    private Boolean tabHabilitado;

    @Column(name = "tab_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tabUltModFecha;

    @Size(max = 45)
    @Column(name = "tab_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tabUltModUsuario;

    @Column(name = "tab_version")
    @Version
    private Integer tabVersion;

    public SgInfTipoAbastecimiento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tabNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tabNombre);
    }

    public Long getTabPk() {
        return tabPk;
    }

    public void setTabPk(Long tabPk) {
        this.tabPk = tabPk;
    }

    public String getTabCodigo() {
        return tabCodigo;
    }

    public void setTabCodigo(String tabCodigo) {
        this.tabCodigo = tabCodigo;
    }

    public String getTabNombre() {
        return tabNombre;
    }

    public void setTabNombre(String tabNombre) {
        this.tabNombre = tabNombre;
    }

    public String getTabNombreBusqueda() {
        return tabNombreBusqueda;
    }

    public void setTabNombreBusqueda(String tabNombreBusqueda) {
        this.tabNombreBusqueda = tabNombreBusqueda;
    }

    public Boolean getTabHabilitado() {
        return tabHabilitado;
    }

    public void setTabHabilitado(Boolean tabHabilitado) {
        this.tabHabilitado = tabHabilitado;
    }

    public LocalDateTime getTabUltModFecha() {
        return tabUltModFecha;
    }

    public void setTabUltModFecha(LocalDateTime tabUltModFecha) {
        this.tabUltModFecha = tabUltModFecha;
    }

    public String getTabUltModUsuario() {
        return tabUltModUsuario;
    }

    public void setTabUltModUsuario(String tabUltModUsuario) {
        this.tabUltModUsuario = tabUltModUsuario;
    }

    public Integer getTabVersion() {
        return tabVersion;
    }

    public void setTabVersion(Integer tabVersion) {
        this.tabVersion = tabVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tabPk);
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
        final SgInfTipoAbastecimiento other = (SgInfTipoAbastecimiento) obj;
        if (!Objects.equals(this.tabPk, other.tabPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfTipoAbastecimiento{" + "tabPk=" + tabPk + ", tabCodigo=" + tabCodigo + ", tabNombre=" + tabNombre + ", tabNombreBusqueda=" + tabNombreBusqueda + ", tabHabilitado=" + tabHabilitado + ", tabUltModFecha=" + tabUltModFecha + ", tabUltModUsuario=" + tabUltModUsuario + ", tabVersion=" + tabVersion + '}';
    }
    
    

}
