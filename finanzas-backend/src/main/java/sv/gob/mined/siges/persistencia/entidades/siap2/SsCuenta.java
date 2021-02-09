package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "ss_cuentas", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cuId", scope = SsCuenta.class)
@Audited
/**
 * Entidad correspondiente a las unidades presupuestarias.
 */
public class SsCuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cu_id")
    private Long cuId;

    @Column(name = "cu_codigo")
    @AtributoNormalizable
    private String cuCodigo;

    @Column(name = "cu_nombre")
    @AtributoNormalizable
    private String cuNombre;

    @Column(name = "cu_descripcion")
    private String cuDescripcion;

    @Column(name = "cu_habilitado")
    private Boolean cuHabilitado;

    @Column(name = "cu_orden")
    private Integer cuOrden;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cu_cuenta_padre")
    private SsCuenta cuCuentaPadre;

    //Auditoria
    @Column(name = "cu_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date cuUltMod;

    @Column(name = "cu_ult_usuario")
    @AtributoUltimoUsuario
    private String cuUltUsuario;

    @Column(name = "cu_version")
    @Version
    private Integer cuVersion;

    public Long getCuId() {
        return cuId;
    }

    public void setCuId(Long cuId) {
        this.cuId = cuId;
    }

    public String getCuCodigo() {
        return cuCodigo;
    }

    public void setCuCodigo(String cuCodigo) {
        this.cuCodigo = cuCodigo;
    }

    public String getCuNombre() {
        return cuNombre;
    }

    public void setCuNombre(String cuNombre) {
        this.cuNombre = cuNombre;
    }

    public String getCuDescripcion() {
        return cuDescripcion;
    }

    public void setCuDescripcion(String cuDescripcion) {
        this.cuDescripcion = cuDescripcion;
    }

    public Boolean getCuHabilitado() {
        return cuHabilitado;
    }

    public void setCuHabilitado(Boolean cuHabilitado) {
        this.cuHabilitado = cuHabilitado;
    }

    public Integer getCuOrden() {
        return cuOrden;
    }

    public void setCuOrden(Integer cuOrden) {
        this.cuOrden = cuOrden;
    }

    public SsCuenta getCuCuentaPadre() {
        return cuCuentaPadre;
    }

    public void setCuCuentaPadre(SsCuenta cuCuentaPadre) {
        this.cuCuentaPadre = cuCuentaPadre;
    }

    public Date getCuUltMod() {
        return cuUltMod;
    }

    public void setCuUltMod(Date cuUltMod) {
        this.cuUltMod = cuUltMod;
    }

    public String getCuUltUsuario() {
        return cuUltUsuario;
    }

    public void setCuUltUsuario(String cuUltUsuario) {
        this.cuUltUsuario = cuUltUsuario;
    }

    public Integer getCuVersion() {
        return cuVersion;
    }

    public void setCuVersion(Integer cuVersion) {
        this.cuVersion = cuVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.cuId);
        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SsCuenta other = (SsCuenta) obj;
        if (this.cuId != null && other.getCuId() != null) {
            return Objects.equals(this.cuId, other.getCuId());
        }
        return this == other;
    }

    @Override
    public String toString() {
        return "SsCuenta{" + "cuId=" + cuId + ", cuCodigo=" + cuCodigo + ", cuNombre=" + cuNombre + ", cuDescripcion=" + cuDescripcion + ", cuHabilitado=" + cuHabilitado + ", cuOrden=" + cuOrden + ", cuCuentaPadre=" + cuCuentaPadre + ", cuUltMod=" + cuUltMod + ", cuUltUsuario=" + cuUltUsuario + ", cuVersion=" + cuVersion + '}';
    }

}
