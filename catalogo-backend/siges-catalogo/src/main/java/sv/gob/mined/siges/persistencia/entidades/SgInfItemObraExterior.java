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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sg_inf_item_obra_exterior", uniqueConstraints = {
    @UniqueConstraint(name = "ioe_codigo_uk", columnNames = {"ioe_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ioePk", scope = SgInfItemObraExterior.class)
@Audited
public class SgInfItemObraExterior implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ioe_pk", nullable = false)
    private Long ioePk;

    @Size(max = 45)
    @Column(name = "ioe_codigo", length = 45)
    @AtributoCodigo
    private String ioeCodigo;

    @Size(max = 255)
    @Column(name = "ioe_nombre", length = 255)
    @AtributoNormalizable
    private String ioeNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ioe_nombre_busqueda", length = 255)
    private String ioeNombreBusqueda;

    @Column(name = "ioe_habilitado")
    @AtributoHabilitado
    private Boolean ioeHabilitado;

    @Column(name = "ioe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ioeUltModFecha;

    @Size(max = 45)
    @Column(name = "ioe_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ioeUltModUsuario;

    @Column(name = "ioe_version")
    @Version
    private Integer ioeVersion;
    
    @Column(name = "ioe_aplica_otro")
    private Boolean ioeAplicaOtros;
    
    @Column(name = "ioe_orden")
    private Integer ioeOrden;
    
    @JoinColumn(name = "ioe_obra_exterior", referencedColumnName = "oex_pk")
    @ManyToOne
    private SgInfObraExterior ioeObraExterior;

    public SgInfItemObraExterior() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ioeNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ioeNombre);
    }

    public Long getIoePk() {
        return ioePk;
    }

    public void setIoePk(Long ioePk) {
        this.ioePk = ioePk;
    }

    public String getIoeCodigo() {
        return ioeCodigo;
    }

    public void setIoeCodigo(String ioeCodigo) {
        this.ioeCodigo = ioeCodigo;
    }

    public String getIoeNombre() {
        return ioeNombre;
    }

    public void setIoeNombre(String ioeNombre) {
        this.ioeNombre = ioeNombre;
    }

    public String getIoeNombreBusqueda() {
        return ioeNombreBusqueda;
    }

    public void setIoeNombreBusqueda(String ioeNombreBusqueda) {
        this.ioeNombreBusqueda = ioeNombreBusqueda;
    }

    public Boolean getIoeHabilitado() {
        return ioeHabilitado;
    }

    public void setIoeHabilitado(Boolean ioeHabilitado) {
        this.ioeHabilitado = ioeHabilitado;
    }

    public LocalDateTime getIoeUltModFecha() {
        return ioeUltModFecha;
    }

    public void setIoeUltModFecha(LocalDateTime ioeUltModFecha) {
        this.ioeUltModFecha = ioeUltModFecha;
    }

    public String getIoeUltModUsuario() {
        return ioeUltModUsuario;
    }

    public void setIoeUltModUsuario(String ioeUltModUsuario) {
        this.ioeUltModUsuario = ioeUltModUsuario;
    }

    public Integer getIoeVersion() {
        return ioeVersion;
    }

    public void setIoeVersion(Integer ioeVersion) {
        this.ioeVersion = ioeVersion;
    }

    public Boolean getIoeAplicaOtros() {
        return ioeAplicaOtros;
    }

    public void setIoeAplicaOtros(Boolean ioeAplicaOtros) {
        this.ioeAplicaOtros = ioeAplicaOtros;
    }

    public Integer getIoeOrden() {
        return ioeOrden;
    }

    public void setIoeOrden(Integer ioeOrden) {
        this.ioeOrden = ioeOrden;
    }

    public SgInfObraExterior getIoeObraExterior() {
        return ioeObraExterior;
    }

    public void setIoeObraExterior(SgInfObraExterior ioeObraExterior) {
        this.ioeObraExterior = ioeObraExterior;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ioePk);
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
        final SgInfItemObraExterior other = (SgInfItemObraExterior) obj;
        if (!Objects.equals(this.ioePk, other.ioePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfItemObraExterior{" + "ioePk=" + ioePk + ", ioeCodigo=" + ioeCodigo + ", ioeNombre=" + ioeNombre + ", ioeNombreBusqueda=" + ioeNombreBusqueda + ", ioeHabilitado=" + ioeHabilitado + ", ioeUltModFecha=" + ioeUltModFecha + ", ioeUltModUsuario=" + ioeUltModUsuario + ", ioeVersion=" + ioeVersion + '}';
    }
    
    

}
