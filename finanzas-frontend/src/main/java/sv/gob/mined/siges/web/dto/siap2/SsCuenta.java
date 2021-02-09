/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cuId", scope = SsCuenta.class)
public class SsCuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cuId;

    private String cuCodigo;

    private String cuNombre;

    private String cuDescripcion;

    private Boolean cuHabilitado;

    private Integer cuOrden;

    private SsCuenta cuCuentaPadre;

    private Integer cuVersion;

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public SsCuenta() {
    }

    public Long getCuId() {
        return cuId;
    }

    public String getCuCodigo() {
        return cuCodigo;
    }

    public String getCuNombre() {
        return cuNombre;
    }

    public String getCuDescripcion() {
        return cuDescripcion;
    }

    public Boolean getCuHabilitado() {
        return cuHabilitado;
    }

    public Integer getCuOrden() {
        return cuOrden;
    }

    public SsCuenta getCuCuentaPadre() {
        return cuCuentaPadre;
    }

    public Integer getCuVersion() {
        return cuVersion;
    }

    public void setCuId(Long cuId) {
        this.cuId = cuId;
    }

    public void setCuCodigo(String cuCodigo) {
        this.cuCodigo = cuCodigo;
    }

    public void setCuNombre(String cuNombre) {
        this.cuNombre = cuNombre;
    }

    public void setCuDescripcion(String cuDescripcion) {
        this.cuDescripcion = cuDescripcion;
    }

    public void setCuHabilitado(Boolean cuHabilitado) {
        this.cuHabilitado = cuHabilitado;
    }

    public void setCuOrden(Integer cuOrden) {
        this.cuOrden = cuOrden;
    }

    public void setCuCuentaPadre(SsCuenta cuCuentaPadre) {
        this.cuCuentaPadre = cuCuentaPadre;
    }

    public void setCuVersion(Integer cuVersion) {
        this.cuVersion = cuVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuId != null ? cuId.hashCode() : 0);
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
        final SsCuenta other = (SsCuenta) obj;
        if (!Objects.equals(this.cuId, other.cuId)) {
            return false;
        }
        return true;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SsCuenta[ cuId=" + cuId + " ]";
    }

}  // </editor-fold>
