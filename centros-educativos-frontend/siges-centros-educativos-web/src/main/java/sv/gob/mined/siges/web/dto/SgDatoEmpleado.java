/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import sv.gob.mined.siges.web.dto.catalogo.SgAfp;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaEmpleado;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelEmpleado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoDatoEmpleado;
import sv.gob.mined.siges.web.enumerados.EnumTipoDatoEmpleadoGuardar;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "demPk", scope = SgDatoEmpleado.class)
public class SgDatoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long demPk;

    private SgPersonalSedeEducativa demPersonalSede;

    private String demCodigoEmpleado;

    private SgAfp demAfp;

    private EnumEstadoDatoEmpleado demEstado;

    private LocalDate demFechaIngresoGob;

    private LocalDate demFechaIngresoMined;

    private SgNivelEmpleado demNivelFk;

    private SgCategoriaEmpleado demCategoriaFk;

    private LocalDateTime demUltModFecha;

    private String demUltModUsuario;

    private Integer demVersion;

    private List<SgDatoContratacion> demDatoContratacion;

    private List<SgExperienciaLaboral> demExperienciaLaboral;
    
    private EnumTipoDatoEmpleadoGuardar demTipoDatoGuardar;
    
    private Boolean demEmpleadoMineducyt;    
    
    private LocalDate demFechaRegistro;
    
    private Boolean demPuedeAplicarPlaza;
    
    public SgDatoEmpleado() {
        this.demDatoContratacion = new ArrayList<>();
        this.demExperienciaLaboral = new ArrayList<>();
        this.demEmpleadoMineducyt = Boolean.FALSE;
    }

    public SgDatoEmpleado(Long demPk, Integer demVersion) {
        this.demPk = demPk;
        this.demVersion = demVersion;
    }
    
    
    @JsonIgnore
    public String getDemTiposNombramiento() {
        if (this.demDatoContratacion != null) {       
            return this.demDatoContratacion.stream()
                    .filter(d -> d.getDcoTipoNombramiento() !=null)
                    .map(d -> d.getDcoTipoNombramiento().getTnoNombre())
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        return null;
    }
    
    @JsonIgnore
    public String getDemTiposContratos() {
        if (this.demDatoContratacion != null) {       
            return this.demDatoContratacion.stream()
                    .filter(d -> d.getDcoTipoContrato() != null)
                    .map(d -> d.getDcoTipoContrato().getTcoNombre())
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        return null;
    }
    
    @JsonIgnore
    public String getDemInstitucionesPagan() {
        if (this.demDatoContratacion != null) {       
            return this.demDatoContratacion.stream()
                    .filter(d -> d.getDcoInstitucionPagaSalario()!= null)
                    .map(d -> d.getDcoInstitucionPagaSalario().getIpsNombre())
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        return null;
    }

    @JsonIgnore
    public String getDemSedesContratacion() {
        if (this.demDatoContratacion != null) {       
            return this.demDatoContratacion.stream()
                    .filter(d -> d.getDcoCentroEducativo()!=null)
                    .map(d -> d.getDcoCentroEducativo())
                    .collect(Collectors.toSet())
                    .stream()
                    .filter(d -> d.getSedDirNombre()!=null)
                    .map(d -> d.getSedCodigoNombre())
                    .collect(Collectors.joining(", "));
        }
        return null;
    }
    
    @JsonIgnore
    public String getDemSedesContratacionTooltip() {
        if (this.demDatoContratacion != null) {
            return this.demDatoContratacion.stream()
                    .filter(d -> d.getDcoCentroEducativo()!=null)
                    .map(d -> d.getDcoCentroEducativo())                    
                    .collect(Collectors.toSet())
                    .stream()
                    .filter(d -> d.getSedDirNombre()!=null)
                    .map(d -> d.getSedDirNombre())
                    .collect(Collectors.joining(", "));
        }
        return null;
    }

    public Long getDemPk() {
        return demPk;
    }

    public void setDemPk(Long demPk) {
        this.demPk = demPk;
    }

    public String getDemCodigoEmpleado() {
        return demCodigoEmpleado;
    }

    public void setDemCodigoEmpleado(String demCodigoEmpleado) {
        this.demCodigoEmpleado = demCodigoEmpleado;
    }

    public EnumEstadoDatoEmpleado getDemEstado() {
        return demEstado;
    }

    public void setDemEstado(EnumEstadoDatoEmpleado demEstado) {
        this.demEstado = demEstado;
    }

    public LocalDate getDemFechaIngresoGob() {
        return demFechaIngresoGob;
    }

    public void setDemFechaIngresoGob(LocalDate demFechaIngresoGob) {
        this.demFechaIngresoGob = demFechaIngresoGob;
    }

    public LocalDate getDemFechaIngresoMined() {
        return demFechaIngresoMined;
    }

    public void setDemFechaIngresoMined(LocalDate demFechaIngresoMined) {
        this.demFechaIngresoMined = demFechaIngresoMined;
    }

    public SgNivelEmpleado getDemNivelFk() {
        return demNivelFk;
    }

    public void setDemNivelFk(SgNivelEmpleado demNivelFk) {
        this.demNivelFk = demNivelFk;
    }

    public SgCategoriaEmpleado getDemCategoriaFk() {
        return demCategoriaFk;
    }

    public void setDemCategoriaFk(SgCategoriaEmpleado demCategoriaFk) {
        this.demCategoriaFk = demCategoriaFk;
    }

    public LocalDateTime getDemUltModFecha() {
        return demUltModFecha;
    }

    public void setDemUltModFecha(LocalDateTime demUltModFecha) {
        this.demUltModFecha = demUltModFecha;
    }

    public String getDemUltModUsuario() {
        return demUltModUsuario;
    }

    public void setDemUltModUsuario(String demUltModUsuario) {
        this.demUltModUsuario = demUltModUsuario;
    }

    public Integer getDemVersion() {
        return demVersion;
    }

    public void setDemVersion(Integer demVersion) {
        this.demVersion = demVersion;
    }

    public List<SgDatoContratacion> getDemDatoContratacion() {
        return demDatoContratacion;
    }

    public void setDemDatoContratacion(List<SgDatoContratacion> demDatoContratacion) {
        this.demDatoContratacion = demDatoContratacion;
    }

    public List<SgExperienciaLaboral> getDemExperienciaLaboral() {
        return demExperienciaLaboral;
    }

    public void setDemExperienciaLaboral(List<SgExperienciaLaboral> demExperienciaLaboral) {
        this.demExperienciaLaboral = demExperienciaLaboral;
    }

    public SgPersonalSedeEducativa getDemPersonalSede() {
        return demPersonalSede;
    }

    public void setDemPersonalSede(SgPersonalSedeEducativa demPersonalSede) {
        this.demPersonalSede = demPersonalSede;
    }

    public SgAfp getDemAfp() {
        return demAfp;
    }

    public void setDemAfp(SgAfp demAfp) {
        this.demAfp = demAfp;
    }

    public EnumTipoDatoEmpleadoGuardar getDemTipoDatoGuardar() {
        return demTipoDatoGuardar;
    }

    public void setDemTipoDatoGuardar(EnumTipoDatoEmpleadoGuardar demTipoDatoGuardar) {
        this.demTipoDatoGuardar = demTipoDatoGuardar;
    }

    public Boolean getDemEmpleadoMineducyt() {
        return demEmpleadoMineducyt;
    }

    public void setDemEmpleadoMineducyt(Boolean demEmpleadoMineducyt) {
        this.demEmpleadoMineducyt = demEmpleadoMineducyt;
    }

    public LocalDate getDemFechaRegistro() {
        return demFechaRegistro;
    }

    public void setDemFechaRegistro(LocalDate demFechaRegistro) {
        this.demFechaRegistro = demFechaRegistro;
    }

    public Boolean getDemPuedeAplicarPlaza() {
        return demPuedeAplicarPlaza;
    }

    public void setDemPuedeAplicarPlaza(Boolean demPuedeAplicarPlaza) {
        this.demPuedeAplicarPlaza = demPuedeAplicarPlaza;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (demPk != null ? demPk.hashCode() : 0);
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
        final SgDatoEmpleado other = (SgDatoEmpleado) obj;
        if (!Objects.equals(this.demPk, other.demPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleado[ demPk=" + demPk + " ]";
    }

}
