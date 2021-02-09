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
import gob.mined.siap2.entities.data.impl.POARiesgo;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.enums.OrigenRiesgo;
import gob.mined.siap2.entities.enums.ValoracionRiesgo;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.POAProyectoDelegate;
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
 * que se utiliza para la visualización de los POA de proyecto.
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaProyectoVer")
public class POAProyectoVer extends POProyectoConLineasAbstract implements Serializable {

    @Inject
    ProyectoDelegate proyectoDelegate;
    @Inject
    POAProyectoDelegate pOAProyectoDelegate;

    protected String idProyecto;
    protected String idAnioFiscal;
    protected POAProyecto poa;
    private String idPOA;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        idProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        idAnioFiscal = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnioFiscal");
        idUnidadTecnica = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUnidadTecnica");

        if (TextUtils.isEmpty(idUnidadTecnica) && usuarioUnidadTecnicas.size() == 1) {
            this.idUnidadTecnica = usuarioUnidadTecnicas.get(0).getId().toString();
        }
        if (!TextUtils.isEmpty(idProyecto)) {
            update = true;
            objeto = proyectoDelegate.getProyecto(Integer.valueOf(idProyecto));
        }

    }

    /**
     * carga las unidades técnicas disponibles para el usuario
     */
    public void realoadUTDisponiblesParaUsuario() {
        if (!TextUtils.isEmpty(idAnioFiscal) && objeto != null) {
            usuarioUnidadTecnicas = pOAProyectoDelegate.getUTDisponiblesParaUsuario(objeto.getId(), Integer.valueOf(idAnioFiscal));
        } else {
            usuarioUnidadTecnicas = Collections.EMPTY_LIST;
        }
    }
    
    
    /**
     * método que se ejecuta cuando el usuario cambia el año seleccionado
     */
    public void cambiaAnioSelecionado() {
        realoadUTDisponiblesParaUsuario();
        if (TextUtils.isEmpty(idUnidadTecnica) && usuarioUnidadTecnicas.size() == 1) {
            this.idUnidadTecnica = usuarioUnidadTecnicas.get(0).getId().toString();
        }
        initProyecto();
    }
    
    
    /**
     * Reinicializa el proyecto cuanto entra en el init, cuando el usuario
     * cambia la UT, o cuando se cambia el año fiscal
     */
    public void initProyecto() {
        try {
            poa = null;
            if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica)&& !TextUtils.isEmpty(idPOA)) {
                poa = pOAProyectoDelegate.getPOAEnLineaBase(Integer.valueOf(idPOA));
            }
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
     * Este método retorna las lineas en edición 
     * @return 
     */
    @Override
    public List<POLinea> getLineas() {
        if (poa != null) {
            return poa.getLineas();
        }
        return new LinkedList();
    }


    /**
     * Este método añade una linea nueva a las lineas en edición
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
     * @param tempLinea 
     */
    @Override
    public void eliminarTmpLinea(POLinea tempLinea) {
        poa.getLineas().remove(tempLinea);
    }

    /**
     * Este método retorna la fecha de inicio del POA
     * @return 
     */
    @Override
    public Date getInicioPO() {
        return objeto.getInicio();
    }
    
    
    /**
     * Este método retorna la fecha d efin del POA
     * @return 
     */
    @Override
    public Date getFinPO() {
        return objeto.getFin();
    }

    /**
     * Este método retorna las lineas base creadas para el POA actual
     * @return 
     */
    public Map<String, Integer> getLineasBase() {
        if (!TextUtils.isEmpty(idAnioFiscal) && !TextUtils.isEmpty(idUnidadTecnica)) {
            return pOAProyectoDelegate.getPOASEnLineaBase(objeto.getId(), Integer.valueOf(idAnioFiscal), Integer.valueOf(idUnidadTecnica));
        }
        return null;
    }

    
    /**
     * Este metodo retorna los posibles años de los POA
     * @return 
     */
    public Map<String, String> getPosiblesAniosPOA() {
        Map<String, String> m = new LinkedHashMap<>();
        if (objeto != null) {
            for (AnioFiscal anio : pOAProyectoDelegate.getTodosAniosPOA(objeto.getId())){
                  m.put(String.valueOf(anio.getAnio()), String.valueOf(anio.getId()));
            }
        }
        return m;
    }

    
    // <editor-fold defaultstate="collapsed" desc="MANEJO DE RIESGOS">
    protected POARiesgo tmpRiesgo;

    /**
     * Este método inicializa un riesgo nuevo
     */
    public void initRiesgo() {
        if (tmpRiesgo == null) {
            tmpRiesgo = new POARiesgo();
        }
    }

    // </editor-fold>
    
    public OrigenRiesgo[] getOrigenRiesgos() {
        return OrigenRiesgo.values();
    }

    public ValoracionRiesgo[] getValoracionRiesgos() {
        return ValoracionRiesgo.values();
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

    public String getIdPOA() {
        return idPOA;
    }

    public void setIdPOA(String idPOA) {
        this.idPOA = idPOA;
    }

    public POARiesgo getTmpRiesgo() {
        return tmpRiesgo;
    }

    public void setTmpRiesgo(POARiesgo tmpRiesgo) {
        this.tmpRiesgo = tmpRiesgo;
    }

    public void setPoa(POAProyecto poa) {
        this.poa = poa;
    }

    // </editor-fold>
}
