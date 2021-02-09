/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.SgFactura;
import sv.gob.mined.siges.web.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactura;
import sv.gob.mined.siges.web.restclient.FacturaRestClient;

@Named
@ApplicationScoped
public class FacturasConverter implements Converter {

    @Inject
    private FacturaRestClient restClient;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            FiltroFactura filtroFactura = new FiltroFactura();
            filtroFactura.setIncluirCampos(new String[]{"facPk",
                "facVersion",
                "facNumero",
                "facSedeFk.sedPk",
                "facSedeFk.sedVersion",
                "facSedeFk.sedTipo",
                "facProveedorFk.proId",
                "facProveedorFk.proVersion",
                "facFechaFactura",
                "facSubTotal",
                "facDeducciones",
                "facTipoItem",
                "facItemPlanCompra.comPk",
                "facItemPlanCompra.comMovimientosFk.movPk",
                "facItemPlanCompra.comMovimientosFk.movAreaInversionPk.adiPk",
                "facItemPlanCompra.comMovimientosFk.movAreaInversionPk.adiNombre",
                "facItemPlanCompra.comMovimientosFk.movAreaInversionPk.adiVersion",
                "facItemPlanCompra.comMovimientosFk.movVersion",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movPk",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesId",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesNombre",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesVersion",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movOrigen",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movVersion",
                "facItemPlanCompra.comVersion",
                "facItemMovimiento.movPk",
                "facItemMovimiento.movAreaInversionPk.adiPk",
                "facItemMovimiento.movAreaInversionPk.adiNombre",
                "facItemMovimiento.movAreaInversionPk.adiVersion",
                "facItemMovimiento.movFuenteIngresos.movPk",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesId",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesNombre",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesVersion",
                "facItemMovimiento.movFuenteIngresos.movOrigen",
                "facItemMovimiento.movFuenteIngresos.movVersion",
                "facItemMovimiento.movVersion",
                "facEstado",
                "facTotal",
                "facTipoDocumento"
            });
            filtroFactura.setFacEstado(EnumFacturaEstado.EN_PROCESO);
            filtroFactura.setFacPk(Long.valueOf(value));
            List<SgFactura> facturas = restClient.buscar(filtroFactura);
            if (!facturas.isEmpty()) {
                return facturas.get(0);
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new ConverterException("Factura inválida");

        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof SgFactura) {
            return String.valueOf(((SgFactura) o).getFacPk());
        } else if (o instanceof String) {
            if (StringUtils.isBlank((String) o)) {
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Factura"));
    }
}
