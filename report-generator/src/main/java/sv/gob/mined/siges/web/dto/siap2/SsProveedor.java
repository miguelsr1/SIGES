/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "proId", scope = SsProveedor.class)
public class SsProveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long proId;
    private String proCodigo;
    private String proEstado;
    private String proNombre;
    private Boolean proActivo;
    private String proNit;
    private String proDireccionFactura;
    private String proTelefonoRepresentante;
    private Boolean proveedorMined;
    private LocalDateTime proUltMod;
    private String proUltUsuario;
    private Integer proVersion;

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public String getProNitNombre() {
        return this.proNit + " - " + this.proNombre;
    }

    public SsProveedor() {
    }

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
        hash = 53 * hash + Objects.hashCode(this.proId);
        hash = 53 * hash + Objects.hashCode(this.proCodigo);
        hash = 53 * hash + Objects.hashCode(this.proEstado);
        hash = 53 * hash + Objects.hashCode(this.proNombre);
        hash = 53 * hash + Objects.hashCode(this.proActivo);
        hash = 53 * hash + Objects.hashCode(this.proNit);
        hash = 53 * hash + Objects.hashCode(this.proUltMod);
        hash = 53 * hash + Objects.hashCode(this.proUltUsuario);
        hash = 53 * hash + Objects.hashCode(this.proVersion);
        return hash;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
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

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SsProveedor[ proId=" + proId + " ]";
    }

}  // </editor-fold>
