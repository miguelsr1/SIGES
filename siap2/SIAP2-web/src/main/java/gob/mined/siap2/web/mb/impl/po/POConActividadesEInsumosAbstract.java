/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.business.utils.CalcularSugerenciasMonto;
import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.TDR;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.CertificadoPresupuestarioDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralPODelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.impl.SelectOneUTBean;
import gob.mined.siap2.web.mb.impl.padq.ContratoOrdenCompra;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * Clase base para el manejo de los POAs Esta es la clase mas básica para el
 * manejo. La mayoría de sus métodos son para ser sobreescritos según el tipo de
 * POA
 *
 * @author Sofis Solutions
 */
public abstract class POConActividadesEInsumosAbstract extends SelectOneUTBean implements Serializable {

    String idUnidadTecnica;
    protected String motivoRechazo;

    protected static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    UsuarioInfo usuarioInfo;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    ArchivoDelegate archivoDelegate;
    @Inject
    GeneralPODelegate generalPODelegate;
    @Inject
    ReporteDelegate reporteDelegate;
    @Inject
    InsumoDelegate insumoDelegate;
    @Inject
    ProcesoAdquisicionDelegate procesoAdquisicionDelegate;
    @Inject
    private TextMB textMB;
    @Inject
    CertificadoPresupuestarioDelegate certPresupDelegate;

    protected boolean update = false;
    protected List<UnidadTecnica> usuarioUnidadTecnicas;

    private List<POMontoFuenteInsumo> montosSeleccionadosParaReporte;

    protected Integer cantidadMontosFuentesDePoInsumo;

    protected Boolean insumoTieneFuenteCertificadaOParaCertificar;

    public void init() {
        usuarioUnidadTecnicas = usuarioInfo.getUnidadTecnicas();
    }

    public static final String TIPO_PO_ACCION_CENTRAL = "TIPO_PO_ACCION_CENTRAL";
    public static final String TIPO_PO_PROYECTO = "TIPO_PO_PROYECTO";
    public static final String TIPO_PO_ASIGNACION_NP = "TIPO_PO_ASIGNACION_NP";
    public static final String TIPO_POG_PROYECTO = "TIPO_POG_PROYECTO";

    /**
     * Retorna el tipo POA con el que se esta trabajando. los tipos posibles son
     * TIPO_PO_ACCION_CENTRAL, TIPO_PO_PROYECTO, TIPO_PO_ASIGNACION_NP,
     * TIPO_POG_PROYECTO Cada managedBean tiene un tipo distinto, por defecto
     * supone que esta en acción central porque es el POA mas básico
     *
     * @return
     */
    public String getTipoPO() {
        return POConActividadesEInsumosAbstract.TIPO_PO_ACCION_CENTRAL;
    }

