/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.data.impl.CertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.PagoInsumo;
import gob.mined.siap2.entities.enums.EstadoCertificadoDispPresupuestaria;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.jdom.Text;

/**
 * Esta clase incluye los métodos de acceso a datos para la Administración de
 * los certificados de disponibilidad presupuestaria.
 *
 * @author Sofis Solutions
 */
@JPADAO
public class CertificadoPresupuestarioDAO extends EclipselinkJpaDAOImp<CertificadoDisponibilidadPresupuestaria, Integer> {

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

        Query query = armarQueryCertificadoDispPresupuestaria(false,
            numero,
            fecha,
            estado,
            idPOInsumo,
            idFuenteRecursos,
            idFuenteFinanciamiento,
            idProcesoAdq,
            idProgramaPres,
            idSubProgramaPres,
            idProyecto,
            idAccCentral,
            idAsigNp,
            orderBy,
            ascending);

        return query.setFirstResult(firstResult)
            .setMaxResults(maxResults)
            .getResultList();

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
    public long countCertificadoDispPresupuestaria(
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
        Integer idAsigNp) {

        Query query = armarQueryCertificadoDispPresupuestaria(true,
            numero,
            fecha,
            estado,
            idPOInsumo,
            idFuenteRecursos,
            idFuenteFinanciamiento,
            idProcesoAdq,
            idProgramaPres,
            idSubProgramaPres,
            idProyecto,
            idAccCentral,
            idAsigNp,
            null,
            null);

        return (long) query.getSingleResult();

    }

    private Query armarQueryCertificadoDispPresupuestaria(boolean count,
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
        String[] orderBy,
        boolean[] ascending) {

        String query = count ? "SELECT COUNT(DISTINCT (certificado))" : "SELECT DISTINCT(certificado)";
        query = query + " FROM CertificadoDisponibilidadPresupuestaria certificado join certificado.fuentes fuentes ";

        List<String> condiciones = new LinkedList();

        if (numero != null) {
            condiciones.add("certificado.numero = :numero");
        }

        if (fecha != null) {
            condiciones.add("certificado.fecha >= :fechaD AND certificado.fecha <= :fechaH");
        }
        
        if (estado != null) {
            condiciones.add("certificado.estado = :estado");
        }

        if (idPOInsumo != null) {
            condiciones.add("fuentes.insumo.id = :idPOInsumo");
        }

        if (idFuenteRecursos != null) {
            condiciones.add("fuentes.fuente.fuenteRecursos.id = :idFuenteRecursos");
        }

        if (idFuenteFinanciamiento != null) {
            condiciones.add("fuentes.fuente.fuenteRecursos.fuenteFinanciamiento.id = :idFuenteFinanciamiento");
        }

        if (idProcesoAdq != null) {
            condiciones.add("fuentes.insumo.procesoInsumo.procesoAdquisicion.id = :idProcesoAdq");
        }

        if (idSubProgramaPres != null) {
            condiciones.add("EXISTS ( "
                + " SELECT 1 FROM POAProyecto poa "
                + " WHERE fuentes.insumo.poa.id =poa.id "
                + " AND poa.proyecto.programaPresupuestario.id = :idSubProgramaPres) ");
        }

        if (idProgramaPres != null) {
            condiciones.add("EXISTS ( "
                + " SELECT 1 FROM POAProyecto poa "
                + " WHERE fuentes.insumo.poa.id =poa.id "
                + " AND poa.proyecto.programaPresupuestario.programaPresupuestario.id = :idProgramaPres) ");
        }

        if (idProyecto != null) {
            condiciones.add("EXISTS ( SELECT 1 FROM POAProyecto poa WHERE poa.proyecto.id = :idProyecto AND fuentes.insumo.poa.id =poa.id) ");
        }

        if (idAccCentral != null) {
            condiciones.add("EXISTS ( SELECT 1 FROM POAConActividades poa WHERE poa.conMontoPorAnio.id = :idAccCentral AND fuentes.insumo.poa.id =poa.id) ");
        }

        if (idAsigNp != null) {
            condiciones.add("EXISTS ( SELECT 1 FROM POAConActividades poa WHERE poa.conMontoPorAnio.id = :idAsigNp AND fuentes.insumo.poa.id =poa.id) ");
        }

        if (!condiciones.isEmpty()) {
            query = query + " WHERE " + TextUtils.join(" AND ", condiciones);
        }

        if (orderBy != null) {
            query = query + " ORDER BY ";

            for (int i = 0; i < orderBy.length; i++) {
                if (i > 0) {
                    query = query + ", ";
                }
                query = query + " certificado." + orderBy[i];
                if (!ascending[i]) {
                    query = query + " DESC";
                }
            }
        }

        Query q = entityManager.createQuery(query);

        if (numero != null) {
            q.setParameter("numero", numero);
        }

        if (fecha != null) {
            q.setParameter("fechaD", DatesUtils.getStartOfDay(fecha));
            q.setParameter("fechaH", DatesUtils.getEndOfDay(fecha));
        }
        
        if (estado != null) {
            q.setParameter("estado", estado);
        }


        if (idPOInsumo != null) {
            q.setParameter("idPOInsumo", idPOInsumo);
        }

        if (idFuenteRecursos != null) {
            q.setParameter("idFuenteRecursos", idFuenteRecursos);
        }

        if (idFuenteFinanciamiento != null) {
            q.setParameter("idFuenteFinanciamiento", idFuenteFinanciamiento);
        }

        if (idProcesoAdq != null) {
            q.setParameter("idProcesoAdq", idProcesoAdq);
        }

        if (idSubProgramaPres != null) {
            q.setParameter("idSubProgramaPres", idSubProgramaPres);
        }

        if (idProgramaPres != null) {
            q.setParameter("idProgramaPres", idProgramaPres);
        }

        if (idProyecto != null) {
            q.setParameter("idProyecto", idProyecto);
        }

        if (idAccCentral != null) {
            q.setParameter("idAccCentral", idAccCentral);
        }

        if (idAsigNp != null) {
            q.setParameter("idAsigNp", idAsigNp);
        }

        return q;
    }

}
