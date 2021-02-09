/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.datatypes.DataActividadesPOA;
import gob.mined.siap2.business.ejbs.impl.POABean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.POAActividadMeta;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroPOA;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class POADelegate implements Serializable {

    @Inject
    private POABean utBean;

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    
    /**
     * Este método permite guardar una unidad técnica (crear o actualizar)
     * @param cnf
     * @return
     * @throws GeneralException 
     */
    public POA guardar(POA cnf, Boolean indicador, String userCod, Boolean metasActividadesObligatorias, Boolean validarSeguimiento) throws GeneralException {
        return utBean.guardar(cnf, indicador, userCod, metasActividadesObligatorias, validarSeguimiento);
    }

    public POA cambiarEstadoPOA(POA poa) throws GeneralException {
        return utBean.cambiarEstadoPOA(poa);
    }
    public POA enviarPOA(Integer idPOA) throws GeneralException {
        return utBean.enviarPOA(idPOA);
    }
    
    public POA aprobarPOA(Integer idPOA) throws GeneralException {
        return utBean.aprobarPOA(idPOA);
    }
    
    
    /**
     * Este método rechaza un POA e indica su motivo de rechazo.
     *
     * @param idPoa
     * @param motivoRechazo
     */
    public void rechazarPOA(Integer idPoa, String motivoRechazo) {
        utBean.rechazarPOA(idPoa, motivoRechazo);
    }
    
    /**
     * Este método permite obtener una POA por Id.
     * @param id
     * @return 
     */
    public POA obtenerPOAPorId(Integer id) {
        return utBean.obtenerPOAPorId(id);
    }


    /**
     * Este método permite obtener todas las POAs
     * @return
     * @throws GeneralException 
     */
    public List<POA> obtenerTodos() throws GeneralException {
        return utBean.obtenerTodos();
    }
    /**
     * 
     * @param filtro
     * @return 
     */
    public List<POA> obtenerPorFiltro(FiltroPOA filtro) {
        return utBean.obtenerPorFiltro(filtro);
    }
    
    public List<DataActividadesPOA> obtenerActividadesPOAPorFiltro(FiltroPOA filtro) {
        List<DataActividadesPOA> listado = new ArrayList();
        List<POAActividadMeta> actividades = utBean.obtenerActividadesPorFiltro(filtro);
        DataActividadesPOA data = null;
        for(POAActividadMeta act: actividades) {
            data = new DataActividadesPOA();
            data.setNombrePOA(act.getMeta() != null && act.getMeta().getPoa() != null? act.getMeta().getPoa().getNombre():"");
            data.setNombreIndicador(act.getMeta().getIndicador().getNombre());
            data.setNombreUnidadTecnicaPOA(act.getMeta().getPoa().getUnidadTecnica().getNombre());
            data.setNombreUnidadTecnicaActividad(act.getUnidadTecnicaResponsable().getNombre());
            data.setNombreActividad(act.getNombre());
            data.setNombreMeta(act.getMeta().getMetaAnual());
            data.setPorcentajeAvance(act.getPorcentajeAvanceSegundoTrimestre());
            data.setUltimoPeriodoReportado(act.getUltimoPeriodoModificado() != null ? "Trim. "+ act.getUltimoPeriodoModificado() : "");
            listado.add(data);
        }
        return listado;
    }
    
    public List<DataActividadesPOA> obtenerMetasPOAPorFiltro(FiltroPOA filtro) {
        List<DataActividadesPOA> listado = new ArrayList();
        List<POAMetaAnual> metas = utBean.obtenerMetasPorFiltro(filtro);
        DataActividadesPOA data = null;
        Boolean colorEncontrado = Boolean.FALSE;
        for(POAMetaAnual meta: metas) {
            data = new DataActividadesPOA();
            data.setNombrePOA(meta.getPoa() != null ? meta.getPoa().getNombre():"");
            data.setNombreIndicador(meta.getIndicador().getNombre());
            data.setNombreUnidadTecnicaPOA(meta.getPoa().getUnidadTecnica().getNombre());
            data.setNombreMeta(meta.getMetaAnual());
            data.setColor("red");
            if(meta.getTotal() != null && meta.getTotalReal() != null && meta.getTotal().compareTo(0) > 0) {
                BigDecimal total = new BigDecimal(meta.getTotal());
                BigDecimal totalReal = new BigDecimal(meta.getTotalReal());
                BigDecimal porcentaje = totalReal.divide(total,2, RoundingMode.HALF_UP);
                porcentaje = porcentaje.multiply(new BigDecimal(100));
                data.setPorcentajeCumplimiento(porcentaje.setScale(1,RoundingMode.HALF_UP));
                
                BigDecimal pendiente = new BigDecimal(100).subtract(data.getPorcentajeCumplimiento());
                
                data.setPendienteCumplimiento(pendiente.setScale(1,RoundingMode.HALF_UP));
                
                colorEncontrado = Boolean.FALSE;
                if(meta.getIndicador() != null ) {
                    if(meta.getIndicador().getFinToleranciaVerde() != null) {
                        if(data.getPendienteCumplimiento().compareTo(meta.getIndicador().getFinToleranciaVerde()) <= 0) {
                            data.setColor("green");
                            colorEncontrado = Boolean.TRUE;
                        }
                    }
                    if(!colorEncontrado) {
                        if(meta.getIndicador().getFinToleranciaAmarillo() != null) {
                            if(data.getPendienteCumplimiento().compareTo(meta.getIndicador().getFinToleranciaAmarillo()) <= 0) {
                                data.setColor("yellow");
                            }
                        }
                    }


                }
            } else {
                data.setPendienteCumplimiento(new BigDecimal(100).setScale(0, RoundingMode.HALF_UP));
                data.setPorcentajeCumplimiento(new BigDecimal(0).setScale(0, RoundingMode.HALF_UP));
            }

            listado.add(data);
        }
        return listado;
    }
    
    public Long obtenerTotalPorFiltro(FiltroPOA filtro) throws DAOGeneralException {
        return utBean.obtenerTotalPorFiltro(filtro);
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
    public List<POA> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        return utBean.obtenerPorCriteria(cto, orderBy, ascending, startPosition, cantidad);
    }      
    
    
    /**
     * Método que se encarga de eliminar una cuenta
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            utBean.eliminar(id);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}