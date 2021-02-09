/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.utilidades.ObjectMapperContextResolver;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "matPk", scope = SgMatriculaParcial.class)
public class SgMatriculaParcial implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long matPk;

    private String matEstPrimerNombre;

    private String matEstSegundoNombre;

    private String matEstPrimerApellido;

    private String matEstSegundoApellido;

    private Long matEstNie;

    private LocalDateTime matUltModFecha;

    private String matUltModUsuario;

    private String matCreacionUsuario;

    private Integer matVersion;

    private String matJson;

    public SgMatriculaParcial() {
    }
    
    public String getMatEstNombreCompleto(){
        StringBuilder s = new StringBuilder();
        if (this.matEstPrimerNombre != null){
            s.append(this.matEstPrimerNombre).append(" ");
        }
        if (this.matEstSegundoNombre != null){
            s.append(this.matEstSegundoNombre).append(" ");
        }
        if (this.matEstPrimerApellido != null){
            s.append(this.matEstPrimerApellido).append(" ");
        }
        if (this.matEstSegundoApellido != null){
            s.append(this.matEstSegundoApellido).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public void actualizar(SgMatricula entity) throws Exception {
        ObjectMapperContextResolver contextResolver = new ObjectMapperContextResolver();
        this.setMatEstPrimerNombre(entity.getMatEstudiante().getEstPersona().getPerPrimerNombre());
        this.setMatEstSegundoNombre(entity.getMatEstudiante().getEstPersona().getPerSegundoNombre());
        this.setMatEstPrimerApellido(entity.getMatEstudiante().getEstPersona().getPerPrimerApellido());
        this.setMatEstSegundoApellido(entity.getMatEstudiante().getEstPersona().getPerSegundoApellido());
        this.setMatEstNie(entity.getMatEstudiante().getEstPersona().getPerNie());
        this.setMatJson(contextResolver.getDefaultMapper().writeValueAsString(entity));
    }

    public SgMatriculaParcial(Long matPk) {
        this.matPk = matPk;
    }

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public String getMatEstPrimerNombre() {
        return matEstPrimerNombre;
    }

    public void setMatEstPrimerNombre(String matEstPrimerNombre) {
        this.matEstPrimerNombre = matEstPrimerNombre;
    }

    public String getMatEstSegundoNombre() {
        return matEstSegundoNombre;
    }

    public void setMatEstSegundoNombre(String matEstSegundoNombre) {
        this.matEstSegundoNombre = matEstSegundoNombre;
    }

    public String getMatEstPrimerApellido() {
        return matEstPrimerApellido;
    }

    public void setMatEstPrimerApellido(String matEstPrimerApellido) {
        this.matEstPrimerApellido = matEstPrimerApellido;
    }

    public String getMatEstSegundoApellido() {
        return matEstSegundoApellido;
    }

    public void setMatEstSegundoApellido(String matEstSegundoApellido) {
        this.matEstSegundoApellido = matEstSegundoApellido;
    }

    public Long getMatEstNie() {
        return matEstNie;
    }

    public void setMatEstNie(Long matEstNie) {
        this.matEstNie = matEstNie;
    }

    public String getMatJson() {
        return matJson;
    }

    public void setMatJson(String matJson) {
        this.matJson = matJson;
    }

    public LocalDateTime getMatUltModFecha() {
        return matUltModFecha;
    }

    public void setMatUltModFecha(LocalDateTime matUltModFecha) {
        this.matUltModFecha = matUltModFecha;
    }

    public String getMatUltModUsuario() {
        return matUltModUsuario;
    }

    public void setMatUltModUsuario(String matUltModUsuario) {
        this.matUltModUsuario = matUltModUsuario;
    }

    public Integer getMatVersion() {
        return matVersion;
    }

    public void setMatVersion(Integer matVersion) {
        this.matVersion = matVersion;
    }

    public String getMatCreacionUsuario() {
        return matCreacionUsuario;
    }

    public void setMatCreacionUsuario(String matCreacionUsuario) {
        this.matCreacionUsuario = matCreacionUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matPk != null ? matPk.hashCode() : 0);
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
        final SgMatriculaParcial other = (SgMatriculaParcial) obj;
        if (!Objects.equals(this.matPk, other.matPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMatriculaParcial[ matPk=" + matPk + " ]";
    }

}
