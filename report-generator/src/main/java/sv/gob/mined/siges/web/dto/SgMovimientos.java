/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.siap2.SgAreaInversion;
import sv.gob.mined.siges.web.dto.siap2.SsTopePresupuestal;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "movPk", scope = SgMovimientos.class)
public class SgMovimientos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long movPk;

    private String movCodigo;

    private SgPresupuestoEscolar movPresupuestoFk;

    private SsTopePresupuestal movTechoPresupuestal;

    private String movFuenteRecursos;

    private EnumMovimientosTipo movTipo;

    private Double movMonto;

    private LocalDateTime movUltmodFecha;

    private String movUltmodUsuario;

    private Integer movVersion;

    private Integer movNumMoviento;

    private EnumMovimientosOrigen movOrigen;

    private SgDetallePlanEscolar movActividadPk;

    private SgMovimientos movFuenteIngresos;

    private SgAreaInversion movAreaInversionPk;

    private SgRubros movRubroPk;

    private BigDecimal movMontoAprobado;

    public Long getMovPk() {
        return movPk;
    }

    public void setMovPk(Long movPk) {
        this.movPk = movPk;
    }

    public String getMovCodigo() {
        return movCodigo;
    }

    public void setMovCodigo(String movCodigo) {
        this.movCodigo = movCodigo;
    }

    public String getMovFuenteRecursos() {
        return movFuenteRecursos;
    }

    public void setMovFuenteRecursos(String movFuenteRecursos) {
        this.movFuenteRecursos = movFuenteRecursos;
    }

    public SgPresupuestoEscolar getMovPresupuestoFk() {
        return movPresupuestoFk;
    }

    public void setMovPresupuestoFk(SgPresupuestoEscolar movPresupuestoFk) {
        this.movPresupuestoFk = movPresupuestoFk;
    }

    public EnumMovimientosTipo getMovTipo() {
        return movTipo;
    }

    public void setMovTipo(EnumMovimientosTipo movTipo) {
        this.movTipo = movTipo;
    }

    public Double getMovMonto() {
        return movMonto;
    }

    public void setMovMonto(Double movMonto) {
        this.movMonto = movMonto;
    }

    public LocalDateTime getMovUltmodFecha() {
        return movUltmodFecha;
    }

    public void setMovUltmodFecha(LocalDateTime movUltmodFecha) {
        this.movUltmodFecha = movUltmodFecha;
    }

    public String getMovUltmodUsuario() {
        return movUltmodUsuario;
    }

    public void setMovUltmodUsuario(String movUltmodUsuario) {
        this.movUltmodUsuario = movUltmodUsuario;
    }

    public Integer getMovVersion() {
        return movVersion;
    }

    public void setMovVersion(Integer movVersion) {
        this.movVersion = movVersion;
    }

    public EnumMovimientosOrigen getMovOrigen() {
        return movOrigen;
    }

    public void setMovOrigen(EnumMovimientosOrigen movOrigen) {
        this.movOrigen = movOrigen;
    }

    public SgDetallePlanEscolar getMovActividadPk() {
        return movActividadPk;
    }

    public void setMovActividadPk(SgDetallePlanEscolar movActividadPk) {
        this.movActividadPk = movActividadPk;
    }

    public Integer getMovNumMoviento() {
        return movNumMoviento;
    }

    public void setMovNumMoviento(Integer movNumMoviento) {
        this.movNumMoviento = movNumMoviento;
    }

    public SgMovimientos getMovFuenteIngresos() {
        return movFuenteIngresos;
    }

    public void setMovFuenteIngresos(SgMovimientos movFuenteIngresos) {
        this.movFuenteIngresos = movFuenteIngresos;
    }

    public boolean isIngreso() {
        return EnumMovimientosTipo.I.equals(movTipo);
    }

    public boolean isEgreso() {
        return EnumMovimientosTipo.E.equals(movTipo);
    }

    public SgRubros getMovRubroPk() {
        return movRubroPk;
    }

    public void setMovRubroPk(SgRubros movRubroPk) {
        this.movRubroPk = movRubroPk;
    }

    public BigDecimal getMovMontoAprobado() {
        return movMontoAprobado;
    }

    public void setMovMontoAprobado(BigDecimal movMontoAprobado) {
        this.movMontoAprobado = movMontoAprobado;
    }

    public SgAreaInversion getMovAreaInversionPk() {
        return movAreaInversionPk;
    }

    public void setMovAreaInversionPk(SgAreaInversion movAreaInversionPk) {
        this.movAreaInversionPk = movAreaInversionPk;
    }

    public SsTopePresupuestal getMovTechoPresupuestal() {
        return movTechoPresupuestal;
    }

    public void setMovTechoPresupuestal(SsTopePresupuestal movTechoPresupuestal) {
        this.movTechoPresupuestal = movTechoPresupuestal;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.movPk);
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
        final SgMovimientos other = (SgMovimientos) obj;
        if (!Objects.equals(this.movPk, other.movPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgMovimiento[ movPk=" + movPk + " ]";
    }

}
