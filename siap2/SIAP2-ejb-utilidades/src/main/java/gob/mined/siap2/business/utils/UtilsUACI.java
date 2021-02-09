/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionLote;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.enums.EstadoItem;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Sofis Solutions
 */
public class UtilsUACI {

    private static final Map<PasosProcesoAdquisicion, TipoTarea[]> MAPEO_PASOS_PROCESO_ADQ_Y_TIPOS_TAREA_METODO_ADQ = new HashMap<PasosProcesoAdquisicion, TipoTarea[]>() {
        TipoTarea[] tareasInicializacion = {TipoTarea.INICIALIZACION};
        TipoTarea[] tareasRecepcionTDR = {TipoTarea.RECEPCION_DE_TDR_O_ESPECIFICACIONES_TECNICAS, TipoTarea.REVISION_DE_TDR_O_ET};
        TipoTarea[] tareasRevisionJefe = {TipoTarea.REVISION_JEFE_UACI};
        TipoTarea[] tareasRevisionTecnico = {TipoTarea.REVISION_TECNICO_UACI};
        TipoTarea[] tareasInvitacion = {TipoTarea.INVITACION_O_PUBLICACION};
        TipoTarea[] tareasRecepcionOfertas = {TipoTarea.RECEPCION_DE_OFERTAS};
        TipoTarea[] tareasEvaluacion = {TipoTarea.EVALUACION};
        TipoTarea[] tareasAdjudicacion = {TipoTarea.ADJUDICACION};
        TipoTarea[] tareasCompromisoPresup = {TipoTarea.COMPROMISO_PRESUPUESTARIO};
        TipoTarea[] tareasContratoOrdenCompra = {TipoTarea.CONTRATO_ORDEN_DE_COMPRA, TipoTarea.CONTRATACION, TipoTarea.CONTRATADO};
        TipoTarea[] tareasOrdenInicio = {TipoTarea.ORDEN_DE_INICIO};
        TipoTarea[] tareasCerrado = {TipoTarea.CERRADO_PROCESO};

        {
            put(PasosProcesoAdquisicion.INICIALIZACION, tareasInicializacion);
            put(PasosProcesoAdquisicion.RECEPCION_TDR_ET_CERT_DISP, tareasRecepcionTDR);
            put(PasosProcesoAdquisicion.REVISION_JEFE_UACI, tareasRevisionJefe);
            put(PasosProcesoAdquisicion.REVISION_TECNICO_UACI, tareasRevisionTecnico);
            put(PasosProcesoAdquisicion.INVITACION, tareasInvitacion);
            put(PasosProcesoAdquisicion.RECEPCION_OFERTAS, tareasRecepcionOfertas);
            put(PasosProcesoAdquisicion.EVALUACION, tareasEvaluacion);
            put(PasosProcesoAdquisicion.ADJUDICACION, tareasAdjudicacion);
            put(PasosProcesoAdquisicion.COMPROMISO_PRESUPUESTARIO, tareasCompromisoPresup);
            put(PasosProcesoAdquisicion.CONTRATO_ORDEN_DE_COMPRA, tareasContratoOrdenCompra);
            put(PasosProcesoAdquisicion.ORDEN_INICIO, tareasOrdenInicio);
            put(PasosProcesoAdquisicion.CERRADO, tareasCerrado);

        }
    };
    
    public static Map<PasosProcesoAdquisicion, TipoTarea[]>  getMapeoProceso(){
        return MAPEO_PASOS_PROCESO_ADQ_Y_TIPOS_TAREA_METODO_ADQ;
    }
    

    /**
     * Este método determina si un insumo está libre para ser incluido en un
     * proceso
     *
     * @param insumo
     * @return
     */
    public static boolean insumoQuedaLibreParaOtroProceso(ProcesoAdquisicionInsumo insumo) {
        for (RelacionProAdqItemInsumo rel : insumo.getRelItemInsumos()) {
            if (rel.getItem().getEstado() != EstadoItem.DESIERTO && rel.getItem().getEstado() != EstadoItem.SIN_EFECTO) {
                return false;
            }
        }
        return true;
    }

    /**
     * Este método transforma un insumo de un POA en un insumo de un proceso de
     * adquisición
     *
     * @param trabajo
     * @return
     */
    public static ProcesoAdquisicionInsumo convertPOInsumosAProcesoAdquisicionInsumo(POInsumos trabajo) {

        ProcesoAdquisicionInsumo base = new ProcesoAdquisicionInsumo();
        base.setPosicion(trabajo.getPosicion());


       base.setUnidadTecnica(trabajo.getUnidadTecnica());
        base.setObservacion(trabajo.getObservacion());
        base.setInsumo(trabajo.getInsumo());

        base.setCantidadAdjudicada(0);

        base.setPoInsumo(trabajo);
        return base;
    }

