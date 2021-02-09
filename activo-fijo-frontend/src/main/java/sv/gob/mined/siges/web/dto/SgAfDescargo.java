/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfOpcionesDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "desPk", scope = SgAfDescargo.class)
public class SgAfDescargo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long desPk;
    private String desCodigoDescargo;
    private String desNombreCoordAdm;
    private String desNombreEncargadoAf;
    private String desNombreDirector;
    private String desNombreAutoriza;
    private String desCargoAutoriza;
    private String desObservacionFallo;
    private Boolean desActivo;
    private LocalDateTime desFechaCreacion;
    private LocalDate desFechaSolicitud;
    private LocalDate desFechaDescargo;
    private LocalDateTime desFechaEnvioSolicitud;
    private LocalDate desFechaFallo;
    private SgAfEstadosDescargo desTipoDescargoFk;
    private SgAfOpcionesDescargo desOpcionDescargoFk;
    private SgSede desSedeFk;
    private SgAfUnidadesAdministrativas desUnidadAdministrativaFk;
    private SgAfEstadosBienes desEstadoFk;
    private String desUsuarioCreacion;
    private LocalDateTime desUltModFecha;
    private String desUltModUsuario;
    private Integer desVersion;
    private List<SgAfDescargoDetalle> sgAfDescargosDetalle;
    private List<SgAfMotivoRechazoDescargo> sgAfMotivosRechazoDescargo;
    private SgAfActaDescargo desActaDescargo;
    private Integer cantidad;
    private BigDecimal totalMonto;
     
    public SgAfDescargo() {
        sgAfDescargosDetalle = new ArrayList();
        sgAfMotivosRechazoDescargo = new ArrayList();
        desUnidadAdministrativaFk = new SgAfUnidadesAdministrativas();
        desActaDescargo = new SgAfActaDescargo();
    }

    @JsonIgnore
    public String getDesNombreUnidadAdministrativa() {
        if(desUnidadAdministrativaFk != null) {
            return desUnidadAdministrativaFk.getUadNombre();
        } else if(desSedeFk != null) {
            return  desSedeFk.getSedNombre();
        }
        return "";
    }
    
    @JsonIgnore
    public Integer getDesNumeroBienesDescargar() {
       return this.getSgAfDescargosDetalle() != null ? this.getSgAfDescargosDetalle().size() : 0;
    }
    
    @JsonIgnore
    public BigDecimal getDesTotalCostoDescargar() {
        BigDecimal costoTotal = new BigDecimal(0);
        if(sgAfDescargosDetalle != null && !sgAfDescargosDetalle.isEmpty()) {
            for(SgAfDescargoDetalle descargoDetalle : sgAfDescargosDetalle) {
                if(descargoDetalle.getDdeBienesDepreciablesFk() != null && descargoDetalle.getDdeBienesDepreciablesFk().getBdeValorAdquisicion() != null) {
                   costoTotal = costoTotal.add(descargoDetalle.getDdeBienesDepreciablesFk().getBdeValorAdquisicion());
                }
            }
        }
        return costoTotal;
    }
    
    public Long getDesPk() {
        return desPk;
    }

    public void setDesPk(Long desPk) {
        this.desPk = desPk;
    }

    public String getDesCodigoDescargo() {
        return desCodigoDescargo;
    }

    public void setDesCodigoDescargo(String desCodigoDescargo) {
        this.desCodigoDescargo = desCodigoDescargo;
    }

    public String getDesNombreCoordAdm() {
        return desNombreCoordAdm;
    }

    public void setDesNombreCoordAdm(String desNombreCoordAdm) {
        this.desNombreCoordAdm = desNombreCoordAdm;
    }

    public String getDesNombreEncargadoAf() {
        return desNombreEncargadoAf;
    }

    public void setDesNombreEncargadoAf(String desNombreEncargadoAf) {
        this.desNombreEncargadoAf = desNombreEncargadoAf;
    }

    public String getDesNombreDirector() {
        return desNombreDirector;
    }

    public void setDesNombreDirector(String desNombreDirector) {
        this.desNombreDirector = desNombreDirector;
    }

    public String getDesNombreAutoriza() {
        return desNombreAutoriza;
    }

    public void setDesNombreAutoriza(String desNombreAutoriza) {
        this.desNombreAutoriza = desNombreAutoriza;
    }

    public String getDesCargoAutoriza() {
        return desCargoAutoriza;
    }

    public void setDesCargoAutoriza(String desCargoAutoriza) {
        this.desCargoAutoriza = desCargoAutoriza;
    }

    public String getDesObservacionFallo() {
        return desObservacionFallo;
    }

    public void setDesObservacionFallo(String desObservacionFallo) {
        this.desObservacionFallo = desObservacionFallo;
    }

    public LocalDateTime getDesFechaCreacion() {
        return desFechaCreacion;
    }

    public void setDesFechaCreacion(LocalDateTime desFechaCreacion) {
        this.desFechaCreacion = desFechaCreacion;
    }

    public LocalDate getDesFechaSolicitud() {
        return desFechaSolicitud;
    }

    public void setDesFechaSolicitud(LocalDate desFechaSolicitud) {
        this.desFechaSolicitud = desFechaSolicitud;
    }

    public LocalDate getDesFechaDescargo() {
        return desFechaDescargo;
    }

    public void setDesFechaDescargo(LocalDate desFechaDescargo) {
        this.desFechaDescargo = desFechaDescargo;
    }

    public LocalDate getDesFechaFallo() {
        return desFechaFallo;
    }

    public void setDesFechaFallo(LocalDate desFechaFallo) {
        this.desFechaFallo = desFechaFallo;
    }

    public SgAfEstadosDescargo getDesTipoDescargoFk() {
        return desTipoDescargoFk;
    }

    public void setDesTipoDescargoFk(SgAfEstadosDescargo desTipoDescargoFk) {
        this.desTipoDescargoFk = desTipoDescargoFk;
    }

    public SgSede getDesSedeFk() {
        return desSedeFk;
    }

    public void setDesSedeFk(SgSede desSedeFk) {
        this.desSedeFk = desSedeFk;
    }

    public SgAfUnidadesAdministrativas getDesUnidadAdministrativaFk() {
        return desUnidadAdministrativaFk;
    }

    public void setDesUnidadAdministrativaFk(SgAfUnidadesAdministrativas desUnidadAdministrativaFk) {
        this.desUnidadAdministrativaFk = desUnidadAdministrativaFk;
    }

    public SgAfEstadosBienes getDesEstadoFk() {
        return desEstadoFk;
    }

    public void setDesEstadoFk(SgAfEstadosBienes desEstadoFk) {
        this.desEstadoFk = desEstadoFk;
    }

    public String getDesUsuarioCreacion() {
        return desUsuarioCreacion;
    }

    public void setDesUsuarioCreacion(String desUsuarioCreacion) {
        this.desUsuarioCreacion = desUsuarioCreacion;
    }

    public LocalDateTime getDesUltModFecha() {
        return desUltModFecha;
    }

    public void setDesUltModFecha(LocalDateTime desUltModFecha) {
        this.desUltModFecha = desUltModFecha;
    }

    public String getDesUltModUsuario() {
        return desUltModUsuario;
    }

    public void setDesUltModUsuario(String desUltModUsuario) {
        this.desUltModUsuario = desUltModUsuario;
    }

    public Integer getDesVersion() {
        return desVersion;
    }

    public void setDesVersion(Integer desVersion) {
        this.desVersion = desVersion;
    }

    public List<SgAfDescargoDetalle> getSgAfDescargosDetalle() {
        return sgAfDescargosDetalle;
    }

    public void setSgAfDescargosDetalle(List<SgAfDescargoDetalle> sgAfDescargosDetalle) {
        this.sgAfDescargosDetalle = sgAfDescargosDetalle;
    }

    public Boolean getDesActivo() {
        return desActivo;
    }

    public void setDesActivo(Boolean desActivo) {
        this.desActivo = desActivo;
    }

    public LocalDateTime getDesFechaEnvioSolicitud() {
        return desFechaEnvioSolicitud;
    }

    public void setDesFechaEnvioSolicitud(LocalDateTime desFechaEnvioSolicitud) {
        this.desFechaEnvioSolicitud = desFechaEnvioSolicitud;
    }

    public List<SgAfMotivoRechazoDescargo> getSgAfMotivosRechazoDescargo() {
        return sgAfMotivosRechazoDescargo;
    }

    public void setSgAfMotivosRechazoDescargo(List<SgAfMotivoRechazoDescargo> sgAfMotivosRechazoDescargo) {
        this.sgAfMotivosRechazoDescargo = sgAfMotivosRechazoDescargo;
    }

    public SgAfOpcionesDescargo getDesOpcionDescargoFk() {
        return desOpcionDescargoFk;
    }

    public void setDesOpcionDescargoFk(SgAfOpcionesDescargo desOpcionDescargoFk) {
        this.desOpcionDescargoFk = desOpcionDescargoFk;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotalMonto() {
        return totalMonto;
    }

    public void setTotalMonto(BigDecimal totalMonto) {
        this.totalMonto = totalMonto;
    }

    public SgAfActaDescargo getDesActaDescargo() {
        return desActaDescargo;
    }

    public void setDesActaDescargo(SgAfActaDescargo desActaDescargo) {
        this.desActaDescargo = desActaDescargo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (desPk != null ? desPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfDescargo)) {
            return false;
        }
        SgAfDescargo other = (SgAfDescargo) object;
        if ((this.desPk == null && other.desPk != null) || (this.desPk != null && !this.desPk.equals(other.desPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfDescargos[ desPk=" + desPk + " ]";
    }
    
}
