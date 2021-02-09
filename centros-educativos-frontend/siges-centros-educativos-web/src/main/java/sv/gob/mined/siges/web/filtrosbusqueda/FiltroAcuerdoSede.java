/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;


public class FiltroAcuerdoSede implements Serializable {
    
    private Long aseSede;
    private Long aseTipoAcuerdo;
    private String aseNumeroAcuerdo;
    private String aseNumeroResolucion;
    private LocalDate aseFechaRegistro;
    private String securityOperation;
    private Boolean seNecesitaDistinct;
    private LocalDate aseFechaRegistroInicio;
    private LocalDate aseFechaRegistroFin;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    public FiltroAcuerdoSede() {
        this.setSecurityOperation(ConstantesOperaciones.BUSCAR_ACUERDO_SEDE);
    }

    public Long getAseSede() {
        return aseSede;
    }

    public void setAseSede(Long aseSede) {
        this.aseSede = aseSede;
    }

    public Long getAseTipoAcuerdo() {
        return aseTipoAcuerdo;
    }

    public void setAseTipoAcuerdo(Long aseTipoAcuerdo) {
        this.aseTipoAcuerdo = aseTipoAcuerdo;
    }

    public String getAseNumeroAcuerdo() {
        return aseNumeroAcuerdo;
    }

    public void setAseNumeroAcuerdo(String aseNumeroAcuerdo) {
        this.aseNumeroAcuerdo = aseNumeroAcuerdo;
    }

    public String getAseNumeroResolucion() {
        return aseNumeroResolucion;
    }

    public void setAseNumeroResolucion(String aseNumeroResolucion) {
        this.aseNumeroResolucion = aseNumeroResolucion;
    }

    

    public LocalDate getAseFechaRegistro() {
        return aseFechaRegistro;
    }

    public void setAseFechaRegistro(LocalDate aseFechaRegistro) {
        this.aseFechaRegistro = aseFechaRegistro;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
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

    public LocalDate getAseFechaRegistroInicio() {
        return aseFechaRegistroInicio;
    }

    public void setAseFechaRegistroInicio(LocalDate aseFechaRegistroInicio) {
        this.aseFechaRegistroInicio = aseFechaRegistroInicio;
    }

    public LocalDate getAseFechaRegistroFin() {
        return aseFechaRegistroFin;
    }

    public void setAseFechaRegistroFin(LocalDate aseFechaRegistroFin) {
        this.aseFechaRegistroFin = aseFechaRegistroFin;
    }

    

}
