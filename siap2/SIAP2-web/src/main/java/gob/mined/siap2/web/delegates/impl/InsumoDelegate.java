/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.datatype.DataCertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.business.ejbs.impl.InsumoBean;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.tipos.FiltroClasFunc;
import gob.mined.siap2.entities.tipos.FiltroCronogramaRecursos;
import gob.mined.siap2.ws.to.CronogramaRecurso;
import gob.mined.siap2.ws.to.ResumenClasificadorFuncional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class InsumoDelegate implements Serializable {

    @Inject
    private InsumoBean bean;

    public List<POInsumos> getEstadosInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        return bean.getEstadosInsumos(noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario, firstResult, maxResults, orderBy, ascending);
    }

    public long countEstadosInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq,Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {
        return bean.countEstadosInsumos(noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario);
    }

    public BigDecimal sumarEstadosInsumos(String nombreCampo, Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {
        return bean.sumarEstadosInsumos(nombreCampo, noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario);
    }

    public BigDecimal sumarPagadoInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {
        return bean.sumarPagadoInsumos(noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario);
    }

    public Collection<ResumenClasificadorFuncional> obtenerResumen(FiltroClasFunc filtro, String tipo) {
        return bean.obtenerResumen(filtro, tipo);
    }

    public List<DataCertificadoDisponibilidadPresupuestaria> getCertificadoDisponibilidadPresupuestariaInsumos(Integer firstResult, Integer maxResults) {
        return bean.getCertificadoDisponibilidadPresupuestariaInsumos(firstResult, maxResults);
    }

    public long countCertificadoDisponibilidadPresupuestariaInsumos() {
        return bean.countCertificadoDisponibilidadPresupuestariaInsumos();
    }

    public List<POMontoFuenteInsumo> getMontofuentesProceso(Integer idUT, Integer idProcesoAdq) {
        return bean.getMontofuentesProceso(idUT, idProcesoAdq);
    }

    /**
     * Este método permite guardar un insumo de POA con su disponibilidad
     * presupuestaria.
     *
     * @param insumo
     * @return
     */
    public POInsumos guardarDisponibilidadPresupuestriaInsumo(POInsumos insumo) {
        return bean.guardarDisponibilidadPresupuestriaInsumo(insumo);
    }

    /**
     * Este método permite enviar el certificado de disponibilidad
     * presupuestaria.
     *
     * @param insumo
     * @return
     */
    public POInsumos enviarCertificadoDisponibiildadPresupuestaria(POInsumos insumo) {
        return bean.enviarCertificadoDisponibiildadPresupuestaria(insumo);
    }

    /**
     * Este método permite obtener un insumo con el certificado de
     * disponibilidad presupuestaria.
     *
     * @param idInsumo
     * @return
     */
    public POInsumos loadInsumoDisponibilidadPresupuestaria(Integer idInsumo) {
        return bean.loadInsumoDisponibilidadPresupuestaria(idInsumo);
    }

    /**
     * Devuelve la categoría presupuestaria de un POA Insumo, conformada por
     * Proyecto (en caso de ser proyecto)-programa presupuestario-subprograma
     * presupuestario
     *
     * @param idPoInsumo
     */
    public String getCategoriaPresupuestariaDePoInsumo(Integer idPoInsumo) {
        return bean.getCategoriaPresupuestariaDePoInsumo(idPoInsumo);
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del programa (si es proyecto) o de AC o ANP de un POA
     * Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoProgramaACoANPDePoInsumo(Integer idPoInsumo) {
        return bean.getCodigoProgramaACoANPDePoInsumo(idPoInsumo);
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del subprograma (si es proyecto) de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoSubprogramaDePoInsumo(Integer idPoInsumo) {
        return bean.getCodigoSubprogramaDePoInsumo(idPoInsumo);
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del proyecto (si no es AC o ANP) de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoProyectoDePoInsumo(Integer idPoInsumo) {
        return bean.getCodigoProyectoDePoInsumo(idPoInsumo);
    }

    /**
     * Devuelve la lista de insumos No UACI según los filtros aplicados
     * @param idAnioFiscal
     * @param idProyecto
     * @param idAC
     * @param idANP
     * @param idUT
     * @param codigo
     * @param codigoInterno
     * @param firstResult
     * @param maxResults
     * @param orderBy
     * @param ascending
     * @return 
     */
    public List<POInsumos> getInsumosNoUACI(Integer idAnioFiscal, Integer idProyecto, Integer idAC, Integer idANP, Integer idUT, String codigo, String codigoInterno, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        return bean.getInsumosNoUACI(idAnioFiscal, idProyecto, idAC, idANP, idUT, codigo, codigoInterno, firstResult, maxResults, orderBy, ascending);
    }
    
    /**
     * Devuelve la cantidad de insumos No UACI según los filtros aplicados
     * @param idAnioFiscal
     * @param idProyecto
     * @param idAC
     * @param idANP
     * @param idUT
     * @param codigo
     * @param codigoInterno
     * @return 
     */
    public long countInsumosNoUACI(Integer idAnioFiscal,Integer idProyecto,Integer idAC, Integer idANP, Integer idUT, String codigo, String codigoInterno) {
        return bean.countInsumosNoUACI(idAnioFiscal, idProyecto, idAC, idANP, idUT, codigo, codigoInterno);
    }

    /**
     * Verifica si un insumo está asociado a un Proyecto
     * @param poInsumo
     * @return 
     */
    public Boolean poInsumoEsDeProyecto(POInsumos poInsumo){
        return bean.poInsumoEsDeProyecto(poInsumo);
    }
    
    /**
     * Verifica si un insumo está asociado a una Acción Central 
     * @param poInsumo
     * @return 
     */
    public Boolean poInsumoEsDeAC(POInsumos poInsumo){
        return bean.poInsumoEsDeAC(poInsumo);
    }
    
    /**
     * Verifica si un insumo está asociado a una Asignación No Programable
     * @param poInsumo
     * @return 
     */
    public Boolean poInsumoEsDeANP(POInsumos poInsumo){
        return bean.poInsumoEsDeANP(poInsumo);
    }

    /**
     * Devuelve la lista de Pólizas de Concentración de un PoInsumo
     * @param idPoInsumo
     * @return 
     */
    public List<PolizaDeConcentracion> getPolizasDePoInsumo(Integer idPoInsumo) {
        return bean.getPolizasDePoInsumo(idPoInsumo);
    }

    /**
     * Calcula y retorna el monto del insumo asociado a Pólizas de concentración
     * aprobadas
     *
     * @return
     */
    public BigDecimal calcularMontoEnPolizasAprobadas(Integer idPoInsumo) {
        return bean.calcularMontoEnPolizasAprobadas(idPoInsumo);
    }

    /**
     * Calcula la diferencia entre el monto estimado y el certificado de un insumo
     * @param insumo
     * @return 
     */
    public BigDecimal calcularDiferenciaEntreEstimadoYCertificado(POInsumos insumo) {
        return bean.calcularDiferenciaEntreEstimadoYCertificado(insumo);
    }

    /**
     * Calcula la suma de los montos del insumo en las actas de recepción aprobadas
     * @return 
     */
    public BigDecimal calcularMontoActasRecepcionAprobadas(Integer idPoInsumo) {
        return bean.calcularMontoActasRecepcionAprobadas(idPoInsumo);
    }

    /**
    * Valor de PEP + valor de reprogramaciones
    * @param insumo
    * @return 
    */
    public BigDecimal calcularDisponibleModificado(POInsumos insumo) {
        return bean.calcularDisponibleModificado(insumo);
    }

    /**
     * Suma de montos descertificados de las fuentes del insumo
     * @param insumo
     * @return 
     */
    public BigDecimal calcularMontoDescertificado(POInsumos insumo) {
        return bean.calcularMontoDescertificado(insumo);
    }

    /**
     * Monto disponible modificado - monto certificado + monto descertificado
     * @param insumo
     * @return 
     */
    public BigDecimal calcularMontoDisponible(POInsumos insumo) {
        return bean.calcularMontoDisponible(insumo);
    }

    public Collection<CronogramaRecurso> obtenerCronogramaRecrusos(FiltroCronogramaRecursos filtro, String tipoReporte) {
        return bean.obtenerCronogramaRecrusos(filtro, tipoReporte);
    }

}
