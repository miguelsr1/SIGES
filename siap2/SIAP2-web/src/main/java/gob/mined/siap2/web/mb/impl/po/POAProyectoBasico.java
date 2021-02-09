/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.business.ejbs.impl.POAProyectoBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.POAProyectoDelegate;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import static gob.mined.siap2.web.mb.impl.po.POConActividadesEInsumosAbstract.logger;
import java.io.Serializable;
import java.util.logging.Level;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que de un POA de proyecto básico.
 *
 * @author Sofis Solutions
 */
public abstract class POAProyectoBasico extends POProyectoConLineasAbstract implements Serializable {

    protected POAProyecto poa;

    @Inject
    protected POAProyectoBean bean;
    @Inject
    protected POAProyectoDelegate pOAProyectoDelegate;
    @Inject
    protected ProyectoDelegate proyectoDelegate;

    @Inject
    UsuarioDelegate usuarioDelegate;
    
    @Override
    public void guardar() {
    }

     
    /**
     * Este método retorna el POA en edición
     * @return 
     */
    @Override
    public GeneralPOA getPOAEnTrabajo(){
        return poa;
    }
    /**
     * este guardar insumo es el mismo que el de colaborar
     */
    @Override
    public void guardarInsumo() {
        try {
            if (tempInsumo.getActividad() == null) {
                tempInsumo.setActividad(getActividadEnUso());
            }
            pOAProyectoDelegate.guardarInsumo(this.objeto.getId(), poa.getId(), tempLinea.getId(), tempActividad.getId(), tempInsumo);

            guardarInsumo(tempInsumo);
            reloadPO();

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
     * método que se encarga de eliminar un insumo
     */
    @Override
    public void eliminarInsumoActividad() {
        try {
            pOAProyectoDelegate.eliminarInsumo(poa.getId(), tempInsumo.getActividad().getId(), tempInsumo);
            reloadPO();

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
     * Este método es el encargado de guardar una actividad de un POA de proyecto
     */    
    @Override
    public void guardarActividad() {
        try {
            tempActividad.setUtResponsable(unidadTecnicaSelecionada);
            if (TextUtils.isEmpty(idUsuarioActividad)) {
                tempActividad.setResponsable(null);
            } else {
                SsUsuario responsable = (SsUsuario) emd.getEntityById(SsUsuario.class.getName(), Integer.valueOf(idUsuarioActividad));
                tempActividad.setResponsable(responsable);
            }

            pOAProyectoDelegate.guardarActividad(this.objeto.getId(), poa.getId(), tempLinea.getId(), (POActividadProyecto) tempActividad);
            reloadPO();
            
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
     * Este metodo elimina la actividad en edición de un POA de proyecto.
     */
    @Override
    public void eliminarActividadLinea() {
        try {
            pOAProyectoDelegate.eliminarActividad(this.objeto.getId(), poa.getId(), tempLinea.getId(), (POActividadProyecto) tempActividad);
            reloadPO();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    
    
    
    
}
