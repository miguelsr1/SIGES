/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.ConMontoPorAnio;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.tipos.FiltroMA;
import gob.mined.siap2.entities.tipos.FiltroReprogramacion;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase incluye los métodos de acceso a datos de un POA con actividades.
 * @author Sofis Solutions
 */
@JPADAO
public class PoaConActividadesDAO extends EclipselinkJpaDAOImp<POAConActividades, Integer> {

    /**
     * Ese método devuelve los POA de trabajo
     * @param idConMontoPorAnio
     * @param idAnioFiscal año fiscal
     * @param idUnidadTecnica id de la unidad técnica
     * @return lista de POA con actividades.
     */
    public List<POAConActividades> getPOATrabajoConMontoPorAnio(Integer idConMontoPorAnio, Integer idAnioFiscal, Integer idUnidadTecnica) {
        return entityManager.createQuery("SELECT poa"
            + " FROM POAConActividades poa"
            + " WHERE poa.conMontoPorAnio.id =:idConMontoPorAnio "
            + " AND poa.anioFiscal.id =:idAnioFiscal"
            + " AND poa.unidadTecnica.id =:idUnidadTecnica"
            + " AND poa.lineaTrabajo IS NULL ")
            .setParameter("idConMontoPorAnio", idConMontoPorAnio)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("idUnidadTecnica", idUnidadTecnica)
            .getResultList();
    }

