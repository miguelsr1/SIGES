/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.tipos.FiltroClasFunc;
import gob.mined.siap2.entities.tipos.FiltroCronogramaRecursos;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.ws.to.CronogramaRecurso;
import gob.mined.siap2.ws.to.ResumenClasificadorFuncional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Query;
import org.jboss.ws.api.addressing.MAP;

/**
 * Esta clase implementa los métodos de acceso a datos para Insumos
 *
 * @author Sofis Solutions
 */
@JPADAO
public class InsumoDAO extends EclipselinkJpaDAOImp<Insumo, Integer> {

    /**
     * Este método devuelve el máximo número de insumo asignado.
     *
     * @param codigoInsumo
     * @return
     */
    public List<String> getMaxCodeStart(String codigoInsumo) {
        return entityManager.createQuery("SELECT MAX(insumo.codigo) "
                + " FROM Insumo insumo "
                + " WHERE insumo.codigo LIKE :codigoInsumo ")
                .setParameter("codigoInsumo", codigoInsumo + "%")
                .getResultList();
    }

    /**
     * Este método devuelve la lista de insumos que incluye query en el nombre.
     *
     * @param query es una expresión a usar en el like del nombre
     * @return lista de insumos que cumplen la condición ordenadas por nombre.
     */
    public List<Insumo> getInsumos(String query) {
        return entityManager.createQuery("SELECT i"
                + " FROM Insumo i"
                + " WHERE  UPPER(i.nombre) LIKE UPPER(:query)"
                + " ORDER BY i.nombre")
                .setParameter("query", "%" + query + "%")
                .getResultList();
    }
    
    /**
     * Este método devuelve la lista de insumos que incluye query en el nombre.
     *
     * @param query es una expresión a usar en el like del nombre
     * @return lista de insumos que cumplen la condición ordenadas por nombre.
     */
    public List<Insumo> getInsumosByFiltro(String query) {
        return entityManager.createQuery("SELECT i"
                + " FROM Insumo i"
                + " WHERE  UPPER(i.nombre) LIKE UPPER(:query)"
                + " ORDER BY i.nombre")
                .setParameter("query", "%" + query + "%")
                .getResultList();
    }
    
    /**
     * Este método devuelve un registro de insumo filtrado por codigo
     *
     * @param codigo es una expresión a usar en el like del nombre
     * @return lista de insumos que cumplen la condición ordenadas por nombre.
     */
    public Insumo getInsumoByCodigo(String codigo) {
        return (Insumo) entityManager.createQuery("SELECT i FROM Insumo i WHERE i.codigo = :codigo")
                .setParameter("codigo", codigo).getSingleResult();
    }

    /**
     * Este método devuelve la lista de insumos por insumo
     *
     * @param noUACI
     * @param codigoSIIP
     * @param idOEG
     * @param idInsumo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @param firstResult
     * @param maxResults
     * @param orderBy
     * @param ascending
     * @return
     */
    public List<POInsumos> getEstadosInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        Query query = createEstadosInsumosQuery("SELECT poInsumo ", null, noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario, orderBy, ascending);

        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    /**
     * Este método devuelve la cantidad de insumos que satisfacen un criterio
     * dado.
     *
     * @param noUACI
     * @param codigoSIIP
     * @param idOEG
     * @param idInsumo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @return
     */
    public long countEstadosInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {
        Query query = createEstadosInsumosQuery("SELECT count(poInsumo) ", null, noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario, null, null);

        return (long) query.getSingleResult();
    }

