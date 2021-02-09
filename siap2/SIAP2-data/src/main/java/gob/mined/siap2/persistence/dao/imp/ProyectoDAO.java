/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.tipos.FiltroReprogramacion;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase implementa los métodos de acceso a datos de Proyectos
 *
 * @author Rodrigo
 */
@JPADAO
public class ProyectoDAO extends EclipselinkJpaDAOImp<Proyecto, Integer> {

    /**
     * Este método devuelve el proyecto con un id dado
     *
     * @param primaryKey
     * @return
     */
    public Proyecto find(Integer primaryKey) {
        return entityManager.find(Proyecto.class, primaryKey);
    }

    /**
     * Este método devuelve los proyectos que tienen valores de indicadores
     *
     * @param idAnioFiscal id del año fiscal.
     * @return lista de proyectos
     */
    public List<Proyecto> getProyectosConPOAParaValoresIndicadores(Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT p FROM Proyecto p "
            + " WHERE EXISTS (SELECT 1 FROM p.pOAProyectos poa WHERE poa.anioFiscal.id = :idAnioFiscal )"
        )
            .setParameter("idAnioFiscal", idAnioFiscal)
            .getResultList();
    }

    /**
     * Este método devuelve los POA de trabajo para un proyecto dado de una
     * unidad técnica en un año fiscal dado.
     *
     * @param idProyecto id del proyecto
     * @param idAnioFiscal id del año fiscal
     * @param idUnidadTecnica id de la unidad técnica
     * @return lista de POAs de proyectos.
     */
    public List<POAProyecto> getPOATrabajo(Integer idProyecto, Integer idAnioFiscal, Integer idUnidadTecnica) {
        return entityManager.createQuery("SELECT poa"
            + " FROM POAProyecto poa"
            + " WHERE poa.proyecto.id =:idProyecto "
            + " AND poa.anioFiscal.id =:anio"
            + " AND poa.unidadTecnica.id =:idUnidadTecnica"
            + " AND poa.lineaTrabajo IS NULL ")
            .setParameter("idProyecto", idProyecto)
            .setParameter("anio", idAnioFiscal)
            .setParameter("idUnidadTecnica", idUnidadTecnica)
            .getResultList();
    }

    /**
     * Este método devuelve los POA de trabajo para un proyecto dado de una
     * unidad técnica en un año fiscal dado.
     * Ordenados por año fiscal
     *
     * @param idProyecto id del proyecto
     * @param idUnidadTecnica id de la unidad técnica
     * @return lista de POA de proyectos.
     */
    public List<POAProyecto> getPOATrabajoADuplicar(Integer idProyecto, Integer idUnidadTecnica) {
        return entityManager.createQuery("SELECT poa"
            + " FROM POAProyecto poa"
            + " WHERE poa.proyecto.id =:idProyecto "
            + " AND poa.unidadTecnica.id =:idUnidadTecnica"
            + " AND poa.lineaTrabajo IS NULL "
            + " ORDER BY poa.anioFiscal.anio")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idUnidadTecnica", idUnidadTecnica)
            .getResultList();
    }
    
    /**
     * Retorna la línea a la que pertenece una actividad
     * 
     * @param idActividad
     * @return 
     */
    public POLinea getLineaDeActividad(Integer idActividad) {
        return (POLinea) entityManager.createQuery("SELECT linea"
            + " FROM POLinea linea join linea.actividades actividad"
            + " WHERE actividad.id =:idActividad ")
            .setParameter("idActividad", idActividad)
            .getSingleResult();
    }
    
    /**
     * Este método devuelve la lista de poa de trabajo de un proyecto dado en un
     * determinado estado.
     *
     * @param idProyecto id del proyecto
     * @param idAnioFiscal id del año fiscal
     * @param estadoPOA estado del POA
     * @return lista de POA
     */
    public List<POAProyecto> getPOAsTrabajo(Integer idProyecto, Integer idAnioFiscal, EstadoPOA estadoPOA) {
        String consulta = "SELECT poa"
            + " FROM POAProyecto poa"
            + " WHERE poa.lineaTrabajo IS NULL ";
        
        
        if (idProyecto != null) {
            consulta = consulta + " AND (poa.proyecto.id=:idProyecto)";
        }
        if (idAnioFiscal != null) {
           consulta = consulta + " AND (poa.anioFiscal.id =:anio) ";
        }
        if (estadoPOA != null) {
            consulta = consulta + " AND (poa.estado =:estado) ";
        }        

        Query query = entityManager.createQuery(consulta);
        if (idProyecto != null) {
            query.setParameter("idProyecto", idProyecto);
        }
        if (idAnioFiscal != null) {
            query.setParameter("anio", idAnioFiscal);
        }
        if (estadoPOA != null) {
            query.setParameter("estado", estadoPOA);
        }
        return query.getResultList();

    }
    
    /**
     * Retorna todos los POAs consolidados
     * @param filtrarUT
     * @param utAfiltrar
     * @param idAnioFiscal
     * @return 
     */
    public List<POAProyecto> getPOAsParaReprogramacion(boolean filtrarUT, List<UnidadTecnica> utAfiltrar, Integer idAnioFiscal) {
        if (filtrarUT && (utAfiltrar == null || utAfiltrar.isEmpty())){
            return new LinkedList();
        }
        
        String consulta ="SELECT poa "
            + " FROM POAProyecto poa "
            + " WHERE poa.lineaTrabajo IS NULL "
            + " AND poa.anioFiscal.id =:anio "
            + " AND poa.estado =:estado "
            + " AND (poa.cierreAnual IS NULL OR poa.cierreAnual = :false)";
        
        if (filtrarUT){
            consulta = consulta + " AND poa.unidadTecnica IN :utAfiltrar ";
        }
        
        Query q= entityManager.createQuery(consulta);
        
        q.setParameter("anio", idAnioFiscal);
        
        q.setParameter("estado", EstadoPOA.TERMINO_CONSOLIDACION);
        
        q.setParameter("false", Boolean.FALSE);
        
        
        if (filtrarUT){
            q.setParameter("utAfiltrar", utAfiltrar);
        }   
        
        return q.getResultList();
    }
    
    /**
     * Este método devuelve la lista de UT pendientes para consolidar.
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @return
     */
    public List<UnidadTecnica> getUTPendientesParaConsolidar(Integer idProyecto, Integer idAnioFiscal) {
        return entityManager.createQuery("SELECT poa.unidadTecnica"
            + " FROM POAProyecto poa"
            + " WHERE poa.proyecto.id =:idProyecto "
            + " AND poa.anioFiscal.id =:anio"
            + " AND poa.lineaTrabajo IS NULL"
            + " AND poa.estado <> :estado")
            .setParameter("idProyecto", idProyecto)
            .setParameter("anio", idAnioFiscal)
            .setParameter("estado", EstadoPOA.CONSOLIDANDO_POA)
            .getResultList();
    }

    /**
     * retorna las unidades técnicas que compariteorn su POA con alguna de las
     * UT pasadas por parámetro
     *
     * @param idProyecto
     * @param anio
     * @param idUnidadTecnica
     * @return
     */
    public List<UnidadTecnica> getQueCompartieron(Integer idProyecto, Integer idAnioFiscal, List<UnidadTecnica> utdisponibles) {
        return entityManager.createQuery("SELECT DISTINCT( poa.unidadTecnica) "
            + " FROM POAProyecto poa"
            + " join poa.lineas linea"
            + " join linea.colaboradoras colaborador"
            + " WHERE poa.proyecto.id =:idProyecto "
            + " AND poa.anioFiscal.id  =:idAnioFiscal"
            + " AND poa.lineaTrabajo IS NULL"
            + " AND colaborador IN :utdisponibles ")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("utdisponibles", utdisponibles)
            .getResultList();
    }

    /**
     * retorna si tiene permiso el usuario para la UT
     *
     * @param idProyecto
     * @param anio
     * @param idUnidadTecnica
     * @return
     */
    public List<UnidadTecnica> esColaborador(List<UnidadTecnica> utdisponibles, Integer idPOA) {
        return entityManager.createQuery("SELECT  poa "
            + " FROM POAProyecto poa, poa.lineas linea, linea.colaboradoras colaborador"
            + " WHERE poa.id =:idPOA "
            + " AND colaborador IN :utdisponibles ")
            .setParameter("idPOA", idPOA)
            .setParameter("utdisponibles", utdisponibles)
            .getResultList();
    }

    /**
     * retorna la lista de POAs en la línea base
     *
     * @param idAccionCentral
     * @param idAnioFiscal
     * @param idUnidadTecnica
     * @return
     */
    public List<POAProyecto> getPOASBase(Integer idProyecto, Integer idAnioFiscal, Integer idUnidadTecnica) {
        return entityManager.createQuery("SELECT poa"
            + " FROM POAProyecto poa"
            + " WHERE poa.proyecto.id =:idProyecto "
            + " AND poa.anioFiscal.id =:idAnioFiscal"
            + " AND poa.unidadTecnica.id =:idUnidadTecnica"
            + " AND poa.lineaTrabajo IS NOT NULL"
            + " ORDER BY poa.fechaFijacion DESC")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idAnioFiscal", idAnioFiscal)
            .setParameter("idUnidadTecnica", idUnidadTecnica)
            .getResultList();
    }

    /**
     * Este método devuelve la categoría de proyecto de un proyecto.
     *
     * @param idProyecto id del proyecto
     * @param idCategoriaConvenio id de la categoría del convenio
     * @return
     */
    public ProyectoCategoriaConvenio getCategoria(Integer idProyecto, Integer idCategoriaConvenio) {
        List<ProyectoCategoriaConvenio> l = entityManager.createQuery("SELECT proyCatConv"
            + " FROM Proyecto proyecto, proyecto.distribuccionCategorias proyCatConv"
            + " WHERE proyecto.id =:idProyecto "
            + " AND proyCatConv.categoriaConvenio.id =:idCategoriaConvenio")
            .setParameter("idProyecto", idProyecto)
            .setParameter("idCategoriaConvenio", idCategoriaConvenio)
            .getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    /**
     * Este método devuelve reprogramaciones de proyectos por filtro.
     *
     * @param filtro filtro a aplicar.
     * @return colección de reprogramaciones.
     */
    public Collection<Reprogramacion> obtenerReprogramacionesPorFiltro(FiltroReprogramacion filtro) {
        String consulta = "select s from Reprogramacion s ";
        boolean conAnd = false;
        String where = "";
        if (filtro != null) {
            if (filtro.getEstado() != null) {
                where = where + " (s.estado= :estado) ";
                conAnd = true;
            }
            if (filtro.getTipoReprog() != null) {
                if (conAnd) {
                    where = where + " and ";
                }
                where = where + " (s.tipoReprogramacion= :tipoReprogramacion) ";
                conAnd = true;
            }
            if (filtro.getUnidadTecnica() != null) {
                if (conAnd) {
                    where = where + " and ";
                }
                where = where + " (s.rep_detalle_lista.poa.unidadTecnica.id=:unidadTecnica) ";
            }
        }
        if (!StringUtils.isEmpty(where)) {
            consulta = consulta + " where " + where;
        }
        Query query = entityManager.createQuery(consulta);
        if (filtro != null) {
            if (filtro.getEstado() != null) {
                query.setParameter("estado", filtro.getEstado());
            }
            if (filtro.getTipoReprog() != null) {
                query.setParameter("tipoReprogramacion", filtro.getTipoReprog());
            }
            if (filtro.getUnidadTecnica() != null) {
                query.setParameter("unidadTecnica", filtro.getUnidadTecnica().getId());
            }
        }
        return query.getResultList();
    }

    /**
     * Este método guarda una reprogramación de proyecto.
     *
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
     * Este método guarda la línea de detalle de una reprogramación.
     *
     * @param reprog
     * @return
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
     * Este método obtiene los grupos PAC de un POA
     *
     * @param id id del POA
     * @return lista de grupos de PAC.
     */
    public Collection<PACGrupo> obtenerGruposPACPorPOAId(Integer id) {
        String consulta = " select s from PACGrupo s join s.pac.poas p"
            + " where (p.id=:id) ";
        return entityManager.createQuery(consulta).setParameter("id", id)
            .getResultList();
    }

    public PAC obtenerPACPorPOAId(Integer id) {
        String consulta = " select pac from PAC pac join pac.poas p"
            + " where (p.id=:id) ";
        List<PAC> l = entityManager.createQuery(consulta).setParameter("id", id)
            .getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

}
