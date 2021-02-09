/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.enumerados.EnumFuncionRedondeo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFormula;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_componente_plan_grado", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpgPk", scope = SgComponentePlanGrado.class)
public class SgComponentePlanGrado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cpg_pk")
    private Long cpgPk;
    
    @JoinColumn(name = "cpg_plan_estudio_fk", referencedColumnName = "pes_pk")
    @ManyToOne(optional = false)
    private SgPlanEstudio cpgPlanEstudio;
    
    @JoinColumn(name = "cpg_grado_fk", referencedColumnName = "gra_pk")
    @ManyToOne(optional = false)
    private SgGrado cpgGrado;
    
    @JoinColumn(name = "cpg_componente_plan_estudio_fk", referencedColumnName = "cpe_pk")
    @ManyToOne(optional = false)
    private SgComponentePlanEstudio cpgComponentePlanEstudio;       
    
    @Column(name = "cpg_codigo_sirai")
    private Long cpgCodigoSirai;
        
    @Size(max = 255)
    @Column(name = "cpg_nombre_publicable",length = 255)
    private String cpgNombrePublicable;
    
    @Column(name = "cpg_cantidad_horas_semanales")
    private Integer cpgCantidadHorasSemanales;
    
    
    @Column(name = "cpg_objeto_promocion")
    private Boolean cpgObjetoPromocion;
    
    @Column(name = "cpg_aplica_grafica_evolucion")
    private Boolean cpgAplicaGraficaEvolucion;

    @JoinColumn(name = "cpg_escala_calificacion_fk", referencedColumnName = "eca_pk")
    @ManyToOne(optional = false)
    private SgEscalaCalificacion cpgEscalaCalificacion;   

    @Column(name = "cpg_periodos_calificacion")
    private Integer cpgPeriodosCalificacion;

    @Column(name = "cpg_cantidad_primera_prueba")
    private Integer cpgCantidadPrimeraPrueba;

    @Column(name = "cpg_cantidad_primera_suficiencia")
    private Integer cpgCantidadPrimeraSuficiencia;

    @Column(name = "cpg_cantidad_segunda_prueba")
    private Integer cpgCantidadSegundaPrueba;

    @Column(name = "cpg_cantidad_segunda_suficiencia")
    private Integer cpgCantidadSegundaSuficiencia;
    
    @Column(name = "cpg_calculo_nota_institucional")
    @Enumerated(value = EnumType.STRING)
    private EnumCalculoNotaInstitucional cpgCalculoNotaInstitucional;
    
    @Column(name = "cpg_funcion_redondeo")
    @Enumerated(value = EnumType.STRING)
    private EnumFuncionRedondeo cpgFuncionRedondeo;
    
    @Column(name = "cpg_precision")
    private Integer cpgPrecision;

    @Column(name = "cpg_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cpgUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cpg_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String cpgUltModUsuario;
    
    @Column(name = "cpg_version")
    @Version
    private Integer cpgVersion;

    @JoinColumn(name = "cpg_formula_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula cpgFormula;   
    
    @Size(max = 4000)
    @Column(name = "cpg_contenido_tematico",length = 4000)
    private String cpgContenidoTematico;
    
    @JoinColumn(name = "cpg_exclusivo_seccion", referencedColumnName = "sec_pk")
    @ManyToOne
    private SgSeccionLite cpgExclusivoSeccion;
    
    @Column(name = "cpg_calificacion_ingresada_ce")
    private Boolean cpgCalificacionIngresadaCE;
    
    @Column(name = "cpg_orden")
    private Integer cpgOrden;
    
    @Column(name = "cpg_codigo_externo")
    private Integer cpgCodigoExterno;
    
    @JoinColumn(name = "cpg_formula_habilitacion_PP_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula cpgFormulaHabilitacionPP;  
    
    @JoinColumn(name = "cpg_formula_habilitacion_PS_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula cpgFormulaHabilitacionPS;  
    
    @JoinColumn(name = "cpg_formula_habilitacion_SP_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula cpgFormulaHabilitacionSP;  
    
    @JoinColumn(name = "cpg_formula_habilitacion_SS_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula cpgFormulaHabilitacionSS;  
    
    @JoinColumn(name = "cpg_parametro_formula_prueba_PP", referencedColumnName = "cpg_pk")
    @ManyToOne
    private SgComponentePlanGrado cpgParametroFormulaPruebaPP;  
    
    @JoinColumn(name = "cpg_parametro_formula_prueba_PS", referencedColumnName = "cpg_pk")
    @ManyToOne
    private SgComponentePlanGrado cpgParametroFormulaPruebaPS;  
    
    @JoinColumn(name = "cpg_parametro_formula_prueba_SP", referencedColumnName = "cpg_pk")
    @ManyToOne
    private SgComponentePlanGrado cpgParametroFormulaPruebaSP;  
    
    @JoinColumn(name = "cpg_parametro_formula_prueba_SS", referencedColumnName = "cpg_pk")
    @ManyToOne
    private SgComponentePlanGrado cpgParametroFormulaPruebaSS;  
    
    @JoinColumn(name = "cpg_parametro_formula_aprobacion", referencedColumnName = "cpg_pk")
    @ManyToOne
    private SgComponentePlanGrado cpgParametroFormulaAprobacion;  


    public SgComponentePlanGrado() {
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public Long getCpgPk() {
        return cpgPk;
    }

    public void setCpgPk(Long cpgPk) {
        this.cpgPk = cpgPk;
    }

    public Integer getCpgCodigoExterno() {
        return cpgCodigoExterno;
    }

    public void setCpgCodigoExterno(Integer cpgCodigoExterno) {
        this.cpgCodigoExterno = cpgCodigoExterno;
    }

    public Integer getCpgOrden() {
        return cpgOrden;
    }

    public void setCpgOrden(Integer cpgOrden) {
        this.cpgOrden = cpgOrden;
    }

    public SgPlanEstudio getCpgPlanEstudio() {
        return cpgPlanEstudio;
    }

    public void setCpgPlanEstudio(SgPlanEstudio cpgPlanEstudio) {
        this.cpgPlanEstudio = cpgPlanEstudio;
    }

    public SgGrado getCpgGrado() {
        return cpgGrado;
    }

    public void setCpgGrado(SgGrado cpgGrado) {
        this.cpgGrado = cpgGrado;
    }

    public SgComponentePlanEstudio getCpgComponentePlanEstudio() {
        return cpgComponentePlanEstudio;
    }

    public void setCpgComponentePlanEstudio(SgComponentePlanEstudio cpgComponentePlanEstudio) {
        this.cpgComponentePlanEstudio = cpgComponentePlanEstudio;
    }


    public Long getCpgCodigoSirai() {
        return cpgCodigoSirai;
    }

    public void setCpgCodigoSirai(Long cpgCodigoSirai) {
        this.cpgCodigoSirai = cpgCodigoSirai;
    }

    
    public String getCpgNombrePublicable() {
        return cpgNombrePublicable;
    }

    public void setCpgNombrePublicable(String cpgNombrePublicable) {
        this.cpgNombrePublicable = cpgNombrePublicable;
    }

    public Integer getCpgCantidadHorasSemanales() {
        return cpgCantidadHorasSemanales;
    }

    public void setCpgCantidadHorasSemanales(Integer cpgCantidadHorasSemanales) {
        this.cpgCantidadHorasSemanales = cpgCantidadHorasSemanales;
    }
        
    public Boolean getCpgObjetoPromocion() {
        return cpgObjetoPromocion;
    }

    public void setCpgObjetoPromocion(Boolean cpgObjetoPromocion) {
        this.cpgObjetoPromocion = cpgObjetoPromocion;
    }

    public SgEscalaCalificacion getCpgEscalaCalificacion() {
        return cpgEscalaCalificacion;
    }

    public void setCpgEscalaCalificacion(SgEscalaCalificacion cpgEscalaCalificacion) {
        this.cpgEscalaCalificacion = cpgEscalaCalificacion;
    }

    public Integer getCpgPeriodosCalificacion() {
        return cpgPeriodosCalificacion;
    }

    public void setCpgPeriodosCalificacion(Integer cpgPeriodosCalificacion) {
        this.cpgPeriodosCalificacion = cpgPeriodosCalificacion;
    }

    public Integer getCpgCantidadPrimeraPrueba() {
        return cpgCantidadPrimeraPrueba;
    }

    public void setCpgCantidadPrimeraPrueba(Integer cpgCantidadPrimeraPrueba) {
        this.cpgCantidadPrimeraPrueba = cpgCantidadPrimeraPrueba;
    }

    public Integer getCpgCantidadPrimeraSuficiencia() {
        return cpgCantidadPrimeraSuficiencia;
    }

    public void setCpgCantidadPrimeraSuficiencia(Integer cpgCantidadPrimeraSuficiencia) {
        this.cpgCantidadPrimeraSuficiencia = cpgCantidadPrimeraSuficiencia;
    }

    public Integer getCpgCantidadSegundaPrueba() {
        return cpgCantidadSegundaPrueba;
    }

    public void setCpgCantidadSegundaPrueba(Integer cpgCantidadSegundaPrueba) {
        this.cpgCantidadSegundaPrueba = cpgCantidadSegundaPrueba;
    }

    public Integer getCpgCantidadSegundaSuficiencia() {
        return cpgCantidadSegundaSuficiencia;
    }

    public void setCpgCantidadSegundaSuficiencia(Integer cpgCantidadSegundaSuficiencia) {
        this.cpgCantidadSegundaSuficiencia = cpgCantidadSegundaSuficiencia;
    }

    public EnumCalculoNotaInstitucional getCpgCalculoNotaInstitucional() {
        return cpgCalculoNotaInstitucional;
    }

    public void setCpgCalculoNotaInstitucional(EnumCalculoNotaInstitucional cpgCalculoNotaInstitucional) {
        this.cpgCalculoNotaInstitucional = cpgCalculoNotaInstitucional;
    }

    public EnumFuncionRedondeo getCpgFuncionRedondeo() {
        return cpgFuncionRedondeo;
    }

    public void setCpgFuncionRedondeo(EnumFuncionRedondeo cpgFuncionRedondeo) {
        this.cpgFuncionRedondeo = cpgFuncionRedondeo;
    }

    public Integer getCpgPrecision() {
        return cpgPrecision;
    }

    public void setCpgPrecision(Integer cpgPrecision) {
        this.cpgPrecision = cpgPrecision;
    }

    public LocalDateTime getCpgUltModFecha() {
        return cpgUltModFecha;
    }

    public void setCpgUltModFecha(LocalDateTime cpgUltModFecha) {
        this.cpgUltModFecha = cpgUltModFecha;
    }

    public String getCpgUltModUsuario() {
        return cpgUltModUsuario;
    }

    public void setCpgUltModUsuario(String cpgUltModUsuario) {
        this.cpgUltModUsuario = cpgUltModUsuario;
    }

    public Integer getCpgVersion() {
        return cpgVersion;
    }

    public void setCpgVersion(Integer cpgVersion) {
        this.cpgVersion = cpgVersion;
    }

    public SgFormula getCpgFormula() {
        return cpgFormula;
    }

    public void setCpgFormula(SgFormula cpgFormula) {
        this.cpgFormula = cpgFormula;
    }

    public String getCpgContenidoTematico() {
        return cpgContenidoTematico;
    }

    public void setCpgContenidoTematico(String cpgContenidoTematico) {
        this.cpgContenidoTematico = cpgContenidoTematico;
    }

    public SgSeccionLite getCpgExclusivoSeccion() {
        return cpgExclusivoSeccion;
    }

    public void setCpgExclusivoSeccion(SgSeccionLite cpgExclusivoSeccion) {
        this.cpgExclusivoSeccion = cpgExclusivoSeccion;
    }

    public Boolean getCpgCalificacionIngresadaCE() {
        return cpgCalificacionIngresadaCE;
    }

    public void setCpgCalificacionIngresadaCE(Boolean cpgCalificacionIngresadaCE) {
        this.cpgCalificacionIngresadaCE = cpgCalificacionIngresadaCE;
    }

    public SgFormula getCpgFormulaHabilitacionPP() {
        return cpgFormulaHabilitacionPP;
    }

    public void setCpgFormulaHabilitacionPP(SgFormula cpgFormulaHabilitacionPP) {
        this.cpgFormulaHabilitacionPP = cpgFormulaHabilitacionPP;
    }

    public SgFormula getCpgFormulaHabilitacionPS() {
        return cpgFormulaHabilitacionPS;
    }

    public void setCpgFormulaHabilitacionPS(SgFormula cpgFormulaHabilitacionPS) {
        this.cpgFormulaHabilitacionPS = cpgFormulaHabilitacionPS;
    }

    public SgFormula getCpgFormulaHabilitacionSP() {
        return cpgFormulaHabilitacionSP;
    }

    public void setCpgFormulaHabilitacionSP(SgFormula cpgFormulaHabilitacionSP) {
        this.cpgFormulaHabilitacionSP = cpgFormulaHabilitacionSP;
    }

    public SgFormula getCpgFormulaHabilitacionSS() {
        return cpgFormulaHabilitacionSS;
    }

    public void setCpgFormulaHabilitacionSS(SgFormula cpgFormulaHabilitacionSS) {
        this.cpgFormulaHabilitacionSS = cpgFormulaHabilitacionSS;
    }

    public SgComponentePlanGrado getCpgParametroFormulaPruebaPP() {
        return cpgParametroFormulaPruebaPP;
    }

    public void setCpgParametroFormulaPruebaPP(SgComponentePlanGrado cpgParametroFormulaPruebaPP) {
        this.cpgParametroFormulaPruebaPP = cpgParametroFormulaPruebaPP;
    }

    public SgComponentePlanGrado getCpgParametroFormulaPruebaPS() {
        return cpgParametroFormulaPruebaPS;
    }

    public void setCpgParametroFormulaPruebaPS(SgComponentePlanGrado cpgParametroFormulaPruebaPS) {
        this.cpgParametroFormulaPruebaPS = cpgParametroFormulaPruebaPS;
    }

    public SgComponentePlanGrado getCpgParametroFormulaPruebaSP() {
        return cpgParametroFormulaPruebaSP;
    }

    public void setCpgParametroFormulaPruebaSP(SgComponentePlanGrado cpgParametroFormulaPruebaSP) {
        this.cpgParametroFormulaPruebaSP = cpgParametroFormulaPruebaSP;
    }

    public SgComponentePlanGrado getCpgParametroFormulaPruebaSS() {
        return cpgParametroFormulaPruebaSS;
    }

    public void setCpgParametroFormulaPruebaSS(SgComponentePlanGrado cpgParametroFormulaPruebaSS) {
        this.cpgParametroFormulaPruebaSS = cpgParametroFormulaPruebaSS;
    }

    public SgComponentePlanGrado getCpgParametroFormulaAprobacion() {
        return cpgParametroFormulaAprobacion;
    }

    public void setCpgParametroFormulaAprobacion(SgComponentePlanGrado cpgParametroFormulaAprobacion) {
        this.cpgParametroFormulaAprobacion = cpgParametroFormulaAprobacion;
    }

    public Boolean getCpgAplicaGraficaEvolucion() {
        return cpgAplicaGraficaEvolucion;
    }

    public void setCpgAplicaGraficaEvolucion(Boolean cpgAplicaGraficaEvolucion) {
        this.cpgAplicaGraficaEvolucion = cpgAplicaGraficaEvolucion;
    }
    
    
    //</editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpgPk != null ? cpgPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgComponentePlanGrado)) {
            return false;
        }
        SgComponentePlanGrado other = (SgComponentePlanGrado) object;
        if ((this.cpgPk == null && other.cpgPk != null) || (this.cpgPk != null && !this.cpgPk.equals(other.cpgPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado[ cpgPk=" + cpgPk + " ]";
    }
    
}
