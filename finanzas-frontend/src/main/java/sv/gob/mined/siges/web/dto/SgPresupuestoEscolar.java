/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pesPk", scope = SgPresupuestoEscolar.class)
public class SgPresupuestoEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pesPk;

    private String pesNombre;

    private String pesNombreBusqueda;

    private String pesCodigo;

    private String pesDescripcion;

    private Boolean pesHabilitado;

    private LocalDateTime pesUltmodFecha;

    private String pesUltmodUsuario;

    private Integer pesVersion;

    private SgSede pesSedeFk;

    private SgAnioFiscal pesAnioFiscalFk;

    private List<SgMovimientos> pesMovimientos;

    private EnumPresupuestoEscolarEstado pesEstado;

    private List<SgDocumentos> pesDocumentos;

    private Boolean pesEdicion;

    private String pesObservacion;

    public SgPresupuestoEscolar() {
    }

    public Long getPesPk() {
        return pesPk;
    }

    public void setPesPk(Long pesPk) {
        this.pesPk = pesPk;
    }

    public String getPesNombre() {
        return pesNombre;
    }

    public void setPesNombre(String pesNombre) {
        this.pesNombre = pesNombre;
    }

    public String getPesNombreBusqueda() {
        return pesNombreBusqueda;
    }

    public void setPesNombreBusqueda(String pesNombreBusqueda) {
        this.pesNombreBusqueda = pesNombreBusqueda;
    }

    public String getPesCodigo() {
        return pesCodigo;
    }

    public void setPesCodigo(String pesCodigo) {
        this.pesCodigo = pesCodigo;
    }

    public String getPesDescripcion() {
        return pesDescripcion;
    }

    public void setPesDescripcion(String pesDescripcion) {
        this.pesDescripcion = pesDescripcion;
    }

    public Boolean getPesHabilitado() {
        return pesHabilitado;
    }

    public void setPesHabilitado(Boolean pesHabilitado) {
        this.pesHabilitado = pesHabilitado;
    }

    public LocalDateTime getPesUltmodFecha() {
        return pesUltmodFecha;
    }

    public void setPesUltmodFecha(LocalDateTime pesUltmodFecha) {
        this.pesUltmodFecha = pesUltmodFecha;
    }

    public String getPesUltmodUsuario() {
        return pesUltmodUsuario;
    }

    public void setPesUltmodUsuario(String pesUltmodUsuario) {
        this.pesUltmodUsuario = pesUltmodUsuario;
    }

    public Integer getPesVersion() {
        return pesVersion;
    }

    public void setPesVersion(Integer pesVersion) {
        this.pesVersion = pesVersion;
    }

    public SgSede getPesSedeFk() {
        return pesSedeFk;
    }

    public void setPesSedeFk(SgSede pesSedeFk) {
        this.pesSedeFk = pesSedeFk;
    }

    public SgAnioFiscal getPesAnioFiscalFk() {
        return pesAnioFiscalFk;
    }

    public void setPesAnioFiscalFk(SgAnioFiscal pesAnioFiscalFk) {
        this.pesAnioFiscalFk = pesAnioFiscalFk;
    }

    public List<SgMovimientos> getPesMovimientos() {
        return pesMovimientos;
    }

    public void setPesMovimientos(List<SgMovimientos> pesMovimientos) {
        this.pesMovimientos = pesMovimientos;
    }

    public EnumPresupuestoEscolarEstado getPesEstado() {
        return pesEstado;
    }

    public void setPesEstado(EnumPresupuestoEscolarEstado pesEstado) {
        this.pesEstado = pesEstado;
    }

    public List<SgDocumentos> getPesDocumentos() {
        return pesDocumentos;
    }

    public void setPesDocumentos(List<SgDocumentos> pesDocumentos) {
        this.pesDocumentos = pesDocumentos;
    }

    public Boolean getPesEdicion() {
        return pesEdicion;
    }

    public void setPesEdicion(Boolean pesEdicion) {
        this.pesEdicion = pesEdicion;
    }

    public String getPesObservacion() {
        return StringUtils.isNotBlank(pesObservacion) ? Jsoup.parse(pesObservacion).text() : "";
    }

    public void setPesObservacion(String pesObservacion) {
        this.pesObservacion = pesObservacion;
    }

    @JsonIgnore
    public String getPesComboLabel() {
        return pesPk.toString().concat(" - ").concat(pesNombre);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pesPk != null ? pesPk.hashCode() : 0);
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
        final SgPresupuestoEscolar other = (SgPresupuestoEscolar) obj;
        if (!Objects.equals(this.pesPk, other.pesPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgPresupuestoEscolar[ pesPk=" + pesPk + " ]";
    }

}
