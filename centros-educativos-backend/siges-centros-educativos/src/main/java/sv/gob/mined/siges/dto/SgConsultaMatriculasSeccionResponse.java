package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 *
 * @author usuario
 */
public class SgConsultaMatriculasSeccionResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String sedeCodigo;
    private Long sedePk;
    private String seccionCodigo;
    private Long seccionPk;
    private String seccionRegimen;
    private Long cantMatMasculino;
    private Long cantMatFemenino;
    
    

    public SgConsultaMatriculasSeccionResponse() {
    }

    public String getSedeCodigo() {
        return sedeCodigo;
    }

    public void setSedeCodigo(String sedeCodigo) {
        this.sedeCodigo = sedeCodigo;
    }

    public String getSeccionCodigo() {
        return seccionCodigo;
    }

    public void setSeccionCodigo(String seccionCodigo) {
        this.seccionCodigo = seccionCodigo;
    }

    public String getSeccionRegimen() {
        return seccionRegimen;
    }

    public void setSeccionRegimen(String seccionRegimen) {
        this.seccionRegimen = seccionRegimen;
    }

    public Long getCantMatMasculino() {
        return cantMatMasculino;
    }

    public void setCantMatMasculino(Long cantMatMasculino) {
        this.cantMatMasculino = cantMatMasculino;
    }

    public Long getCantMatFemenino() {
        return cantMatFemenino;
    }

    public void setCantMatFemenino(Long cantMatFemenino) {
        this.cantMatFemenino = cantMatFemenino;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getSeccionPk() {
        return seccionPk;
    }

    public void setSeccionPk(Long seccionPk) {
        this.seccionPk = seccionPk;
    }
    
    

   
}
