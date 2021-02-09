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
@Table(name = "sg_estado_proceso_legalizacion", uniqueConstraints = {
    @UniqueConstraint(name = "epl_codigo_uk", columnNames = {"epl_codigo"})
    ,
    @UniqueConstraint(name = "epl_nombre_uk", columnNames = {"epl_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eplPk", scope = SgEstadoProcesoLegalizacion.class)
@Audited
public class SgEstadoProcesoLegalizacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "epl_pk", nullable = false)
    private Long eplPk;

    @Size(max = 45)
    @Column(name = "epl_codigo", length = 45)
    @AtributoCodigo
    private String eplCodigo;

    @Size(max = 255)
    @Column(name = "epl_nombre", length = 255)
    @AtributoNormalizable
    private String eplNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "epl_nombre_busqueda", length = 255)
    private String eplNombreBusqueda;

    @Column(name = "epl_habilitado")
    @AtributoHabilitado
    private Boolean eplHabilitado;
    
    @Column(name = "epl_aplica_dato_presentacion")
    private Boolean eplAplicaDatoPresentacion;
    
    @Column(name = "epl_aplica_estado_proceso")
    private Boolean eplAplicaEstadoProceso;

    @Column(name = "epl_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime eplUltModFecha;

    @Size(max = 45)
    @Column(name = "epl_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String eplUltModUsuario;

    @Column(name = "epl_version")
    @Version
    private Integer eplVersion;

    public SgEstadoProcesoLegalizacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.eplNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.eplNombre);
    }

    public Long getEplPk() {
        return eplPk;
    }

    public void setEplPk(Long eplPk) {
        this.eplPk = eplPk;
    }

    public String getEplCodigo() {
        return eplCodigo;
    }

    public void setEplCodigo(String eplCodigo) {
        this.eplCodigo = eplCodigo;
    }

    public String getEplNombre() {
        return eplNombre;
    }

    public void setEplNombre(String eplNombre) {
        this.eplNombre = eplNombre;
    }

    public String getEplNombreBusqueda() {
        return eplNombreBusqueda;
    }

    public void setEplNombreBusqueda(String eplNombreBusqueda) {
        this.eplNombreBusqueda = eplNombreBusqueda;
    }

    public Boolean getEplHabilitado() {
        return eplHabilitado;
    }

    public void setEplHabilitado(Boolean eplHabilitado) {
        this.eplHabilitado = eplHabilitado;
    }

    public LocalDateTime getEplUltModFecha() {
        return eplUltModFecha;
    }

    public void setEplUltModFecha(LocalDateTime eplUltModFecha) {
        this.eplUltModFecha = eplUltModFecha;
    }

    public String getEplUltModUsuario() {
        return eplUltModUsuario;
    }

    public void setEplUltModUsuario(String eplUltModUsuario) {
        this.eplUltModUsuario = eplUltModUsuario;
    }

    public Integer getEplVersion() {
        return eplVersion;
    }

    public void setEplVersion(Integer eplVersion) {
        this.eplVersion = eplVersion;
    }

    public Boolean getEplAplicaDatoPresentacion() {
        return eplAplicaDatoPresentacion;
    }

    public void setEplAplicaDatoPresentacion(Boolean eplAplicaDatoPresentacion) {
        this.eplAplicaDatoPresentacion = eplAplicaDatoPresentacion;
    }

    public Boolean getEplAplicaEstadoProceso() {
        return eplAplicaEstadoProceso;
    }

    public void setEplAplicaEstadoProceso(Boolean eplAplicaEstadoProceso) {
        this.eplAplicaEstadoProceso = eplAplicaEstadoProceso;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.eplPk);
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
        final SgEstadoProcesoLegalizacion other = (SgEstadoProcesoLegalizacion) obj;
        if (!Objects.equals(this.eplPk, other.eplPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEstadoProcesoLegalizacion{" + "eplPk=" + eplPk + ", eplCodigo=" + eplCodigo + ", eplNombre=" + eplNombre + ", eplNombreBusqueda=" + eplNombreBusqueda + ", eplHabilitado=" + eplHabilitado + ", eplUltModFecha=" + eplUltModFecha + ", eplUltModUsuario=" + eplUltModUsuario + ", eplVersion=" + eplVersion + '}';
    }
    
    

}
