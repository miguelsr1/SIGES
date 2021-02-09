/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "id", scope = SolicitudDescargo.class)
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

