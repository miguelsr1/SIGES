/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;

public class FiltroComponentePlanGrado implements Serializable {

    private Long cpgPlanEstudioPk;
    private Long cpgGradoPk;
    private Long cpgComponentePlanEstudioPk;
    private Long cpgOpcionPk;
    private Long cpgProgramaEducativoPk;
    private Long cpgSeccionPk;
    private Boolean cpgObjetoPromocion;
    private Long cpgSeccionExclusiva;
    private Long cpgAgregandoSeccionExclusiva;
    private Long cpgPk;
    private List<Integer> codigosExternos;
    private Boolean cpgCalificacionIngresadaCE;
    private TipoComponentePlanEstudio cpgTipoComponentePlanEstudio;
    private Boolean cpgEsTipoPaes;
    private Long cpgObjetoPromocionOPrueba;
    private String cpeNombre;
    private List<String> cpeNombreBusquedaList;
    private List<Long> cpgPkList;

    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroComponentePlanGrado() {
    }

    public Long getCpgPlanEstudioPk() {
        return cpgPlanEstudioPk;
    }

    public void setCpgPlanEstudioPk(Long cpgPlanEstudioPk) {
        this.cpgPlanEstudioPk = cpgPlanEstudioPk;
    }

    public Long getCpgGradoPk() {
        return cpgGradoPk;
    }

    public void setCpgGradoPk(Long cpgGradoPk) {
        this.cpgGradoPk = cpgGradoPk;
    }

    public Long getCpgComponentePlanEstudioPk() {
        return cpgComponentePlanEstudioPk;
    }

    public void setCpgComponentePlanEstudioPk(Long cpgComponentePlanEstudioPk) {
        this.cpgComponentePlanEstudioPk = cpgComponentePlanEstudioPk;
    }

    public Long getCpgOpcionPk() {
        return cpgOpcionPk;
    }

    public void setCpgOpcionPk(Long cpgOpcionPk) {
        this.cpgOpcionPk = cpgOpcionPk;
    }

    public Long getCpgProgramaEducativoPk() {
        return cpgProgramaEducativoPk;
    }

    public void setCpgProgramaEducativoPk(Long cpgProgramaEducativoPk) {
        this.cpgProgramaEducativoPk = cpgProgramaEducativoPk;
    }

    public Long getCpgSeccionPk() {
        return cpgSeccionPk;
    }

    public void setCpgSeccionPk(Long cpgSeccionPk) {
        this.cpgSeccionPk = cpgSeccionPk;
    }

    public Boolean getCpgObjetoPromocion() {
        return cpgObjetoPromocion;
    }

    public void setCpgObjetoPromocion(Boolean cpgObjetoPromocion) {
        this.cpgObjetoPromocion = cpgObjetoPromocion;
    }

    public Boolean getCpgCalificacionIngresadaCE() {
        return cpgCalificacionIngresadaCE;
    }

    public void setCpgCalificacionIngresadaCE(Boolean cpgCalificacionIngresadaCE) {
        this.cpgCalificacionIngresadaCE = cpgCalificacionIngresadaCE;
    }
        
    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
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

    public Long getCpgSeccionExclusiva() {
        return cpgSeccionExclusiva;
    }

    public void setCpgSeccionExclusiva(Long cpgSeccionExclusiva) {
        this.cpgSeccionExclusiva = cpgSeccionExclusiva;
    }

    public Long getCpgAgregandoSeccionExclusiva() {
        return cpgAgregandoSeccionExclusiva;
    }

    public void setCpgAgregandoSeccionExclusiva(Long cpgAgregandoSeccionExclusiva) {
        this.cpgAgregandoSeccionExclusiva = cpgAgregandoSeccionExclusiva;
    }

    public Long getCpgPk() {
        return cpgPk;
    }

    public void setCpgPk(Long cpgPk) {
        this.cpgPk = cpgPk;
    }

    public List<Integer> getCodigosExternos() {
        return codigosExternos;
    }

    public void setCodigosExternos(List<Integer> codigosExternos) {
        this.codigosExternos = codigosExternos;
    }

    public TipoComponentePlanEstudio getCpgTipoComponentePlanEstudio() {
        return cpgTipoComponentePlanEstudio;
    }

    public void setCpgTipoComponentePlanEstudio(TipoComponentePlanEstudio cpgTipoComponentePlanEstudio) {
        this.cpgTipoComponentePlanEstudio = cpgTipoComponentePlanEstudio;
    }

    public Boolean getCpgEsTipoPaes() {
        return cpgEsTipoPaes;
    }

    public void setCpgEsTipoPaes(Boolean cpgEsTipoPaes) {
        this.cpgEsTipoPaes = cpgEsTipoPaes;
    }

    public Long getCpgObjetoPromocionOPrueba() {
        return cpgObjetoPromocionOPrueba;
    }

    public void setCpgObjetoPromocionOPrueba(Long cpgObjetoPromocionOPrueba) {
        this.cpgObjetoPromocionOPrueba = cpgObjetoPromocionOPrueba;
    }

    public String getCpeNombre() {
        return cpeNombre;
    }

    public void setCpeNombre(String cpeNombre) {
        this.cpeNombre = cpeNombre;
    }

    public List<String> getCpeNombreBusquedaList() {
        return cpeNombreBusquedaList;
    }

    public void setCpeNombreBusquedaList(List<String> cpeNombreBusquedaList) {
        this.cpeNombreBusquedaList = cpeNombreBusquedaList;
    }

    public List<Long> getCpgPkList() {
        return cpgPkList;
    }

    public void setCpgPkList(List<Long> cpgPkList) {
        this.cpgPkList = cpgPkList;
    }



}
