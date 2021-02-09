/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumTipoComponentePentaho;
import sv.gob.mined.siges.persistencia.entidades.SgOperacion;

public class FiltroMenuPentaho implements Serializable {
    
    private Long menuPk;
    private String nombre;
    private EnumTipoComponentePentaho tipo;
    private List<EnumTipoComponentePentaho> listTipo;
    private SgOperacion operacionFk;
    private Long operacionId;

    public Long getOperacionId() {
        return operacionId;
    }

    public void setOperacionId(Long operacionId) {
        this.operacionId = operacionId;
    }
    private Boolean habilitado;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroMenuPentaho() {
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    
    public Long getMenuPk() {
        return menuPk;
    }

    public void setMenuPk(Long menuPk) {
        this.menuPk = menuPk;
    }
    
     public EnumTipoComponentePentaho getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoComponentePentaho tipo) {
        this.tipo = tipo;
    }

    public List<EnumTipoComponentePentaho> getListTipo() {
        return listTipo;
    }

    public void setListTipo(List<EnumTipoComponentePentaho> listTipo) {
        this.listTipo = listTipo;
    }


    
}
