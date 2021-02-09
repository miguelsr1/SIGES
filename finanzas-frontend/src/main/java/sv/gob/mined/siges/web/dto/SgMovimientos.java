/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class SgMovimientos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long movPk;

    private String movCodigo;

    private SgPresupuestoEscolar movPresupuestoFk;

    private SsTopePresupuestal movTechoPresupuestal;

    private String movFuenteRecursos;

    private EnumMovimientosTipo movTipo;

    private BigDecimal movMonto;

    private LocalDateTime movUltmodFecha;

    private String movUltmodUsuario;

    private Integer movVersion;

    private Integer movNumMoviento;

    private EnumMovimientosOrigen movOrigen;

    private SgDetallePlanEscolar movActividadPk;

    private SgMovimientos movFuenteIngresos;

    private SgRubros movRubroPk;

    private Boolean movAnulacion;

    private Boolean movEditado;

    private BigDecimal movMontoAprobado;

    private SgAreaInversion movAreaInversionPk;

    private SgAreaInversion movSubAreaInversionPk;

    private BigDecimal saldoEgresos;

    private BigDecimal saldoDesembolosos;

    private BigDecimal saldoTransferencias;

    private BigDecimal totalDesembolsos;

    private BigDecimal saldoEgresosAprobado;

    private BigDecimal saldoOtrosIngresosAprobado;

    private BigDecimal totalAnual;

    private SgMovimientos movFuenteIngresosOriginal;

    private SgMovimientosEdicion editados;

    private BigDecimal movEnero = new BigDecimal(0);
    private BigDecimal movFebrero = new BigDecimal(0);
    private BigDecimal movMarzo = new BigDecimal(0);
    private BigDecimal movAbril = new BigDecimal(0);
    private BigDecimal movMayo = new BigDecimal(0);
    private BigDecimal movJunio = new BigDecimal(0);
    private BigDecimal movJulio = new BigDecimal(0);
    private BigDecimal movAgosto = new BigDecimal(0);
    private BigDecimal movSeptiembre = new BigDecimal(0);
    private BigDecimal movOctubre = new BigDecimal(0);
    private BigDecimal movNoviembre = new BigDecimal(0);
    private BigDecimal movDiciembre = new BigDecimal(0);
    private BigDecimal movTotalAnual = new BigDecimal(0);

    public SgMovimientos() {
        movEnero = BigDecimal.ZERO;
        movFebrero = BigDecimal.ZERO;
        movMarzo = BigDecimal.ZERO;
        movAbril = BigDecimal.ZERO;
        movMayo = BigDecimal.ZERO;
        movJunio = BigDecimal.ZERO;
        movJulio = BigDecimal.ZERO;
        movAgosto = BigDecimal.ZERO;
        movSeptiembre = BigDecimal.ZERO;
        movOctubre = BigDecimal.ZERO;
        movNoviembre = BigDecimal.ZERO;
        movDiciembre = BigDecimal.ZERO;
        movTotalAnual = BigDecimal.ZERO;
        movMontoAprobado = BigDecimal.ZERO;
        movMonto = BigDecimal.ZERO;

    }

    public String getMovCodigoTipo() {
        return this.movNumMoviento + (this.movOrigen != null ? "-" + this.movOrigen.getText() : null);
    }

    public String getMovCodigoRubro() {
        return this.movNumMoviento
                + (this.movRubroPk != null ? "-" + this.movRubroPk.getRuNombre() : null);
    }

    @JsonIgnore
    public String getFuenteIngresoTooltip() {
        if (this.movTipo.equals(EnumMovimientosTipo.E) && this.movFuenteIngresos != null && this.movFuenteIngresos.getMovOrigen().equals(EnumMovimientosOrigen.P)) {
            return this.movFuenteIngresos.getMovRubroPk().getRuNombre() + " - " + this.movFuenteIngresos.getMovFuenteRecursos();
        }
        if (this.movTipo.equals(EnumMovimientosTipo.E) && this.movFuenteIngresos != null && this.movFuenteIngresos.getMovOrigen().equals(EnumMovimientosOrigen.T)) {
            return this.movFuenteIngresos.getMovTechoPresupuestal().getSubComponente().getGesCategoriaComponente().getCpeNombre() + "-"
                    + this.movFuenteIngresos.getMovTechoPresupuestal().getSubComponente().getGesNombre();
        }
        return null;
    }

    @JsonIgnore
    public String getMovFuenteIngreso() {
        if (this.movTipo.equals(EnumMovimientosTipo.I) && this.movOrigen.equals(EnumMovimientosOrigen.P) && this.movOrigen != null) {
            return this.movNumMoviento + "-" + this.movFuenteRecursos + "-" + this.movRubroPk.getRuNombre();
        } else if (this.movTipo.equals(EnumMovimientosTipo.I) && this.movOrigen.equals(EnumMovimientosOrigen.T) && this.movOrigen != null) {
            return this.movNumMoviento + "-" + this.movTechoPresupuestal.getSubComponente().getGesCategoriaComponente().getCpeNombre()
                    + "-" + this.movTechoPresupuestal.getSubComponente().getGesNombre();
        }
        return null;
    }

    @JsonIgnore
    public String getMovFuenteIngresoEdicion() {
        if ((this.movTipo.equals(EnumMovimientosTipo.I) || (this.movTipo.equals(EnumMovimientosTipo.IM))) && this.movOrigen.equals(EnumMovimientosOrigen.P) && this.movOrigen != null) {
            return this.movNumMoviento + "-" + this.movFuenteRecursos + "-" + this.movRubroPk.getRuNombre();
        } else if ((this.movTipo.equals(EnumMovimientosTipo.I) || (this.movTipo.equals(EnumMovimientosTipo.IM))) && this.movOrigen.equals(EnumMovimientosOrigen.T) && this.movOrigen != null) {
            return this.movNumMoviento + "-" + this.movTechoPresupuestal.getSubComponente().getGesCategoriaComponente().getCpeNombre()
                    + "-" + this.movTechoPresupuestal.getSubComponente().getGesNombre();
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
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

    public BigDecimal getMovMonto() {
        return movMonto;
    }

    public void setMovMonto(BigDecimal movMonto) {
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

    public boolean isIngresoEdicion() {
        return EnumMovimientosTipo.IM.equals(movTipo);
    }

    public boolean isEgresoEdicion() {
        return EnumMovimientosTipo.EM.equals(movTipo);
    }

    public boolean isTransferencia() {

        return EnumMovimientosOrigen.T.equals(movOrigen);
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

    public SgAreaInversion getMovSubAreaInversionPk() {
        return movSubAreaInversionPk;
    }

    public void setMovSubAreaInversionPk(SgAreaInversion movSubAreaInversionPk) {
        this.movSubAreaInversionPk = movSubAreaInversionPk;
    }

    public BigDecimal getSaldoEgresos() {
        return saldoEgresos;
    }

    public void setSaldoEgresos(BigDecimal saldoEgresos) {
        this.saldoEgresos = saldoEgresos;
    }

    public BigDecimal getSaldoDesembolosos() {
        return saldoDesembolosos;
    }

    public void setSaldoDesembolosos(BigDecimal saldoDesembolosos) {
        this.saldoDesembolosos = saldoDesembolosos;
    }

    public BigDecimal getSaldoTransferencias() {
        return saldoTransferencias;
    }

    public void setSaldoTransferencias(BigDecimal saldoTransferencias) {
        this.saldoTransferencias = saldoTransferencias;
    }

    public BigDecimal getTotalDesembolsos() {
        return totalDesembolsos;
    }

    public void setTotalDesembolsos(BigDecimal totalDesembolsos) {
        this.totalDesembolsos = totalDesembolsos;
    }

    public BigDecimal getMovEnero() {
        return movEnero;
    }

    public void setMovEnero(BigDecimal movEnero) {
        this.movEnero = movEnero;
    }

    public BigDecimal getMovFebrero() {
        return movFebrero;
    }

    public void setMovFebrero(BigDecimal movFebrero) {
        this.movFebrero = movFebrero;
    }

    public BigDecimal getMovMarzo() {
        return movMarzo;
    }

    public void setMovMarzo(BigDecimal movMarzo) {
        this.movMarzo = movMarzo;
    }

    public BigDecimal getMovAbril() {
        return movAbril;
    }

    public void setMovAbril(BigDecimal movAbril) {
        this.movAbril = movAbril;
    }

    public BigDecimal getMovMayo() {
        return movMayo;
    }

    public void setMovMayo(BigDecimal movMayo) {
        this.movMayo = movMayo;
    }

    public BigDecimal getMovJunio() {
        return movJunio;
    }

    public void setMovJunio(BigDecimal movJunio) {
        this.movJunio = movJunio;
    }

    public BigDecimal getMovJulio() {
        return movJulio;
    }

    public void setMovJulio(BigDecimal movJulio) {
        this.movJulio = movJulio;
    }

    public BigDecimal getMovAgosto() {
        return movAgosto;
    }

    public void setMovAgosto(BigDecimal movAgosto) {
        this.movAgosto = movAgosto;
    }

    public BigDecimal getMovSeptiembre() {
        return movSeptiembre;
    }

    public void setMovSeptiembre(BigDecimal movSeptiembre) {
        this.movSeptiembre = movSeptiembre;
    }

    public BigDecimal getMovOctubre() {
        return movOctubre;
    }

    public void setMovOctubre(BigDecimal movOctubre) {
        this.movOctubre = movOctubre;
    }

    public BigDecimal getMovNoviembre() {
        return movNoviembre;
    }

    public void setMovNoviembre(BigDecimal movNoviembre) {
        this.movNoviembre = movNoviembre;
    }

    public BigDecimal getMovDiciembre() {
        return movDiciembre;
    }

    public void setMovDiciembre(BigDecimal movDiciembre) {
        this.movDiciembre = movDiciembre;
    }

    public BigDecimal getMovTotalAnual() {
        return movTotalAnual;
    }

    public void setMovTotalAnual(BigDecimal movTotalAnual) {
        this.movTotalAnual = movTotalAnual;
    }

    public Boolean getMovAnulacion() {
        return movAnulacion;
    }

    public void setMovAnulacion(Boolean movAnulacion) {
        this.movAnulacion = movAnulacion;
    }

    public SsTopePresupuestal getMovTechoPresupuestal() {
        return movTechoPresupuestal;
    }

    public void setMovTechoPresupuestal(SsTopePresupuestal movTechoPresupuestal) {
        this.movTechoPresupuestal = movTechoPresupuestal;
    }

    public SgMovimientos getMovFuenteIngresosOriginal() {
        return movFuenteIngresosOriginal;
    }

    public void setMovFuenteIngresosOriginal(SgMovimientos movFuenteIngresosOriginal) {
        this.movFuenteIngresosOriginal = movFuenteIngresosOriginal;
    }

    public SgMovimientosEdicion getEditados() {
        return editados;
    }

    public void setEditados(SgMovimientosEdicion editados) {
        this.editados = editados;
    }

    public BigDecimal getTotalAnual() {

        return totalAnual;
    }

    public void setTotalAnual(BigDecimal totalAnual) {
        this.totalAnual = totalAnual;
    }

    public void sumarMeses() {
//        if (movTotalAnual.signum() != 0) {
        this.movTotalAnual
                = this.movEnero.add(
                        this.movFebrero).add(
                                this.movMarzo).add(
                                this.movAbril).add(
                                this.movMayo).add(
                                this.movJunio).add(
                                this.movJulio).add(
                                this.movAgosto).add(
                                this.movSeptiembre).add(
                                this.movOctubre).add(
                                this.movNoviembre).add(this.movDiciembre);
//        }
    }

    public BigDecimal getSaldoEgresosAprobado() {
        return saldoEgresosAprobado;
    }

    public void setSaldoEgresosAprobado(BigDecimal saldoEgresosAprobado) {
        this.saldoEgresosAprobado = saldoEgresosAprobado;
    }

    public BigDecimal getSaldoOtrosIngresosAprobado() {
        return saldoOtrosIngresosAprobado;
    }

    public void setSaldoOtrosIngresosAprobado(BigDecimal saldoOtrosIngresosAprobado) {
        this.saldoOtrosIngresosAprobado = saldoOtrosIngresosAprobado;
    }

    public Boolean getMovEditado() {
        return movEditado;
    }

    public void setMovEditado(Boolean movEditado) {
        this.movEditado = movEditado;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hash-equals">
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
// </editor-fold>
}