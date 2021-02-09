/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import static gob.mined.siap2.web.mb.impl.po.POConActividadesEInsumosAbstract.logger;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que se utiliza para colaborar en una POA de proyecto
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaProyectoColaborar")
public class POAColaborar extends POAProyectoBasico implements Serializable {

    @Inject
    ProyectoDelegate proyectoDelegate;


    protected String idProyecto;
    protected String idAnioFiscal;
    List<AnioFiscal> posiblesAniosPOA;

    protected List<UnidadTecnica> utQueCompartieron;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        idProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idAnioFiscal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        idUnidadTecnica = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");

        if (!TextUtils.isEmpty(idProyecto)) {
            update = true;
            objeto = proyectoDelegate.getProyecto(Integer.valueOf(idProyecto));
            posiblesAniosPOA = pOAProyectoDelegate.getAniosDisponiblesProgramacionPOA(objeto.getId());
        }
        cambiaAnioSelecionado();
    }
    
    
    /**
     * Este método se utiliza para inicializar
     */
    public void initProyecto() {
        try {
            poa = null;
            objeto = proyectoDelegate.getProyecto(Integer.valueOf(idProyecto));
            reloadPO();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    

    /**
     * método que se ejecuta cuando el usuario cambia el año seleccionado
     */
    public void cambiaAnioSelecionado() {
        reloadUTQueCompartieron();
        if (TextUtils.isEmpty(idUnidadTecnica) && utQueCompartieron.size() == 1) {
            this.idUnidadTecnica = utQueCompartieron.get(0).getId().toString();
        }
        initProyecto();
    }

    /**
     * método que retorna las UT que han compartido
     */
    public void reloadUTQueCompartieron() {
        utQueCompartieron = Collections.EMPTY_LIST;
        idUnidadTecnica = null;
        if (!TextUtils.isEmpty(idAnioFiscal)) {            
            utQueCompartieron = pOAProyectoDelegate.getUTQueCompartieronConUT(objeto.getId(), Integer.valueOf(idAnioFiscal), usuarioUnidadTecnicas);
        }
        
    }

    /**
     * Este método retorna si el usuario es colaborador en una linea pasada por parámetro
     * 
     * @param linea
     * @return 
     */
    public Boolean esColaborador(POLinea linea) {
        for (UnidadTecnica ut : usuarioUnidadTecnicas) {
            if (linea.getColaboradoras().contains(ut)) {
                return true;
            }
        }
        return false;
    }

    
    /**
     * Recarga el POA en edición
     */
    public void reloadPO() {
        if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica)) {
            poa = pOAProyectoDelegate.getPOATrabajo(objeto.getId(), Integer.valueOf(idAnioFiscal), Integer.valueOf(idUnidadTecnica));
        }
    }
    
   
    /**
     * Retorna las lineas con las que se puede trabajar
     * 
     * @return 
     */
    @Override
    public List<POLinea> getLineas() {
        List<POLinea> res = new LinkedList();
        if (poa != null) {
            for (POLinea linea : poa.getLineas()) {
                if (esColaborador(linea)) {
                    res.add(linea);
                }
            }
        }
        return res;
    }
    
    /**]
     * verifica si tiene permiso de trabajar en el POA
     * 
     * @return 
     */
    public boolean tienePermisoEdicion() {
        if (poa != null) {
            return pOAProyectoDelegate.tienePermisoEdicionPOA(poa);
        }
        return true;
    }


    /**
     * método que retorna los años a seleccionar
     *
     * @return
     */
    public Map<String, String> getPosiblesAniosPOA() {
        Map<String, String> m = new LinkedHashMap<>();
        if (objeto != null) {
            for (AnioFiscal anio : posiblesAniosPOA) {
                m.put(String.valueOf(anio.getAnio()), String.valueOf(anio.getId()));
            }
        }
        return m;
    }


    /**
     * Este método retorna la lista de años para el POA
     * 
     * @return 
     */
    @Override
    public List<Integer> getListAniosPO() {
        List<Integer> l = new LinkedList<>();
        if (idAnioFiscal != null) {
            l.add(Integer.valueOf(idAnioFiscal));
        }
        return l;
    }

    /**
     * Este método guarda la linea en edición.
     * 
     * @param tempLinea 
     */
    @Override
    public void addTmpLinea(POLinea tempLinea) {
        if (!poa.getLineas().contains(tempLinea)) {
            poa.getLineas().add(tempLinea);
        }
    }

    /**
     * Este método elimina una linea
     * 
     * @deprecated 
     * @param tempLinea 
     */
    @Override
    public void eliminarTmpLinea(POLinea tempLinea) {
        poa.getLineas().remove(tempLinea);
    }

    /**
     * Este método retorna la fecha de inicio del POA
     * 
     * @return 
     */
    @Override
    public Date getInicioPO() {
        return objeto.getInicio();
    }

    /**
     * Este método retorna la fecha de fin de un POA
     * 
     * @return 
     */
    @Override
    public Date getFinPO() {
        return objeto.getFin();
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public POAProyecto getPoa() {
        return poa;
    }

    public List<UnidadTecnica> getUtQueCompartieron() {
        return utQueCompartieron;
    }

    public void setUtQueCompartieron(List<UnidadTecnica> utQueCompartieron) {
        this.utQueCompartieron = utQueCompartieron;
    }

    public void setPoa(POAProyecto poa) {
        this.poa = poa;
    }

    // </editor-fold>
}
