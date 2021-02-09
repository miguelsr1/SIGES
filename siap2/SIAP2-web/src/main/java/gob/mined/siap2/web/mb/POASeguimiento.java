/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.entities.data.impl.POAActividadMeta;
import gob.mined.siap2.entities.data.impl.PeriodoSeguimientoIndicadores;
import gob.mined.siap2.entities.data.impl.ProgramacionTrimestral;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UnidadTecnicaDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralDelegate;
import gob.mined.siap2.web.delegates.impl.IndicadorDelegate;
import gob.mined.siap2.web.delegates.impl.POADelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramacionTrimestralDelegate;
import gob.mined.siap2.web.delegates.impl.ValoresDeIndicadoresDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "POASeguimiento")
public class POASeguimiento implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    UtilsMB utilsMB;
    
    @Inject
    EntityManagementDelegate emd;
    
    @Inject
    GeneralDelegate peled;

    @Inject
    VersionesDelegate versionDelegate;

    @Inject
    POADelegate poaDelegate;
    
    @Inject
    IndicadorDelegate indicadorDelegate;
    
    @Inject
    UnidadTecnicaDelegate unidadTecnicaDelegate;
    
    @Inject
    ProgramaDelegate progamaDelegate;
    
    @Inject
    private UsuarioInfo usuarioInfo;
    
    @Inject
    private UsuarioDelegate usuarioDelegate;
    
    @Inject
    ProgramacionTrimestralDelegate progTrimestralDelegate;
    
    private POA poa;
    private POAMetaAnual meta;
    private POAActividadMeta actividad;
    private Integer anioFiscalId;
    private Boolean permisoDarSeguimientoMeta = false;
    private Boolean permisoDarSeguimientoActividades = false;
    
    private Boolean permisoEditarPrimerTrimestre = false;
    private Boolean permisoEditarSegundoTrimestre = false;
    private Boolean permisoEditarTercerTrimestre = false;
    private Boolean permisoEditarCuartoTrimestre = false;
    List<POAActividadMeta> listaActividades;
    private Boolean metaActual = false;
    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            poa = poaDelegate.obtenerPOAPorId(Integer.valueOf(id));
            anioFiscalId = poa.getAnio() != null ? poa.getAnio().getId() : null;
            if(poa.getMetasAnuales() == null) {
                poa.setMetasAnuales(new ArrayList());
            }
            if(poa.getUnidadTecnica() != null && poa.getUnidadTecnica().getId() != null) {
                permisoDarSeguimientoMeta = usuarioDelegate.getUsuarioTienePermisoEnUTPadreConOperacion(poa.getUnidadTecnica().getId(), ConstantesEstandares.Operaciones.REGISTRAR_POA_APOYO, usuarioInfo.getUserCod());
            }
           
            if(permisoDarSeguimientoMeta) {
                //TO DO -Invocar el método para saber si se tiene permiso o no
               // permisoDarSeguimientoActividades = usuarioDelegate.getUsuarioTienePermisoEnUTOperativaConOperacion(poa.getUnidadTecnica().getId(), ConstantesEstandares.Operaciones.REGISTRAR_POA_APOYO, usuarioInfo.getUserCod());
                permisoDarSeguimientoActividades = true;
            }
            if(anioFiscalId != null && anioFiscalId > 0) {
                //Integer anioActual = LocalDate.now().getYear();
                ProgramacionTrimestral prog = progTrimestralDelegate.obtenerPorAnioFiscal(anioFiscalId);
                if(prog != null) {
                    LocalDateTime dateActual = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0);
                    LocalDateTime dateDesde = null;
                    LocalDateTime dateHasta = null;
                    if(prog.getFechaDesdePrimerTrimestre() != null && prog.getFechaHastaPrimerTrimestre() != null) {
                       // if(anioActual.compareTo(anioFiscalId) < 0) {
                        //    permisoEditarPrimerTrimestre = true;
                       // } else {
                            dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdePrimerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                            dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaPrimerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);
                            if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                                permisoEditarPrimerTrimestre = true;
                            } 
                        //}
                    }
                    
                    if(prog.getFechaDesdeSegundoTrimestre() != null && prog.getFechaHastaSegundoTrimestre()!= null) {
                        //if(anioActual.compareTo(anioFiscalId) < 0) {
                        //    permisoEditarSegundoTrimestre = true;
                       // } else {
                            dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeSegundoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                            dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaSegundoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);

                            if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                                permisoEditarSegundoTrimestre = true;
                            }
                        //}
                    }
                    
                    if(prog.getFechaDesdeTercerTrimestre() != null && prog.getFechaHastaTercerTrimestre() != null) {
                       // if(anioActual.compareTo(anioFiscalId) < 0) {
                        //    permisoEditarTercerTrimestre = true;
                       // } else {
                            dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeTercerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                            dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaTercerTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);

                            if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                                permisoEditarTercerTrimestre = true;
                            }
                       // }
                    }
                    
                    if(prog.getFechaDesdeCuartoTrimestre() != null && prog.getFechaHastaCuartoTrimestre() != null) {
                       // if(anioActual.compareTo(anioFiscalId) < 0) {
                        //    permisoEditarCuartoTrimestre = true;
                       // } else {
                            dateDesde = LocalDateTime.ofInstant(prog.getFechaDesdeCuartoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
                            dateHasta = LocalDateTime.ofInstant(prog.getFechaHastaCuartoTrimestre().toInstant(),ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59);

                            if(dateActual.isAfter(dateDesde) && dateActual.isBefore(dateHasta)) {
                                permisoEditarCuartoTrimestre = true;
                            }
                        //}
                    }
                }
            }
            
        }
    }
    
    public void loadActividad(String index) {
        if (StringUtils.isNotBlank(index)) {
            actividad = meta.getActividades().get(Integer.valueOf(index).intValue());
        }
        this.metaActual = true;
    }
    
     public void loadMeta(String index) {
        if (StringUtils.isNotBlank(index)) {
            meta = poa.getMetasAnuales().get(Integer.valueOf(index).intValue());
        }
        if(meta != null) {
            listaActividades = meta.getActividades();
        }
        this.metaActual = true;
    }
     
     public void reloadMontosTRimestralesRealTarea() {
        Integer monto = 0;
        if(meta != null) {
            if(meta.getProgramaPrimerTrimestreReal() != null) {
                monto = monto + meta.getProgramaPrimerTrimestreReal();
            }
            if(meta.getProgramaSegundoTrimestreReal() != null) {
                monto = monto + meta.getProgramaSegundoTrimestreReal();
            }
            if(meta.getProgramaTercerTrimestreReal() != null) {
                monto = monto + meta.getProgramaTercerTrimestreReal();
            }
            if(meta.getProgramaCuartoTrimestreReal() != null) {
                monto = monto + meta.getProgramaCuartoTrimestreReal();
            }
        }
        
        meta.setTotalReal(monto);
    }

  
      /**
     * Agrega un programa institucional a un programa presupuestario
     */
    public void saveActividadMetaAnual() {
        try {
            if(actividad.getMeta() == null) {
                listaActividades.add(actividad);
                meta.setActividades(listaActividades);
                
                if(meta.getPoa() == null) {  
                    poa.getMetasAnuales().add(meta);
                    meta.setPoa(poa);
                }
                actividad.setMeta(meta);
            }
            
            RequestContext.getCurrentInstance().execute("$('#anadirActividadMeta').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Guarda el poa en edición
     *
     * @return
     */
    public String guardar() {
        try {
            if(anioFiscalId != null && anioFiscalId > 0) {
                poa.setAnio(versionDelegate.getAniosFiscalesPorId(anioFiscalId));  
            }
            if(metaActual!= null && metaActual) {
                if(meta.getPoa() == null) {  
                    poa.getMetasAnuales().add(meta);
                    meta.setPoa(poa);
                } 
            } 
            
            poa = poaDelegate.guardar(poa, Boolean.TRUE, usuarioInfo.getUserCod(), Boolean.FALSE, Boolean.TRUE);
            if(metaActual!= null && metaActual) {
               metaActual = false; 
            }
            
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
     /**
     * Dirige el sitio a la consulta de programas presupuestarios
     *
     * @return
     */
    public String cerrar() {
        this.metaActual = false;
        return null;
    }
    
    /**
     * Dirige el sitio a la consulta de programas presupuestarios
     *
     * @return
     */
    public String salir() {
        this.metaActual = false;
        return "consultaSeguimientoPOA.xhtml?faces-redirect=true";
    }
    
    public POA getPoa() {
        return poa;
    }

    public void setPoa(POA poa) {
        this.poa = poa;
    }

    public POAMetaAnual getMeta() {
        return meta;
    }

    public void setMeta(POAMetaAnual meta) {
        this.meta = meta;
    }

    public POAActividadMeta getActividad() {
        return actividad;
    }

    public void setActividad(POAActividadMeta actividad) {
        this.actividad = actividad;
    }

    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public Boolean getMetaActual() {
        return metaActual;
    }

    public void setMetaActual(Boolean metaActual) {
        this.metaActual = metaActual;
    }

    public List<POAActividadMeta> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<POAActividadMeta> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public Boolean getPermisoDarSeguimientoMeta() {
        return permisoDarSeguimientoMeta;
    }

    public void setPermisoDarSeguimientoMeta(Boolean permisoDarSeguimientoMeta) {
        this.permisoDarSeguimientoMeta = permisoDarSeguimientoMeta;
    }

    public Boolean getPermisoDarSeguimientoActividades() {
        return permisoDarSeguimientoActividades;
    }

    public void setPermisoDarSeguimientoActividades(Boolean permisoDarSeguimientoActividades) {
        this.permisoDarSeguimientoActividades = permisoDarSeguimientoActividades;
    }

    public Boolean getPermisoEditarPrimerTrimestre() {
        return permisoEditarPrimerTrimestre;
    }

    public void setPermisoEditarPrimerTrimestre(Boolean permisoEditarPrimerTrimestre) {
        this.permisoEditarPrimerTrimestre = permisoEditarPrimerTrimestre;
    }

    public Boolean getPermisoEditarSegundoTrimestre() {
        return permisoEditarSegundoTrimestre;
    }

    public void setPermisoEditarSegundoTrimestre(Boolean permisoEditarSegundoTrimestre) {
        this.permisoEditarSegundoTrimestre = permisoEditarSegundoTrimestre;
    }

    public Boolean getPermisoEditarTercerTrimestre() {
        return permisoEditarTercerTrimestre;
    }

    public void setPermisoEditarTercerTrimestre(Boolean permisoEditarTercerTrimestre) {
        this.permisoEditarTercerTrimestre = permisoEditarTercerTrimestre;
    }

    public Boolean getPermisoEditarCuartoTrimestre() {
        return permisoEditarCuartoTrimestre;
    }

    public void setPermisoEditarCuartoTrimestre(Boolean permisoEditarCuartoTrimestre) {
        this.permisoEditarCuartoTrimestre = permisoEditarCuartoTrimestre;
    }
     
     
     
}
