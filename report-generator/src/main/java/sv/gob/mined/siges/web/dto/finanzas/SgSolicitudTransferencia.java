/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.finanzas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudesTransferencia;

 
public class SgSolicitudTransferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long strPk;

    private Boolean strHabilitado;

    private LocalDateTime strUltModFecha;

    private String strUltModUsuario;

    private Integer strVersion;
    
    private String strSacGOES;
    
    private String strSacUFI;
    
    private String strCompromisoPresupuestario;
    
    private EnumEstadoSolicitudesTransferencia strEstado;
    
    private BigDecimal strImporteTotal;
    
    private SgTransferenciaDireccionDep tacTransferenciaDireccionDepFk;

    public SgSolicitudTransferencia() {
    }

    public SgSolicitudTransferencia(Long strPk) {
        this.strPk = strPk;
    }

     
    public Long getStrPk() {
        return strPk;
    }

    public void setStrPk(Long strPk) {
        this.strPk = strPk;
    }

    public Boolean getStrHabilitado() {
        return strHabilitado;
    }

    public void setStrHabilitado(Boolean strHabilitado) {
        this.strHabilitado = strHabilitado;
    }

    public LocalDateTime getStrUltModFecha() {
        return strUltModFecha;
    }

    public void setStrUltModFecha(LocalDateTime strUltModFecha) {
        this.strUltModFecha = strUltModFecha;
    }

    public String getStrUltModUsuario() {
        return strUltModUsuario;
    }

    public void setStrUltModUsuario(String strUltModUsuario) {
        this.strUltModUsuario = strUltModUsuario;
    }

    public Integer getStrVersion() {
        return strVersion;
    }

    public void setStrVersion(Integer strVersion) {
        this.strVersion = strVersion;
    }

    public String getStrSacGOES() {
        return strSacGOES;
    }

    public void setStrSacGOES(String strSacGOES) {
        this.strSacGOES = strSacGOES;
    }

    public String getStrSacUFI() {
        return strSacUFI;
    }

    public void setStrSacUFI(String strSacUFI) {
        this.strSacUFI = strSacUFI;
    }

    public String getStrCompromisoPresupuestario() {
        return strCompromisoPresupuestario;
    }

    public void setStrCompromisoPresupuestario(String strCompromisoPresupuestario) {
        this.strCompromisoPresupuestario = strCompromisoPresupuestario;
    }

    public EnumEstadoSolicitudesTransferencia getStrEstado() {
        return strEstado;
    }

    public void setStrEstado(EnumEstadoSolicitudesTransferencia strEstado) {
        this.strEstado = strEstado;
    }

    public BigDecimal getStrImporteTotal() {
        return strImporteTotal;
    }

    public void setStrImporteTotal(BigDecimal strImporteTotal) {
        this.strImporteTotal = strImporteTotal;
    }

    public SgTransferenciaDireccionDep getTacTransferenciaDireccionDepFk() {
        return tacTransferenciaDireccionDepFk;
    }

    public void setTacTransferenciaDireccionDepFk(SgTransferenciaDireccionDep tacTransferenciaDireccionDepFk) {
        this.tacTransferenciaDireccionDepFk = tacTransferenciaDireccionDepFk;
    }

    

     
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this. strPk);
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
        final SgSolicitudTransferencia other = (SgSolicitudTransferencia) obj;
        if (!Objects.equals(this.strPk, other.strPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudesTransferencia[ bncPk=" + strPk + " ]";
    }

}
