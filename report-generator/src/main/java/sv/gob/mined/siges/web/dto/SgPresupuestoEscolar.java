/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
