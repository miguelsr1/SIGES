/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.impl.POAProyectoBean;
import gob.mined.siap2.business.ejbs.impl.validacionesPresupuestales.POAValidaciones;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoPOA;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa los métodos para comunicación entre la capa lógica y de
 * presentación.
 *
 * @author Sofis Solutions
 */
@Named
public class POAProyectoDelegate implements Serializable {

    @Inject
    private POAProyectoBean bean;
    @Inject
    private POAValidaciones pOAValidaciones;

    /**
     * Este método determina si un usuario tiene permisos de edición en un poa.
     *
     * @param poa
     * @return
     */
    public boolean tienePermisoEdicionPOA(POAProyecto poa) {
        return pOAValidaciones.tienePermisoEdicionPOA(poa);
    }

    /**
     * Este método devuelve un POA de trabajo de un proyecto en un determinado
     * año.
     *
     * @param idProyecto
     * @param anio
     * @param idUnidadTecnica
     * @return
     */
    public POAProyecto getPOATrabajo(Integer idProyecto, Integer anio, Integer idUnidadTecnica) {
        return bean.getPOATrabajo(idProyecto, anio, idUnidadTecnica);
    }

    /**
     * Este método permite enviar un POA de un proyecto.
     *
     * @param idProyecto
     * @param idPOA
     * @return
     */
    public POAProyecto enviarPOA(Integer idProyecto, Integer idPOA) {
        return bean.enviarPOA(idProyecto, idPOA);
    }

    public void confirmarCierreAnualPOA(Integer idProyectoPOA, Boolean continuar) {
        bean.confirmarCierreAnual(idProyectoPOA, continuar);
    }
    /**
     * Este método valida un POA
     *
     * @param idPoa
     */
    public void validarPOA(Integer idPoa) {
        bean.validarPOA(idPoa);
    }

    /**
     * Este método consolida los POA de un proyecto.
     *
     * @param listPoa
     * @param idProyecto
     */
    public void consolidar(List<POAProyecto> listPoa, Integer idProyecto) {
        bean.consolidar(listPoa, idProyecto);
    }

    /**
     * Este método rechaza un POA e indica su motivo de rechazo.
     *
     * @param idPoa
     * @param motivoRechazo
     */
    public void rechazarPOA(Integer idPoa, String motivoRechazo) {
        bean.rechazarPOA(idPoa, motivoRechazo);
    }

    public List<POAProyecto> getPOAsTrabajo(Integer idProyecto, Integer idAnioFiscal, EstadoPOA estado) {
        return bean.getPOAsTrabajo(idProyecto, idAnioFiscal, estado);
    }

    /**
     * Retorna todos los POAs consolidados
     *
     * @param filtrarUT
     * @param utAfiltrar
     * @param idAnioFiscal
     * @return
     */
    public List<POAProyecto> getPOAsParaReprogramacion(boolean filtrarUT, List<UnidadTecnica> utAfiltrar, Integer idAnioFiscal) {
        return bean.getPOAsParaReprogramacion(filtrarUT, utAfiltrar, idAnioFiscal);
    }

    /**
     * Este método devuelve las unidades técnicas que tienen POA pendiente de
     * consolidad en un año fiscal.
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @return
     */
    public List<UnidadTecnica> getUTPendientesParaConsolidar(Integer idProyecto, Integer idAnioFiscal) {
        return bean.getUTPendientesParaConsolidar(idProyecto, idAnioFiscal);
    }

    /**
     * Este método devuelve una lista de POA para duplicar
     *
     * @param idPOA
     * @return
     */
    public List<POAProyecto> getPOASParaDucplicar(Integer idPOA) {
        return bean.getPOASParaDucplicar(idPOA);
    }

    /**
     * Este método duplica una línea en un POA
     *
     * @param idPoa
     * @param idInsumosAduplicar
     * @return
     */
    public POAProyecto duplicarLineaEnPOA(Integer idPoa, List<Integer> idInsumosAduplicar) {
        return bean.duplicarLineaEnPOA(idPoa, idInsumosAduplicar);
    }

    /**
     * Este método devuelve los POA en la línea base.
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @param idUnidadTecnica
     * @return
     */
    public Map<String, Integer> getPOASEnLineaBase(Integer idProyecto, Integer idAnioFiscal, Integer idUnidadTecnica) {
        return bean.getPOASEnLineaBase(idProyecto, idAnioFiscal, idUnidadTecnica);
    }

    /**
     * Este método devuelve la línea base de un POA
     *
     * @param idPOA
     * @return
     */
    public POAProyecto getPOAEnLineaBase(Integer idPOA) {
        return bean.getPOAEnLineaBase(idPOA);
    }

