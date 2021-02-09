/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros.catalogo;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumTipoFormula;


public class FiltroFormula implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;
    private EnumTipoFormula tipoFormula;
    private List<EnumTipoFormula> listTipoFormula;
    private Long descartarPk;
    private Boolean tieneSubFormula;
    private Boolean incluirSubformulas;
    private Long formulaPk;
    private Long descartarSubFormulasPk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroFormula() {
    }
     

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoExacto() {
        return codigoExacto;
    }

    public void setCodigoExacto(String codigoExacto) {
        this.codigoExacto = codigoExacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public EnumTipoFormula getTipoFormula() {
        return tipoFormula;
    }

    public void setTipoFormula(EnumTipoFormula tipoFormula) {
        this.tipoFormula = tipoFormula;
    }

    public List<EnumTipoFormula> getListTipoFormula() {
        return listTipoFormula;
    }

    public void setListTipoFormula(List<EnumTipoFormula> listTipoFormula) {
        this.listTipoFormula = listTipoFormula;
    }

    public Boolean getIncluirSubformulas() {
        return incluirSubformulas;
    }

    public void setIncluirSubformulas(Boolean incluirSubformulas) {
        this.incluirSubformulas = incluirSubformulas;
    }

    public Long getFormulaPk() {
        return formulaPk;
    }

    public void setFormulaPk(Long formulaPk) {
        this.formulaPk = formulaPk;
    }

    public Long getDescartarPk() {
        return descartarPk;
    }

    public void setDescartarPk(Long descartarPk) {
        this.descartarPk = descartarPk;
    }

    public Boolean getTieneSubFormula() {
        return tieneSubFormula;
    }

    public void setTieneSubFormula(Boolean tieneSubFormula) {
        this.tieneSubFormula = tieneSubFormula;
    }

    public Long getDescartarSubFormulasPk() {
        return descartarSubFormulasPk;
    }

    public void setDescartarSubFormulasPk(Long descartarSubFormulasPk) {
        this.descartarSubFormulasPk = descartarSubFormulasPk;
    }
      
}
