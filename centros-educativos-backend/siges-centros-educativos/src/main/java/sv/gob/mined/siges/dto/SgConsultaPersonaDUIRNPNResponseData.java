/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;


public class SgConsultaPersonaDUIRNPNResponseData implements Serializable {
    
    private String dui;
    private String nom1;
    private String nom2;
    private String nom3;
    private String ape1;
    private String ape2;
    private String apelCsda;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaNaci;
    
    private String muniNaci;
    private String deptNaci;
    private String paisNaci;
    private String nombMadre;
    private String nombPadre;
    

    public SgConsultaPersonaDUIRNPNResponseData() {
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNom1() {
        return nom1;
    }

    public void setNom1(String nom1) {
        this.nom1 = nom1;
    }

    public String getNom2() {
        return nom2;
    }

    public void setNom2(String nom2) {
        this.nom2 = nom2;
    }

    public String getNom3() {
        return nom3;
    }

    public void setNom3(String nom3) {
        this.nom3 = nom3;
    }

    public String getApe1() {
        return ape1;
    }

    public void setApe1(String ape1) {
        this.ape1 = ape1;
    }

    public String getApe2() {
        return ape2;
    }

    public void setApe2(String ape2) {
        this.ape2 = ape2;
    }

    public String getApelCsda() {
        return apelCsda;
    }

    public void setApelCsda(String apelCsda) {
        this.apelCsda = apelCsda;
    }

    public LocalDate getFechaNaci() {
        return fechaNaci;
    }

    public void setFechaNaci(LocalDate fechaNaci) {
        this.fechaNaci = fechaNaci;
    }

    public String getMuniNaci() {
        return muniNaci;
    }

    public void setMuniNaci(String muniNaci) {
        this.muniNaci = muniNaci;
    }

    public String getDeptNaci() {
        return deptNaci;
    }

    public void setDeptNaci(String deptNaci) {
        this.deptNaci = deptNaci;
    }

    public String getPaisNaci() {
        return paisNaci;
    }

    public void setPaisNaci(String paisNaci) {
        this.paisNaci = paisNaci;
    }

    public String getNombMadre() {
        return nombMadre;
    }

    public void setNombMadre(String nombMadre) {
        this.nombMadre = nombMadre;
    }

    public String getNombPadre() {
        return nombPadre;
    }

    public void setNombPadre(String nombPadre) {
        this.nombPadre = nombPadre;
    }
    
    
    

}
