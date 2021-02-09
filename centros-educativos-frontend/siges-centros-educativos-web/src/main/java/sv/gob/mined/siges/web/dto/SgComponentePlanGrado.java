/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgFormula;
import sv.gob.mined.siges.web.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.web.enumerados.EnumFuncionRedondeo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpgPk", scope = SgComponentePlanGrado.class)
public class SgComponentePlanGrado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cpgPk;

    private SgPlanEstudio cpgPlanEstudio;

    private SgGrado cpgGrado;

    private SgComponentePlanEstudio cpgComponentePlanEstudio;

    private Long cpgCodigoSirai;

    private String cpgNombrePublicable;

    private Integer cpgCantidadHorasSemanales;

    private Boolean cpgObjetoPromocion;
    
    private Boolean cpgAplicaGraficaEvolucion;

    private SgEscalaCalificacion cpgEscalaCalificacion;

    private Integer cpgPeriodosCalificacion;

    private Integer cpgCantidadPrimeraPrueba;

    private Integer cpgCantidadPrimeraSuficiencia;

    private Integer cpgCantidadSegundaPrueba;

    private Integer cpgCantidadSegundaSuficiencia;

    private EnumCalculoNotaInstitucional cpgCalculoNotaInstitucional;

    private EnumFuncionRedondeo cpgFuncionRedondeo;

    private Integer cpgPrecision;
    
    private SgFormula cpgFormula;  

    private LocalDateTime cpgUltModFecha;

    private String cpgUltModUsuario;

    private Integer cpgVersion;
    
    private String cpgContenidoTematico;
    
    private SgSeccion cpgExclusivoSeccion;
    
    private Boolean cpgCalificacionIngresadaCE;
    
    private Integer cpgOrden;
        
    private Integer cpgCodigoExterno;
    
    private SgFormula cpgFormulaHabilitacionPP;      

    private SgFormula cpgFormulaHabilitacionPS;  

    private SgFormula cpgFormulaHabilitacionSP;  
  
    private SgFormula cpgFormulaHabilitacionSS;  
    
    private SgComponentePlanGrado cpgParametroFormulaPruebaPP;  
    
    private SgComponentePlanGrado cpgParametroFormulaPruebaPS;  
    
    private SgComponentePlanGrado cpgParametroFormulaPruebaSP;  
   
    private SgComponentePlanGrado cpgParametroFormulaPruebaSS; 
    
    private SgComponentePlanGrado cpgParametroFormulaAprobacion;  

    public SgComponentePlanGrado() {
        cpgPeriodosCalificacion = 0;
        cpgCantidadPrimeraPrueba = 0;
        cpgCantidadPrimeraSuficiencia = 0;
        cpgCantidadSegundaPrueba = 0;
        cpgCantidadSegundaSuficiencia = 0;
        cpgObjetoPromocion=Boolean.FALSE; 
        cpgCalificacionIngresadaCE = Boolean.TRUE;
        cpgAplicaGraficaEvolucion=Boolean.FALSE;

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

    public String getCpgContenidoTematico() {
        return cpgContenidoTematico;
    }

    public void setCpgContenidoTematico(String cpgContenidoTematico) {
        this.cpgContenidoTematico = cpgContenidoTematico;
    }    
        
    public SgFormula getCpgFormula() {
        return cpgFormula;
    }

    public void setCpgFormula(SgFormula cpgFormula) {
        this.cpgFormula = cpgFormula;
    }

    public SgSeccion getCpgExclusivoSeccion() {
        return cpgExclusivoSeccion;
    }

    public void setCpgExclusivoSeccion(SgSeccion cpgExclusivoSeccion) {
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
        final SgComponentePlanGrado other = (SgComponentePlanGrado) obj;
        if (!Objects.equals(this.cpgPk, other.cpgPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgComponentePlanGrado[ cpgPk=" + cpgPk + " ]";
    }

}
