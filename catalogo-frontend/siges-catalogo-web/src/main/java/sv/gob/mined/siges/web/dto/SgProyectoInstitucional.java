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
import java.util.stream.Collectors;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pinPk", scope = SgProyectoInstitucional.class)
public class SgProyectoInstitucional implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long pinPk;
    
    private String pinCodigo;
    
    private Boolean pinHabilitado;
    
    private String pinNombre;
    
    private String pinNombreBusqueda;
    
    private String pinDescripcion;
    
    private LocalDateTime pinUltModFecha;
    
    private String pinUltModUsuario;
    
    private Integer pinVersion;
    
    private List<SgAsociacion> pinAsociaciones;

    private SgProgramaInstitucional pinProgramaInstitucional;
    
    private LocalDate pinFechaInicio;
    
    private LocalDate pinFechaFin;
    
    private List<SgComponente> pinComponentes; 
    
    private List<SgBeneficio> pinBeneficios; 
    
    private Integer pinAnio;

    private String pinOrigenTransferencia;
    
    private String pinConvenio;
    
    private String pinCondicionesEntrega;

    private BigDecimal pinMonto;
    
    public SgProyectoInstitucional() {
        this.pinHabilitado = Boolean.TRUE;
        pinAsociaciones=new ArrayList();
    }
    
    @JsonIgnore
    public String getPinTiposBeneficiosString(){
        if (pinBeneficios != null){
            return pinBeneficios.stream().map(b -> b.getBnfTipoBeneficio().getText()).collect(Collectors.joining(", "));
        }
        return null;
    }
    
    @JsonIgnore
    public String getPinBeneficiosString(){
        if (pinBeneficios != null){
            return pinBeneficios.stream().map(b -> b.getBnfNombre()).collect(Collectors.joining(", "));
        }
        return null;
    }

    public Long getPinPk() {
        return pinPk;
    }

    public void setPinPk(Long pinPk) {
        this.pinPk = pinPk;
    }

    public String getPinCodigo() {
        return pinCodigo;
    }

    public void setPinCodigo(String pinCodigo) {
        this.pinCodigo = pinCodigo;
    }

    public Boolean getPinHabilitado() {
        return pinHabilitado;
    }

    public void setPinHabilitado(Boolean pinHabilitado) {
        this.pinHabilitado = pinHabilitado;
    }

    public String getPinNombre() {
        return pinNombre;
    }

    public void setPinNombre(String pinNombre) {
        this.pinNombre = pinNombre;
    }

    public String getPinNombreBusqueda() {
        return pinNombreBusqueda;
    }

    public void setPinNombreBusqueda(String pinNombreBusqueda) {
        this.pinNombreBusqueda = pinNombreBusqueda;
    }

    public LocalDateTime getPinUltModFecha() {
        return pinUltModFecha;
    }

    public void setPinUltModFecha(LocalDateTime pinUltModFecha) {
        this.pinUltModFecha = pinUltModFecha;
    }

    public String getPinUltModUsuario() {
        return pinUltModUsuario;
    }

    public void setPinUltModUsuario(String pinUltModUsuario) {
        this.pinUltModUsuario = pinUltModUsuario;
    }

    public Integer getPinVersion() {
        return pinVersion;
    }

    public void setPinVersion(Integer pinVersion) {
        this.pinVersion = pinVersion;
    }

    public String getPinDescripcion() {
        return pinDescripcion;
    }

    public void setPinDescripcion(String pinDescripcion) {
        this.pinDescripcion = pinDescripcion;
    }

    public List<SgAsociacion> getPinAsociaciones() {
        return pinAsociaciones;
    }

    public void setPinAsociaciones(List<SgAsociacion> pinAsociaciones) {
        this.pinAsociaciones = pinAsociaciones;
    }

    public SgProgramaInstitucional getPinProgramaInstitucional() {
        return pinProgramaInstitucional;
    }

    public void setPinProgramaInstitucional(SgProgramaInstitucional pinProgramaInstitucional) {
        this.pinProgramaInstitucional = pinProgramaInstitucional;
    }

    public LocalDate getPinFechaInicio() {
        return pinFechaInicio;
    }

    public void setPinFechaInicio(LocalDate pinFechaInicio) {
        this.pinFechaInicio = pinFechaInicio;
    }

    public LocalDate getPinFechaFin() {
        return pinFechaFin;
    }

    public void setPinFechaFin(LocalDate pinFechaFin) {
        this.pinFechaFin = pinFechaFin;
    }

    public List<SgComponente> getPinComponentes() {
        return pinComponentes;
    }

    public void setPinComponentes(List<SgComponente> pinComponentes) {
        this.pinComponentes = pinComponentes;
    }

    public List<SgBeneficio> getPinBeneficios() {
        return pinBeneficios;
    }

    public void setPinBeneficios(List<SgBeneficio> pinBeneficios) {
        this.pinBeneficios = pinBeneficios;
    }

    public Integer getPinAnio() {
        return pinAnio;
    }

    public void setPinAnio(Integer pinAnio) {
        this.pinAnio = pinAnio;
    }

    public String getPinOrigenTransferencia() {
        return pinOrigenTransferencia;
    }

    public void setPinOrigenTransferencia(String pinOrigenTransferencia) {
        this.pinOrigenTransferencia = pinOrigenTransferencia;
    }

    public String getPinConvenio() {
        return pinConvenio;
    }

    public void setPinConvenio(String pinConvenio) {
        this.pinConvenio = pinConvenio;
    }

    public String getPinCondicionesEntrega() {
        return pinCondicionesEntrega;
    }

    public void setPinCondicionesEntrega(String pinCondicionesEntrega) {
        this.pinCondicionesEntrega = pinCondicionesEntrega;
    }

    public BigDecimal getPinMonto() {
        return pinMonto;
    }

    public void setPinMonto(BigDecimal pinMonto) {
        this.pinMonto = pinMonto;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pinPk != null ? pinPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgProyectoInstitucional)) {
            return false;
        }
        SgProyectoInstitucional other = (SgProyectoInstitucional) object;
        if ((this.pinPk == null && other.pinPk != null) || (this.pinPk != null && !this.pinPk.equals(other.pinPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgProyectoInstitucional[ pinPk=" + pinPk + " ]";
    }
    
}