    /**
     * Este método devuelve la suma del valor de un campo en insumos que
     * satisfacen un criterio dado.
     *
     * @param idInsumo
     * @param nombreCampo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @return
     */
    public BigDecimal sumarEstadosInsumos(String nombreCampo, Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {
        Query query = createEstadosInsumosQuery("SELECT sum(poInsumo." + nombreCampo + ") ", null, noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario, null, null);
        return (BigDecimal) query.getSingleResult();
    }

    /**
     * Este método suma todo lo pagado por insumo
     *
     * @param nombreCampo
     * @param noUACI
     * @param codigoSIIP
     * @param idOEG
     * @param idInsumo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @return
     */
    public BigDecimal sumarPagadoInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {

        String select = "SELECT sum (pagoInsumos.importe) "
                + " FROM PagoInsumo pagoInsumos "
                + " WHERE  pagoInsumos.contrato.quedanEmitido = TRUE "
                + " AND EXISTS (SELECT 1  ";

        Query query = createEstadosInsumosQuery(select, "pagoInsumos.relacionItemInsumo.insumo.poInsumo.id = poInsumo.id", noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario, null, null);
        return (BigDecimal) query.getSingleResult();
    }

    /**
     * Método que construye la query a partir de distintas condiciones.
     *
     * @param count
     * @param idInsumo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @param orderBy
     * @param ascending
     * @return
     */
    private Query createEstadosInsumosQuery(String select, String extraCondExists, Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario, String[] orderBy, boolean[] ascending) {
        String query = select;// ? "SELECT count(poInsumo) " : "SELECT poInsumo ";

        query = query + " FROM POInsumos poInsumo "
                + " WHERE poInsumo.poa.lineaTrabajo IS NULL ";

        if (!TextUtils.isEmpty(extraCondExists)) {
            query = query + " AND " + extraCondExists;
        }

        if (idOEG != null) {
            query = query + " AND poInsumo.insumo.objetoEspecificoGasto.clasificador = :idOEG ";
        }

        if (idInsumo != null) {
            query = query + " AND poInsumo.id = :idInsumo ";
        }

        if (idAnioFiscal != null) {
            query = query + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal ";
        }
        if (idPOA != null) {
            query = query + " AND poInsumo.poa.id = :idPOA ";
        }

        if (idProyecto != null) {
            query = query + " AND EXISTS (SELECT 1 FROM POAProyecto poaProyecto WHERE poaProyecto.proyecto.id = :idProyecto AND poaProyecto.id = poInsumo.poa.id ) ";
        }

        if (noUACI != null) {
            query = query + " AND poInsumo.noUACI = :noUACI ";
        }

        if (codigoSIIP != null) {
            query = query + " AND EXISTS (SELECT 1 FROM POAProyecto poaProyecto WHERE poaProyecto.proyecto.codigoSIIP = :codigoSIIP AND poaProyecto.id = poInsumo.poa.id ) ";
        }

        if (idProcesoAdq != null) {
            query = query + " AND poInsumo.procesoInsumo.procesoAdquisicion.id = :idProcesoAdq ";
        }

        if (idProgramaPresupuestario != null) {
            query = query + " AND EXISTS (SELECT 1 FROM ProgramaPresupuestario programaPresupuestario, POAProyecto poaProyecto WHERE programaPresupuestario.id = :idProgramaPresupuestario AND poaProyecto.id = poInsumo.poa.id AND programaPresupuestario.id = poaProyecto.proyecto.programaPresupuestario.programaPresupuestario.id ) ";
        }

        if (idSubprogramaPresupuestario != null) {
            query = query + " AND EXISTS (SELECT 1 FROM ProgramaPresupuestario subprogramaPresupuestario, POAProyecto poaProyecto WHERE subprogramaPresupuestario.id = :idSubprogramaPresupuestario AND poaProyecto.id = poInsumo.poa.id AND subprogramaPresupuestario.id = poaProyecto.proyecto.programaPresupuestario.id ) ";
        }

        if (orderBy != null) {
            query = query + " ORDER BY ";

            for (int i = 0; i < orderBy.length; i++) {
                if (i > 0) {
                    query = query + ", ";
                }
                query = query + " poInsumo." + orderBy[i];
                if (!ascending[i]) {
                    query = query + " DESC";
                }
            }
        }

        if (!TextUtils.isEmpty(extraCondExists)) {
            query = query + " ) ";
        }

        Query jpaQuery = entityManager.createQuery(query);

        if (idOEG != null) {
            jpaQuery = jpaQuery.setParameter("idOEG", idOEG);
        }

        if (noUACI != null) {
            jpaQuery = jpaQuery.setParameter("noUACI", noUACI);
        }

        if (codigoSIIP != null) {
            jpaQuery = jpaQuery.setParameter("codigoSIIP", codigoSIIP);
        }

        if (idInsumo != null) {
            jpaQuery = jpaQuery.setParameter("idInsumo", idInsumo);
        }

        if (idAnioFiscal != null) {
            jpaQuery = jpaQuery.setParameter("idAnioFiscal", idAnioFiscal);
        }
        if (idPOA != null) {
            jpaQuery = jpaQuery.setParameter("idPOA", idPOA);
        }
        if (idProyecto != null) {
            jpaQuery = jpaQuery.setParameter("idProyecto", idProyecto);
        }
        if (idProcesoAdq != null) {
            jpaQuery = jpaQuery.setParameter("idProcesoAdq", idProcesoAdq);
        }
        if (idProgramaPresupuestario != null) {
            jpaQuery = jpaQuery.setParameter("idProgramaPresupuestario", idProgramaPresupuestario);
        }
        if (idSubprogramaPresupuestario != null) {
            jpaQuery = jpaQuery.setParameter("idSubprogramaPresupuestario", idSubprogramaPresupuestario);
        }

        return jpaQuery;
    }

    /**
     * Método que construye la query de suma a partir de distintas condiciones.
     *
     * @param count
     * @param idInsumo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @param orderBy
     * @param ascending
     * @return
     */
    /*    private Query createEstadosInsumosQuery(boolean count, String nombreCampo, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, String[] orderBy, boolean[] ascending) {
        String query = "SELECT sum(poInsumo." + nombreCampo + ") ";

        query = query + " FROM POInsumos poInsumo "
            + " WHERE poInsumo.poa.lineaTrabajo IS NULL ";
        
        if (idInsumo != null) {
            query = query + " AND poInsumo.id = :idInsumo ";
        }

        if (idAnioFiscal != null) {
            query = query + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal ";
        }
        if (idPOA != null) {
            query = query + " AND poInsumo.poa.id = :idPOA ";
        }

        if (idProyecto != null) {
            query = query + " AND EXISTS (SELECT 1 FROM POAProyecto poaProyecto WHERE poaProyecto.proyecto.id = :idProyecto AND poaProyecto.id = poInsumo.poa.id ) ";
        }

        if (idProcesoAdq != null) {
            query = query + " AND poInsumo.procesoInsumo.procesoAdquisicion.id = :idProcesoAdq ";
        }

        if (orderBy != null) {
            query = query + " ORDER BY ";

            for (int i = 0; i < orderBy.length; i++) {
                if (i > 0) {
                    query = query + ", ";
                }
                query = query + " poInsumo." + orderBy[i];
                if (!ascending[i]) {
                    query = query + " DESC";
                }
            }
        }

        Query jpaQuery = entityManager.createQuery(query);
        
        if (idInsumo != null) {
            jpaQuery = jpaQuery.setParameter("idInsumo", idInsumo);
        }

        if (idAnioFiscal != null) {
            jpaQuery = jpaQuery.setParameter("idAnioFiscal", idAnioFiscal);
        }
        if (idPOA != null) {
            jpaQuery = jpaQuery.setParameter("idPOA", idPOA);
        }
        if (idProyecto != null) {
            jpaQuery = jpaQuery.setParameter("idProyecto", idProyecto);
        }
        if (idProcesoAdq != null) {
            jpaQuery = jpaQuery.setParameter("idProcesoAdq", idProcesoAdq);
        }

        return jpaQuery;
    }
     */
    /**
     * Este método devuelve un resumen por clasificador funcional del gasto de
     * los insumos según filtro.
     *
     * @param filtro
     * @return
     */
    public Collection<ResumenClasificadorFuncional> obtenerResumen(FiltroClasFunc filtro, String tipo) {

        //contiene el secundo campo del select en adelante
        String select = ", sum(insumo.montoTotal), sum(insumo.montoTotalCertificado), sum(insumo.montoComprometido), sum(insumo.montoPepOriginal),  sum(procesoInsumo.montoTotalAdjudicado), sum(insumo.montoTotal - insumo.montoPepOriginal)";
        String from = " FROM POInsumos insumo LEFT JOIN insumo.procesoInsumo procesoInsumo ";
        String where = " WHERE insumo.poa.lineaTrabajo IS NULL ";
        String grupBy = "";

        switch (tipo) {
            case "UT":
                select = "insumo.actividad.utResponsable.nombre" + select;
                grupBy = "GROUP BY insumo.actividad.utResponsable";
                break;
            case "CF":
                from = from + ", POAProyecto poaProy";
                where = where + " AND insumo.poa.id = poaProy.id ";

                select = "poaProy.proyecto.programaPresupuestario.clasificadorFuncional.nombre" + select;
                grupBy = "GROUP BY poaProy.proyecto.programaPresupuestario.clasificadorFuncional";
                break;
            case "PP":
                from = from + ", POAProyecto poaProy";
                where = where + " AND insumo.poa.id = poaProy.id ";

                select = "poaProy.proyecto.programaPresupuestario.nombre" + select;
                grupBy = "GROUP BY poaProy.proyecto.programaPresupuestario";
                break;
            case "PI":
                from = from + ", POAProyecto poaProy";
                where = where + " AND insumo.poa.id = poaProy.id ";

                select = "poaProy.proyecto.programaInstitucional.nombre" + select;
                grupBy = "GROUP BY poaProy.proyecto.programaInstitucional";
                break;
            case "PR":
                from = from + ", POAProyecto poaProy";
                where = where + " AND insumo.poa.id = poaProy.id ";

                select = "poaProy.proyecto.nombre" + select;
                grupBy = "GROUP BY poaProy.proyecto.nombre";
                break;
            case "PA":
                select = "insumo.procesoInsumo.procesoAdquisicion.nombre" + select;
                grupBy = "GROUP BY insumo.procesoInsumo.procesoAdquisicion.nombre";
                break;
            case "FU":
//                select = " montoAporte.fuente.fuenteRecursos.nombre, sum(montoAporte.monto), sum(case when montoAporte.certificadoDisponibilidadPresupuestariaAprobada = TRUE then montoAporte.certificado else 0 end), 0 ";
                select = "montoAporte.fuente.fuenteRecursos.nombre" + select;
                from = from + ", POMontoFuenteInsumo montoAporte ";
                where = where + " AND montoAporte.insumo.id = insumo.id ";
                grupBy = " GROUP BY montoAporte.fuente.fuenteRecursos.nombre ";
                break;
            case "FF":
//                select = " montoAporte.fuente.fuenteRecursos.fuenteFinanciamiento.nombre, sum(montoAporte.monto), sum(case when montoAporte.certificadoDisponibilidadPresupuestariaAprobada = TRUE then montoAporte.certificado else 0 end), 0 ";
                select = "montoAporte.fuente.fuenteRecursos.fuenteFinanciamiento.nombre" + select;
                from = from + ", POMontoFuenteInsumo montoAporte ";
                where = where + " AND montoAporte.insumo.id = insumo.id ";
                grupBy = " GROUP BY montoAporte.fuente.fuenteRecursos.fuenteFinanciamiento.nombre ";
                break;
            case "OG":
                select = "insumo.insumo.objetoEspecificoGasto.clasificador " + select;
                grupBy = "GROUP BY insumo.insumo.objetoEspecificoGasto.clasificador";
                break;
            case "RU":
                select = "insumo.insumo.objetoEspecificoGasto.clasificador " + select;
                grupBy = "GROUP BY insumo.insumo.objetoEspecificoGasto.clasificador";
                break;
            default:
                break;
        }

        if (filtro != null && !TextUtils.isEmpty(filtro.getIdAnioFiscal())) {
            where = where + " AND insumo.poa.anioFiscal.id = :idAnioFiscal ";
        }

        String consulta = "SELECT " + select
                + from
                + where
                + grupBy;

        Collection<ResumenClasificadorFuncional> respuesta = new ArrayList<>();

        Query query = entityManager.createQuery(consulta);
        if (filtro != null && !TextUtils.isEmpty(filtro.getIdAnioFiscal())) {
            query.setParameter("idAnioFiscal", NumberUtils.getIntegerONull(filtro.getIdAnioFiscal()));
        }

        List resultado = query.getResultList();

        if (tipo.equals("RU")) {
            //Cargo un Map con clave = rubro y valor = list con todos los oeg cuyo 2 primeros caracteres coinciden con el rubro
            Map<String, List<Object>> mapRubroResultados = new HashMap<>();
            for (Object o : resultado) {
                Object[] elemento = (Object[]) o;
                String nombre = elemento[0] + "";
                String rubro = nombre.substring(0, 2);
                if (mapRubroResultados.containsKey(rubro)) {
                    mapRubroResultados.get(rubro).add(o);
                } else {
                    List<Object> listaObject = new LinkedList<>();
                    listaObject.add(o);
                    mapRubroResultados.put(rubro, listaObject);
                }
            }
            for (Map.Entry<String, List<Object>> entry : mapRubroResultados.entrySet()) {
                String clave = entry.getKey();
                List<Object> valor = entry.getValue();
                ResumenClasificadorFuncional rcf = new ResumenClasificadorFuncional();
                rcf.setNombreClasificador(clave);
                BigDecimal importeEstimado = BigDecimal.ZERO;
                BigDecimal importeComprometido = BigDecimal.ZERO;
                BigDecimal importePep = BigDecimal.ZERO;
                BigDecimal importeAdjudicado = BigDecimal.ZERO;
                BigDecimal importeModificado = BigDecimal.ZERO;
                for (Object o : valor) {
                    Object[] elemento = (Object[]) o;
                    if (elemento[1] != null) {
                        importeEstimado = importeEstimado.add((BigDecimal) elemento[1]);
                    }
                    if (elemento[3] != null) {
                        importeComprometido = importeComprometido.add((BigDecimal) elemento[3]);
                    }
                    if (elemento[4] != null) {
                        importePep = importePep.add((BigDecimal) elemento[4]);
                    }
                    if (elemento[5] != null) {
                        importeAdjudicado = importeAdjudicado.add((BigDecimal) elemento[5]);
                    }
                    if (elemento[6] != null) {
                        importeModificado = importeModificado.add((BigDecimal) elemento[6]);
                    }
                }
                rcf.setImporteEstimado(importeEstimado);
                rcf.setImporteComprometido(importeComprometido);
                rcf.setPep(importePep);
                rcf.setImporteAdjudicado(importeAdjudicado);
                rcf.setImporteModificado(importeModificado);
                respuesta.add(rcf);
            }
        } else {
            for (Object o : resultado) {
                ResumenClasificadorFuncional rcf = new ResumenClasificadorFuncional();
                Object[] elemento = (Object[]) o;
                String nombre = elemento[0] + "";
                rcf.setNombreClasificador(nombre);
                rcf.setImporteEstimado((BigDecimal) elemento[1]);
                //if (!tipo.equals("FU") && !tipo.equals("FF")) {
                rcf.setImporteComprometido((BigDecimal) elemento[3]);
                rcf.setPep((BigDecimal) elemento[4]);
                rcf.setImporteAdjudicado((BigDecimal) elemento[5]);
                rcf.setImporteModificado((BigDecimal) elemento[6]);
                //}
                respuesta.add(rcf);
            }
        }

        return respuesta;
    }

    public List getCertificadoDisponibilidadPresupuestariaInsumos(Integer firstResult, Integer maxResults) {
        Query query = createCertificadosDisponibilidadPresupuestariaQuery(false);
        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public long countCertificadoDisponibilidadPresupuestariaInsumos() {
        Query query = createCertificadosDisponibilidadPresupuestariaQuery(true);
        return (long) query.getSingleResult();
    }

    private Query createCertificadosDisponibilidadPresupuestariaQuery(boolean count) {
        String query = "";
        if (count) {
            query = "SELECT COUNT(ut.id) ";
        } else {
            query = "SELECT ut.id, proceso.id, ut.nombre, proceso.nombre, proceso.anio.anio ";
        }

        query = query
                + " FROM ProcesoAdquisicion proceso, "
                + " UnidadTecnica ut "
                + " WHERE  EXISTS ( SELECT 1 FROM POInsumos poInsumo WHERE  poInsumo.poa.unidadTecnica.id = ut.id AND  poInsumo.procesoInsumo.procesoAdquisicion.id = proceso.id ) ";

        Query jpaQuery = entityManager.createQuery(query);

        return jpaQuery;
    }

    /**
     * Retorna las fuentes de los POAs que perteneces a una UT y están dentro de
     * un proceso de adquisición
     *
     * @param idUT
     * @param idProcesoAdq
     * @return
     */
    public List<POInsumos> getInsumosUTEnProceso(Integer idUT, Integer idProcesoAdq) {
        return entityManager.createQuery(" SELECT poInsumo FROM POInsumos poInsumo "
                + " WHERE  poInsumo.poa.unidadTecnica.id = :idUT"
                + " AND poInsumo.procesoInsumo.procesoAdquisicion.id = :idProcesoAdq")
                .setParameter("idUT", idUT)
                .setParameter("idProcesoAdq", idProcesoAdq)
                .getResultList();
    }

    public List<POInsumos> getInsumosNoUACI(Set<Integer> listaIdUTUsuarioActual, Integer idAnioFiscal, Integer idProyecto, Integer idAC, Integer idANP, Integer idUT, String codigo, String codigoInterno, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        Query query = createInsumosNoUACIQuery("SELECT poInsumo ", null, listaIdUTUsuarioActual, idAnioFiscal, idProyecto, idAC, idANP, idUT, codigo, codigoInterno, orderBy, ascending);

        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    private Query createInsumosNoUACIQuery(String select, String extraCondExists, Set<Integer> listaIdUTUsuarioActual, Integer idAnioFiscal, Integer idProyecto, Integer idAC, Integer idANP, Integer idUT, String codigo, String codigoInterno, String[] orderBy, boolean[] ascending) {
        String query = select;

        query = query + " FROM POInsumos poInsumo "
                + " WHERE poInsumo.noUACI = true";

        if (!TextUtils.isEmpty(extraCondExists)) {
            query = query + " AND " + extraCondExists;
        }

        if (listaIdUTUsuarioActual != null) {
            query = query + " AND poInsumo.unidadTecnica.id IN :listaIdUTUsuarioActual ";
        }

        if (idAnioFiscal != null) {
            query = query + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal ";
        }

        if (idProyecto != null) {
            query = query + " AND EXISTS (SELECT 1 FROM POAProyecto poaProyecto WHERE poaProyecto.proyecto.id = :idProyecto AND poaProyecto.id = poInsumo.poa.id ) ";
        }

        if (idAC != null) {
            query = query + " AND EXISTS (SELECT 1 FROM POAConActividades poaConAct WHERE poaConAct.conMontoPorAnio.id = :idAC AND poaConAct.id = poInsumo.poa.id AND poaConAct.conMontoPorAnio.tipo = :tipoPoaAC) ";
        }

        if (idANP != null) {
            query = query + " AND EXISTS (SELECT 1 FROM POAConActividades poaConAct WHERE poaConAct.conMontoPorAnio.id = :idANP AND poaConAct.id = poInsumo.poa.id AND poaConAct.conMontoPorAnio.tipo = :tipoPoaANP) ";
        }

        if (idUT != null) {
            query = query + " AND poInsumo.unidadTecnica.id = :idUT ";
        }

        if (codigo != null) {
            query = query + " AND poInsumo.insumo.codigo LIKE :codigo ";
        }

        if (codigoInterno != null) {
            query = query + " AND CAST (poInsumo.id AS VARCHAR2(255)) = :codigoInterno ";
        }

        if (orderBy != null) {
            query = query + " ORDER BY ";

            for (int i = 0; i < orderBy.length; i++) {
                if (i > 0) {
                    query = query + ", ";
                }
                query = query + " poInsumo." + orderBy[i];
                if (!ascending[i]) {
                    query = query + " DESC";
                }
            }
        }

        if (!TextUtils.isEmpty(extraCondExists)) {
            query = query + " ) ";
        }

        Query jpaQuery = entityManager.createQuery(query);

        if (listaIdUTUsuarioActual != null) {
            jpaQuery = jpaQuery.setParameter("listaIdUTUsuarioActual", listaIdUTUsuarioActual);
        }

        if (idAnioFiscal != null) {
            jpaQuery = jpaQuery.setParameter("idAnioFiscal", idAnioFiscal);
        }

        if (idProyecto != null) {
            jpaQuery = jpaQuery.setParameter("idProyecto", idProyecto);
        }

        if (idAC != null) {
            jpaQuery = jpaQuery.setParameter("idAC", idAC);
            jpaQuery = jpaQuery.setParameter("tipoPoaAC", TipoMontoPorAnio.ACCION_CENTRAL);
        }

        if (idANP != null) {
            jpaQuery = jpaQuery.setParameter("idANP", idANP);
            jpaQuery = jpaQuery.setParameter("tipoPoaANP", TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE);
        }

        if (idUT != null) {
            jpaQuery = jpaQuery.setParameter("idUT", idUT);
        }

        if (codigo != null) {
            jpaQuery = jpaQuery.setParameter("codigo", codigo);
        }

        if (codigoInterno != null) {
            jpaQuery = jpaQuery.setParameter("codigoInterno", codigoInterno);
        }

        return jpaQuery;
    }

    public long countInsumosNoUACI(Set<Integer> listaIdUTUsuarioActual, Integer idAnioFiscal, Integer idProyecto, Integer idAC, Integer idANP, Integer idUT, String codigo, String codigoInterno) {
        Query query = createInsumosNoUACIQuery("SELECT count(poInsumo) ", null, listaIdUTUsuarioActual, idAnioFiscal, idProyecto, idAC, idANP, idUT, codigo, codigoInterno, null, null);
        return (long) query.getSingleResult();
    }

    public List<PolizaDeConcentracion> getPolizasDePoInsumo(Integer idPoInsumo) {
        return entityManager.createQuery(" SELECT poliza FROM PolizaDeConcentracion poliza "
                + " WHERE  poliza.fuente.insumo.id = :idPoInsumo")
                .setParameter("idPoInsumo", idPoInsumo)
                .getResultList();
    }

    /**
     * Este método devuelve el cronograma de recursos según filtros
     *
     * @param filtro
     * @param tipoReporte
     * @return
     */
    public Collection<CronogramaRecurso> obtenerCronogramaRecrusos(FiltroCronogramaRecursos filtro, String tipoReporte) {
        //contiene el segundo campo del select en adelante
        //lo planificado: si es UACI se suma lo adjudicado, si es no UACI se suma lo certificado 
        String select = ", sum(CASE WHEN insumo.noUACI = FALSE AND procesoInsumo.montoTotalAdjudicado IS NOT NULL THEN (procesoInsumo.montoTotalAdjudicado) "
                + "WHEN insumo.noUACI = TRUE AND insumo.pasoValidacionCertificadoDeDispPresupuestaria = TRUE THEN (insumo.montoTotalCertificado) "
                + "ELSE 0 END), "
                + "sum(insumo.montoEnActasRecepcionAprobadas), sum(insumo.montoEnQUEDAN), "
                //Suma el monto en QUEDAN (si es distinto de null) o el monto en Pólizas (si es distinto de null)
                + "sum(CASE WHEN insumo.montoEnQUEDAN IS NOT NULL THEN (insumo.montoEnQUEDAN) "
                + "WHEN insumo.montoEnPolizasAplicadas IS NOT NULL THEN (insumo.montoEnPolizasAplicadas) "
                + "ELSE 0 END) ";
//                + "sum(CASE WHEN pago.relacionItemInsumo.insumo.poInsumo.id = insumo.id AND " 
//                + "pago.contrato.anioPago.anio <= 2017 AND pago.contrato.mesPago.mes <= 11 AND pago.importe IS NOT NULL THEN (pago.importe) "
//                + "ELSE 0 END) ";
        String from = " FROM POInsumos insumo LEFT JOIN insumo.procesoInsumo procesoInsumo, POAProyecto poaProy ";
        String where = " WHERE insumo.poa.lineaTrabajo IS NULL ";
        String grupBy = "";

        switch (tipoReporte) {
            case "UT":
                select = "insumo.actividad.utResponsable.nombre" + select;
                grupBy = "GROUP BY insumo.actividad.utResponsable";
                break;
            case "CF":
//                from = from + ", POAProyecto poaProy";
                where = where + " AND insumo.poa.id = poaProy.id ";

                select = "poaProy.proyecto.programaPresupuestario.clasificadorFuncional.nombre" + select;
                grupBy = "GROUP BY poaProy.proyecto.programaPresupuestario.clasificadorFuncional";
                break;
            case "PP":
//                from = from + ", POAProyecto poaProy";
                where = where + " AND insumo.poa.id = poaProy.id ";

                select = "poaProy.proyecto.programaPresupuestario.nombre" + select;
                grupBy = "GROUP BY poaProy.proyecto.programaPresupuestario";
                break;
            case "PI":
//                from = from + ", POAProyecto poaProy";
                where = where + " AND insumo.poa.id = poaProy.id ";

                select = "poaProy.proyecto.programaInstitucional.nombre" + select;
                grupBy = "GROUP BY poaProy.proyecto.programaInstitucional";
                break;
            case "PR":
//                from = from + ", POAProyecto poaProy";
                where = where + " AND insumo.poa.id = poaProy.id ";

                select = "poaProy.proyecto.nombre" + select;
                grupBy = "GROUP BY poaProy.proyecto.nombre";
                break;
            case "PA":
                select = "insumo.procesoInsumo.procesoAdquisicion.nombre" + select;
                grupBy = "GROUP BY insumo.procesoInsumo.procesoAdquisicion.nombre";
                break;
            case "FU":
                select = "montoAporte.fuente.fuenteRecursos.nombre" + select;
                from = from + ", POMontoFuenteInsumo montoAporte ";
                where = where + " AND montoAporte.insumo.id = insumo.id ";
                grupBy = " GROUP BY montoAporte.fuente.fuenteRecursos.nombre ";
                break;
            case "FF":
                select = "montoAporte.fuente.fuenteRecursos.fuenteFinanciamiento.nombre" + select;
                from = from + ", POMontoFuenteInsumo montoAporte ";
                where = where + " AND montoAporte.insumo.id = insumo.id ";
                grupBy = " GROUP BY montoAporte.fuente.fuenteRecursos.fuenteFinanciamiento.nombre ";
                break;
            case "OG":
                select = "insumo.insumo.objetoEspecificoGasto.clasificador " + select;
                grupBy = "GROUP BY insumo.insumo.objetoEspecificoGasto.clasificador";
                break;
            case "RU":
                select = "insumo.insumo.objetoEspecificoGasto.clasificador " + select;
                grupBy = "GROUP BY insumo.insumo.objetoEspecificoGasto.clasificador";
                break;
            default:
                break;
        }

        if (filtro != null) {
            if (!TextUtils.isEmpty(filtro.getIdAnioFiscal())) {
                where = where + " AND insumo.poa.anioFiscal.id = :idAnioFiscal ";
            }
            if (!TextUtils.isEmpty(filtro.getIdFuenteFinanciamiento())) {
                if (!tipoReporte.equals("FU") && !tipoReporte.equals("FF")) {
                    from = from + ", POMontoFuenteInsumo montoAporte ";
                }
                where = where + " AND montoAporte.insumo.id = insumo.id AND montoAporte.fuente.fuenteRecursos.fuenteFinanciamiento.id = :idFuenteFinanciamiento ";
            }
            if (!TextUtils.isEmpty(filtro.getIdFuenteRecursos())) {
                where = where + " AND montoAporte.insumo.id = insumo.id AND montoAporte.fuente.fuenteRecursos.id = :idFuenteRecursos ";
            }
            if (!TextUtils.isEmpty(filtro.getIdPAC())) {
                where = where + " AND insumo.pacGrupo.pac.id = :idPAC ";
            }
            if (!TextUtils.isEmpty(filtro.getIdPOA())) {
                where = where + " AND insumo.poa.id = :idPOA ";
            }
            if (!TextUtils.isEmpty(filtro.getIdProyecto())) {
                where = where + " AND poaProy.proyecto.id = :idProyecto AND poaProy.id = insumo.poa.id ";
            }
            // El rubro son los primeros 2 dígitos del OEG
            if (filtro.getRubro() != null && filtro.getRubro().toString().length() == 2) {
                where = where + " AND SUBSTRING(insumo.insumo.objetoEspecificoGasto.clasificador, 1, 2) = :rubro ";
            }
            if (!TextUtils.isEmpty(filtro.getTipoInsumo())) {
                where = where + " AND insumo.noUACI = :tipoInsumo ";
            }
        }

        String consulta = "SELECT " + select
                + from
                + where
                + grupBy;

        Collection<CronogramaRecurso> respuesta = new ArrayList<>();

        Query query = entityManager.createQuery(consulta);
        if (filtro != null) {
            if (!TextUtils.isEmpty(filtro.getIdAnioFiscal())) {
                query.setParameter("idAnioFiscal", NumberUtils.getIntegerONull(filtro.getIdAnioFiscal()));
            }
            if (!TextUtils.isEmpty(filtro.getIdFuenteFinanciamiento())) {
                query.setParameter("idFuenteFinanciamiento", NumberUtils.getIntegerONull(filtro.getIdFuenteFinanciamiento()));
            }
            if (!TextUtils.isEmpty(filtro.getIdFuenteRecursos())) {
                query.setParameter("idFuenteRecursos", NumberUtils.getIntegerONull(filtro.getIdFuenteRecursos()));
            }
            if (!TextUtils.isEmpty(filtro.getIdPAC())) {
                query.setParameter("idPAC", NumberUtils.getIntegerONull(filtro.getIdPAC()));
            }
            if (!TextUtils.isEmpty(filtro.getIdPOA())) {
                query.setParameter("idPOA", NumberUtils.getIntegerONull(filtro.getIdPOA()));
            }
            if (!TextUtils.isEmpty(filtro.getIdProyecto())) {
                query.setParameter("idProyecto", NumberUtils.getIntegerONull(filtro.getIdProyecto()));
            }
            if (filtro.getRubro() != null && filtro.getRubro().toString().length() == 2) {
                query.setParameter("rubro", filtro.getRubro());
            }
            if (!TextUtils.isEmpty(filtro.getTipoInsumo())) {
                Boolean tipoNoUACI = filtro.getTipoInsumo().equals("NOUACI");
                query.setParameter("tipoInsumo", tipoNoUACI);
            }
        }

        List resultado = query.getResultList();

        if (tipoReporte.equals("RU")) {
            //Cargo un Map con clave = rubro y valor = list con todos los oeg cuyo 2 primeros caracteres coinciden con el rubro
            Map<String, List<Object>> mapRubroResultados = new HashMap<>();
            for (Object o : resultado) {
                Object[] elemento = (Object[]) o;
                String nombre = elemento[0] + "";
                String rubro = nombre.substring(0, 2);
                if (mapRubroResultados.containsKey(rubro)) {
                    mapRubroResultados.get(rubro).add(o);
                } else {
                    List<Object> listaObject = new LinkedList<>();
                    listaObject.add(o);
                    mapRubroResultados.put(rubro, listaObject);
                }
            }
            for (Map.Entry<String, List<Object>> entry : mapRubroResultados.entrySet()) {
                String clave = entry.getKey();
                List<Object> valor = entry.getValue();
                CronogramaRecurso cr = new CronogramaRecurso();
                cr.setNombreClasificador(clave);
                BigDecimal montoPlanificado = BigDecimal.ZERO;
                BigDecimal montoActas = BigDecimal.ZERO;
                BigDecimal montoQUEDAN = BigDecimal.ZERO;
                BigDecimal montoPagado = BigDecimal.ZERO;
                Boolean ejecutadoMenorAPlanificado = Boolean.TRUE;
                for (Object o : valor) {
                    Object[] elemento = (Object[]) o;
                    if (elemento[1] != null) {
                        montoPlanificado = montoPlanificado.add((BigDecimal) elemento[1]);
                    }
                    if (elemento[2] != null) {
                        montoActas = montoActas.add((BigDecimal) elemento[2]);
                    }
                    if (elemento[3] != null) {
                        montoQUEDAN = montoQUEDAN.add((BigDecimal) elemento[3]);
                    }
                    if (elemento[4] != null) {
                        montoPagado = montoPagado.add((BigDecimal) elemento[4]);
                    }
                }
                cr.setMontoPlanificado(montoPlanificado);
                cr.setMontoActas(montoActas);
                cr.setMontoQuedan(montoQUEDAN);
                BigDecimal montoProyectado = montoPlanificado.subtract(montoPagado);
                cr.setMontoProyectado(montoProyectado);
                if (montoPlanificado != null && montoPagado != null) {
                    ejecutadoMenorAPlanificado = montoPagado.compareTo(montoPlanificado) < 0;
                }
                cr.setEjecutadoMenorAPlanificado(ejecutadoMenorAPlanificado);
                respuesta.add(cr);
            }
        } else {
            for (Object o : resultado) {
                CronogramaRecurso cr = new CronogramaRecurso();
                Object[] elemento = (Object[]) o;
                String nombre = elemento[0] + "";

                BigDecimal montoPlanificado = (BigDecimal) elemento[1];
                BigDecimal montoActas = (BigDecimal) elemento[2];
                BigDecimal montoQUEDAN = (BigDecimal) elemento[3];
                BigDecimal montoPagado = (BigDecimal) elemento[4];
                BigDecimal montoProyectado = montoPlanificado.subtract(montoPagado);             
                Boolean ejecutadoMenorAPlanificado = Boolean.TRUE;

                cr.setNombreClasificador(nombre);
                cr.setMontoPlanificado(montoPlanificado);
                cr.setMontoActas(montoActas);
                cr.setMontoQuedan(montoQUEDAN);
                cr.setMontoProyectado(montoProyectado);
                if (montoPlanificado != null && montoPagado != null) {
                    ejecutadoMenorAPlanificado = montoPagado.compareTo(montoPlanificado) < 0;
                }
                cr.setEjecutadoMenorAPlanificado(ejecutadoMenorAPlanificado);
                
                respuesta.add(cr);
            }
        }
        return respuesta;
    }
}
