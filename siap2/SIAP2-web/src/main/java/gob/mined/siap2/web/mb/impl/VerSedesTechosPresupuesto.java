/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.TopeDetalleMatriculas;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.TipoSubcomponente;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.web.utils.dataProvider.LazyLoadingTopePresupuestal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "verSedesTechosPresupuesto")
public class VerSedesTechosPresupuesto implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;

    private LazyDataModel lazyModel;
    private Integer opcion = 0;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private RelacionGesPresEsAnioFiscal rel;

    @PostConstruct
    public void init() {
        String id = buscarIdentificador("id");
        String op = buscarIdentificador("op");
        opcion = Integer.parseInt(op);
        rel = (RelacionGesPresEsAnioFiscal) emd.getEntityById(RelacionGesPresEsAnioFiscal.class.getCanonicalName(), Integer.parseInt(id));
        filterTable();
    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            
            if(opcion != null) {
                if (opcion.compareTo(1) ==0) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.estado", EstadoTopePresupuestal.EN_PROCESO);
                    criterios.add(criterio);
                } else if (opcion.compareTo(2) == 0) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.estado", EstadoTopePresupuestal.APROBACION);
                    criterios.add(criterio);
                }
            }
            
            if(rel != null && rel.getId() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "topePresupuestal.relGesPres.id", rel.getId());
                criterios.add(criterio);
            }
                
           // MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATER, "topePresupuestal.cantidadMatricula", 0);
           // criterios.add(criterio);
                
            CriteriaTO condicion;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

           /** String[] propiedades = {"topePresupuestal.sede.codigo","topePresupuestal.sede.nombre","topePresupuestal.sede.direccion.departamento.nombre","cantidadMatriculas",
                "topePresupuestal.sede.organismosAdministracionEscolar.oaeTipoOrganismoAdministrativo.toaNombre",
                "nivel.nombre","modalidadEducativa.nombre","modalidadAtencion.nombre"
            };**/
           
            String[] propiedades = {"topePresupuestal.anioFiscal.anio","topePresupuestal.gesPresEs.categoriaPresupuestoEscolar.nombre","topePresupuestal.gesPresEs.nombre",
                "topePresupuestal.subCuenta.codigo","topePresupuestal.sede.codigo","topePresupuestal.montoAprobado","topePresupuestal.monto",
                "topePresupuestal.cantidadMatricula","topePresupuestal.cantidadMatriculaAprobada",
                "topePresupuestal.sede.nombre","topePresupuestal.sede.direccion.departamento.nombre","cantidadMatriculas",
                "topePresupuestal.sede.organismosAdministracionEscolar.oaeTipoOrganismoAdministrativo.toaNombre",
                "nivel.nombre","modalidadEducativa.nombre","modalidadAtencion.nombre"
            };
            
            String className = TopeDetalleMatriculas.class.getName();
            String[] orderBy = {"topePresupuestal.sede.codigo","nivel.nombre","modalidadEducativa.nombre","modalidadAtencion.nombre"};
            boolean[] asc = {true};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModel = new LazyLoadingTopePresupuestal(dataProvider);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
     /**
     * Este método busca la llave de los parametros enviados desde la vista y
     * obtiene su valor
     *
     * @param parametro
     * @return
     */
    public String buscarIdentificador(String parametro) {
        try {
            String valor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parametro);
            if (valor == null) {
                valor = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get(parametro);
            }
            return valor;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    public Boolean getMostrarMonto(BigDecimal monto) {
        if(monto == null || (monto != null && (monto.compareTo(new BigDecimal(BigInteger.ZERO)) == 0))) {
            return false;
        }
        return true;
    }
    
    public BigDecimal obtenerMonto(BigDecimal monto, Integer matriculasTotales, Integer matriculas) {
        BigDecimal montoRetorno = BigDecimal.ZERO;
        if(monto != null && matriculasTotales != null && matriculas != null && matriculasTotales.compareTo(0) > 0) {
            montoRetorno = monto.divide(new BigDecimal(matriculasTotales), 2, RoundingMode.HALF_UP);
            montoRetorno = montoRetorno.multiply(new BigDecimal(matriculas));
        }
        return montoRetorno.setScale(2, RoundingMode.HALF_UP);
    }
    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }


    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }
    public RelacionGesPresEsAnioFiscal getRel() {
        return rel;
    }

    public void setRel(RelacionGesPresEsAnioFiscal rel) {
        this.rel = rel;
    }
    
     public Integer getOpcion() {
        return opcion;
    }

    public void setOpcion(Integer opcion) {
        this.opcion = opcion;
    }
    // </editor-fold>

   

    

}
