/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoDatoContratacion;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgInstitucionPagaSalario;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgLeySalario;
import sv.gob.mined.siges.web.dto.catalogo.SgModoPago;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAlfabetizador;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoContrato;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoInstitucionPaga;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoNombramiento;
import sv.gob.mined.siges.web.enumerados.EnumModeloContrato;
import sv.gob.mined.siges.web.enumerados.TipoPersonalSedeEducativa;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dcoPk", scope = SgDatoContratacion.class)
public class SgDatoContratacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dcoPk;

    private SgDatoEmpleado dcoDatoEmpleado;
    
    private TipoPersonalSedeEducativa dcoTipo;

    private SgLeySalario dcoLeySalario;

    private SgFuenteFinanciamiento dcoFuenteFinanciamiento;

    private SgInstitucionPagaSalario dcoInstitucionPagaSalario;

    private String dcoInstitucionPagaSalarioOtro;

    private SgTipoInstitucionPaga dcoTipoInstitucionPaga;

    private LocalTime dcoHoraIngreso;

    private LocalTime dcoHoraSalida;

    private Double dcoSalarioMensual;

    private SgTipoNombramiento dcoTipoNombramiento;

    private SgModoPago dcoModoPago;

    private SgSede dcoCentroEducativo;

    private LocalDate dcoDesde;

    private LocalDate dcoHasta;

    private SgCargo dcoCargo;

    private SgTipoContrato dcoTipoContrato;

    private LocalDateTime dcoUltModFecha;

    private String dcoUltModUsuario;

    private Integer dcoVersion;

    private List<SgJornadaLaboral> dcoJornadasLaborales;
    
    private String dcoRazonesInterinato;
    
    private EnumModeloContrato dcoModeloContrato;

    private String dcoCodigoPlaza;
    
    private Boolean dcoActividadesDocentes;
    
    private SgEstadoDatoContratacion dcoEstado;

    private SgTipoAlfabetizador dcoTipoAlfabetizador;

    
    public SgDatoContratacion() {
        this.dcoActividadesDocentes = Boolean.FALSE;
    }

    public SgDatoContratacion(Long dcoPk) {
        this.dcoPk = dcoPk;
    }
    
    public String getDcoJornadasString(){
        if (this.dcoJornadasLaborales != null){
            return this.dcoJornadasLaborales.stream().map(j -> j.getJlaNombre()).collect(Collectors.joining(", "));
        }
        return null;
    }

    public Long getDcoPk() {
        return dcoPk;
    }

    public void setDcoPk(Long dcoPk) {
        this.dcoPk = dcoPk;
    }

    public LocalDate getDcoDesde() {
        return dcoDesde;
    }

    public void setDcoDesde(LocalDate dcoDesde) {
        this.dcoDesde = dcoDesde;
    }

    public LocalDate getDcoHasta() {
        return dcoHasta;
    }

    public void setDcoHasta(LocalDate dcoHasta) {
        this.dcoHasta = dcoHasta;
    }

    public SgSede getDcoCentroEducativo() {
        return dcoCentroEducativo;
    }

    public void setDcoCentroEducativo(SgSede dcoCentroEducativo) {
        this.dcoCentroEducativo = dcoCentroEducativo;
    }

    public SgCargo getDcoCargo() {
        return dcoCargo;
    }

    public void setDcoCargo(SgCargo dcoCargo) {
        this.dcoCargo = dcoCargo;
    }

    public SgTipoContrato getDcoTipoContrato() {
        return dcoTipoContrato;
    }

    public void setDcoTipoContrato(SgTipoContrato dcoTipoContrato) {
        this.dcoTipoContrato = dcoTipoContrato;
    }

    public LocalDateTime getDcoUltModFecha() {
        return dcoUltModFecha;
    }

    public void setDcoUltModFecha(LocalDateTime dcoUltModFecha) {
        this.dcoUltModFecha = dcoUltModFecha;
    }

    public String getDcoUltModUsuario() {
        return dcoUltModUsuario;
    }

    public void setDcoUltModUsuario(String dcoUltModUsuario) {
        this.dcoUltModUsuario = dcoUltModUsuario;
    }

    public Integer getDcoVersion() {
        return dcoVersion;
    }

    public void setDcoVersion(Integer dcoVersion) {
        this.dcoVersion = dcoVersion;
    }

    public SgDatoEmpleado getDcoDatoEmpleado() {
        return dcoDatoEmpleado;
    }

    public void setDcoDatoEmpleado(SgDatoEmpleado dcoDatoEmpleado) {
        this.dcoDatoEmpleado = dcoDatoEmpleado;
    }

    public SgLeySalario getDcoLeySalario() {
        return dcoLeySalario;
    }

    public void setDcoLeySalario(SgLeySalario dcoLeySalario) {
        this.dcoLeySalario = dcoLeySalario;
    }

    public SgFuenteFinanciamiento getDcoFuenteFinanciamiento() {
        return dcoFuenteFinanciamiento;
    }

    public void setDcoFuenteFinanciamiento(SgFuenteFinanciamiento dcoFuenteFinanciamiento) {
        this.dcoFuenteFinanciamiento = dcoFuenteFinanciamiento;
    }

    public SgInstitucionPagaSalario getDcoInstitucionPagaSalario() {
        return dcoInstitucionPagaSalario;
    }

    public void setDcoInstitucionPagaSalario(SgInstitucionPagaSalario dcoInstitucionPagaSalario) {
        this.dcoInstitucionPagaSalario = dcoInstitucionPagaSalario;
    }

    public String getDcoInstitucionPagaSalarioOtro() {
        return dcoInstitucionPagaSalarioOtro;
    }

    public void setDcoInstitucionPagaSalarioOtro(String dcoInstitucionPagaSalarioOtro) {
        this.dcoInstitucionPagaSalarioOtro = dcoInstitucionPagaSalarioOtro;
    }

    public SgTipoInstitucionPaga getDcoTipoInstitucionPaga() {
        return dcoTipoInstitucionPaga;
    }

    public void setDcoTipoInstitucionPaga(SgTipoInstitucionPaga dcoTipoInstitucionPaga) {
        this.dcoTipoInstitucionPaga = dcoTipoInstitucionPaga;
    }

    public LocalTime getDcoHoraIngreso() {
        return dcoHoraIngreso;
    }

    public void setDcoHoraIngreso(LocalTime dcoHoraIngreso) {
        this.dcoHoraIngreso = dcoHoraIngreso;
    }

    public LocalTime getDcoHoraSalida() {
        return dcoHoraSalida;
    }

    public void setDcoHoraSalida(LocalTime dcoHoraSalida) {
        this.dcoHoraSalida = dcoHoraSalida;
    }

    public Double getDcoSalarioMensual() {
        return dcoSalarioMensual;
    }

    public void setDcoSalarioMensual(Double dcoSalarioMensual) {
        this.dcoSalarioMensual = dcoSalarioMensual;
    }

    public SgTipoNombramiento getDcoTipoNombramiento() {
        return dcoTipoNombramiento;
    }

    public void setDcoTipoNombramiento(SgTipoNombramiento dcoTipoNombramiento) {
        this.dcoTipoNombramiento = dcoTipoNombramiento;
    }

    public SgModoPago getDcoModoPago() {
        return dcoModoPago;
    }

    public void setDcoModoPago(SgModoPago dcoModoPago) {
        this.dcoModoPago = dcoModoPago;
    }

    public List<SgJornadaLaboral> getDcoJornadasLaborales() {
        return dcoJornadasLaborales;
    }

    public void setDcoJornadasLaborales(List<SgJornadaLaboral> dcoJornadasLaborales) {
        this.dcoJornadasLaborales = dcoJornadasLaborales;
    }

    public String getDcoRazonesInterinato() {
        return dcoRazonesInterinato;
    }

    public void setDcoRazonesInterinato(String dcoRazonesInterinato) {
        this.dcoRazonesInterinato = dcoRazonesInterinato;
    }

    public EnumModeloContrato getDcoModeloContrato() {
        return dcoModeloContrato;
    }

    public void setDcoModeloContrato(EnumModeloContrato dcoModeloContrato) {
        this.dcoModeloContrato = dcoModeloContrato;
    }

    public String getDcoCodigoPlaza() {
        return dcoCodigoPlaza;
    }

    public void setDcoCodigoPlaza(String dcoCodigoPlaza) {
        this.dcoCodigoPlaza = dcoCodigoPlaza;
    }

    public Boolean getDcoActividadesDocentes() {
        return dcoActividadesDocentes;
    }

    public void setDcoActividadesDocentes(Boolean dcoActividadesDocentes) {
        this.dcoActividadesDocentes = dcoActividadesDocentes;
    }


    public SgEstadoDatoContratacion getDcoEstado() {
        return dcoEstado;
    }

    public void setDcoEstado(SgEstadoDatoContratacion dcoEstado) {
        this.dcoEstado = dcoEstado;
    }

    public SgTipoAlfabetizador getDcoTipoAlfabetizador() {
        return dcoTipoAlfabetizador;
    }

    public void setDcoTipoAlfabetizador(SgTipoAlfabetizador dcoTipoAlfabetizador) {
        this.dcoTipoAlfabetizador = dcoTipoAlfabetizador;

    }

    public TipoPersonalSedeEducativa getDcoTipo() {
        return dcoTipo;
    }

    public void setDcoTipo(TipoPersonalSedeEducativa dcoTipo) {
        this.dcoTipo = dcoTipo;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dcoPk != null ? dcoPk.hashCode() : 0);
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
        final SgDatoContratacion other = (SgDatoContratacion) obj;
        if (!Objects.equals(this.dcoPk, other.dcoPk)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion[ dcoPk=" + dcoPk + " ]";
    }

}
