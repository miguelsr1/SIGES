/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpnPk", scope = SgComponente.class)
public class SgComponente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long cpnPk;

    private SgProyectoInstitucional cpnProyectoInstitucional;
    
    private String cpnNombre;

    private String cpnDescripcion;

    private LocalDateTime cpnUltModFecha;

    private String cpnUltModUsuario;

    private Integer cpnVersion;
    
    
    public SgComponente() {
    }

    public Long getCpnPk() {
        return cpnPk;
    }

    public void setCpnPk(Long cpnPk) {
        this.cpnPk = cpnPk;
    }

    public SgProyectoInstitucional getCpnProyectoInstitucional() {
        return cpnProyectoInstitucional;
    }

    public void setCpnProyectoInstitucional(SgProyectoInstitucional cpnProyectoInstitucional) {
        this.cpnProyectoInstitucional = cpnProyectoInstitucional;
    }

    public String getCpnNombre() {
        return cpnNombre;
    }

    public void setCpnNombre(String cpnNombre) {
        this.cpnNombre = cpnNombre;
    }

    public String getCpnDescripcion() {
        return cpnDescripcion;
    }

    public void setCpnDescripcion(String cpnDescripcion) {
        this.cpnDescripcion = cpnDescripcion;
    }

    public LocalDateTime getCpnUltModFecha() {
        return cpnUltModFecha;
    }

    public void setCpnUltModFecha(LocalDateTime cpnUltModFecha) {
        this.cpnUltModFecha = cpnUltModFecha;
    }

    public String getCpnUltModUsuario() {
        return cpnUltModUsuario;
    }

    public void setCpnUltModUsuario(String cpnUltModUsuario) {
        this.cpnUltModUsuario = cpnUltModUsuario;
    }

    public Integer getCpnVersion() {
        return cpnVersion;
    }

    public void setCpnVersion(Integer cpnVersion) {
        this.cpnVersion = cpnVersion;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpnPk != null ? cpnPk.hashCode() : 0);
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
        final SgComponente other = (SgComponente) obj;
        if (!Objects.equals(this.cpnPk, other.cpnPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgComponente[ cpnPk=" + cpnPk + " ]";
    }
    
}
