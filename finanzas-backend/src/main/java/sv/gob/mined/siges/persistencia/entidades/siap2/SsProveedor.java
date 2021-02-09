package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "ss_proveedor", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "proId", scope = SsTransferenciaComponente.class)
/**
 * Entidad correspondiente a los proveedores
 */
public class SsProveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pro_id")
    private Long proId;

    @AtributoCodigo
    @Column(name = "pro_codigo")
    private String proCodigo;

    @Column(name = "pro_estado")
    private String proEstado;

    @AtributoNombre
    @Column(name = "pro_nombre")
    private String proNombre;

    @AtributoHabilitado
    @Column(name = "activo")
    private Boolean proActivo;

    @Column(name = "pro_nit")
    private String proNit;

    @Column(name = "direccion_factura", nullable = true)
    private String proDireccionFactura;

    @Column(name = "telefono_representante", nullable = true)
    private String proTelefonoRepresentante;

    @Column(name = "proveedor_mined")
    private Boolean proveedorMined;

    //Auditoria
    @Column(name = "pro_ult_mod ")
    @AtributoUltimaModificacion
    private LocalDateTime proUltMod;

    @Column(name = "pro_ult_usuario")
    @AtributoUltimoUsuario
    private String proUltUsuario;

    @Column(name = "pro_version")
    @Version
    private Integer proVersion;

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public String getProCodigo() {
        return proCodigo;
    }

    public void setProCodigo(String proCodigo) {
        this.proCodigo = proCodigo;
    }

    public String getProEstado() {
        return proEstado;
    }

    public void setProEstado(String proEstado) {
        this.proEstado = proEstado;
    }

    public String getProNombre() {
        return proNombre;
    }

    public void setProNombre(String proNombre) {
        this.proNombre = proNombre;
    }

    public Boolean getProActivo() {
        return proActivo;
    }

    public void setProActivo(Boolean proActivo) {
        this.proActivo = proActivo;
    }

    public String getProNit() {
        return proNit;
    }

    public void setProNit(String proNit) {
        this.proNit = proNit;
    }

    public LocalDateTime getProUltMod() {
        return proUltMod;
    }

    public void setProUltMod(LocalDateTime proUltMod) {
        this.proUltMod = proUltMod;
    }

    public String getProUltUsuario() {
        return proUltUsuario;
    }

    public void setProUltUsuario(String proUltUsuario) {
        this.proUltUsuario = proUltUsuario;
    }

    public String getProDireccionFactura() {
        return proDireccionFactura;
    }

    public void setProDireccionFactura(String proDireccionFactura) {
        this.proDireccionFactura = proDireccionFactura;
    }

    public String getProTelefonoRepresentante() {
        return proTelefonoRepresentante;
    }

    public void setProTelefonoRepresentante(String proTelefonoRepresentante) {
        this.proTelefonoRepresentante = proTelefonoRepresentante;
    }

    public Boolean getProveedorMined() {
        return proveedorMined;
    }

    public void setProveedorMined(Boolean proveedorMined) {
        this.proveedorMined = proveedorMined;
    }

    public Integer getProVersion() {
        return proVersion;
    }

    public void setProVersion(Integer proVersion) {
        this.proVersion = proVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.proId);
        hash = 89 * hash + Objects.hashCode(this.proCodigo);
        hash = 89 * hash + Objects.hashCode(this.proEstado);
        hash = 89 * hash + Objects.hashCode(this.proNombre);
        hash = 89 * hash + Objects.hashCode(this.proActivo);
        hash = 89 * hash + Objects.hashCode(this.proNit);
        hash = 89 * hash + Objects.hashCode(this.proUltMod);
        hash = 89 * hash + Objects.hashCode(this.proUltUsuario);
        hash = 89 * hash + Objects.hashCode(this.proVersion);
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
        final SsProveedor other = (SsProveedor) obj;
        if (!Objects.equals(this.proId, other.proId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SsProveedor{" + "proId=" + proId + '}';
    }
}
