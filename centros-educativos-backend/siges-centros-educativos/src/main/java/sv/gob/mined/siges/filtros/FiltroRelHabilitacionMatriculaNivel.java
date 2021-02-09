/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoMatricula;

/**
 *
 * @author usuario
 */
public class FiltroRelHabilitacionMatriculaNivel implements Serializable {
    
    private Long rhnNivelFk;
    private Long rhnModalidadAtencionFk;
    private Long rhnSubmodalidadFk;
    private LocalDate rhnFecha;
    private Boolean nivelAndNullOrNivelAndModAtSubMod; //Cuando esta en true busca nivel y modalidad null submodalidad null OR todo distinto de null
    private Long rhnSedeFk;
    private EnumEstadoHabilitacionPeriodoMatricula rhnEstadoHabilitacion;
    private List<Long> rhnHabilitacionPeriodosPkList;
    
    
    private EnumAmbito ambito;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    
    public FiltroRelHabilitacionMatriculaNivel() {
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

    public Long getRhnNivelFk() {
        return rhnNivelFk;
    }

    public void setRhnNivelFk(Long rhnNivelFk) {
        this.rhnNivelFk = rhnNivelFk;
    }

    public Long getRhnModalidadAtencionFk() {
        return rhnModalidadAtencionFk;
    }

    public void setRhnModalidadAtencionFk(Long rhnModalidadAtencionFk) {
        this.rhnModalidadAtencionFk = rhnModalidadAtencionFk;
    }

    public Long getRhnSubmodalidadFk() {
        return rhnSubmodalidadFk;
    }

    public void setRhnSubmodalidadFk(Long rhnSubmodalidadFk) {
        this.rhnSubmodalidadFk = rhnSubmodalidadFk;
    }

 
    
    public EnumAmbito getAmbito() {
        return ambito;
    }

    public void setAmbito(EnumAmbito ambito) {
        this.ambito = ambito;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public LocalDate getRhnFecha() {
        return rhnFecha;
    }

    public void setRhnFecha(LocalDate rhnFecha) {
        this.rhnFecha = rhnFecha;
    }

    public Boolean getNivelAndNullOrNivelAndModAtSubMod() {
        return nivelAndNullOrNivelAndModAtSubMod;
    }

    public void setNivelAndNullOrNivelAndModAtSubMod(Boolean nivelAndNullOrNivelAndModAtSubMod) {
        this.nivelAndNullOrNivelAndModAtSubMod = nivelAndNullOrNivelAndModAtSubMod;
    }

    public Long getRhnSedeFk() {
        return rhnSedeFk;
    }

    public void setRhnSedeFk(Long rhnSedeFk) {
        this.rhnSedeFk = rhnSedeFk;
    }

    public EnumEstadoHabilitacionPeriodoMatricula getRhnEstadoHabilitacion() {
        return rhnEstadoHabilitacion;
    }

    public void setRhnEstadoHabilitacion(EnumEstadoHabilitacionPeriodoMatricula rhnEstadoHabilitacion) {
        this.rhnEstadoHabilitacion = rhnEstadoHabilitacion;
    }

    public List<Long> getRhnHabilitacionPeriodosPkList() {
        return rhnHabilitacionPeriodosPkList;
    }

    public void setRhnHabilitacionPeriodosPkList(List<Long> rhnHabilitacionPeriodosPkList) {
        this.rhnHabilitacionPeriodosPkList = rhnHabilitacionPeriodosPkList;
    }

  
    
}
