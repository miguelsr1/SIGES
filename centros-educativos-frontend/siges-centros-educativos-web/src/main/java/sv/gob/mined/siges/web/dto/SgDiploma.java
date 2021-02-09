/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dilPk", scope = SgDiploma.class)
public class SgDiploma implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long dilPk;

    private SgSede dilSedeFk;

    private SgAnioLectivo dilAnioLectivoFk;
 
    private SgDiplomado dilDiplomadoFk;

    private LocalDateTime dilUltModFecha;

    private String dilUltModUsuario;

    private Integer dilVersion;
    
    private List<SgDiplomaEstudiante> diplomaEstudiantes;
    
    private SgSelloFirma dilSelloDirector;

    private SgSelloFirmaTitular dilSelloMinistro;
    
    public SgDiploma() {

    }

    public Long getDilPk() {
        return dilPk;
    }

    public void setDilPk(Long dilPk) {
        this.dilPk = dilPk;
    }

    public LocalDateTime getDilUltModFecha() {
        return dilUltModFecha;
    }

    public void setDilUltModFecha(LocalDateTime dilUltModFecha) {
        this.dilUltModFecha = dilUltModFecha;
    }

    public String getDilUltModUsuario() {
        return dilUltModUsuario;
    }

    public void setDilUltModUsuario(String dilUltModUsuario) {
        this.dilUltModUsuario = dilUltModUsuario;
    }

    public Integer getDilVersion() {
        return dilVersion;
    }

    public void setDilVersion(Integer dilVersion) {
        this.dilVersion = dilVersion;
    }

    public SgSede getDilSedeFk() {
        return dilSedeFk;
    }

    public void setDilSedeFk(SgSede dilSedeFk) {
        this.dilSedeFk = dilSedeFk;
    }

    public SgAnioLectivo getDilAnioLectivoFk() {
        return dilAnioLectivoFk;
    }

    public void setDilAnioLectivoFk(SgAnioLectivo dilAnioLectivoFk) {
        this.dilAnioLectivoFk = dilAnioLectivoFk;
    }

    public SgDiplomado getDilDiplomadoFk() {
        return dilDiplomadoFk;
    }

    public void setDilDiplomadoFk(SgDiplomado dilDiplomadoFk) {
        this.dilDiplomadoFk = dilDiplomadoFk;
    }

    public List<SgDiplomaEstudiante> getDiplomaEstudiantes() {
        return diplomaEstudiantes;
    }

    public void setDiplomaEstudiantes(List<SgDiplomaEstudiante> diplomaEstudiantes) {
        this.diplomaEstudiantes = diplomaEstudiantes;
    }

    public SgSelloFirma getDilSelloDirector() {
        return dilSelloDirector;
    }

    public void setDilSelloDirector(SgSelloFirma dilSelloDirector) {
        this.dilSelloDirector = dilSelloDirector;
    }

    public SgSelloFirmaTitular getDilSelloMinistro() {
        return dilSelloMinistro;
    }

    public void setDilSelloMinistro(SgSelloFirmaTitular dilSelloMinistro) {
        this.dilSelloMinistro = dilSelloMinistro;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dilPk != null ? dilPk.hashCode() : 0);
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
        final SgDiploma other = (SgDiploma) obj;
        if (!Objects.equals(this.dilPk, other.dilPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDiploma[ dilPk=" + dilPk + " ]";
    }
    
}
