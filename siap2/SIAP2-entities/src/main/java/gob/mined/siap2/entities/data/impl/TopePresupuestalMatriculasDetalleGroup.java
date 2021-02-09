package gob.mined.siap2.entities.data.impl;

import gob.mined.siges.entities.data.impl.SgModalidad;
import gob.mined.siges.entities.data.impl.SgModalidadAtencion;
import gob.mined.siges.entities.data.impl.SgNivel;
import java.io.Serializable;

public class TopePresupuestalMatriculasDetalleGroup implements Serializable{
    private SgNivel nivel;
    private SgModalidad modalidadEducativa;
    private SgModalidadAtencion modalidadAtencion;

    private Integer cantidadMatriculas;
    
    public SgNivel getNivel() {
        return nivel;
    }

    public void setNivel(SgNivel nivel) {
        this.nivel = nivel;
    }

    public SgModalidad getModalidadEducativa() {
        return modalidadEducativa;
    }

    public void setModalidadEducativa(SgModalidad modalidadEducativa) {
        this.modalidadEducativa = modalidadEducativa;
    }

    public SgModalidadAtencion getModalidadAtencion() {
        return modalidadAtencion;
    }

    public void setModalidadAtencion(SgModalidadAtencion modalidadAtencion) {
        this.modalidadAtencion = modalidadAtencion;
    }

    public Integer getCantidadMatriculas() {
        return cantidadMatriculas;
    }

    public void setCantidadMatriculas(Integer cantidadMatriculas) {
        this.cantidadMatriculas = cantidadMatriculas;
    }

    
}
