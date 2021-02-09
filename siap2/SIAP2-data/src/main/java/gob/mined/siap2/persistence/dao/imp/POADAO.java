/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.ObjetoEspecificoGasto;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POARiesgo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.tipos.FiltroRiesgo;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siap2.ws.to.RiesgoTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase incluye los métodos de acceso a datos de POA
 * @author Sofis Solutions
 */
@JPADAO
public class POADAO extends EclipselinkJpaDAOImp<GeneralPOA, Integer> {

    /**
     * retorna la lista de POAs pertenecientes al proyecto para el año fiscal
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @return
     */
    public List<POAProyecto> getPOASTrabajoProyecto(Integer idProyecto, Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa FROM POAProyecto poa "
            + " WHERE poa.proyecto.id = :idProyecto "
            + " AND poa.anioFiscal.id = :idAnioFiscal "
            + " AND poa.lineaTrabajo IS NULL")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .getResultList();
    }

    /**
     * retorna la lista de de UT que tienen POA para el proyecto y el año fiscal
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @return
     */
    public List<UnidadTecnica> getUTConPOA(Integer idProyecto, Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa.unidadTecnica FROM POAProyecto poa "
            + " WHERE poa.proyecto.id = :idProyecto "
            + " AND poa.anioFiscal.id = :idAnioFiscal "
            + " AND poa.lineaTrabajo IS NULL")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .getResultList();

    }

    /**
     * suma el monto usado en un aporte, para todos los POA (POA de trabajo) de
     * un proyecto, que coinciden con el año fiscal Notar que excluye un POA
     * pasado por parámetro (que es el que se esta por guardar)
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @param idPOAAExcluir
     * @return
     */
    public BigDecimal suparUsadoPorPOAsEnTrabajo(Integer idProyecto, Integer idAnioFiscal, Integer idFuenteRecursos, Integer idPOAAExcluir) {
        return (BigDecimal) entityManager.createQuery("SELECT SUM(montoAporte.monto) "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " join insumo.montosFuentes montoAporte"
            + " WHERE poa.lineaTrabajo IS NULL "
            + " AND poa.id <> :idPOAAExcluir "
            + " AND poa.proyecto.id = :idProyecto"
            + " AND poa.anioFiscal.id = :idAnioFiscal "
            + " AND montoAporte.fuente.fuenteRecursos.id = :idFuenteRecursos")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("idFuenteRecursos", idFuenteRecursos)
            .setParameter("idPOAAExcluir", idPOAAExcluir)
            .getSingleResult();
    }

    /**
     * suma el monto usado en un aporte, para todos los POA (POA de trabajo) de
     * un proyecto, que coinciden con el año fiscal Notar que excluye un insumo
     * pasado por parámetro
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @param idPOAAExcluir
     * @return
     */
    public BigDecimal suparUsadoPorPOAsYExchulirInsumoEnTrabajo(Integer idProyecto, Integer idAnioFiscal, Integer idFuenteRecursos, Integer idInsumoAExcluir) {
        return (BigDecimal) entityManager.createQuery("SELECT SUM(montoAporte.monto) "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " join insumo.montosFuentes montoAporte"
            + " WHERE poa.lineaTrabajo IS NULL "
            + " AND insumo.id <> :idInsumoAExcluir "
            + " AND poa.proyecto.id = :idProyecto"
            + " AND poa.anioFiscal.id = :idAnioFiscal "
            + " AND montoAporte.fuente.fuenteRecursos.id = :idFuenteRecursos")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("idFuenteRecursos", idFuenteRecursos)
            .setParameter("idInsumoAExcluir", idInsumoAExcluir)
            .getSingleResult();
    }

    public BigDecimal totalizarAportesEnPOATrabajo(Integer idProyecto,Integer idEstructura, Integer idAporte, Integer idInsumoAExcluir) {
        return (BigDecimal) entityManager.createQuery("SELECT SUM(montoAporte.monto) "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " join insumo.montosFuentes montoAporte"
            + " WHERE poa.lineaTrabajo IS NULL "
            + " AND linea.producto.proyectoEstructura.id = :idEstructura"
            + " AND insumo.id <> :idInsumoAExcluir "
            + " AND poa.proyecto.id = :idProyecto"
            + " AND montoAporte.fuente.id = :idAporte")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idEstructura", idEstructura)
            .setParameter("idAporte", idAporte)
            .setParameter("idInsumoAExcluir", idInsumoAExcluir)
            .getSingleResult();
    }

    /**
     * suma el monto usado en un aporte, para todos los POA (POA de la línea
     * base) de un proyecto, que coinciden con el año fiscal Notar que excluye
     * un POA pasado por parámetro. Este POA tiene que ser el id del POA de la
     * línea trabajo que se esta por transformar en nueva base
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @param idPOAAExcluir
     * @return
     */
    public BigDecimal suparUsadoPorPOAsEnBase(Integer idProyecto, Integer idEstructuraProyecto, Integer idAporte, Integer idPOAAExcluir) {
        String query = "SELECT SUM(montoAporte.monto) "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " join insumo.montosFuentes montoAporte"
            + " WHERE ( poa.lineaTrabajo IS NOT NULL ) AND ( poa.lineaTrabajo.lineaTrabajo IS NULL ) "            
            + " AND poa.proyecto.id = :idProyecto"
            + " AND linea.producto.proyectoEstructura.id = :idEstructuraProyecto"
            + " AND montoAporte.fuente.id = :idAporte";
        
        if (idPOAAExcluir != null){
            query =  query + " AND poa.lineaTrabajo.id <> :idPOAAExcluir ";
        }
        
        Query q = entityManager.createQuery(query)
            .setParameter("idProyecto", idProyecto)
            .setParameter("idEstructuraProyecto", idEstructuraProyecto)
            .setParameter("idAporte", idAporte);
        
        if (idPOAAExcluir!= null){            
            q.setParameter("idPOAAExcluir", idPOAAExcluir);
        }
        
        
        return (BigDecimal) q.getSingleResult();
    }

    /**
     * suma el monto usado por la distribución de los insumos dentro de una
     * categoría
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @param idPOAAExcluir
     * @return
     */
    public BigDecimal suparUsadoEnTramoExluirInsumo(Integer idProyectoAporteTramoTramo, Integer idInsumo) {
        return (BigDecimal) entityManager.createQuery("SELECT SUM(montoAporte.monto) "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " join insumo.montosFuentes montoAporte"
            + " WHERE poa.lineaTrabajo IS NULL "
            + " AND insumo.id != :idInsumo"
            + " AND insumo.tramo.id = :idProyectoAporteTramoTramo")
            .setParameter("idInsumo", idInsumo)
            .setParameter("idProyectoAporteTramoTramo", idProyectoAporteTramoTramo)
            .getSingleResult();
 
    }
    
    
        /**
     * suma el monto usado por la distribución de los insumos dentro de una
     * categoría
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @param idPOAAExcluir
     * @return
     */
    public BigDecimal suparUsadoEnTramoExluirPOA(Integer idProyectoAporteTramoTramo, Integer idPOA) {
        String sql = "SELECT SUM(montoAporte.monto) "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " join insumo.montosFuentes montoAporte"
            + " WHERE poa.lineaTrabajo IS NULL "
            + " AND insumo.tramo.id = :idProyectoAporteTramoTramo";
        
        if (idPOA != null){
            sql = sql + " AND poa.id != :idPOA";
        }
        Query query = entityManager.createQuery(sql)            
            .setParameter("idProyectoAporteTramoTramo", idProyectoAporteTramoTramo);
        
        if (idPOA != null){
            query.setParameter("idPOA", idPOA);
        }
        
        return (BigDecimal) query.getSingleResult();
    }
    
    
    
    
    
    
    
    /**
     * Este método devuelve los objetos específicos del gasto para la inclusión en la PEP para un año fiscal.
     * @param idAnioFiscal
     * @return 
     */
    public List<ObjetoEspecificoGasto> getOEGsParaPEPEnProyectos(Integer idAnioFiscal) {
        return entityManager.createQuery(
            " SELECT insumo.insumo.objetoEspecificoGasto "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " WHERE  poa.lineaTrabajo IS NULL "
            + " AND poa.proyecto.pep = :TRUE "
            + " AND poa.anioFiscal.id = :idAnioFiscal "
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("TRUE", true)
            .getResultList();

    }

    /**
     * Este método devuelve los objetos específicos del gasto para la inclusión en la PEP para un año fiscal.
     * @param idAnioFiscal
     * @return 
     */
    public List<ObjetoEspecificoGasto> getOEGsParaPEPEnProyectos(Integer idAnioFiscal, Integer idProyecto) {
        return entityManager.createQuery(
            " SELECT insumo.insumo.objetoEspecificoGasto "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " WHERE  poa.lineaTrabajo IS NULL "
            + " AND poa.proyecto.pep = :TRUE "
            + " AND poa.anioFiscal.id = :idAnioFiscal "
            + " AND poa.proyecto.id = :idProyecto "
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("TRUE", true)
            .setParameter("idProyecto", idProyecto)
            .getResultList();

    }

    
    /**
     * Este método devuelve todos los objetos específicos del gasto de los insumos con monto para un año dado.
     * @param idAnioFiscal
     * @return 
     */
    public List<ObjetoEspecificoGasto> getOEGsParaPEPEnConMontoPorAnio(Integer idAnioFiscal) {
        return entityManager.createQuery(
            " SELECT insumo.insumo.objetoEspecificoGasto "
            + " FROM POAConActividades poa "
            + " join poa.actividades actividad"
            + " join actividad.insumos insumo"
            + " WHERE  poa.lineaTrabajo IS NULL "
            + " AND poa.anioFiscal.id = :idAnioFiscal  "
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .getResultList();

    }

    /**
     * Este método devuelve todos los objetos específicos del gasto de los insumos con monto para un año dado.
     * @param idAnioFiscal
     * @return 
     */
    public List<ObjetoEspecificoGasto> getOEGsParaPEPEnConMontoPorAnio(Integer idAnioFiscal, Integer idProyecto) {
        return entityManager.createQuery(
            " SELECT insumo.insumo.objetoEspecificoGasto "
            + " FROM POAConActividades poa "
            + " join poa.actividades actividad"
            + " join actividad.insumos insumo"
            + " WHERE  poa.lineaTrabajo IS NULL "
            + " AND poa.anioFiscal.id = :idAnioFiscal  "
            + " AND poa.id = :idProyecto  "
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("idProyecto", idProyecto)
            .getResultList();

    }
    
    /**
     * Este método devuelve un objeto específico de gasto, filtrado por obj_clasificador
     * @param idClasificador
     * @return 
     */
    public ObjetoEspecificoGasto getOEGByCodigo(Integer idClasificador) {
        return (ObjetoEspecificoGasto) entityManager.createQuery("SELECT g from ObjetoEspecificoGasto g where g.clasificador = :idClasificador")
            .setParameter("idClasificador", idClasificador)
            .getSingleResult();
    }

    
    /**
     * retorna los insumos a los que les corresponde PEP
     * @param idAnioFiscal
     * @return 
     */
    public List<POInsumos> getInsumosParaPEP(Integer idAnioFiscal) {
        return entityManager.createQuery(
            " SELECT poInsumo "
            + " FROM POInsumos poInsumo "
            + " WHERE poInsumo.poa.lineaTrabajo IS NULL "
            + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal  "
            + " AND ( "
                + "( poInsumo.poa.tipo =:poaConActividades) "
                + " OR (poInsumo.poa.id IN (SELECT poaProy.id FROM POAProyecto poaProy WHERE poaProy.proyecto.pep = :TRUE ))"
            + ")"
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("TRUE", true)
            .setParameter("poaConActividades", TipoPOA.POA_CON_ACTIVIDADES)
            .getResultList();

    }

    
    
        /**
     * Este método devuelve la lista de insumos PARA PEP sin FCM
     *
     * @param anio
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<POInsumos> getInsumosPEPISinFCM(Integer idAnioFiscal, Integer firstResult, Integer maxResults) {
        return entityManager.createQuery(
            " SELECT poInsumo "
            + " FROM POInsumos poInsumo "
            + " WHERE poInsumo.poa.lineaTrabajo IS NULL "
            + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal "
            + " AND poInsumo.flujosDeCajaAnio IS EMPTY  "
            + " AND ( "
                + "( poInsumo.poa.tipo =:poaConActividades) "
                + " OR (poInsumo.poa.id IN (SELECT poaProy.id FROM POAProyecto poaProy WHERE poaProy.proyecto.pep = :TRUE ))"
            + ")"
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("TRUE", true)
            .setParameter("poaConActividades", TipoPOA.POA_CON_ACTIVIDADES)
            .setFirstResult(firstResult)
            .setMaxResults(maxResults)
            .getResultList();
        
    }
    /**
     * Este método calcula la cantidad de insumos UACI sin flujo asociado en un
     * determinado año.
     *
     * @param anio
     * @return
     */
    public long countInsumosPEPISinFCM(Integer idAnioFiscal) {
                return (long)  entityManager.createQuery(
            " SELECT count(poInsumo) "
            + " FROM POInsumos poInsumo "
            + " WHERE poInsumo.poa.lineaTrabajo IS NULL "
            + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal "
            + " AND poInsumo.flujosDeCajaAnio IS EMPTY  "
            + " AND ( "
                + "( poInsumo.poa.tipo =:poaConActividades) "
                + " OR (poInsumo.poa.id IN (SELECT poaProy.id FROM POAProyecto poaProy WHERE poaProy.proyecto.pep = :TRUE ))"
            + ")"
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("TRUE", true)
            .setParameter("poaConActividades", TipoPOA.POA_CON_ACTIVIDADES)
            .getSingleResult();
    }
    
    /**
     * Este método devuelve todos los grupos de PAC que no tienen flujo asociado
     * en un determinado año.
     *
     * @param anio
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<PACGrupo> getPEPGrupoSinFCM(Integer idAnioFiscal, Integer firstResult, Integer maxResults) {
        return entityManager.createQuery("SELECT DISTINCT(g) FROM PAC p, p.grupos g, g.insumos poInsumo"
            + " WHERE poInsumo.poa.lineaTrabajo IS NULL "
            + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal "
            + " AND poInsumo.flujosDeCajaAnio IS EMPTY  "
            + " AND ( "
                + "( poInsumo.poa.tipo =:poaConActividades) "
                + " OR (poInsumo.poa.id IN (SELECT poaProy.id FROM POAProyecto poaProy WHERE poaProy.proyecto.pep = :TRUE ))"
            + ")"
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("TRUE", true)
            .setParameter("poaConActividades", TipoPOA.POA_CON_ACTIVIDADES)
            .setFirstResult(firstResult)
            .setMaxResults(maxResults)
            .getResultList();
    }
    /**
     * Este método calcula la cantidad de grupos de PAC sin cerrar en un
     * determinado año.
     *
     * @param anio
     * @return
     */
    public long countPEPGrupoSinFCM(Integer idAnioFiscal) {
        return (long) entityManager.createQuery("SELECT count(DISTINCT(g)) FROM PAC p, p.grupos g, g.insumos poInsumo"
            + " WHERE poInsumo.poa.lineaTrabajo IS NULL "
            + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal "
            + " AND poInsumo.flujosDeCajaAnio IS EMPTY  "
            + " AND ( "
                + "( poInsumo.poa.tipo =:poaConActividades) "
                + " OR (poInsumo.poa.id IN (SELECT poaProy.id FROM POAProyecto poaProy WHERE poaProy.proyecto.pep = :TRUE ))"
            + ")"
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("TRUE", true)
            .setParameter("poaConActividades", TipoPOA.POA_CON_ACTIVIDADES)
            .getSingleResult();
    }

    /**
     * retorna los insumos que tienen pendiente cargar pep
     * 
     * @param idAnioFiscal
     * @return 
     */
    
    public List<POInsumos> getInsumosPEPPendientesCompletarFCM(Integer idAnioFiscal) {
                return entityManager.createQuery(
            " SELECT poInsumo "
            + " FROM POInsumos poInsumo "
            + " WHERE poInsumo.poa.lineaTrabajo IS NULL "
            + " AND poInsumo.poa.anioFiscal.id = :idAnioFiscal  "
            + " AND ( "
                + "( poInsumo.poa.tipo =:poaConActividades) "
                + " OR (poInsumo.poa.id IN (SELECT poaProy.id FROM POAProyecto poaProy WHERE poaProy.proyecto.pep = :TRUE ))"
            + " ) "
            + " AND poInsumo.montoTotal <> "
                + "(SELECT SUM (flujoCajaMenusal.monto) "
                + "FROM POInsumos iterInsumo "
                + "join iterInsumo.flujosDeCajaAnio flujosDeCajaAnio "
                + "join flujosDeCajaAnio.flujoCajaMenusal flujoCajaMenusal "
                + "WHERE iterInsumo.id = poInsumo.id "
                + ")"
            
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("TRUE", true)
            .setParameter("poaConActividades", TipoPOA.POA_CON_ACTIVIDADES)
            .getResultList();       
        
       
    }
    
    public List<Object[]> getFCMEnOEGParaPEP(Integer idAnioFiscal, Integer anio, Integer clasificador) {
        return entityManager.createQuery(
            "  SELECT flujoCajaMenusal.mes, SUM(flujoCajaMenusal.monto) "
            + " FROM POInsumos insumo"
            + " join insumo.flujosDeCajaAnio flujoCaja "
            + " join flujoCaja.flujoCajaMenusal  flujoCajaMenusal"
            + " WHERE  insumo.poa.lineaTrabajo IS NULL "
            + " AND insumo.poa.anioFiscal.id = :idAnioFiscal "
            + " AND insumo.insumo.objetoEspecificoGasto.clasificador = :clasificador"
            + " AND flujoCaja.anio = :anio   "
            + " AND ("
                + " (insumo.poa.tipo != :poaPRoyecto ) "
                + " OR "
                + " (insumo.poa.tipo =:poaPRoyecto AND EXISTS (SELECT 1 FROM POAProyecto poa WHERE poa.id = insumo.poa.id AND poa.proyecto.pep = :TRUE))"
                + " )   "
            + " GROUP BY flujoCajaMenusal.mes "
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("anio", anio)
            .setParameter("TRUE", true)
            .setParameter("poaPRoyecto", TipoPOA.POA_PROYECTO)
            .setParameter("clasificador", clasificador)
            .getResultList();

    }

    /**
     * Este método devuelve los POA de trabajo en un año dado.
     * @param idAnioFiscal
     * @return 
     */
    public List<GeneralPOA> getPOAsTrabajoEnAnioFiscal(Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa FROM GeneralPOA poa "
            + " WHERE poa.anioFiscal.id = :idAnioFiscal "
            + " AND poa.lineaTrabajo IS NULL")
            .setParameter("idAnioFiscal", idAnioFiscal)
            .getResultList();
    }

    /**
     * Este método devuelve los POA que no estén cerrados en un año especifico.
     * @param idAnioFiscal
     * @return 
     */
    public List<GeneralPOA> getPOANoCierreEnAnioFiscal(Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa FROM GeneralPOA poa "
            + " WHERE poa.anioFiscal.id = :idAnioFiscal "
            + " AND poa.cierreAnual = FALSE AND poa.lineaTrabajo IS NULL")
            .setParameter("idAnioFiscal", idAnioFiscal)
            .getResultList();
    }

    /**
     * Este método devuelve el POA de un proyecto.
     * @param idPoInsumo id del insumo en el POA.
     * @return 
     */
    public POAProyecto obtenerPOAProyectoPorIdPoInsumo(Integer idPoInsumo) {
        List<POAProyecto> listPoaProy = entityManager.createQuery("SELECT poa "
            + " FROM POAProyecto poa "
            + " join poa.lineas linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " WHERE  insumo.id = :idPoInsumo ")
            .setParameter("idPoInsumo", idPoInsumo)
            .getResultList();
        POAProyecto poaProy = null;
        if (!listPoaProy.isEmpty()) {
            poaProy = listPoaProy.get(0);
        }
        return poaProy;
    }

    /**
     * este método devuelve un POA con actividades en el que está un insumo específico.
     * @param idPoInsumo
     * @return 
     */
    public POAConActividades obtenerPOAConActividadesPorIdPoInsumo(Integer idPoInsumo) {
        List<POAConActividades> lisPoaActividad = entityManager.createQuery("SELECT poa "
            + " FROM POAConActividades poa "
            + " join poa.actividades actividad"
            + " join actividad.insumos insumo"
            + " WHERE  insumo.id = :idPoInsumo ")
            .setParameter("idPoInsumo", idPoInsumo)
            .getResultList();
        POAConActividades poaAct = null;
        if (!lisPoaActividad.isEmpty()) {
            poaAct = lisPoaActividad.get(0);
        }
        return poaAct;
    }

    /**
     * Este método devuelve una línea de POA para un id de insumo en un POA específico
     * @param idPoInsumo
     * @return 
     */
    public POLinea obtenerPOLineaPorIdPoInsumo(Integer idPoInsumo) {
        List<POLinea> listPoaProy = entityManager.createQuery("SELECT linea "
            + " FROM POLinea linea "
            + " join linea.actividades actividad"
            + " join actividad.insumos insumo"
            + " WHERE  insumo.id = :idPoInsumo ")
            .setParameter("idPoInsumo", idPoInsumo)
            .getResultList();
        POLinea poaProy = null;
        if (!listPoaProy.isEmpty()) {
            poaProy = listPoaProy.get(0);
        }
        return poaProy;
    }

    /**
     * Este método permite obtener los riesgos para un filtro dado.
     * @param filtro
     * @return 
     */
    public Collection<RiesgoTO> obtenerRiesgosPorFiltro(FiltroRiesgo filtro) {
        Collection<RiesgoTO> respuesta = new ArrayList();
        String consulta = "select poa  FROM POAProyecto poa  ";
        String where = "";
        boolean conAnd = false;
        if (filtro != null) {
            if (filtro.getAnioFiscal() != null) {
                if (conAnd) {
                    where = where + " and ";
                }
                where = where + " ( poa.anioFiscal.id = :idAnioFiscal )";
                conAnd = true;
            }
            if (filtro.getProgramaPresupuestario() != null) {
                if (conAnd) {
                    where = where + " and ";
                }
                where = where + " ( poa.proyecto.programaPresupuestario.id = :idProgramaPresupuestario)";
                conAnd = true;
            }
            if (filtro.getProgramaInstitucional() != null) {
                if (conAnd) {
                    where = where + " and ";
                }
                where = where + " ( poa.proyecto.programaInstitucional.id = :idProgramaInstitucional)";
                conAnd = true;
            }
        }
        if (!StringUtils.isEmpty(where)) {
            consulta = consulta + " where " + where;
        }
         
        Query query = entityManager.createQuery(consulta);
        if (filtro != null) {
            if (filtro.getAnioFiscal() != null) {
                query.setParameter("idAnioFiscal", filtro.getAnioFiscal().getId());
            }
            if (filtro.getProgramaInstitucional() != null) {
                query.setParameter("idProgramaInstitucional", filtro.getProgramaInstitucional().getId());
            }
            if (filtro.getProgramaPresupuestario() != null) {
                query.setParameter("idProgramaPresupuestario", filtro.getProgramaPresupuestario().getId());
            }
        }
        List<POAProyecto> resultado = query.getResultList();
        for (POAProyecto poa : resultado) {
            for (POARiesgo rie : poa.getRiesgos()) {

                RiesgoTO riesgo = new RiesgoTO();
                riesgo.setNombreProyecto(poa.getProyecto().getNombre());
                riesgo.setAnioFiscal(poa.getAnioFiscal());
                riesgo.setProgramaInstitucional(poa.getProyecto().getProgramaInstitucional());
                riesgo.setProgramaPresupuestario(poa.getProyecto().getProgramaPresupuestario());
                riesgo.setRiesgo(rie);
                riesgo.setUnidadTecnica(poa.getUnidadTecnica());
                respuesta.add(riesgo);
            }
        }
        return respuesta;
    }

        
    /**
     * Este método devuelve la suma de los insumos en un proyecto dado en un año dado.
     * @param idAnioFiscal
     * @param idProyectoEstProducto
     * @return 
     */
    public BigDecimal sumarInsumosEnProducto(Integer idAnioFiscal, Integer idProyectoEstProducto) {
        
        return (BigDecimal) entityManager.createQuery("SELECT SUM(insumo.montoTotal) "
                + " FROM POAProyecto poa "
                + " join poa.lineas linea "
                + " join linea.actividades actividad"
                + " join actividad.insumos insumo"
                + " WHERE poa.lineaTrabajo IS NULL "
                + " AND poa.anioFiscal.id = :idAnioFiscal "
                + " AND linea.producto.id = :idProyectoEstProducto")
                .setParameter("idProyectoEstProducto", idProyectoEstProducto)
                .setParameter("idAnioFiscal", idAnioFiscal)
                .getSingleResult();
    }
}
