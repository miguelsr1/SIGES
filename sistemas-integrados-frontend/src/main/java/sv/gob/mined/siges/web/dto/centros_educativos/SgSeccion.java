/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "secPk", scope = SgSeccion.class)
public class SgSeccion implements Serializable {

    private Long secPk;

    private String secCodigo;

    private String secNombre;


    private SgJornadaLaboral secJornadaLaboral;
    
    private Boolean secAccesoInternet;
    
    private Boolean secAccesoComputadora;

    private LocalDateTime secUltModFecha;

    private String secUltModUsuario;

    private Integer secVersion;
    
    
    public SgSeccion() {
    }

    public Long getSecPk() {
        return secPk;
    }

    @JsonIgnore
    public String getNombreSeccion() {
        if (secJornadaLaboral != null) {
            return secNombre + " - " + secJornadaLaboral.getJlaNombre();
        } else {
            return secNombre;
        }
    }
    
    public void setSecPk(Long secPk) {
        this.secPk = secPk;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public String getSecNombre() {
        return secNombre;
    }

    public void setSecNombre(String secNombre) {
        this.secNombre = secNombre;
    }

    public SgJornadaLaboral getSecJornadaLaboral() {
        return secJornadaLaboral;
    }

    public void setSecJornadaLaboral(SgJornadaLaboral secJornadaLaboral) {
        this.secJornadaLaboral = secJornadaLaboral;
    }


    public LocalDateTime getSecUltModFecha() {
        return secUltModFecha;
    }

    public void setSecUltModFecha(LocalDateTime secUltModFecha) {
        this.secUltModFecha = secUltModFecha;
    }

    public String getSecUltModUsuario() {
        return secUltModUsuario;
    }

    public void setSecUltModUsuario(String secUltModUsuario) {
        this.secUltModUsuario = secUltModUsuario;
    }

    public Integer getSecVersion() {
        return secVersion;
    }

    public void setSecVersion(Integer secVersion) {
        this.secVersion = secVersion;
    }

    public Boolean getSecAccesoInternet() {
        return secAccesoInternet;
    }

    public void setSecAccesoInternet(Boolean secAccesoInternet) {
        this.secAccesoInternet = secAccesoInternet;
    }

    public Boolean getSecAccesoComputadora() {
        return secAccesoComputadora;
    }

    public void setSecAccesoComputadora(Boolean secAccesoComputadora) {
        this.secAccesoComputadora = secAccesoComputadora;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.secPk);
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
        final SgSeccion other = (SgSeccion) obj;
        if (!Objects.equals(this.secPk, other.secPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSeccion{" + "secPk=" + secPk + ", secNombre=" + secNombre + ", secVersion=" + secVersion + '}';
    }

}
