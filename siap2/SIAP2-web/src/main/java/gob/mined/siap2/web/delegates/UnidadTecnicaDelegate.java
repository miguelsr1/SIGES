/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.impl.UnidadTecnicaBean;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class UnidadTecnicaDelegate implements Serializable {

    @Inject
    private UnidadTecnicaBean utBean;

    /**
     * Este método permite guardar una unidad técnica (crear o actualizar)
     * @param cnf
     * @return
     * @throws GeneralException 
     */
    public UnidadTecnica guardar(UnidadTecnica cnf) throws GeneralException {
        return utBean.guardar(cnf);
    }

    /**
     * Este método permite obtener una unidad técnica por Id.
     * @param id
     * @return 
     */
    public UnidadTecnica obtenerUnidadTecnicaPorId(Integer id) {
        return utBean.obtenerUnidadTecnicaPorId(id);
    }

    /**
     * Este método permite obtener una unidad técnica por código.
     * @param codigo
     * @return
     * @throws GeneralException 
     */
    public UnidadTecnica obtenerUnidadTecnicaPorCodigo(String codigo) throws GeneralException {
        return utBean.obtenerUnidadTecnicaPorCodigo(codigo);
    }

    /**
     * Este método permite obtener todas las unidades técnicas
     * @return
     * @throws GeneralException 
     */
    public List<UnidadTecnica> obtenerTodos() throws GeneralException {
        return utBean.obtenerTodos();
    }

    /**
     * Este método permite unidades técnicas según un criterio o filtro.
     * @param cto
     * @param orderBy
     * @param ascending
     * @param startPosition
     * @param cantidad
     * @return
     * @throws GeneralException 
     */
    public List<UnidadTecnica> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        return utBean.obtenerPorCriteria(cto, orderBy, ascending, startPosition, cantidad);
    }

    /**
     * Dado el id de una unidad técnica retorna los posible padres para la misma
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public List<UnidadTecnica> getPadresByUnidadTecnicaId(Integer id) throws GeneralException {
        return utBean.getPadresByUnidadTecnicaId(id);
    }
    
    /**
     * Este método devuelve un conjunto de unidades técnicas que incluyen query en su nombre.
     * @param query
     * @return 
     */
    public List<UnidadTecnica> getUTBynombre(String query){
        return utBean.getUTBynombre(query);
    }
      
    /**
     * Este método devuelve un conjunto de unidades técnicas que incluyen query en su nombre y que pertenezcan al usuario.
     * @param query
     * @return 
     */
    public List<UnidadTecnica> getUTBynombreAndUsuario(FiltroUnidadTecnica filtro){
        return utBean.getUTBynombreAndUsuario(filtro);
    }
    /**
     * Este método devuelve un conjunto de unidades técnicas que incluyen query en su nombre y que pertenezcan al usuario.
     * @param query
     * @return 
     */
    public List<UnidadTecnica> obtenerUnidadesPorFiltro(FiltroUnidadTecnica filtro){
        return utBean.obtenerUnidadesPorFiltro(filtro);
    }
    
}
