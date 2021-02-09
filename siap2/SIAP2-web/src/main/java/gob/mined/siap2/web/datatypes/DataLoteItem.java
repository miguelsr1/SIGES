/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DataLoteItem {

    public final static String ROOT = "ROOT";
    public final static String ITEM = "ITEM";
    public final static String LOTE = "LOTE";
    public final static String INSUMO = "INSUMO";

    private String tipo;
    private Object objeto;

    private String nombre;
    private boolean usado;
    private boolean persistido;
    private boolean editar;
    private boolean estaEnPausa;
    private Integer cantidadItemInsumo;
    private String uTecnica;
    private String insumo;
    private Integer codigoInsumo;
    private BigDecimal precioUnitEstimado;
    private BigDecimal montoTotalEstimado;
    private Integer nroItem;
    private BigDecimal montoTotalCertificado;
    private RelacionProAdqItemInsumo relacionInsumoItem;    

    public DataLoteItem(String tipo, Object objeto, boolean persistido) {
        this.tipo = tipo;
        this.objeto = objeto;
        this.persistido = persistido;
        this.editar = false;
        this.estaEnPausa = false;
    }

    public DataLoteItem(String tipo, Object objeto) {
        this.tipo = tipo;
        this.objeto = objeto;
        this.editar = false;
        this.estaEnPausa = false;
    }

    public DataLoteItem(String type, String nombre, boolean usado, Object objeto) {
        this.nombre = nombre;
        this.usado = usado;
        this.tipo = type;
        this.objeto = objeto;
        this.editar = false;
        this.estaEnPausa = false;
    }

    public DataLoteItem() {
        this.editar = false;
        this.estaEnPausa = false;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public boolean getPersistido() {
        return persistido;
    }

    public void setPersistido(boolean persistido) {
        this.persistido = persistido;
    }

    public boolean getEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public Integer getCantidadItemInsumo() {
        return cantidadItemInsumo;
    }

    public void setCantidadItemInsumo(Integer cantidadItemInsumo) {
        this.cantidadItemInsumo = cantidadItemInsumo;
    }

    public boolean getEstaEnPausa() {
        return estaEnPausa;
    }

    public void setEstaEnPausa(boolean estaEnPausa) {
        this.estaEnPausa = estaEnPausa;
    }

    public String getuTecnica() {
        return uTecnica;
    }

    public void setuTecnica(String uTecnica) {
        this.uTecnica = uTecnica;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getPrecioUnitEstimado() {
        return precioUnitEstimado;
    }

    public void setPrecioUnitEstimado(BigDecimal precioUnitEstimado) {
        this.precioUnitEstimado = precioUnitEstimado;
    }

    public BigDecimal getMontoTotalEstimado() {
        return montoTotalEstimado;
    }

    public void setMontoTotalEstimado(BigDecimal montoTotalEstimado) {
        this.montoTotalEstimado = montoTotalEstimado;
    }

    public Integer getNroItem() {
        return nroItem;
    }

    public void setNroItem(Integer nroItem) {
        this.nroItem = nroItem;
    }

    public Integer getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(Integer codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }
    
    public BigDecimal getMontoTotalCertificado() {
        return montoTotalCertificado;
    }

    public void setMontoTotalCertificado(BigDecimal montoTotalCertificado) {
        this.montoTotalCertificado = montoTotalCertificado;
    }

    public RelacionProAdqItemInsumo getRelacionInsumoItem() {
        return relacionInsumoItem;
    }

    public void setRelacionInsumoItem(RelacionProAdqItemInsumo relacionInsumoItem) {
        this.relacionInsumoItem = relacionInsumoItem;
    }
}