    /**
     * Este método realiza la validación de los datos de un contrato
     * @param contrato 
     */
    public static void validarContrato(ContratoOC contrato) {

        if (contrato.getUltUsuario() == null) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_CONTRATO_ADMINISTRADOR_VACIO);
            throw b;
        }
        if (!contrato.getFechasDesdeOrdenInicio()) {
            if (contrato.getFechaInicio() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CONTRATO_FECHA_INICIO_VACIA);
                throw b;
            }
            if (contrato.getFechaFin() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CONTRATO_FECHA_FIN_VACIA);
                throw b;
            }

            if ((contrato.getFechaInicio().after(contrato.getFechaFin()))) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CONTRATO_FECHA_INICIO_MAYOR_A_FECHA_FIN);
                throw b;
            }
            if (contrato.getMontoAdjudicado() == null || contrato.getMontoAdjudicado().compareTo(BigDecimal.ZERO) == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CONTRATO_DEBE_INGRESAR_MONTO_ADJUDICADO);
                throw b;
            }
        }
        if (contrato.getFechaInicio() != null && contrato.getFechaFin() != null && contrato.getPlazoEntrega() != null) {
            Date fechaFinCalculada = DatesUtils.sumarRestarDiasFecha(contrato.getFechaInicio(), contrato.getPlazoEntrega());
            if (0 != DatesUtils.getDateDiff(fechaFinCalculada, contrato.getFechaFin(), TimeUnit.DAYS)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_FECHA_DE_INICIO_MAS_PLAZO_EN_DIAS_NO_COINCIDE_CON_FECHA_FIN);
                throw b;
            }
        }

    }

    /**
     * Este método determina si un ítem está en un proceso de adquisición
     * @param proceso
     * @param item
     * @return 
     */
    public static boolean existeItemEnContratos(ProcesoAdquisicion proceso, ProcesoAdquisicionItem item) {
        //busca si ya existe un contrato con ese item
        for (ContratoOC c : proceso.getContratos()) {
            for (ProcesoAdquisicionItem item2 : c.getItems()) {
                if (item2.equals(item)) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Este método devuelve el conjunto de todos los ítems adjudicados en un proceso de adquisición.
     * @param proceso
     * @return 
     */
    public static Set<ProcesoAdquisicionItem> getItemsAdjudicados(ProcesoAdquisicion proceso) {
        Set<ProcesoAdquisicionItem> res = new LinkedHashSet<>();
        for (ProcesoAdquisicionItem item : proceso.getItems()) {
            if (item.getEstado() == EstadoItem.ADJUDICADO) {
                res.add(item);
            }
        }

        for (ProcesoAdquisicionLote lote : proceso.getLotes()) {
            for (ProcesoAdquisicionItem item : lote.getItems()) {
                if (item.getEstado() == EstadoItem.ADJUDICADO) {
                    res.add(item);
                }
            }
        }

        return res;
    }

    /**
     * 
     * @param proceso
     * @param adquisicionProveedor
     * @return 
     */
    public static ContratoOC getContratoPorProveedor(ProcesoAdquisicion proceso, ProcesoAdquisicionProveedor adquisicionProveedor) {
        for (ContratoOC c : proceso.getContratos()) {
            if (c.getProcesoAdquisicionProveedor().equals(adquisicionProveedor)) {
                return c;
            }
        }
        return null;

    }

    /**
     * Este método devuelve el ítem con id itemID en el proceso de adquisición
     * @param proceso proceso de adquisición
     * @param itemID id del ítem a buscar
     * @return 
     */
    public static ProcesoAdquisicionItem getItem(ProcesoAdquisicion proceso, Integer itemID) {
        for (ProcesoAdquisicionItem i : proceso.getItems()) {
            if (i.getId().equals(itemID)) {
                return i;
            }
        }
        for (ProcesoAdquisicionLote l : proceso.getLotes()) {
            for (ProcesoAdquisicionItem i : l.getItems()) {
                if (i.getId().equals(itemID)) {
                    return i;
                }
            }
        }
        return null;

    }

    /**
     * Este método determina el estado de transición a partir de un estado dado.
     * @param estado
     * @return 
     */
    public static String getPermisoParaCambiarEstado(PasosProcesoAdquisicion estado) {
        switch (estado) {
            case ADJUDICACION:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_ADJUDICACION;

            case INICIALIZACION:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_INICIALIZACION;

            case RECEPCION_TDR_ET_CERT_DISP:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_RECEPCION_TDR_ET_CERT_DISP;

            case REVISION_JEFE_UACI:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_REVISION_JEFE_UACI;

            case REVISION_TECNICO_UACI:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_REVISION_TECNICO_UACI;

            case INVITACION:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_INVITACION;

            case RECEPCION_OFERTAS:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_RECEPCION_OFERTAS;

            case EVALUACION:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_EVALUACION;

            case COMPROMISO_PRESUPUESTARIO:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_COMPROMISO_PRESUPUESTARIO;

            case CONTRATO_ORDEN_DE_COMPRA:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_CONTRATO_ORDEN_DE_COMPRA;

            case ORDEN_INICIO:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_ORDEN_INICIO;

            case CERRADO:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_CERRADO;

            default:
                return ConstantesEstandares.Operaciones.CAMBIAR_ESTADO_CON_PROCESO_EN_CERRADO;

        }
    }

    public static  List<ProcesoAdquisicionInsumo> getInsumosAdjudicados(ProcesoAdquisicion proceso) {
        List<ProcesoAdquisicionInsumo> insumosAdjudicados = new LinkedList();
        
        for (ProcesoAdquisicionInsumo insumo : proceso.getInsumos()) {
            boolean adjudicado = false;
            Iterator<RelacionProAdqItemInsumo> iter = insumo.getRelItemInsumos().iterator();
            while (iter.hasNext() && !adjudicado) {
                RelacionProAdqItemInsumo rel = iter.next();
                if (rel.getItem().getEstado() == EstadoItem.ADJUDICADO) {
                    adjudicado = true;
                }
            }

            if (adjudicado) {
                insumosAdjudicados.add(insumo);
            }

        }
        
        return insumosAdjudicados;
    }
    
    
    
    /**
     * Retorna si el insumo ya se encuentra adjudicado
     * 
     * @param poInusumo
     * @return 
     */
    public static boolean insumoAdjudicado(POInsumos poInusumo){
        if (poInusumo.getProcesoInsumo() == null){
            return false;
        }
        for (RelacionProAdqItemInsumo rel : poInusumo.getProcesoInsumo().getRelItemInsumos()){
            if ( rel.getItem().getEstado() == EstadoItem.ADJUDICADO){
                return true;
            }
        }
        return false;
        
    }

}
