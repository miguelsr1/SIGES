/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.enumerados.EnumEstadoAcuerdoSistema;


public class FiltroAcuerdoSistema implements Serializable {
    
    private Long asiSistemaIntegrado;
    private Long asiTipoAcuerdo;
    private EnumEstadoAcuerdoSistema asiEstado;
    private Long asiNumeroAcuerdo;
    private LocalDate asiFechaCreacion;
    private LocalDate asiFechaCreacionInicio;
    private LocalDate asiFechaCreacionFin;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String securityOperation;
    private Boolean seNecesitaDistinct;
    private String[] incluirCampos;
    
    public FiltroAcuerdoSistema() {
        
    }

    public Long getAsiSistemaIntegrado() {
        return asiSistemaIntegrado;
    }

    public void setAsiSistemaIntegrado(Long asiSistemaIntegrado) {
        this.asiSistemaIntegrado = asiSistemaIntegrado;
    }

    public Long getAsiTipoAcuerdo() {
        return asiTipoAcuerdo;
    }

    public void setAsiTipoAcuerdo(Long asiTipoAcuerdo) {
        this.asiTipoAcuerdo = asiTipoAcuerdo;
    }

    public EnumEstadoAcuerdoSistema getAsiEstado() {
        return asiEstado;
    }

    public void setAsiEstado(EnumEstadoAcuerdoSistema asiEstado) {
        this.asiEstado = asiEstado;
    }

    public Long getAsiNumeroAcuerdo() {
        return asiNumeroAcuerdo;
    }

    public void setAsiNumeroAcuerdo(Long asiNumeroAcuerdo) {
        this.asiNumeroAcuerdo = asiNumeroAcuerdo;
    }

    public LocalDate getAsiFechaCreacion() {
        return asiFechaCreacion;
    }

    public void setAsiFechaCreacion(LocalDate asiFechaCreacion) {
        this.asiFechaCreacion = asiFechaCreacion;
    }

    public LocalDate getAsiFechaCreacionInicio() {
        return asiFechaCreacionInicio;
    }

    public void setAsiFechaCreacionInicio(LocalDate asiFechaCreacionInicio) {
        this.asiFechaCreacionInicio = asiFechaCreacionInicio;
    }

    public LocalDate getAsiFechaCreacionFin() {
        return asiFechaCreacionFin;
    }

    public void setAsiFechaCreacionFin(LocalDate asiFechaCreacionFin) {
        this.asiFechaCreacionFin = asiFechaCreacionFin;
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
    

}
