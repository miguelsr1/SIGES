/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.TipoValoracion;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "esvPk", scope = SgEstudianteValoracion.class)
public class SgEstudianteValoracion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long esvPk;

    private TipoValoracion esvTipoValoracion;

    private LocalDate esvFechaPublicacion;

    private String esvValoracion;

    private SgEstudiante esvEstudiante;

    private LocalDateTime esvUltModFecha;

    private String esvUltModUsuario;

    private Integer esvVersion;
    
    private String esvCreacionUsuario;

    public Long getEsvPk() {
        return esvPk;
    }

    public void setEsvPk(Long esvPk) {
        this.esvPk = esvPk;
    }

    public TipoValoracion getEsvTipoValoracion() {
        return esvTipoValoracion;
    }

    public void setEsvTipoValoracion(TipoValoracion esvTipoValoracion) {
        this.esvTipoValoracion = esvTipoValoracion;
    }

    public LocalDate getEsvFechaPublicacion() {
        return esvFechaPublicacion;
    }

    public void setEsvFechaPublicacion(LocalDate esvFechaPublicacion) {
        this.esvFechaPublicacion = esvFechaPublicacion;
    }

    public String getEsvValoracion() {
        return esvValoracion;
    }

    public void setEsvValoracion(String esvValoracion) {
        this.esvValoracion = esvValoracion;
    }

    public SgEstudiante getEsvEstudiante() {
        return esvEstudiante;
    }

    public void setEsvEstudiante(SgEstudiante esvEstudiante) {
        this.esvEstudiante = esvEstudiante;
    }

    public LocalDateTime getEsvUltModFecha() {
        return esvUltModFecha;
    }

    public void setEsvUltModFecha(LocalDateTime esvUltModFecha) {
        this.esvUltModFecha = esvUltModFecha;
    }

    public String getEsvUltModUsuario() {
        return esvUltModUsuario;
    }

    public void setEsvUltModUsuario(String esvUltModUsuario) {
        this.esvUltModUsuario = esvUltModUsuario;
    }

    public Integer getEsvVersion() {
        return esvVersion;
    }

    public void setEsvVersion(Integer esvVersion) {
        this.esvVersion = esvVersion;
    }

    @Override
    public String toString() {
        return "SgEstudianteValidacion{" + "esvPk=" + esvPk + '}';
    }

    public String getEsvCreacionUsuario() {
        return esvCreacionUsuario;
    }

    public void setEsvCreacionUsuario(String esvCreacionUsuario) {
        this.esvCreacionUsuario = esvCreacionUsuario;
    }

}