    /**
     * METODOS A SOBRESCRIBIR SEGUN NECESIDAD
     */
    // <editor-fold defaultstate="collapsed" desc="METODOS GENERALES">
    /**
     * Este método se utiliza para recargar el PO
     */
    public void reloadPO() {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método se utiliza como un guardar general
     */
    public void guardar() {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método retorna la lista de años del PO
     *
     * @return
     */
    protected List<Integer> getListAniosPO() {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método retorna la fecha de inicio del PO
     *
     * @return
     */
    public Date getInicioPO() {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método retorna la fecha de fin del PO
     *
     * @return
     */
    public Date getFinPO() {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método retorna el POA actual de trabajo
     *
     * @return
     */
    public GeneralPOA getPOAEnTrabajo() {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }
// </editor-fold>    

    /**
     * METODO A SOBRESCRIBIR PARA MANEJO DE ACTIVIDADES
     */
    // <editor-fold defaultstate="collapsed" desc="ACTIVIDAD">
    /**
     * Este método se utiliza para guardar una actividad
     *
     * @param actividad
     */
    public void guardarActividad(POActividadBase actividad) {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método se utiliza para eliminar la actividad
     *
     * @param actividad
     */
    public void eliminarActividad(POActividadBase actividad) {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método se utiliza para retornar los usuarios a los que es accesible
     * la actividad en edición
     *
     * @return
     */
    public Map<String, String> getUsuariosForActividad() {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

//    public boolean esActividadNoProgramable() {
//        return false;
//    }
    // </editor-fold>
    /**
     * METODO A SOBRESCRIBIR PARA MANEJO DE INSUMOS
     *
     * @param insumo
     */
    // <editor-fold defaultstate="collapsed" desc="INSUMO">
    /**
     * Este método se utiliza para guardar un insumo
     *
     * @param insumo
     */
    public void guardarInsumo(POInsumos insumo) {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método es utilizado para eliminar un insumo
     *
     * @param insumo
     */
    public void eliminarInsumo(POInsumos insumo) {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    //validar montos por fuente 
    /**
     * Este método es utilizado para validar el monto por fuente de los insumos
     *
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    public void validateMontoInsumoPorFuente(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método puede ser utilizado para validar el monto total de los
     * insumos
     *
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    public void validateMontoInsumoTotal(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }
    // </editor-fold>

    public POActividadBase getActividadEnUso() {
        return (POActividadBase) tempActividad;
    }

    public POInsumos getInsumoEnUso() {
        return tempInsumo;
    }

    /**
     * Devuelve un String con la concatenación de los últimos precios unitarios
     * adjudicados.
     *
     * @return
     */
    public String getUltimos5PreciosUnitarios() {
        String respuesta = "";
        if (tempInsumo != null && tempInsumo.getInsumo() != null) {
            respuesta = procesoAdquisicionDelegate.obtenerUltimos5PreciosInsumoAdjudicado(tempInsumo.getInsumo().getId());
            if (respuesta.length() > 0) {
                respuesta = textMB.obtenerTexto("labels.UltimosPreciosAdjudicados") + ": " + respuesta;
            }
        }
        return respuesta;
    }

    /**
     * LOGICA PARA EL MANEJO DE ACTIVIDADES
     */
    // <editor-fold defaultstate="collapsed" desc="AÑADIR ACTIVIDAD">
    String idUsuarioActividad;
    POActividadBase tempActividad;

    /**
     * Este método se utiliza para iniciar una actividad, ya existente o crear
     * una nueva
     */
    public void initActividad() {
        //initLinea();
        if (tempActividad == null) {
            tempActividad = new POActividad();
            tempActividad.setEstado(EstadoActividadPOA.NO_FINALIZADA);
            tempActividad.setInsumos(new LinkedList());
        }
        if (tempActividad.getResponsable() == null) {
            idUsuarioActividad = null;
        } else {
            idUsuarioActividad = tempActividad.getResponsable().getUsuId().toString();
        }
        super.initSelectOneUTBean();
        unidadTecnicaSelecionada = tempActividad.getUtResponsable();
    }

    /**
     * *
     * Este método se utiliza para guardar la actividad en edición
     */
    public void guardarActividad() {
        try {
            tempActividad.setUtResponsable(unidadTecnicaSelecionada);
            if (TextUtils.isEmpty(idUsuarioActividad)) {
                tempActividad.setResponsable(null);
            } else {
                SsUsuario responsable = (SsUsuario) emd.getEntityById(SsUsuario.class.getName(), Integer.valueOf(idUsuarioActividad));
                tempActividad.setResponsable(responsable);
            }

            guardarActividad(tempActividad);

            //se vuelve a guardar el proyecto
            guardar();

            RequestContext.getCurrentInstance().execute("$('#anadirActividad').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para eliminar la actividad en edición
     */
    public void eliminarActividadLinea() {
        try {
            if (!tempActividad.getInsumos().isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTEN_INSUMOS_ASOCIADOS_A_LA_ACTIVIDAD);
                throw b;
            }

            eliminarActividad(tempActividad);

            //se vuelve a guardar el proyecto
            guardar();

            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE ISNUMOTS
     */
    // <editor-fold defaultstate="collapsed" desc="AÑADIR INSMUMO">
    protected POInsumos tempInsumo;
    protected Integer idInsumo;
    //private Integer anioInsumo;
    private Date FCMDesde;
    private Integer FCMCantMeses;
    private String tmpIdTramo;

    /**
     * Este método se utiliza para iniciar un insumo, ya sea un insumo nuevo o
     * uno existente.
     */
    public void initInsumo() {
        montosSeleccionadosParaReporte = new LinkedList<>();
        //initLinea();
        if (tempInsumo == null) {
            tempInsumo = new POInsumos();
            idInsumo = null;
            //tempInsumo.setTipoMontoEstructura(TipoMontoEstructura.IMPORTE);
            tempInsumo.setMontosFuentes(new LinkedList());
            tempInsumo.setFlujosDeCajaAnio(new LinkedHashSet());
            tempInsumo.setNoUACI(false);
            tempInsumo.setEnviadoParaCertificar(false);
            tempInsumo.setPasoValidacionCertificadoDeDispPresupuestaria(false);
            //tmpIdTramo = null;
            //anioInsumo = null;
            tempInsumo.setTramo(getTramoCorrespondeInsumo());
            //
            tempInsumo.setPoa(getPOAEnTrabajo());
            tempInsumo.setUnidadTecnica(getPOAEnTrabajo().getUnidadTecnica());
        }

        tmpIdTramo = null;
        if (tempInsumo.getTramo() != null) {
            tmpIdTramo = tempInsumo.getTramo().getId().toString();
        }
        //idInsumo = tempInsumo.getId();
        //anioInsumo = DatesUtils.getYearOfDate(tempInsumo.getFechaContratacion());

        if (tempInsumo.getPoa() == null) {
            tempInsumo.setPoa(getPOAEnTrabajo());
            if (getPOAEnTrabajo() != null) {
                tempInsumo.setUnidadTecnica(getPOAEnTrabajo().getUnidadTecnica());
            }
        }

        cantidadMontosFuentesDePoInsumo = 1;//Por defecto es una fuente (para AC y ANP)
        insumoTieneFuenteCertificadaOParaCertificar = insumoTieneFuenteCertificadaOParaCertificar(tempInsumo);

        calcularTotalProgramacionFinancieraInsumo();
        calcularTotalProgramacionFinancieraMontosFuentes();

    }

    /**
     * Calcula el total de los montos del insumo ingresado en los 12 meses
     */
    public void calcularTotalProgramacionFinancieraInsumo() {
        tempInsumo.setTotalProgramacionFinanciera(BigDecimal.ZERO);
        for (FlujoCajaAnio fca : tempInsumo.getFlujosDeCajaAnio()) {
            for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                tempInsumo.setTotalProgramacionFinanciera(tempInsumo.getTotalProgramacionFinanciera().add(fcm.getMonto()));
                //Si es un proyecto con solo una fuente, copio el saldo del mes a la relación monto fuente con FCM
                if (tempInsumo.getMontosFuentes().size() == 1) {
                    if (fcm.getMontosFuentesInsumosFCM() != null && !fcm.getMontosFuentesInsumosFCM().isEmpty()) {
                        fcm.getMontosFuentesInsumosFCM().get(0).setMonto(fcm.getMonto());
                    }
                }
            }
        }
    }

    /**
     * Calcula el total de los montos de las fuentes del insumo en los 12 meses
     */
    public void calcularTotalProgramacionFinancieraMontosFuentes() {
        for (POMontoFuenteInsumo montoFuente : tempInsumo.getMontosFuentes()) {
            montoFuente.setTotalProgramacionFinanciera(BigDecimal.ZERO);
            if (montoFuente.getMontosFuentesInsumosFCM() != null) {
                for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                    if (montoFuenteFCM.getMonto() != null) {
                        montoFuente.setTotalProgramacionFinanciera(montoFuente.getTotalProgramacionFinanciera().add(montoFuenteFCM.getMonto()));
                    }
                }
            }
        }
    }

    /**
     * Ordena los montos fuentes por código de fuente de recurso
     *
     * @param listaMontosFuentes
     * @return
     */
    public List<POMontoFuenteInsumo> ordenarMontosFuentes(List<POMontoFuenteInsumo> listaMontosFuentes) {
        if (listaMontosFuentes != null) {
            Collections.sort(listaMontosFuentes, new Comparator<POMontoFuenteInsumo>() {
                @Override
                public int compare(POMontoFuenteInsumo o1, POMontoFuenteInsumo o2) {
                    return o1.getFuente().getFuenteRecursos().getCodigo().compareTo(o2.getFuente().getFuenteRecursos().getCodigo());
                }
            });
        }
        return listaMontosFuentes;
    }

    /**
     * Ordena los montos fuentes FCM por código de fuente de recurso
     *
     * @param listaMontosFuentesFCM
     * @return
     */
    public List<POMontoFuenteInsumoFlujoCajaMensual> ordenarMontosFuentesFCM(List<POMontoFuenteInsumoFlujoCajaMensual> listaMontosFuentesFCM) {
        if (listaMontosFuentesFCM != null) {
            Collections.sort(listaMontosFuentesFCM, new Comparator<POMontoFuenteInsumoFlujoCajaMensual>() {
                @Override
                public int compare(POMontoFuenteInsumoFlujoCajaMensual o1, POMontoFuenteInsumoFlujoCajaMensual o2) {
                    return o1.getMontoFuenteInsumo().getFuente().getFuenteRecursos().getCodigo().compareTo(o2.getMontoFuenteInsumo().getFuente().getFuenteRecursos().getCodigo());
                }
            });
        }
        return listaMontosFuentesFCM;
    }

    /**
     * Este método se utiliza para generar el flujo de caja mensual del insumo
     * en edición
     */
    public void generarFCM() {
        try {
            if (FCMDesde == null || FCMCantMeses == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_INGRESAR_MES_INICIAL_Y_CANTIDAD_MESES);
                throw b;
            }
            if (FCMCantMeses == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CANTIDAD_MESES_INCORRECTA);
                throw b;
            }

            int inicioAnio = DatesUtils.getYearOfDate(FCMDesde);
            int finAnio = DatesUtils.getYearOfDate(DatesUtils.sumarMeses(FCMDesde, FCMCantMeses));

            //si esta en un poa valida los años generados cumplan
            if (getPOAEnTrabajo() != null) {
                int poaAnio = getPOAEnTrabajo().getAnioFiscal().getAnio();
                if (inicioAnio != poaAnio || finAnio != poaAnio) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_LA_FECHA_A_GENERAR_EL_FLUJO_DE_CAJA_MENSUAL_COMPRENDE_ANIOS_DISTINTOS_AL_POA);
                    throw b;
                }
            }

            //Se genera el flujo de caja pero no se sustituye por el que tiene el insumo, si no que se copian los valores
            for (POMontoFuenteInsumo montoFuente : tempInsumo.getMontosFuentes()) {
                for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                    montoFuenteFCM.setMonto(BigDecimal.ZERO);
                }
            }

            Set<FlujoCajaAnio> fca = FlujoCajaMensualUtils.generarFlujoCajaMensual(FCMDesde, FCMCantMeses, tempInsumo.getMontoTotal());
            for (FlujoCajaAnio anio : fca) {
                for (POFlujoCajaMenusal fcm : anio.getFlujoCajaMenusal()) {
                    for (FlujoCajaAnio fcaInsumo : tempInsumo.getFlujosDeCajaAnio()) {
                        if (fcaInsumo.getAnio().equals(anio.getAnio())) {
                            for (POFlujoCajaMenusal fcmInsumo : fcaInsumo.getFlujoCajaMenusal()) {
                                if (fcmInsumo.getMes().equals(fcm.getMes())) {
                                    fcmInsumo.setMonto(fcm.getMonto());
                                }
                            }
                        }
                    }
                }
            }
            calcularTotalProgramacionFinancieraInsumo();
            calcularTotalProgramacionFinancieraMontosFuentes();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para guardar el insumo en edición
     */
    public void guardarInsumo() {
        try {

            if (tempInsumo.getFechaContratacion() != null && getPOAEnTrabajo() != null) {
                int anioInsumo = DatesUtils.getYearOfDate(tempInsumo.getFechaContratacion());
                if (anioInsumo != getPOAEnTrabajo().getAnioFiscal().getAnio().intValue()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ANIO_CONTRATACION_DE_INSUMO_DINSTINTO_POA);
                    throw b;
                }
            }

            guardarInsumo(tempInsumo);
            //se vuelve a guardar el proyecto
            guardar();
            RequestContext.getCurrentInstance().execute("$('#anadirInsumo').modal('hide');");
            RequestContext.getCurrentInstance().execute("$('#veranadirInsumo').modal('hide');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método implementa el cambio de un insumo desde UACI a no-UACI
     */
    public void reloadNoUacio() {
        if (tempInsumo.getNoUACI() == true) {
            if (tempInsumo.getFlujosDeCajaAnio() == null || tempInsumo.getFlujosDeCajaAnio().isEmpty()) {

                tempInsumo.setFlujosDeCajaAnio(new LinkedHashSet());
                //se setea un flujo de caja mensual solo
                Calendar cal = Calendar.getInstance();
                cal.setTime(getInicioPO());
                int year = cal.get(Calendar.YEAR);
                tempInsumo.getFlujosDeCajaAnio().add(FlujoCajaMensualUtils.generarFCM(year));
                crearRelacionesMontoFuenteInsumoConFlujoDeCajMensual();
            }
        }
    }

    private void crearRelacionesMontoFuenteInsumoConFlujoDeCajMensual() {
        for (FlujoCajaAnio fca : tempInsumo.getFlujosDeCajaAnio()) {
            for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                if (fcm.getMontosFuentesInsumosFCM() == null) {
                    fcm.setMontosFuentesInsumosFCM(new LinkedList<POMontoFuenteInsumoFlujoCajaMensual>());
                }
                for (POMontoFuenteInsumo montoFuenteInsumo : tempInsumo.getMontosFuentes()) {
                    if (montoFuenteInsumo.getMontosFuentesInsumosFCM() == null) {
                        montoFuenteInsumo.setMontosFuentesInsumosFCM(new LinkedList<POMontoFuenteInsumoFlujoCajaMensual>());
                    }
                    POMontoFuenteInsumoFlujoCajaMensual montoFuenteInsumoFCM = new POMontoFuenteInsumoFlujoCajaMensual();
                    montoFuenteInsumoFCM.setFlujoCajaMensual(fcm);
                    montoFuenteInsumoFCM.setMontoFuenteInsumo(montoFuenteInsumo);
                    fcm.getMontosFuentesInsumosFCM().add(montoFuenteInsumoFCM);
                    montoFuenteInsumo.getMontosFuentesInsumosFCM().add(montoFuenteInsumoFCM);
                }
            }
        }
    }

    /**
     * metodo utilizado en pog y poa que retorna el tramo al que corresponde el
     * insumo en el momento de la creacion del mismo
     *
     * @return
     */
    public ProyectoAporteTramoTramo getTramoCorrespondeInsumo() {
        return null;
    }

    /**
     * Este método elimina el insumo en edición
     */
    public void eliminarInsumoActividad() {
        try {

            eliminarInsumo(tempInsumo);

            //se vuelve a guardar el proyecto
            guardar();

            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método recalcula el monto total del insumo, en base al unitario y la
     * cantidad.
     */
    public void recalcualrMontoTotalTmpInsumo() {
        try {
            tempInsumo.setMontoTotal(tempInsumo.getMontoUnit().multiply(new BigDecimal(tempInsumo.getCantidad())));
        } catch (Exception e) {
        }
    }

    public void recalcularImporteFuente(POMontoFuenteInsumo fuente) {
        try {
//            if (tempInsumo.getTipoMontoEstructura() ==TipoMontoEstructura.IMPORTE){  
//                BigDecimal porcentaje = fuente.getMonto().multiply(NumberUtils.CIEN);
//                fuente.setPorcentaje(porcentaje.divide(tempInsumo.getMontoTotal(), 2,RoundingMode.DOWN));                
//            }else if (tempInsumo.getTipoMontoEstructura() ==TipoMontoEstructura.PORCENTAJE){
//                fuente.setMonto(NumberUtils.porcentaje(fuente.getPorcentaje(), tempInsumo.getMontoTotal(), RoundingMode.DOWN));                
//            }
        } catch (Exception e) {
        }
    }

    // </editor-fold>
    /**
     * LOGICA PARA EL MANEJO DE TDR
     */
    // <editor-fold defaultstate="collapsed" desc="AÑADIR TDR">
    protected TDR tempTDR;
    private UploadedFile tempFile;

    /**
     * Este método se utiliza para incializar un TDR
     */
    public void initTDR() {
        tempTDR = generalPODelegate.getTDRInsumo(tempInsumo.getId());
        if (tempTDR == null) {
            tempTDR = new TDR();
            RequestContext.getCurrentInstance().execute("$('#anadirTDRModal').modal('show');");
        } else {
            RequestContext.getCurrentInstance().execute("$('#verTDRModal').modal('show');");
        }
    }

    /**
     * Este método se utiliza para guardar un TDR
     */
    public void guardarTDR() {
        try {
            if (TextUtils.isEmpty(tempTDR.getContenido()) && tempTDR.getTempUploadedFile() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_TDR_VACIO_AL_MENOS_ESCRIBA_TEXTO_O_SELECCIONE_ARCHIVO);
                throw b;
            }
            //tempTDR.setTempUploadedFile(ArchivoUtils.getDataFile(tempFile));
            generalPODelegate.saveTDRInsumo(tempInsumo.getId(), tempTDR);
            reloadPO();
            RequestContext.getCurrentInstance().execute("$('#anadirTDRModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para guardar la disponibilidad presupuestaria del
     * insumo
     */
    public void guardarDisponibilidadPresupuestaria() {
        try {
            insumoDelegate.guardarDisponibilidadPresupuestriaInsumo(tempInsumo);
            reloadPO();
            RequestContext.getCurrentInstance().execute("$('#anadirDisponibilidadPresModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para generar el certificado de disponibilidad
     * presupuestaria del insumo
     *
     * @param idPOMontoFuenteInsumo
     */
    public void generarCertificadoDisponibilidadPOInsumo(Integer idPOMontoFuenteInsumo) {
        try {
            byte[] bytespdf = reporteDelegate.generarCertificadoDisponibilidadPOInsumo(idPOMontoFuenteInsumo);
            ArchivoUtils.downloadPdfFromBytes(bytespdf, "CertificadoDeDisponibilidadPresupuestaria.pdf");
            //reloadPO();
            //RequestContext.getCurrentInstance().execute("$('#anadirTDRModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para validar el certificado de disponibilidad
     * presupuestaria
     */
    public void eviarValidacionCertificado() {
        try {
            if (montosSeleccionadosParaReporte.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PARA_ENVIAR_A_VALIDAR_AL_MENOS_DEBE_SELECCIONAR_UN_MONTO);
                throw b;
            }
            List<POMontoFuenteInsumo> fuentesSeleccionadas = new LinkedList<>();

            certPresupDelegate.enviarAvalidarFuentes(tempInsumo.getPoa().getUnidadTecnica().getId(), montosSeleccionadosParaReporte);
            reloadPO();
            RequestContext.getCurrentInstance().execute("$('#anadirDisponibilidadPresModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }

    }

    /**
     * Verifica si un existe un monto fuente entre los que tiene el poInsumo que
     * esté asociado a un certificado de disponibilidad presupuestaria con
     * estado vacío o rechazado
     *
     * @return
     */
    public Boolean existeMontoFuenteDisponibleParaEnviar() {
        List<POMontoFuenteInsumo> montosFuentes = new LinkedList<>();
        if (tempInsumo != null && tempInsumo.getMontosFuentes() != null) {
            montosFuentes = tempInsumo.getMontosFuentes();
        }
        return certPresupDelegate.existeMontoFuenteDisponibleParaEnviar(montosFuentes);
    }

    /**
     * Este método se utiliza para descargar un archivo
     *
     * @param archivo
     */
    public void downloadFile(Archivo archivo) {
        try {
            ArchivoUtils.downloadFile(archivo, archivoDelegate.getFile(archivo));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para subir el TDR
     *
     * @param event
     */
    public void fileUploadListener(FileUploadEvent event) {
        try {
            tempFile = event.getFile();
            if (tempFile != null) {
                tempTDR.setTempUploadedFile(ArchivoUtils.getDataFile(tempFile));
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // </editor-fold>
    /**
     * *
     * LOGICA PARA SUMAR LOS TOTALES DE LO USADO
     */
    /**
     * Este método se utiliza para retornar el total usado por las lineas.
     *
     * @param linea
     * @return
     */
    public BigDecimal getTotalLinea(POLinea linea) {
        BigDecimal total = BigDecimal.ZERO;
        for (POActividadBase actividad : linea.getActividades()) {
            for (POInsumos insumo : actividad.getInsumos()) {
                if (insumo.getMontoTotal() != null) {
                    total = total.add(insumo.getMontoTotal());
                }
            }
        }
        return total;

    }

    /**
     * Este método retorna el total usado de la actividad
     *
     * @param actividad
     * @return
     */
    public BigDecimal getTotalActividad(POActividadBase actividad) {
        BigDecimal total = BigDecimal.ZERO;
        for (POInsumos insumo : actividad.getInsumos()) {
            if (insumo.getMontoTotal() != null) {
                total = total.add(insumo.getMontoTotal());
            }
        }
        return total;

    }

    /**
     * Verifica si un monto fuente está asociado a un certificado de
     * disponibilidad presupuestaria con estado vacío o rechazado
     *
     * @param montoFienteId
     * @return
     */
    public Boolean estaMontoFuenteDisponibleParaEnviar(Integer montoFienteId) {
        return certPresupDelegate.estaMontoFuenteDisponibleParaEnviar(montoFienteId);
    }

    // <editor-fold defaultstate="collapsed" desc="getter caluclados">
    /**
     * Este método retorna los años disponibles en el proyecto proyecto
     *
     * @return
     */
    public Map<Integer, Integer> getAniosProyecto() {
        Map<Integer, Integer> m = new LinkedHashMap<>();
        List<Integer> l = getListAniosPO();
        if (l != null) {
            for (Integer anio : l) {
                if (anio != null) {
                    m.put(anio, anio);
                }
            }
        }
        return m;
    }

    /**
     * Este método retorna el nombre del tipo de seguimiento
     *
     * @param t
     * @param i
     * @return
     */
    public String getTituloSeguimiento(TipoSeguimiento t, Integer i) {
        return TipoSeguimientoUtils.getListName(t).get(i);
    }

    /**
     * Este método retorna los nombres en el caso de seguimiento mensual
     *
     * @param i
     * @return
     */
    public String getMesName(Integer i) {
        return TipoSeguimientoUtils.getListName(TipoSeguimiento.MENSUAL).get(i);
    }

    /**
     * Este método retorna el multiplicador según el tipo de seguimiento
     *
     * @param t
     * @return
     */
    public Integer getMultiplicador(TipoSeguimiento t) {
        return TipoSeguimientoUtils.getMultiplicador(t);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getIdUsuarioActividad() {
        return idUsuarioActividad;
    }

    public void setIdUsuarioActividad(String idUsuarioActividad) {
        this.idUsuarioActividad = idUsuarioActividad;
    }

    public POActividadBase getTempActividad() {
        return tempActividad;
    }

    public void setTempActividad(POActividadBase tempActividad) {
        this.tempActividad = tempActividad;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public Date getFCMDesde() {
        return FCMDesde;
    }

    public void setFCMDesde(Date FCMDesde) {
        this.FCMDesde = FCMDesde;
    }

    public Integer getFCMCantMeses() {
        return FCMCantMeses;
    }

    public void setFCMCantMeses(Integer FCMCantMeses) {
        this.FCMCantMeses = FCMCantMeses;
    }

    public String getTmpIdTramo() {
        return tmpIdTramo;
    }

    public void setTmpIdTramo(String tmpIdTramo) {
        this.tmpIdTramo = tmpIdTramo;
    }

    public List<UnidadTecnica> getUsuarioUnidadTecnicas() {
        return usuarioUnidadTecnicas;
    }

    public void setUsuarioUnidadTecnicas(List<UnidadTecnica> usuarioUnidadTecnicas) {
        this.usuarioUnidadTecnicas = usuarioUnidadTecnicas;
    }

    public POInsumos getTempInsumo() {
        return tempInsumo;
    }

    public String getIdUnidadTecnica() {
        return idUnidadTecnica;
    }

    public void setIdUnidadTecnica(String idUnidadTecnica) {
        this.idUnidadTecnica = idUnidadTecnica;
    }

    public UsuarioInfo getUsuarioInfo() {
        return usuarioInfo;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public ArchivoDelegate getArchivoDelegate() {
        return archivoDelegate;
    }

    public void setArchivoDelegate(ArchivoDelegate archivoDelegate) {
        this.archivoDelegate = archivoDelegate;
    }

    public GeneralPODelegate getGeneralPODelegate() {
        return generalPODelegate;
    }

    public void setGeneralPODelegate(GeneralPODelegate generalPODelegate) {
        this.generalPODelegate = generalPODelegate;
    }

    public TDR getTempTDR() {
        return tempTDR;
    }

    public void setTempTDR(TDR tempTDR) {
        this.tempTDR = tempTDR;
    }

    public UploadedFile getTempFile() {
        return tempFile;
    }

    public void setTempFile(UploadedFile tempFile) {
        this.tempFile = tempFile;
    }

    public void setTempInsumo(POInsumos tempInsumo) {
        this.tempInsumo = tempInsumo;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public List<POMontoFuenteInsumo> getMontosSeleccionadosParaReporte() {
        return montosSeleccionadosParaReporte;
    }

    public void setMontosSeleccionadosParaReporte(List<POMontoFuenteInsumo> montosSeleccionadosParaReporte) {
        this.montosSeleccionadosParaReporte = montosSeleccionadosParaReporte;
    }

    public Integer getCantidadMontosFuentesDePoInsumo() {
        return cantidadMontosFuentesDePoInsumo;
    }

    public void setCantidadMontosFuentesDePoInsumo(Integer cantidadMontosFuentesDePoInsumo) {
        this.cantidadMontosFuentesDePoInsumo = cantidadMontosFuentesDePoInsumo;
    }

    public Boolean getInsumoTieneFuenteCertificadaOParaCertificar() {
        return insumoTieneFuenteCertificadaOParaCertificar;
    }

    public void setInsumoTieneFuenteCertificadaOParaCertificar(Boolean insumoTieneFuenteCertificadaOParaCertificar) {
        this.insumoTieneFuenteCertificadaOParaCertificar = insumoTieneFuenteCertificadaOParaCertificar;
    }

    // </editor-fold>
    /**
     * lógica para el manejo de sugerencias
     */
    /**
     * Este método verifica si existe alguna sugerencia para el monto de la
     * fuente
     *
     * @param monto
     * @return
     */
    public boolean tieneSugerencia(POMontoFuenteInsumo monto) {
        return CalcularSugerenciasMonto.tieneSugerencia(monto);
    }

    /**
     * Este método realiza el calculo de la sugerencia
     *
     * @param monto
     * @param montoTotal
     * @return
     */
    public BigDecimal caclularSugerencia(POMontoFuenteInsumo monto, BigDecimal montoTotal) {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método se utiliza para solicitar una desertificación en los montos
     * de algunas de las fuentes
     *
     * @param idMonto
     */
    public void solicitarDescertificacionMontoPOInsumo(Integer idMonto) {
        try {
            generalPODelegate.solicitarDescertificacionMontoPOInsumo(idMonto);
            reloadPO();
            RequestContext.getCurrentInstance().execute("$('#anadirDescertificarInsumoPorFuenteModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Solo permito solicitar la desertificación de un insumo si el insumo no
     * pertenece a ningún proceso de adquisición y si tiene por lo menos un
     * monto certificado (si no, no va a poder descertificar nada)
     *
     * @return
     */
    public Boolean mostrarDescertificacionInsumo(Integer idInsumo) {
        POInsumos insumo = generalPODelegate.getPOInsumoByID(idInsumo);
        Boolean mostrar = insumo.getProcesoInsumo() == null;
        if (mostrar) {
            mostrar = false;
            Iterator<POMontoFuenteInsumo> itMontosFuentes = insumo.getMontosFuentes().iterator();
            while (itMontosFuentes.hasNext() && !mostrar) {
                POMontoFuenteInsumo montoFuente = itMontosFuentes.next();
                mostrar = BooleanUtils.isTrue(montoFuente.getCertificadoDisponibilidadPresupuestariaAprobada());
            }
        }
        return mostrar;
    }

    /**
     * Verifica si alguna de las fuentes del insumo fue enviada para
     * certificación o ya fue certificada
     *
     * @return
     */
    public Boolean insumoTieneFuenteCertificadaOParaCertificar(POInsumos poInsumo) {
        try {
            return generalPODelegate.insumoTieneFuenteCertificadaOParaCertificar(poInsumo);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
        return null;
    }

    public String getObservacionesInsumoParaTabla(String observacion) {
        return TextUtils.limitarLargoTexto(observacion, 100);
    }
    
    /**
     * Devuelve el id de un contrato al que pertenece un PoInsumo
     * @param poInsumo
     * @return 
     */
    public Boolean getContratoAsociadoAInsumo(POInsumos poInsumo){
        return generalPODelegate.getContratoAsociadoAInsumo(poInsumo) != null;
    }
    
    /**
     * Genera y descarga el reporte de ficha de un Contrato / OC que contiene determinado insumo
     * @param poInsumo 
     */
    public void generarFichaContratoQueContieneInsumo(POInsumos poInsumo){
        
        ContratoOC contrato = generalPODelegate.getContratoAsociadoAInsumo(poInsumo);
        
        byte[] bytespdf = reporteDelegate.generarFichaContrato(contrato.getId());
        FacesContext fc = FacesContext.getCurrentInstance();

        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength(bytespdf.length);
        
        String nombreArchivo = "FichaContrato_" + contrato.getNroContrato() + ".pdf";
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");
        OutputStream output = null;
        try {
            output = ec.getResponseOutputStream();
            output.write(bytespdf);
        } catch (IOException ex) {
            Logger.getLogger(ContratoOrdenCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
        fc.responseComplete();
    }

}
