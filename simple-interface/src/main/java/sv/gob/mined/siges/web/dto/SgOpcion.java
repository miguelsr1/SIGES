/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "opcPk", scope = SgOpcion.class)
public class SgOpcion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long opcPk;

    private String opcCodigo;

    private String opcNombre;
    
    private String opcDescripcion;

    private Integer opcVersion;

    private SgModalidad opcModalidad;


    public SgOpcion() {
    }

    public Long getOpcPk() {
        return opcPk;
    }

    public void setOpcPk(Long opcPk) {
        this.opcPk = opcPk;
    }

    public String getOpcCodigo() {
        return opcCodigo;
    }

    public void setOpcCodigo(String opcCodigo) {
        this.opcCodigo = opcCodigo;
    }

    public String getOpcNombre() {
        return opcNombre;
    }

    public void setOpcNombre(String opcNombre) {
        this.opcNombre = opcNombre;
    }


    public String getOpcDescripcion() {
        return opcDescripcion;
    }

    public void setOpcDescripcion(String opcDescripcion) {
        this.opcDescripcion = opcDescripcion;
    }

    public Integer getOpcVersion() {
        return opcVersion;
    }

    public void setOpcVersion(Integer opcVersion) {
        this.opcVersion = opcVersion;
    }


    public SgModalidad getOpcModalidad() {
        return opcModalidad;
    }

    public void setOpcModalidad(SgModalidad opcModalidad) {
        this.opcModalidad = opcModalidad;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opcPk != null ? opcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgOpcion other = (SgOpcion) obj;
        if (!Objects.equals(this.opcPk, other.opcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgOpcion[ opcPk=" + opcPk + " ]";
    }

}
