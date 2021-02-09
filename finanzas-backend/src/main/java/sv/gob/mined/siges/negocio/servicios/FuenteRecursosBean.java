/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroFuenteRecursos;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.FuenteRecursosDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsFuenteRecursos;

/**
 * Servicio que gestiona las transferencias
 *
 * @author Sofis Solutions
 */
@Stateless
public class FuenteRecursosBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ConsultaHistoricoBean ch;

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
                CodigueraDAO<SsFuenteRecursos> codDAO = new CodigueraDAO<>(em, SsFuenteRecursos.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_FUENTE_REC);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroFuenteRecursos filtro) throws GeneralException {
        try {
            FuenteRecursosDAO codDAO = new FuenteRecursosDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_FUENTE_REC);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTransferencia>
     * @throws GeneralException
     */
    public List<SsFuenteRecursos> obtenerPorFiltro(FiltroFuenteRecursos filtro) throws GeneralException {
        try {
            FuenteRecursosDAO codDAO = new FuenteRecursosDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_FUENTE_REC);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los requerimientos acumulados por fuente de recurso
     *
     * @param
     * @return List
     * @throws Exception
     */
    public List<SsFuenteRecursos> objetoExistePorIdFuente(Long movPk) throws GeneralException {
        try {

            String squery = "select\n"
                    + "	sfr.fue_id,\n"
                    + "	sfr.fue_nombre\n"
                    + "from\n"
                    + "	siap2.ss_fuente_recursos sfr\n"
                    + "where\n"
                    + "	sfr.fue_id in (\n"
                    + "	select\n"
                    + "		stc.tc_fuente_recursos_fk\n"
                    + "	from\n"
                    + "		siap2.ss_transferencias_componente stc\n"
                    + "	where\n"
                    + "		stc.tc_subcomponente = (\n"
                    + "		select\n"
                    + "			stp.tp_componente\n"
                    + "		from\n"
                    + "			siap2.ss_tope_presupestal stp\n"
                    + "		where\n"
                    + "			stp.tp_movimiento = " + movPk + " ))";

            Query query = em.createNativeQuery(squery);

            List resultado = query.getResultList();

            List<SsFuenteRecursos> respuesta = new ArrayList<>();

            resultado.forEach(
                    z -> {
                        Object[] fila = (Object[]) z;
                        respuesta.add(transformarFilaEnDTO(fila));
                    });

            return respuesta;

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Transforma una fila en el objeto TransferenciaCedAgrup
     *
     * @param fila
     * @return
     */
    public SsFuenteRecursos transformarFilaEnDTO(Object[] fila) {
        SsFuenteRecursos ele = new SsFuenteRecursos();
        ele.setNombre(fila[1].toString());
        return ele;
    }

}
