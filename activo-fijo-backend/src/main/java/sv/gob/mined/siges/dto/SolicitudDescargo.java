/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.entidades.SgAfDescargo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosBienes;

/**
 *
 * @author Sofis Solutions
 */
@XmlRootElement
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = SolicitudDescargo.class)
public class SolicitudDescargo implements Serializable {
    private Long id;
    private SgAfDescargo descargo;
    private SgAfEstadosBienes estado;
    public SgAfDescargo getDescargo() {
        return descargo;
    }

    public void setDescargo(SgAfDescargo descargo) {
        this.descargo = descargo;
    }

    public SgAfEstadosBienes getEstado() {
        return estado;
    }

    public void setEstado(SgAfEstadosBienes estado) {
        this.estado = estado;
    } 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
