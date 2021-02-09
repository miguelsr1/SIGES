/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.entidades.SgAfTraslado;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosBienes;

/**
 *
 * @author Sofis Solutions
 */
@XmlRootElement
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = SolicitudTraslado.class)
public class SolicitudTraslado implements Serializable {
    private Long id;
    private SgAfTraslado traslado;
    private SgAfEstadosBienes estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SgAfTraslado getTraslado() {
        return traslado;
    }

    public void setTraslado(SgAfTraslado traslado) {
        this.traslado = traslado;
    }

    public SgAfEstadosBienes getEstado() {
        return estado;
    }

    public void setEstado(SgAfEstadosBienes estado) {
        this.estado = estado;
    }
    
    
}
