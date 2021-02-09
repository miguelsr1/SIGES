package sv.gob.mined.siges.dto;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.TipoSede;

public class SgPrediccionDesercionRequest implements Serializable {

    
    private TipoSede est_tipo_sede;
    private Long est_zona;
    private Long est_grado;
    private Long est_sexo;
    private Integer est_edad;
    private Integer est_embarazo;
    private Integer est_hijos;
    private Long est_estado_civil;
    private Integer est_repetidor;
    private Integer est_inasistencias;

    public TipoSede getEst_tipo_sede() {
        return est_tipo_sede;
    }

    public void setEst_tipo_sede(TipoSede est_tipo_sede) {
        this.est_tipo_sede = est_tipo_sede;
    }

    public Long getEst_zona() {
        return est_zona;
    }

    public void setEst_zona(Long est_zona) {
        this.est_zona = est_zona;
    }

    public Long getEst_grado() {
        return est_grado;
    }

    public void setEst_grado(Long est_grado) {
        this.est_grado = est_grado;
    }

    public Integer getEst_edad() {
        return est_edad;
    }

    public void setEst_edad(Integer est_edad) {
        this.est_edad = est_edad;
    }

    public Integer getEst_embarazo() {
        return est_embarazo;
    }

    public void setEst_embarazo(Integer est_embarazo) {
        this.est_embarazo = est_embarazo;
    }

    public Integer getEst_hijos() {
        return est_hijos;
    }

    public void setEst_hijos(Integer est_hijos) {
        this.est_hijos = est_hijos;
    }

    public Long getEst_estado_civil() {
        return est_estado_civil;
    }

    public void setEst_estado_civil(Long est_estado_civil) {
        this.est_estado_civil = est_estado_civil;
    }

    public Integer getEst_repetidor() {
        return est_repetidor;
    }

    public void setEst_repetidor(Integer est_repetidor) {
        this.est_repetidor = est_repetidor;
    }

    public Integer getEst_inasistencias() {
        return est_inasistencias;
    }

    public void setEst_inasistencias(Integer est_inasistencias) {
        this.est_inasistencias = est_inasistencias;
    }

    public Long getEst_sexo() {
        return est_sexo;
    }

    public void setEst_sexo(Long est_sexo) {
        this.est_sexo = est_sexo;
    }

    @Override
    public String toString() {
        return "SgPrediccionDesercionRequest{" + "est_tipo_sede=" + est_tipo_sede + ", est_zona=" + est_zona + ", est_grado=" + est_grado + ", est_sexo=" + est_sexo + ", est_edad=" + est_edad + ", est_embarazo=" + est_embarazo + ", est_hijos=" + est_hijos + ", est_estado_civil=" + est_estado_civil + ", est_repetidor=" + est_repetidor + ", est_inasistencias=" + est_inasistencias + '}';
    }
    
    

    

    



}
