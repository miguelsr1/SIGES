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
@Table(name = "sg_inf_tipo_manejo_desechos", uniqueConstraints = {
    @UniqueConstraint(name = "tmd_codigo_uk", columnNames = {"tmd_codigo"})
    ,
    @UniqueConstraint(name = "tmd_nombre_uk", columnNames = {"tmd_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tmdPk", scope = SgInfTipoManejoDesechos.class)
@Audited
public class SgInfTipoManejoDesechos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tmd_pk", nullable = false)
    private Long tmdPk;

    @Size(max = 45)
    @Column(name = "tmd_codigo", length = 45)
    @AtributoCodigo
    private String tmdCodigo;

    @Size(max = 255)
    @Column(name = "tmd_nombre", length = 255)
    @AtributoNormalizable
    private String tmdNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tmd_nombre_busqueda", length = 255)
    private String tmdNombreBusqueda;

    @Column(name = "tmd_habilitado")
    @AtributoHabilitado
    private Boolean tmdHabilitado;

    @Column(name = "tmd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tmdUltModFecha;

    @Size(max = 45)
    @Column(name = "tmd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tmdUltModUsuario;

    @Column(name = "tmd_version")
    @Version
    private Integer tmdVersion;
    
    @Column(name="tmd_orden")
    private Integer tmdOrden;
    
    @Column(name="tmd_aplica_otros")
    private Boolean tmdAplicaOtros;        
    

    public SgInfTipoManejoDesechos() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tmdNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tmdNombre);
    }

    public Long getTmdPk() {
        return tmdPk;
    }

    public void setTmdPk(Long tmdPk) {
        this.tmdPk = tmdPk;
    }

    public String getTmdCodigo() {
        return tmdCodigo;
    }

    public void setTmdCodigo(String tmdCodigo) {
        this.tmdCodigo = tmdCodigo;
    }

    public String getTmdNombre() {
        return tmdNombre;
    }

    public void setTmdNombre(String tmdNombre) {
        this.tmdNombre = tmdNombre;
    }

    public String getTmdNombreBusqueda() {
        return tmdNombreBusqueda;
    }

    public void setTmdNombreBusqueda(String tmdNombreBusqueda) {
        this.tmdNombreBusqueda = tmdNombreBusqueda;
    }

    public Boolean getTmdHabilitado() {
        return tmdHabilitado;
    }

    public void setTmdHabilitado(Boolean tmdHabilitado) {
        this.tmdHabilitado = tmdHabilitado;
    }

    public LocalDateTime getTmdUltModFecha() {
        return tmdUltModFecha;
    }

    public void setTmdUltModFecha(LocalDateTime tmdUltModFecha) {
        this.tmdUltModFecha = tmdUltModFecha;
    }

    public String getTmdUltModUsuario() {
        return tmdUltModUsuario;
    }

    public void setTmdUltModUsuario(String tmdUltModUsuario) {
        this.tmdUltModUsuario = tmdUltModUsuario;
    }

    public Integer getTmdVersion() {
        return tmdVersion;
    }

    public void setTmdVersion(Integer tmdVersion) {
        this.tmdVersion = tmdVersion;
    }

    public Integer getTmdOrden() {
        return tmdOrden;
    }

    public void setTmdOrden(Integer tmdOrden) {
        this.tmdOrden = tmdOrden;
    }

    public Boolean getTmdAplicaOtros() {
        return tmdAplicaOtros;
    }

    public void setTmdAplicaOtros(Boolean tmdAplicaOtros) {
        this.tmdAplicaOtros = tmdAplicaOtros;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tmdPk);
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
        final SgInfTipoManejoDesechos other = (SgInfTipoManejoDesechos) obj;
        if (!Objects.equals(this.tmdPk, other.tmdPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfTipoManejoDesechos{" + "tmdPk=" + tmdPk + ", tmdCodigo=" + tmdCodigo + ", tmdNombre=" + tmdNombre + ", tmdNombreBusqueda=" + tmdNombreBusqueda + ", tmdHabilitado=" + tmdHabilitado + ", tmdUltModFecha=" + tmdUltModFecha + ", tmdUltModUsuario=" + tmdUltModUsuario + ", tmdVersion=" + tmdVersion + '}';
    }
    
    

}
