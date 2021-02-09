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
@Table(name = "sg_materiales_piso", schema = Constantes.SCHEMA_CATALOGO, uniqueConstraints = {
    @UniqueConstraint(name = "map_codigo_uk", columnNames = {"map_codigo"})
    ,
    @UniqueConstraint(name = "map_nombre_uk", columnNames = {"map_nombre"})})
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mapPk", scope = SgMaterialPiso.class)
@Audited
public class SgMaterialPiso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "map_pk", nullable = false)
    private Long mapPk;

    @Size(max = 45)
    @Column(name = "map_codigo", length = 45)
    @AtributoCodigo
    private String mapCodigo;

    @Size(max = 255)
    @Column(name = "map_nombre", length = 255)
    @AtributoNormalizable
    private String mapNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "map_nombre_busqueda", length = 255)
    private String mapNombreBusqueda;

    @Column(name = "map_habilitado")
    @AtributoHabilitado
    private Boolean mapHabilitado;

    @Column(name = "map_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mapUltModFecha;

    @Size(max = 45)
    @Column(name = "map_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mapUltModUsuario;

    @Column(name = "map_version")
    @Version
    private Integer mapVersion;

    public SgMaterialPiso() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mapNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mapNombre);
    }

    public Long getMapPk() {
        return mapPk;
    }

    public void setMapPk(Long mapPk) {
        this.mapPk = mapPk;
    }

    public String getMapCodigo() {
        return mapCodigo;
    }

    public void setMapCodigo(String mapCodigo) {
        this.mapCodigo = mapCodigo;
    }

    public String getMapNombre() {
        return mapNombre;
    }

    public void setMapNombre(String mapNombre) {
        this.mapNombre = mapNombre;
    }

    public String getMapNombreBusqueda() {
        return mapNombreBusqueda;
    }

    public void setMapNombreBusqueda(String mapNombreBusqueda) {
        this.mapNombreBusqueda = mapNombreBusqueda;
    }

    public Boolean getMapHabilitado() {
        return mapHabilitado;
    }

    public void setMapHabilitado(Boolean mapHabilitado) {
        this.mapHabilitado = mapHabilitado;
    }

    public LocalDateTime getMapUltModFecha() {
        return mapUltModFecha;
    }

    public void setMapUltModFecha(LocalDateTime mapUltModFecha) {
        this.mapUltModFecha = mapUltModFecha;
    }

    public String getMapUltModUsuario() {
        return mapUltModUsuario;
    }

    public void setMapUltModUsuario(String mapUltModUsuario) {
        this.mapUltModUsuario = mapUltModUsuario;
    }

    public Integer getMapVersion() {
        return mapVersion;
    }

    public void setMapVersion(Integer mapVersion) {
        this.mapVersion = mapVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mapPk);
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
        final SgMaterialPiso other = (SgMaterialPiso) obj;
        if (!Objects.equals(this.mapPk, other.mapPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMaterialPiso{" + "mapPk=" + mapPk + ", mapCodigo=" + mapCodigo + ", mapNombre=" + mapNombre + ", mapNombreBusqueda=" + mapNombreBusqueda + ", mapHabilitado=" + mapHabilitado + ", mapUltModFecha=" + mapUltModFecha + ", mapUltModUsuario=" + mapUltModUsuario + ", mapVersion=" + mapVersion + '}';
    }
    
    

}
