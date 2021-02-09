/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.filtrosbusqueda;
import java.io.Serializable;
import sv.gob.mined.siges.web.dto.SgOperacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoComponentePentaho;
/**
 *
 * @author Sofis Solutions
 */
public class FiltroMenuPentaho implements Serializable{
    private Long menuPk;
    private String nombre;
    private Long operacionId;
    private SgOperacion operacionFk;
    private Boolean habilitado;
    private Long first;
    private Long maxResults;
    private String[] orderBy;

    public Long getMenuPk() {
        return menuPk;
    }

    public void setMenuPk(Long menuPk) {
        this.menuPk = menuPk;
    }
    private boolean[] ascending;
    private String[] incluirCampos;
    private EnumTipoComponentePentaho tipo;

    
    
    public FiltroMenuPentaho() {
    }
    
     public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getOperacionId() {
        return operacionId;
    }

    public void setOperacionId(Long operacionId) {
        this.operacionId = operacionId;
    }
    
    public SgOperacion getOperacionFk() {
        return operacionFk;
    }

    public void setOperacionFk(SgOperacion operacionFk) {
        this.operacionFk = operacionFk;
    }    
    
    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }
    
    public EnumTipoComponentePentaho getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoComponentePentaho tipo) {
        this.tipo = tipo;
    }
    
}
