/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.business.datatype.DataPOSeleccion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.POAProyectoDelegate;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import static gob.mined.siap2.web.mb.impl.po.POConActividadesEInsumosAbstract.logger;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que asocia lineas de otro POA al actual.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaProyectoAsociarLineas")
public class POAProyectoAsociarLineas extends POProyectoConLineasAbstract implements Serializable {

    @Inject
    ProyectoDelegate proyectoDelegate;

    @Inject
    POAProyectoDelegate pOAProyectoDelegate;

    private POAProyecto poa;
    private List<POAProyecto> listaADuplicar;

    private DataPOSeleccion pogADuplicar;
    private POInsumos insumoADuplicar;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        String idPoa = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPoa");
        initProyecto(Integer.valueOf(idPoa));
    }

    /**
     * Este método se encarga de hacer la inicialización
     * @param idPOA 
     */
    public void initProyecto(Integer idPOA) {
        try {
            poa = (POAProyecto) emd.getEntityById(POAProyecto.class.getName(), idPOA);
            listaADuplicar = pOAProyectoDelegate.getPOASParaDucplicar(poa.getId());

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            //RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método se encarga de agregar las lineas a duplicar al POA actual
     * 
     * @return 
     */
    public String guardarDuplicacion() {
        try {

            List<Integer> insumosAduplicar = new LinkedList();
            for (POAProyecto poa : listaADuplicar) {
                for (POLinea linea : poa.getLineas()) {
                    for (POActividadBase actividad : linea.getActividades()) {
                        for (POInsumos insumo : actividad.getInsumos()) {
                            if (insumo.getDuplicar()) {
                                insumosAduplicar.add(insumo.getId());
                            }
                        }
                    }
                }
            }

            pOAProyectoDelegate.duplicarLineaEnPOA(poa.getId(), insumosAduplicar);

            return "crearEditarPOAProyecto.xhtml?id="+ poa.getProyecto().getId()
                +"&idAnioFiscal="+ poa.getAnioFiscal().getId()
                +"&idUnidadTecnica="+ poa.getUnidadTecnica().getId()
                + "&faces-redirect=true";
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

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public POAProyecto getPoa() {
        return poa;
    }

    public POInsumos getInsumoADuplicar() {
        return insumoADuplicar;
    }

    public void setInsumoADuplicar(POInsumos insumoADuplicar) {
        this.insumoADuplicar = insumoADuplicar;
    }

    public DataPOSeleccion getPogADuplicar() {
        return pogADuplicar;
    }

    public void setPogADuplicar(DataPOSeleccion pogADuplicar) {
        this.pogADuplicar = pogADuplicar;
    }

    public ProyectoDelegate getProyectoDelegate() {
        return proyectoDelegate;
    }

    public void setProyectoDelegate(ProyectoDelegate proyectoDelegate) {
        this.proyectoDelegate = proyectoDelegate;
    }

    public POAProyectoDelegate getpOAProyectoDelegate() {
        return pOAProyectoDelegate;
    }

    public void setpOAProyectoDelegate(POAProyectoDelegate pOAProyectoDelegate) {
        this.pOAProyectoDelegate = pOAProyectoDelegate;
    }

    public List<POAProyecto> getListaADuplicar() {
        return listaADuplicar;
    }

    public void setListaADuplicar(List<POAProyecto> listaADuplicar) {
        this.listaADuplicar = listaADuplicar;
    }

    public void setPoa(POAProyecto poa) {
        this.poa = poa;
    }

    // </editor-fold>
}