    /**
     * Este método devuelve los POA de un año fiscal de una unidad técnica.
     * @param idConMontoPorAnio
     * @param idAnioFiscal año fiscal
     * @param idUnidadTecnica id de la unidad técnica
     * @return lista de POA con Actividades que satisfacen los requisitos.
     */
    public List<POAConActividades> getPOATrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, Integer idUnidadTecnica) {
        return entityManager.createQuery("SELECT poa"
            + " FROM POAConActividades poa"
            + " WHERE poa.conMontoPorAnio.id =:idConMontoPorAnio "
            + " AND poa.anioFiscal.id =:idAnioFiscal"
            + " AND poa.unidadTecnica.id =:idUnidadTecnica"
            + " AND poa.lineaTrabajo IS NULL ")
            .setParameter("idConMontoPorAnio", idConMontoPorAnio)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("idUnidadTecnica", idUnidadTecnica)
            .getResultList();
    }

    /**
     * Este método devuelve los POA de trabajo según un criterio
     * @param idConMontoPorAnio
     * @param idAnioFiscal id del año fiscal.
     * @param estado estado del POA
     * @param tipo tipo de POA
     * @return lista de POA que satisfacen el criterio indicado
     */
    public List<POAConActividades> getPOAsTrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, EstadoPOA estado, TipoMontoPorAnio tipo) {
        String consulta =" SELECT poa  FROM POAConActividades poa WHERE  poa.lineaTrabajo IS NULL";
        
        if (idConMontoPorAnio!= null){
            consulta= consulta+ " AND poa.conMontoPorAnio.id =:idConMontoPorAnio ";
        }
        if (idAnioFiscal!= null){
            consulta= consulta+  " AND poa.anioFiscal.id =:idAnioFiscal";
        } 
        if (estado!= null){
            consulta= consulta + " AND poa.estado = :estado";
        }        
        if (tipo!= null){
            consulta= consulta + " AND poa.conMontoPorAnio.tipo = :tipo";
        }
        
        Query query = entityManager.createQuery(consulta);
        
        
        if (idConMontoPorAnio!= null){
           query.setParameter("idConMontoPorAnio", idConMontoPorAnio);
        }
        if (idAnioFiscal!= null){
             query.setParameter("idAnioFiscal", idAnioFiscal);
        } 
        if (estado!= null){
            query.setParameter("estado", estado);
        }
        if (tipo!= null){
            query.setParameter("tipo", tipo);
        }
        
        return query.getResultList();
    }
    
    
    
    /**
     * Retorna todos los POAs consolidados
     * @param filtrarUT
     * @param utAfiltrar
     * @param idAnioFiscal
     * @param tipo
     * @return 
     */
    public List<POAConActividades> getPOAsParaReprogramacion(boolean filtrarUT, List<UnidadTecnica> utAfiltrar, Integer idAnioFiscal, TipoMontoPorAnio tipo) {
       if (filtrarUT && (utAfiltrar == null || utAfiltrar.isEmpty())){
            return new LinkedList();
        }
        String consulta ="SELECT poa"
            + " FROM POAConActividades poa"
            + " WHERE poa.lineaTrabajo IS NULL "
            + " AND (poa.anioFiscal.id =:anio) "
            + " AND (poa.estado =:estado) "
            + " AND (poa.cierreAnual IS NULL OR poa.cierreAnual = :false)";
        
        if (filtrarUT){
            consulta = consulta + " AND poa.unidadTecnica IN :utAfiltrar ";
        }
        if (tipo!= null){
            consulta= consulta + " AND poa.conMontoPorAnio.tipo = :tipo";
        }
        Query q= entityManager.createQuery(consulta);
        q.setParameter("anio", idAnioFiscal);
        q.setParameter("estado", EstadoPOA.TERMINO_CONSOLIDACION);
        q.setParameter("false", Boolean.FALSE);
        
        if (filtrarUT){
            q.setParameter("utAfiltrar", utAfiltrar);
        }   
        if (tipo!= null){
            q.setParameter("tipo", tipo);
        }
        return q.getResultList();
    }
    
    /**
     * Este método devuelve la lista de POA con actividades para el año fiscal indicado.
     * @param idConMontoPorAnio
     * @param idAnioFiscal año fiscal.
     * @return 
     */
    public List<POAConActividades> getPOALineaBase(Integer idConMontoPorAnio, Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa.lineaBase "
            + " FROM POAConActividades  poa"
            + " WHERE poa.conMontoPorAnio.id =:idConMontoPorAnio "
            + " AND poa.anioFiscal.id =:idAnioFiscal"
            + " AND poa.lineaTrabajo IS NULL ")
            .setParameter("idConMontoPorAnio", idConMontoPorAnio)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .getResultList();
    }
    
    /**
     * Este método determina si un POA está completado para consolidar.
     * @param idConMontoPorAnio
     * @param idAnioFiscal año fiscal.
     * @return indicador de si está completo true = si
     */
    public boolean isCompletoParaConsolidado(Integer idConMontoPorAnio, Integer idAnioFiscal) {
        List result = entityManager.createQuery("SELECT poa "
            + " FROM POAConActividades poa"
            + " WHERE poa.conMontoPorAnio.id =:idConMontoPorAnio "
            + " AND poa.anioFiscal.id =:idAnioFiscal"
            + " AND poa.lineaTrabajo IS NULL"
            + " AND (poa.estado = :estado1"
            + " OR poa.estado = :estado2)")
            .setParameter("idConMontoPorAnio", idConMontoPorAnio)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("estado1", EstadoPOA.EDITANDO_UT)
            .setParameter("estado2", EstadoPOA.VALIDANDO_POA)
            .getResultList();
        return result == null || result.isEmpty();
    }

    /**
     * Este método devuelve los POA línea base según criterio.
     * @param idConMontoPorAnio
     * @param idAnioFiscal año fiscal.
     * @param idUnidadTecnica unidad técnica
     * @return lista de POA línea base que satisfacen el criterio.
     */
    public List<POAConActividades> getPOASBase(Integer idConMontoPorAnio, Integer idAnioFiscal, Integer idUnidadTecnica) {
        return entityManager.createQuery("SELECT poa"
            + " FROM  POAConActividades poa"
            + " WHERE poa.conMontoPorAnio.id =:idConMontoPorAnio "
            + " AND poa.anioFiscal.id =:idAnioFiscal"
            + " AND poa.unidadTecnica.id =:idUnidadTecnica"
            + " AND poa.lineaTrabajo IS NOT NULL"
            + " ORDER BY poa.fechaFijacion DESC")
            .setParameter("idConMontoPorAnio", idConMontoPorAnio)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("idUnidadTecnica", idUnidadTecnica)
            .getResultList();
    }

    /**
     * Este método devuelve los montos por año según filtro.
     * @param filtro
     * @return 
     */
    public Collection<ConMontoPorAnio> obtenerConMontoPorAnioPorFiltro(FiltroMA filtro) {
        String sql = "select s from ConMontoPorAnio s ";
        boolean conAnd = false;
        String where = "";
        if (filtro != null) {
            //TODO tienen que ser de la UT del usuario y que estén en el POA del año en ejecución
        }
        Query query = entityManager.createQuery(sql);
        return query.getResultList();
    }
    
    /**
     * Este método guarda una reprogramación y actualiza el estado si es la primera vez que se guarda.
     * @param reprog repgoramación a guardar.
     * @return reprogramación actualizada.
     */
    public Reprogramacion guardarReprogramacion(Reprogramacion reprog) {
        //TODO Revisar
        if (reprog != null) {
             
            if (reprog.getRep_detalle_lista() != null) {
                for (ReprogramacionDetalle det : reprog.getRep_detalle_lista()) {
                    det.setReprogramacionId(reprog);

                }
            }
            if (reprog.getId() == null) {
                reprog.setEstado(EstadoReprogramacion.FORMULACION);
                entityManager.persist(reprog);
            } else {
                entityManager.merge(reprog);
            }

        }
        return reprog;
    }

    /**
     * Este método guarda una línea de detalle de una reprogramación.
     * @param reprog línea de detalle de una reprogramación
     * @return línea de reprogramación actualizada.
     */
    public ReprogramacionDetalle guardarReprogramacionDetalle(ReprogramacionDetalle reprog) {
        //TODO Revisar
        if (reprog != null) {
            if (reprog.getId() == null) {
                entityManager.persist(reprog);
            } else {
                reprog = entityManager.merge(reprog);
            }
        }
        return reprog;
    }

    /**
     * Este método permite obtener una reprogramación por filtro.
     * @param filtro filtro con las condiciones a aplicar.
     * @return colección de reprogramaciones que satisfacen el filtro.
     */
    public Collection<Reprogramacion> obtenerReprogramacionesPorFiltro(FiltroReprogramacion filtro) {
        String consulta="select s from Reprogramacion s ";
        boolean conAnd=false;
        String where="";
        if (filtro!=null) {
            if (filtro.getEstado()!=null) {
                where = where +" (s.estado= :estado) ";
                conAnd=true;
            }
            if (filtro.getTipoReprog()!=null) {
                if (conAnd) {
                    where = where +" and ";
                }
                where = where +" (s.tipoReprogramacion= :tipoReprogramacion) ";
                conAnd=true;
            }
            if (filtro.getUnidadTecnica()!=null) {
                if (conAnd) {
                    where = where +" and ";
                }
                where = where +" (s.rep_detalle_lista.poa.unidadTecnica.id=:unidadTecnica) ";
            }
        }
        if (!StringUtils.isEmpty(where)) {
            consulta = consulta +" where "+where;
        }
        Query query = entityManager.createQuery(consulta);
        if (filtro!=null){
            if (filtro.getEstado()!=null){
                query.setParameter("estado", filtro.getEstado());
            }
            if (filtro.getTipoReprog()!=null){
                query.setParameter("tipoReprogramacion", filtro.getTipoReprog());
            }
            if (filtro.getUnidadTecnica()!=null) {
                query.setParameter("unidadTecnica", filtro.getUnidadTecnica().getId());
            }
        }
        return query.getResultList();
    }
    
    /**
     * Este método devuelve los grupos de un PAC según POA
     * @param id del POA
     * @return lista de grupos del PAC
     */
    public Collection<PACGrupo> obtenerGruposPACPorPOAId(Integer id) {
        String consulta =" select s from PACGrupo s join s.pac.poas p"
                +" where (p.id=:id) ";
        return entityManager.createQuery(consulta).setParameter("id", id)
                .getResultList();
    }


}
