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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
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
@Table(name = "sg_items_evaluar_organismos", uniqueConstraints = {
    @UniqueConstraint(name = "ieo_codigo_uk", columnNames = {"ieo_codigo"})
    ,
    @UniqueConstraint(name = "ieo_nombre_uk", columnNames = {"ieo_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ieoPk", scope = SgItemEvaluarOrganismo.class)
@Audited
public class SgItemEvaluarOrganismo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ieo_pk", nullable = false)
    private Long ieoPk;

    @Size(max = 45)
    @Column(name = "ieo_codigo", length = 45)
    @AtributoCodigo
    private String ieoCodigo;

    @Size(max = 255)
    @Column(name = "ieo_nombre", length = 255)
    @AtributoNormalizable
    private String ieoNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ieo_nombre_busqueda", length = 255)
    private String ieoNombreBusqueda;

    @Column(name = "ieo_orden")
    private Integer ieoOrden;

    @Column(name = "ieo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ieoUltModFecha;

    @Size(max = 45)
    @Column(name = "ieo_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ieoUltModUsuario;

    @Column(name = "ieo_version")
    @Version
    private Integer ieoVersion;
    
    @JoinColumn(name = "ieo_tipo_organismo_fk", referencedColumnName = "toa_pk")
    @ManyToOne
    private SgTipoOrganismoAdministrativo ieoTipoOrganismo;

    public SgItemEvaluarOrganismo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ieoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ieoNombre);
    }

    public Long getIeoPk() {
        return ieoPk;
    }

    public void setIeoPk(Long ieoPk) {
        this.ieoPk = ieoPk;
    }

    public String getIeoCodigo() {
        return ieoCodigo;
    }

    public void setIeoCodigo(String ieoCodigo) {
        this.ieoCodigo = ieoCodigo;
    }

    public String getIeoNombre() {
        return ieoNombre;
    }

    public void setIeoNombre(String ieoNombre) {
        this.ieoNombre = ieoNombre;
    }

    public String getIeoNombreBusqueda() {
        return ieoNombreBusqueda;
    }

    public void setIeoNombreBusqueda(String ieoNombreBusqueda) {
        this.ieoNombreBusqueda = ieoNombreBusqueda;
    }

    public Integer getIeoOrden() {
        return ieoOrden;
    }

    public void setIeoOrden(Integer ieoOrden) {
        this.ieoOrden = ieoOrden;
    }

    public LocalDateTime getIeoUltModFecha() {
        return ieoUltModFecha;
    }

    public void setIeoUltModFecha(LocalDateTime ieoUltModFecha) {
        this.ieoUltModFecha = ieoUltModFecha;
    }

    public String getIeoUltModUsuario() {
        return ieoUltModUsuario;
    }

    public void setIeoUltModUsuario(String ieoUltModUsuario) {
        this.ieoUltModUsuario = ieoUltModUsuario;
    }

    public Integer getIeoVersion() {
        return ieoVersion;
    }

    public void setIeoVersion(Integer ieoVersion) {
        this.ieoVersion = ieoVersion;
    }

    public SgTipoOrganismoAdministrativo getIeoTipoOrganismo() {
        return ieoTipoOrganismo;
    }

    public void setIeoTipoOrganismo(SgTipoOrganismoAdministrativo ieoTipoOrganismo) {
        this.ieoTipoOrganismo = ieoTipoOrganismo;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ieoPk);
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
        final SgItemEvaluarOrganismo other = (SgItemEvaluarOrganismo) obj;
        if (!Objects.equals(this.ieoPk, other.ieoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgItemEvaluarOrganismo{" + "ieoPk=" + ieoPk + 
                ", ieoCodigo=" + ieoCodigo + ", ieoNombre=" + ieoNombre + 
                ", ieoNombreBusqueda=" + ieoNombreBusqueda + 
                ", ieoUltModFecha=" + ieoUltModFecha + ", ieoUltModUsuario=" + ieoUltModUsuario + 
                ", ieoVersion=" + ieoVersion + '}';
    }
    
    

}
