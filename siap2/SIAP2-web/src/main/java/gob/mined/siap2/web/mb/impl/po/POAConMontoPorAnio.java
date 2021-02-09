/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.business.utils.POAUtils;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.ConMontoPorAnio;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.web.delegates.impl.POAConActividadesDelegate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

/**
 * Este backing bean implementa la lógica para trabajar con los POA que tienen 
 * actividades (acción central y asignación no programable) 
 * 
 * @author Sofis Solutions
 */
public abstract class POAConMontoPorAnio extends POConActividadesEInsumosAbstract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    protected POAConActividadesDelegate podaAConActividadesDelegate;

    protected ConMontoPorAnio objeto;
    protected POAConActividades poa;
    protected String idAnioFiscal;

    /**
     * Retorna el POA en edición
     * 
     * @return 
     */
    @Override
    public GeneralPOA getPOAEnTrabajo() {
        return poa;
    }

    /**
     * Retorna si el usuario tiene permiso de edición sobre el poa
     * @return 
     */
    public boolean tienePermisoEdicion() {
        if (poa != null) {
            return podaAConActividadesDelegate.tienePermisoEdicionPOAAccionCentral(poa);
        }
        return true;
    }

    /**
     * Retorna la fecha de inicio del POA
     * 
     * @return 
     */
    @Override
    public Date getInicioPO() {
        Date start = null;
        if (poa != null && poa.getAnioFiscal() != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, poa.getAnioFiscal().getAnio());
            cal.set(Calendar.DAY_OF_YEAR, 1);
            start = cal.getTime();
        }
        return start;
    }

    /**
     * Retorna la fecha de fin del POA
     * 
     * @return 
     */
    @Override
    public Date getFinPO() {
        Date end = null;
        if (poa != null && poa.getAnioFiscal() != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, poa.getAnioFiscal().getAnio());
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            end = cal.getTime();
        }
        return end;

    }

    /**
     * Retorna los años para los que esta disponible el POA
     * 
     * @return 
     */
    @Override
    protected List<Integer> getListAniosPO() {
        List<Integer> l = new LinkedList<>();
        if (poa.getAnioFiscal() != null) {
            l.add(poa.getAnioFiscal().getAnio());
        }
        return l;
    }

    /**
     * Se guarda una actividad en el POA
     * 
     * @param actividad 
     */
    @Override
    public void guardarActividad(POActividadBase actividad) {
        if (!poa.getActividades().contains(actividad)) {
            poa.getActividades().add(actividad);
        }
    }

    /**
     * Elimina una actividad del POA
     * 
     * @param actividad 
     */
    @Override
    public void eliminarActividad(POActividadBase actividad) {
        poa.getActividades().remove(actividad);
    }

    /**
     * Retorna los usuarios disponibles a los que se le pude asignar una actividad
     * 
     * @return 
     */
    @Override
    public Map<String, String> getUsuariosForActividad() {
        Map<String, String> map = new LinkedHashMap<>();
        if (poa != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ssUsuOfiRolesCollection.usuOfiRolesUnidadTecnica.id", poa.getUnidadTecnica().getId());

            String[] propiedades = {"usuId", "usuCod"};
            String[] orderBy = {"usuCod"};
            boolean[] asc = {true};
            List<EntityReference<Integer>> l = emd.getEntitiesReferenceByCriteria(SsUsuario.class.getName(), criterio, null, null, propiedades, orderBy, asc);
            for (EntityReference u : l) {
                map.put((String) u.getPropertyMap().get("usuCod"), String.valueOf(u.getPropertyMap().get("usuId")));
            }
        }
        return map;
    }

    /**
     * Guarda un insumo en el POA
     * 
     * @param insumo 
     */
    @Override
    public void guardarInsumo(POInsumos insumo) {

        if (insumo.getActividad() == null) {
            insumo.setActividad(getActividadEnUso());
            getActividadEnUso().getInsumos().add(insumo);
        }
    }

    /**
     * Este método elimina un insumo del POA
     * 
     * @param insumo 
     */
    @Override
    public void eliminarInsumo(POInsumos insumo) {
        insumo.getActividad().getInsumos().remove(insumo);
    }

    /**
     * este método valida el monto del insumo en el POA
     * 
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    @Override
    public void validateMontoInsumoTotal(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        BigDecimal montoIngresado = new BigDecimal(value.toString());
        BigDecimal montoUsado = BigDecimal.ZERO;
        for (POActividadBase a : poa.getActividades()) {
            for (POInsumos i : a.getInsumos()) {
                if (tempInsumo != i) {
                    montoUsado = montoUsado.add(i.getMontoTotal());
                }
            }
        }
        montoUsado = montoUsado.add(montoIngresado);

        //busco el monto de la ut
        BigDecimal montoUT = getMontoTotal();

        if (montoUT == null) {
            FacesMessage msg = new FacesMessage("Error al validar montos.", "Error: la unidad técnica no tiene monto asignado para el año fiscal");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        if (montoUsado.compareTo(montoUT) > 0) {
            FacesMessage msg = new FacesMessage("Error al validar montos.", "Error: la suma de los montos (" + montoUsado + ") es mayor que el monto asignado a la unidad técnica (" + montoUT + ")" );
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    /**
     * Este método retorna el monto total del POA disponible de la UT para el año del POA
     * 
     * @return 
     */
    public BigDecimal getMontoTotal() {
        return POAUtils.getMonto(objeto, poa.getUnidadTecnica(), poa.getAnioFiscal());       
 
    }

    /**
     * Este método retorna el monto total usado en el POA
     * 
     * @return 
     */
    public BigDecimal getMontoUsado() {
        BigDecimal montoUsado = BigDecimal.ZERO;
        for (POActividadBase a : poa.getActividades()) {
            for (POInsumos i : a.getInsumos()) {
                montoUsado = montoUsado.add(i.getMontoTotal());
            }
        }
        return montoUsado;
    }

    /**
     * Este método retorna el saldo disponible en el POA
     * 
     * @return 
     */
    public BigDecimal getSaldo() {
        BigDecimal total = getMontoTotal();
        if (total == null) {
            return null;
        }
        return total.subtract(getMontoUsado());
    }

    /**
     * Este método retorna el monto total del POA para la UT en el año
     * 
     * @param poa
     * @return 
     */
    public BigDecimal getMontoTotalPOA(POAConActividades poa) {
        if (poa == null || objeto == null) {
            return null;
        }
        return POAUtils.getMonto(objeto, poa.getUnidadTecnica(), poa.getAnioFiscal());
    }

    /**
     * Este método retorna el monto total del POA en edición
     * 
     * @return 
     */
    public BigDecimal getMontoTotalPOA() {
        return getMontoTotalPOA(poa);
    }

    /**
     * Este método retorna el monto usado en el POA 
     * 
     * @param poa
     * @return 
     */
    public BigDecimal getMontoUsadoPOA(POAConActividades poa) {
        if (poa == null) {
            return null;
        }
        BigDecimal montoUsado = BigDecimal.ZERO;
        for (POActividadBase a : poa.getActividades()) {
            for (POInsumos i : a.getInsumos()) {
                montoUsado = montoUsado.add(i.getMontoTotal());
            }
        }
        return montoUsado;
    }

    /**
     * Este método retorna el monto usado en el POA en edición
     * 
     * @return 
     */
    public BigDecimal getMontoUsadoPOA() {
        return getMontoUsadoPOA(poa);
    }

    /**
     * Este método retorna el saldo disponible en el POA
     * 
     * @param poa
     * @return 
     */
    public BigDecimal getSaldoPOA(POAConActividades poa) {
        if (poa == null) {
            return null;
        }
        BigDecimal total = getMontoTotalPOA(poa);
        if (total == null) {
            return null;
        }
        return total.subtract(getMontoUsadoPOA(poa));
    }

    /**
     * Este método retorna el saldo disponible en el POA en edición
     * 
     * @return 
     */
    public BigDecimal getSaldoPOA() {
        return getSaldoPOA(poa);
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public ConMontoPorAnio getObjeto() {
        return objeto;
    }

    public void setObjeto(ConMontoPorAnio objeto) {
        this.objeto = objeto;
    }

    public POAConActividades getPoa() {
        return poa;
    }

    public void setPoa(POAConActividades poa) {
        this.poa = poa;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

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

    public POInsumos getTempInsumo() {
        return tempInsumo;
    }

    public void setTempInsumo(POInsumos tempInsumo) {
        this.tempInsumo = tempInsumo;
    }

    // </editor-fold>
}
