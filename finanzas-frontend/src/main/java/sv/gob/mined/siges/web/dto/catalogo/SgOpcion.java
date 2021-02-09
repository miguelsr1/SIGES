/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "opcPk", scope = SgOpcion.class)
public class SgOpcion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long opcPk;

    private String opcCodigo;

    private String opcNombre;

    private Boolean opcHabilitado;

    private LocalDateTime opcUltModFecha;

    private String opcUltModUsuario;

    private Integer opcVersion;


    public SgOpcion() {
        this.opcHabilitado = Boolean.TRUE;
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

    public LocalDateTime getOpcUltModFecha() {
        return opcUltModFecha;
    }

    public void setOpcUltModFecha(LocalDateTime opcUltModFecha) {
        this.opcUltModFecha = opcUltModFecha;
    }

    public String getOpcUltModUsuario() {
        return opcUltModUsuario;
    }

    public void setOpcUltModUsuario(String opcUltModUsuario) {
        this.opcUltModUsuario = opcUltModUsuario;
    }

    public Integer getOpcVersion() {
        return opcVersion;
    }

    public void setOpcVersion(Integer opcVersion) {
        this.opcVersion = opcVersion;
    }

    public Boolean getOpcHabilitado() {
        return opcHabilitado;
    }

    public void setOpcHabilitado(Boolean opcHabilitado) {
        this.opcHabilitado = opcHabilitado;
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