    /**
     * Este método devuelve los componentes de un proyecto en los que interviene
     * una unidad técnica.
     *
     * @param objeto
     * @param idUT
     * @return
     */
    public List<Integer> getComponentesProyecto(Proyecto objeto, Integer idUT) {
        return bean.getComponentesProyecto(objeto, idUT);
    }

    /**
     * Este método devuelve las macroactividades de un proyecto en las que
     * interviene una unidad técnica.
     *
     * @param objeto
     * @param idUnidadTecnica
     * @return
     */
    public List<Integer> getMacroActividadesProyecto(Proyecto objeto, Integer idUnidadTecnica) {
        return bean.getMacroActividadesProyecto(objeto, idUnidadTecnica);
    }

    public List<UnidadTecnica> getUTQueCompartieronConUT(Integer idProyecto, Integer idAnioFiscal, List<UnidadTecnica> unidades) {
        return bean.getUTQueCompartieronConUT(idProyecto, idAnioFiscal, unidades);
    }

    /**
     * Este método devuelve las unidades técnicas habilitadas para un usuario.
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @return
     */
    public List<UnidadTecnica> getUTDisponiblesParaUsuario(Integer idProyecto, Integer idAnioFiscal) {
        return bean.getUTDisponiblesParaUsuario(idProyecto, idAnioFiscal);
    }

    /**
     * Este método devuelve los años disponibles para programación del POA.
     *
     * @param IdProyecto
     * @return
     */
    public List<AnioFiscal> getAniosDisponiblesProgramacionPOA(Integer IdProyecto) {
        return bean.getAniosDisponiblesProgramacionPOA(IdProyecto);
    }

    /**
     * Este método devuelve todos los años del POA.
     *
     * @param IdProyecto
     * @return
     */
    public List<AnioFiscal> getTodosAniosPOA(Integer IdProyecto) {
        return bean.getTodosAniosPOA(IdProyecto);
    }

    /**
     * Este método devuelve los tramos de un proyecto en una categoría.
     *
     * @param idProyecto
     * @param categoriaConvenio
     * @return
     */
    public List<ProyectoAporteTramoTramo> getTramos(Integer idProyecto, CategoriaConvenio categoriaConvenio) {
        return bean.getTramos(idProyecto, categoriaConvenio);
    }

    /**
     * Este método devuelve el aporte por tramo en un proyecto.
     *
     * @param idProyectoAporteTramoTramo
     * @return
     */
    public ProyectoAporteTramoTramo getProyectoAporteTramoTramo(Integer idProyectoAporteTramoTramo) {
        return bean.getProyectoAporteTramoTramo(idProyectoAporteTramoTramo);
    }

    /**
     * Este método guarda un insumo del POA.
     *
     * @param idProyecto
     * @param idPOA
     * @param idLinea
     * @param idActividad
     * @param insumo
     * @return
     */
    public POInsumos guardarInsumo(Integer idProyecto, Integer idPOA, Integer idLinea, Integer idActividad, POInsumos insumo) {
        return bean.guardarInsumo(idProyecto, idPOA, idLinea, idActividad, insumo);
    }

    /**
     * Este método elimina un insumo del POA.
     *
     * @param idPOA
     * @param idActividad
     * @param insumo
     */
    public void eliminarInsumo(Integer idPOA, Integer idActividad, POInsumos insumo) {
        bean.eliminarInsumo(idPOA, idActividad, insumo);
    }

    /**
     * Este método permite guardar una actividad el POA.
     *
     * @param idProyecto
     * @param idPOA
     * @param idLinea
     * @param actividad
     * @return
     */
    public POActividadProyecto guardarActividad(Integer idProyecto, Integer idPOA, Integer idLinea, POActividadProyecto actividad) {
        return bean.guardarActividad(idProyecto, idPOA, idLinea, actividad);
    }

    /**
     * Este método permite eliminar una actividad del POA
     *
     * @param idProyecto
     * @param idPOA
     * @param idLinea
     * @param actividad
     */
    public void eliminarActividad(Integer idProyecto, Integer idPOA, Integer idLinea, POActividadProyecto actividad) {
        bean.eliminarActividad(idProyecto, idPOA, idLinea, actividad);
    }

    /**
     * Este método permite obtener las líneas de actividades de un POA.
     *
     * @param poaId
     * @return
     */
    public Collection<POActividadBase> obtenerLineasPorPOA(Integer poaId) {
        return bean.obtenerLineasPorPOA(poaId);
    }

    public void confirmarCierreAnual(Integer idProyecto, Integer idAnioFiscal) {
        bean.confirmarCierreAnual(idProyecto, idAnioFiscal);
    }

    public Boolean existenActividadesPendientesEnPOA(Integer idPOA) {
        return bean.existenActividadesPendientesEnPOA(idPOA);
    }

    public Boolean existenInsumosPendientesEnPOA(Integer idPOA) {
        return bean.existenInsumosPendientesEnPOA(idPOA);
    }

}
