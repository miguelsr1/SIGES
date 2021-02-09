/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroFichaSalud;
import sv.gob.mined.siges.negocio.validaciones.FichaSaludValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.FichaSaludDAO;
import sv.gob.mined.siges.persistencia.entidades.SgFichaSalud;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEnfermedadNoTransmisible;

@Stateless
@Traced
public class FichaSaludBean {
   
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SigesDireccion
     * @throws GeneralException
     */
    public SgFichaSalud obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgFichaSalud> codDAO = new CodigueraDAO<>(em, SgFichaSalud.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }
    
    /**
     * Devuelve si el objeto existe
     *
     * @param id
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgFichaSalud> codDAO = new CodigueraDAO<>(em, SgFichaSalud.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        

    /**
     * Guarda el objeto indicado
     *
     * @param entity
     * @return SgFichaSalud
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgFichaSalud guardar(SgFichaSalud entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (FichaSaludValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgFichaSalud> codDAO = new CodigueraDAO<>(em, SgFichaSalud.class);
                    return codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroFichaSalud filtro) throws GeneralException {
        try {
            FichaSaludDAO codDAO = new FichaSaludDAO(em);
            return codDAO.cantidadTotal(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgFichaSalud> obtenerPorFiltro(FiltroFichaSalud filtro) throws GeneralException {
        try {
            FichaSaludDAO codDAO = new FichaSaludDAO(em);
            List<SgFichaSalud> fichas=codDAO.obtenerPorFiltro(filtro);
            if(BooleanUtils.isTrue(filtro.getIncluirENT())){
                for(SgFichaSalud ficha:fichas){
                    ficha.setFsaEnfermedadNoTransmitible(obtenerEnfermedades(ficha.getFsaPk()));
                }
            }
            return fichas;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    public List<SgEnfermedadNoTransmisible> obtenerEnfermedades(Long fichaPk) {
        String query = "select dis.* from "
                + "centros_educativos.sg_ficha_salud_enfer_no_transm spd\n" +
                "join catalogo.sg_enfer_no_transm dis on spd.ent_pk = dis.ent_pk\n" +
                "where spd.fsa_pk = :perPk";
        javax.persistence.Query q = em.createNativeQuery(query, SgEnfermedadNoTransmisible.class);
        q.setParameter("perPk", fichaPk);

        List<SgEnfermedadNoTransmisible> objs = q.getResultList();
        return objs;
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgFichaSalud> codDAO = new CodigueraDAO<>(em, SgFichaSalud.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
