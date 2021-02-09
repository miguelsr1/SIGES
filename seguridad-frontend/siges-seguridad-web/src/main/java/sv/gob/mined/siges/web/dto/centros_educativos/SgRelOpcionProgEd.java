/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roePk", scope = SgRelOpcionProgEd.class)
public class SgRelOpcionProgEd implements Serializable {
     private static final long serialVersionUID = 1L;
    
    private Long roePk;
    
    private SgOpcion roeOpcion;
    
    private SgProgramaEducativo roeProgramaEducativo;
   
    private Boolean roeHabilitado;
    
    private LocalDateTime roeUltModFecha;
    
    private String roeUltModUsuario;
    
    private Integer roeVersion;
        

    public SgRelOpcionProgEd() {
        this.roeHabilitado=Boolean.TRUE;
    }

    public Long getRoePk() {
        return roePk;
    }

    public void setRoePk(Long roePk) {
        this.roePk = roePk;
    }

    public SgOpcion getRoeOpcion() {
        return roeOpcion;
    }

    public void setRoeOpcion(SgOpcion roeOpcion) {
        this.roeOpcion = roeOpcion;
    }

    public SgProgramaEducativo getRoeProgramaEducativo() {
        return roeProgramaEducativo;
    }

    public void setRoeProgramaEducativo(SgProgramaEducativo roeProgramaEducativo) {
        this.roeProgramaEducativo = roeProgramaEducativo;
    }

    public Boolean getRoeHabilitado() {
        return roeHabilitado;
    }

    public void setRoeHabilitado(Boolean roeHabilitado) {
        this.roeHabilitado = roeHabilitado;
    }

    public LocalDateTime getRoeUltModFecha() {
        return roeUltModFecha;
    }

    public void setRoeUltModFecha(LocalDateTime roeUltModFecha) {
        this.roeUltModFecha = roeUltModFecha;
    }

    public String getRoeUltModUsuario() {
        return roeUltModUsuario;
    }

    public void setRoeUltModUsuario(String roeUltModUsuario) {
        this.roeUltModUsuario = roeUltModUsuario;
    }

    public Integer getRoeVersion() {
        return roeVersion;
    }

    public void setRoeVersion(Integer roeVersion) {
        this.roeVersion = roeVersion;
    }

    public String getNombrePrograma() {
        return roeProgramaEducativo.getPedNombre();
    }
   

}
