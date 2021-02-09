/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.negocio.validaciones.RelGradoPlanEstudioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelGradoPlanEstudioDAO;
import sv.gob.mined.siges.persistencia.daos.catalogo.SgConsultaDefinicionTitulo;
import sv.gob.mined.siges.persistencia.entidades.SgRelGradoPlanEstudio;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelGradoPlanEstudioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;


    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelGradoPlanEstudio
     * @throws GeneralException
     */
    public SgRelGradoPlanEstudio obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelGradoPlanEstudio> codDAO = new CodigueraDAO<>(em, SgRelGradoPlanEstudio.class);
                SgRelGradoPlanEstudio rg = codDAO.obtenerPorId(id,null);
                if (rg != null){
                    if (rg.getRgpDefinicionTitulo() != null){
                        rg.getRgpDefinicionTitulo().size();
                    }
                }
                return rg;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelGradoPlanEstudio> codDAO = new CodigueraDAO<>(em, SgRelGradoPlanEstudio.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param rgp SgRelGradoPlanEstudio
     * @return SgRelGradoPlanEstudio
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelGradoPlanEstudio guardar(SgRelGradoPlanEstudio rgp) throws GeneralException {
        try {
            if (rgp != null) {
                if (RelGradoPlanEstudioValidacionNegocio.validar(rgp)) {
                 
                    CodigueraDAO<SgRelGradoPlanEstudio> codDAO = new CodigueraDAO<>(em, SgRelGradoPlanEstudio.class);
                    return codDAO.guardar(rgp,null);                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rgp;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelGradoPlanEstudio filtro) throws GeneralException {
        try {
            RelGradoPlanEstudioDAO codDAO = new RelGradoPlanEstudioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelGradoPlanEstudio>
     * @throws GeneralException
     */
    public List<SgRelGradoPlanEstudio> obtenerPorFiltro(FiltroRelGradoPlanEstudio filtro) throws GeneralException {
        try {
            RelGradoPlanEstudioDAO codDAO = new RelGradoPlanEstudioDAO(em);
            List<SgRelGradoPlanEstudio> rel=codDAO.obtenerPorFiltro(filtro);
            //TODO: inicializar solamente si viene un booleano en el filtro
            for(SgRelGradoPlanEstudio graPla:rel){
                if(graPla.getRgpDefinicionTitulo()!=null){
                    graPla.getRgpDefinicionTitulo().size();
                }
            }
            return rel;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelGradoPlanEstudio> codDAO = new CodigueraDAO<>(em, SgRelGradoPlanEstudio.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param rel
     * @return 
     * @throws GeneralException
     */
    public List<SgConsultaDefinicionTitulo> obtenerDefinicionTitulo(FiltroRelGradoPlanEstudio rel) throws GeneralException {
        if (rel != null) {
            try {
                List<SgConsultaDefinicionTitulo> listDef=new ArrayList();
                Session session = em.unwrap(Session.class);
                SQLQuery q = session.createSQLQuery("select dti_pk,dti_codigo,dti_nombre,dti_version,dti_nombre_certificado from catalogo.sg_definiciones_titulo def where exists \n" +
                "(select 1 from centros_educativos.sg_rel_grado_plan_estudio rgp \n" +
                "inner join centros_educativos.sg_rel_grado_plan_definicion_titulo rgpd on rgp.rgp_pk=rgpd.rgp_pk where rgp.rgp_grado_fk=:grado and rgp.rgp_plan_estudio_fk=:plan\n" +
                "and def.dti_pk=dti_pk );");
                q.setParameter("grado",rel.getRgpGrado());
                q.setParameter("plan",rel.getRgpPlanEstudio());
               
                List<Object[]> rows =q.list();
                if(rows.isEmpty()){

                    q = session.createSQLQuery("select dti_pk,dti_codigo,dti_nombre,dti_version from catalogo.sg_definiciones_titulo def where exists (select 1 from centros_educativos.sg_grados_definicion_titulo where gra_pk=:grado and def.dti_pk=dti_pk); ");
                    q.setParameter("grado",rel.getRgpGrado());
                    rows =q.list();
                    
                }
                for(Object[] ob:rows){
                        SgConsultaDefinicionTitulo def= new SgConsultaDefinicionTitulo();
                        def.setDtiPk(Long.parseLong(ob[0].toString()));
                        def.setDtiCodigo(ob[1].toString());
                        def.setDtiNombre(ob[2].toString());
                        def.setDtiVersion(Integer.parseInt(ob[3].toString()));
                        def.setDtiNombreCertificado(ob[4].toString());
                        listDef.add(def);
                    }
                return listDef;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

}
