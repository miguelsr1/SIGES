/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ipaPk", scope = SgIpsAcceso.class)
public class SgIpsAcceso implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ipaPk;
    private String ipaCodigo;
    private Boolean ipaHabilitado;
    private String ipaNombre;
    private String ipaNombreBusqueda;
    private String ipaDescripcion;
    private String ipaIpAcceso;
    private LocalDateTime ipaUltModFecha;
    private String ipaUltModUsuario;
    private Integer ipaVersion;

    public SgIpsAcceso() {
        this.ipaHabilitado = Boolean.TRUE;
    }

    public SgIpsAcceso(Long ipaPk) {
        this.ipaPk = ipaPk;
    }

    public Long getIpaPk() {
        return ipaPk;
    }

    public void setIpaPk(Long ipaPk) {
        this.ipaPk = ipaPk;
    }

    public String getIpaCodigo() {
        return ipaCodigo;
    }

    public void setIpaCodigo(String ipaCodigo) {
        this.ipaCodigo = ipaCodigo;
    }

    public Boolean getIpaHabilitado() {
        return ipaHabilitado;
    }

    public void setIpaHabilitado(Boolean ipaHabilitado) {
        this.ipaHabilitado = ipaHabilitado;
    }

    public String getIpaNombre() {
        return ipaNombre;
    }

    public void setIpaNombre(String ipaNombre) {
        this.ipaNombre = ipaNombre;
    }

    public String getIpaNombreBusqueda() {
        return ipaNombreBusqueda;
    }

    public void setIpaNombreBusqueda(String ipaNombreBusqueda) {
        this.ipaNombreBusqueda = ipaNombreBusqueda;
    }

    public String getIpaDescripcion() {
        return ipaDescripcion;
    }

    public void setIpaDescripcion(String ipaDescripcion) {
        this.ipaDescripcion = ipaDescripcion;
    }

    public LocalDateTime getIpaUltModFecha() {
        return ipaUltModFecha;
    }

    public void setIpaUltModFecha(LocalDateTime ipaUltModFecha) {
        this.ipaUltModFecha = ipaUltModFecha;
    }

    public String getIpaUltModUsuario() {
        return ipaUltModUsuario;
    }

    public void setIpaUltModUsuario(String ipaUltModUsuario) {
        this.ipaUltModUsuario = ipaUltModUsuario;
    }

    public Integer getIpaVersion() {
        return ipaVersion;
    }

    public void setIpaVersion(Integer ipaVersion) {
        this.ipaVersion = ipaVersion;
    }

    public String getIpaIpAcceso() {
        return ipaIpAcceso;
    }

    public void setIpaIpAcceso(String ipaIpAcceso) {
        this.ipaIpAcceso = ipaIpAcceso;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.ipaPk);
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
        final SgIpsAcceso other = (SgIpsAcceso) obj;
        if (!Objects.equals(this.ipaPk, other.ipaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgIpsAcceso{" + "ipaPk=" + ipaPk + '}';
    }
}
