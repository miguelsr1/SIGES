/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.siap2.SgAreaInversion;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsTopePresupuestal;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_presupuesto_escolar_edicion_movimiento", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityReference
/**
 *
 * Entidad correspondiente a los movimientos (lìnea de detalle) del presupuesto
 * escolar anual.
 */
public class SgMovimientosEdicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mov_pk", nullable = false)
    private Long movPk;

    @Size(max = 45)
    @Column(name = "mov_codigo", length = 45)
    private String movCodigo;

    @JoinColumn(name = "mov_presupuesto_fk", referencedColumnName = "pes_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgPresupuestoEscolar movPresupuestoFk;

    @JoinColumn(name = "mov_actividad_pk", referencedColumnName = "dpe_pk")
    @ManyToOne
    private SgDetallePlanEscolar movActividadPk;

    @Size(max = 255)
    @Column(name = "mov_fuente_recursos", length = 255)
    private String movFuenteRecursos;

    @Column(name = "mov_tipo")
    @Enumerated(value = EnumType.STRING)
    private EnumMovimientosTipo movTipo;

    @Column(name = "mov_monto")
    private BigDecimal movMonto;

    @AtributoUltimaModificacion
    @Column(name = "mov_ult_mod_fecha")
    private LocalDateTime movUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "mov_ult_mod_usuario", length = 45)
    private String movUltmodUsuario;

    @Column(name = "mov_version")
    @Version
    private Integer movVersion;

    @Column(name = "mov_num_movimiento")
    @Version
    private Integer movNumMoviento;

    @Size(max = 20)
    @Column(name = "mov_origen", length = 20)
    private String movOrigen;

    @JoinColumn(name = "mov_fuente_ingreso_pk", referencedColumnName = "mov_pk")
    @ManyToOne
    private SgMovimientosEdicion movFuenteIngresos;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "mov_rubro_pk", referencedColumnName = "ru_id")
    @ManyToOne
    private SgRubros movRubroPk;

    @Column(name = "mov_monto_aprobado")
    private BigDecimal movMontoAprobado;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "mov_area_inversion_pk", referencedColumnName = "ai_id")
    @ManyToOne
    private SgAreaInversion movAreaInversionPk;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "mov_subarea_inversion_pk", referencedColumnName = "ai_id")
    @ManyToOne
    private SgAreaInversion movSubAreaInversionPk;

    @JoinColumn(name = "mov_fuente_ingreso_original_pk", referencedColumnName = "mov_pk")
    @ManyToOne
    private SgMovimientos movFuenteIngresosOriginal;

    @JoinColumn(name = "mov_original_editado", referencedColumnName = "mov_pk")
    @ManyToOne
    private SgMovimientos movOriginalEditado;

    @Column(name = "mov_enero", nullable = true)
    private BigDecimal movEnero;

    @Column(name = "mov_febrero", nullable = true)
    private BigDecimal movFebrero;

    @Column(name = "mov_marzo", nullable = true)
    private BigDecimal movMarzo;

    @Column(name = "mov_abril", nullable = true)
    private BigDecimal movAbril;

    @Column(name = "mov_mayo", nullable = true)
    private BigDecimal movMayo;

    @Column(name = "mov_junio", nullable = true)
    private BigDecimal movJunio;

    @Column(name = "mov_julio", nullable = true)
    private BigDecimal movJulio;

    @Column(name = "mov_agosto", nullable = true)
    private BigDecimal movAgosto;

    @Column(name = "mov_septiembre", nullable = true)
    private BigDecimal movSeptiembre;

    @Column(name = "mov_octubre", nullable = true)
    private BigDecimal movOctubre;

    @Column(name = "mov_noviembre", nullable = true)
    private BigDecimal movNoviembre;

    @Column(name = "mov_diciembre", nullable = true)
    private BigDecimal movDiciembre;

    @Column(name = "mov_total_anual", nullable = true)
    private BigDecimal movTotalAnual;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "mov_techo_presupuestal", referencedColumnName = "tp_id")
    @ManyToOne
    private SsTopePresupuestal movTechoPresupuestal;

    public SgMovimientosEdicion() {
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

    }

    public String getMovCodigoTipo() {
        return this.movNumMoviento + " - " + this.movOrigen;
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
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

    public SgPresupuestoEscolar getMovPresupuestoFk() {
        return movPresupuestoFk;
    }

    public void setMovPresupuestoFk(SgPresupuestoEscolar movPresupuestoFk) {
        this.movPresupuestoFk = movPresupuestoFk;
    }

    public String getMovFuenteRecursos() {
        return movFuenteRecursos;
    }

    public void setMovFuenteRecursos(String movFuenteRecursos) {
        this.movFuenteRecursos = movFuenteRecursos;
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

    public String getMovOrigen() {
        return movOrigen;
    }

    public void setMovOrigen(String movOrigen) {
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

    public SgMovimientosEdicion getMovFuenteIngresos() {
        return movFuenteIngresos;
    }

    public void setMovFuenteIngresos(SgMovimientosEdicion movFuenteIngresos) {
        this.movFuenteIngresos = movFuenteIngresos;
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

    public SgMovimientos getMovFuenteIngresosOriginal() {
        return movFuenteIngresosOriginal;
    }

    public void setMovFuenteIngresosOriginal(SgMovimientos movFuenteIngresosOriginal) {
        this.movFuenteIngresosOriginal = movFuenteIngresosOriginal;
    }

    public SsTopePresupuestal getMovTechoPresupuestal() {
        return movTechoPresupuestal;
    }

    public void setMovTechoPresupuestal(SsTopePresupuestal movTechoPresupuestal) {
        this.movTechoPresupuestal = movTechoPresupuestal;
    }

    public SgMovimientos getMovOriginalEditado() {
        return movOriginalEditado;
    }

    public void setMovOriginalEditado(SgMovimientos movOriginalEditado) {
        this.movOriginalEditado = movOriginalEditado;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.movPk);
        hash = 67 * hash + Objects.hashCode(this.movCodigo);
        hash = 67 * hash + Objects.hashCode(this.movPresupuestoFk);
        hash = 67 * hash + Objects.hashCode(this.movActividadPk);
        hash = 67 * hash + Objects.hashCode(this.movFuenteRecursos);
        hash = 67 * hash + Objects.hashCode(this.movTipo);
        hash = 67 * hash + Objects.hashCode(this.movMonto);
        hash = 67 * hash + Objects.hashCode(this.movUltmodFecha);
        hash = 67 * hash + Objects.hashCode(this.movUltmodUsuario);
        hash = 67 * hash + Objects.hashCode(this.movVersion);
        hash = 67 * hash + Objects.hashCode(this.movNumMoviento);
        hash = 67 * hash + Objects.hashCode(this.movOrigen);
        hash = 67 * hash + Objects.hashCode(this.movFuenteIngresos);
        hash = 67 * hash + Objects.hashCode(this.movRubroPk);
        hash = 67 * hash + Objects.hashCode(this.movMontoAprobado);
        hash = 67 * hash + Objects.hashCode(this.movAreaInversionPk);
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
        final SgMovimientosEdicion other = (SgMovimientosEdicion) obj;
        if (!Objects.equals(this.movPk, other.movPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
}
