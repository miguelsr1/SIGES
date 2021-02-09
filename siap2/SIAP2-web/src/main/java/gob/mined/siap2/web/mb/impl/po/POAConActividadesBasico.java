/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.impl.POAConActividadesDelegate;
import static gob.mined.siap2.web.mb.impl.po.POConActividadesEInsumosAbstract.logger;
import java.io.Serializable;
import java.util.logging.Level;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa la lógica para trabajar con los POA que tienen 
 * actividades (acción central y asignación no programable) haciendo las 
 * llamadas a los ejb correspondientes
 *
 * @author rgonzalez
 */
public class POAConActividadesBasico extends POAConMontoPorAnio implements Serializable {

    @Inject
    protected POAConActividadesDelegate pOAConActividadesDelegate;

    @Override
    public void guardar() {
//        poa = pOAProyectoDelegate.guardarPOATrabajo(objeto.getId(), poa);
    }

    /**
     * este guardar insumo es el mismo que el de colaborar
     */
    @Override
    public void guardarInsumo() {
        try {
            if (tempInsumo.getActividad() == null) {
                tempInsumo.setActividad(tempActividad);
            }
            tempInsumo = pOAConActividadesDelegate.guardarInsumo(poa.getId(), tempActividad.getId(), tempInsumo);
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
     * Este método que se encarga de eliminar un insumo
     */
    @Override
    public void eliminarInsumoActividad() {
        try {
            pOAConActividadesDelegate.eliminarInsumo(poa.getId(), tempInsumo.getActividad().getId(), tempInsumo);
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
     * Este método se encarga de guardar una actividad
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

            tempActividad = pOAConActividadesDelegate.guardarActividad(poa.getId(), tempActividad);

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
     * Este método se encarga de eliminar la actividad en edición
     */
    @Override
    public void eliminarActividadLinea() {
        try {
            pOAConActividadesDelegate.eliminarActividad(poa.getId(), tempActividad);
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
