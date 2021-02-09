/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

/**
 *
 * @author bruno
 */
public class CodigosDTO {
    private String primerCod;
    private String ultimoCod;
    private Integer ultimoCorrelativo;

    public String getPrimerCod() {
        return primerCod;
    }

    public void setPrimerCod(String primerCod) {
        this.primerCod = primerCod;
    }

    public String getUltimoCod() {
        return ultimoCod;
    }

    public void setUltimoCod(String ultimoCod) {
        this.ultimoCod = ultimoCod;
    }

    public Integer getUltimoCorrelativo() {
        return ultimoCorrelativo;
    }

    public void setUltimoCorrelativo(Integer ultimoCorrelativo) {
        this.ultimoCorrelativo = ultimoCorrelativo;
    }
    
    
    
}
