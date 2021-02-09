/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.CertificadoPresupuestarioBean;
import gob.mined.siap2.entities.data.impl.CertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.enums.EstadoCertificadoDispPresupuestaria;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa el delegate de certificado presupuestario
 * @author Sofis Solutions
 */
@Named
public class CertificadoPresupuestarioDelegate implements Serializable {

    @Inject
    private CertificadoPresupuestarioBean bean;

    /**
     * Este método se encarga de cargar un certificado de disponibilidad presupuestaria
     * 
     * @param id 
     * @return  
     */
    public CertificadoDisponibilidadPresupuestaria getCertificadoPresupuestario(Integer id) {
        return bean.getCertificadoPresupuestario(id);
    }
    
    

    /**
     * Este método se encarga de aprobar un certificado de disponibilidad presupuestaria
     * 
     * @param certificado
     * @return  
     */
    public CertificadoDisponibilidadPresupuestaria aprobarCertificadoDispPresupuestario(Integer idCertificado) {
        return bean.aprobarCertificadoDispPresupuestario(idCertificado);
    }

      
        /**
     * Este método reenvía una notificación para validar las fuentes de los insumo
     *
     * @param cert
     * @return 
     */
    public CertificadoDisponibilidadPresupuestaria reenviarCertificadoParaEnviar(CertificadoDisponibilidadPresupuestaria cert) {
        return bean.reenviarCertificadoParaEnviar(cert);
    }

    /**
     * Este método se encarga de aprobar un certificado de disponibilidad presupuestaria
     * 
     * @param certificado
     * @return  
     */
    public CertificadoDisponibilidadPresupuestaria rechazarCertificadoDispPresupuestario(CertificadoDisponibilidadPresupuestaria certificado) {
        return bean.rechazarCertificadoDispPresupuestario(certificado);
    }


/**
     * Genera un certificado que contiene los montos fuentes seleccionados para enviar a validar
     * @param idUT
     * @param idProceso
     * @param montosSeleccionadosParaReporte 
     */
    public void enviarAvalidarFuentes(Integer idUT, List<POMontoFuenteInsumo> montosSeleccionadosParaReporte) {
        bean.enviarAvalidarFuentes(idUT, montosSeleccionadosParaReporte);
    }

    /**
     * Verifica si un monto fuente está asociado a un certificado de disponibilidad presupuestaria con estado vacío o rechazado
     * @param montoFienteId
     * @return 
     */
    public Boolean estaMontoFuenteDisponibleParaEnviar(Integer montoFienteId) {
        return bean.estaMontoFuenteDisponibleParaEnviar(montoFienteId);
    }
    
    
    
    /**
     * Este método se encarga de agregar un comentario al información adicional un certificado de disponibilidad
     * presupuestaria
     *
     * @param id
     * @param contenido
     * @return
     */
    public CertificadoDisponibilidadPresupuestaria agregarComentarioCertificado(Integer id, String contenido) {
        return bean.agregarComentarioCertificado(id, contenido);
    }
     
    
    
    
    
    /**
     * Este método es el encargado de traer los certificados de disponibilidad
     * presupuestaria por filtro
     *
     * @param count
     * @param numero
     * @param fecha
     * @param estado
     * @param idPOInsumo
     * @param idFuenteRecursos
     * @param idFuenteFinanciamiento
     * @param idProcesoAdq
     * @param idProgramaPres
     * @param idSubProgramaPres
     * @param idProyecto
     * @param idAccCentral
     * @param idAsigNp
     * @param firstResult
     * @param maxResults
     * @param orderBy
     * @param ascending
     * @return
     */
    public List<CertificadoDisponibilidadPresupuestaria> getCertificadoDispPresupuestaria(
        Integer numero,
        Date fecha,
        EstadoCertificadoDispPresupuestaria estado,
        Integer idPOInsumo,
        Integer idFuenteRecursos,
        Integer idFuenteFinanciamiento,
        Integer idProcesoAdq,
        Integer idProgramaPres,
        Integer idSubProgramaPres,
        Integer idProyecto,
        Integer idAccCentral,
        Integer idAsigNp,
        Integer firstResult,
        Integer maxResults,
        String[] orderBy,
        boolean[] ascending) {

        return bean.getCertificadoDispPresupuestaria( numero, fecha, estado, idPOInsumo, idFuenteRecursos, idFuenteFinanciamiento, idProcesoAdq, idProgramaPres, idSubProgramaPres, idProyecto, idAccCentral, idAsigNp, firstResult, maxResults, orderBy, ascending);

    }

    /**
     * Este método es el encargado de contar los certificados de disponibilidad
     * presupuestaria por filtro
     *
     * @param count
     * @param numero
     * @param fecha
     * @param estado
     * @param idPOInsumo
     * @param idFuenteRecursos
     * @param idFuenteFinanciamiento
     * @param idProcesoAdq
     * @param idProgramaPres
     * @param idSubProgramaPres
     * @param idProyecto
     * @param idAccCentral
     * @param idAsigNp
     * @return
     */
    public long countCertificadoDispPresupuestaria(Integer numero,
        Date fecha,
        EstadoCertificadoDispPresupuestaria estado,
        Integer idPOInsumo,
        Integer idFuenteRecursos,
        Integer idFuenteFinanciamiento,
        Integer idProcesoAdq,
        Integer idProgramaPres,
        Integer idSubProgramaPres,
        Integer idProyecto,
        Integer idAccCentral,
        Integer idAsigNp) {

        return bean.countCertificadoDispPresupuestaria(numero, fecha, estado, idPOInsumo, idFuenteRecursos, idFuenteFinanciamiento, idProcesoAdq, idProgramaPres, idSubProgramaPres, idProyecto, idAccCentral, idAsigNp);
    }

    /**
     * Verifica si un existe un monto fuente entre los que tiene el poInsumo que esté
     * asociado a un certificado de disponibilidad presupuestaria con estado vacío o rechazado
     * @return 
     */
    public Boolean existeMontoFuenteDisponibleParaEnviar(List<POMontoFuenteInsumo> montosFuentes) {
        return bean.existeMontoFuenteDisponibleParaEnviar(montosFuentes);
    }
    
}
