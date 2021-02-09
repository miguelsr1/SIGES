/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agesic.firma.datatypes;

import java.util.List;

/**
 *
 * @author sofis
 */
public class ResultadoValidacion {
    
    String code;
    String message;
    Integer docIndex;
    private List<byte[]> pdf;

    public ResultadoValidacion() {
    }

    public ResultadoValidacion(String code, String message, Integer docIndex, List<byte[]> pdfList) {
        this.code = code;
        this.message = message;
        this.docIndex = docIndex;
        this.pdf = pdfList;
    }

    
    
    public Integer getDocIndex() {
        return docIndex;
    }

    public void setDocIndex(Integer docIndex) {
        this.docIndex = docIndex;
    }
    
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the pdf
     */
    public List<byte[]> getPdf() {
        return pdf;
    }

    /**
     * @param pdf the pdf to set
     */
    public void setPdf(List<byte[]> pdf) {
        this.pdf = pdf;
    }

    
    
    
    
}
