/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgMedioTransporte;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "menPk", scope = SgMenorEncuestaEstudiante.class)
public class SgMenorEncuestaEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long menPk;
    
    @JsonIgnore
    private Long menViewPk; //Solo para la vista
    
    private Boolean menMatriculadoSiges;

    private Boolean menTieneNie;
        
    private Long menNie;

    private Boolean menEstudia;

    private Boolean menEsFamiliar;

    private SgTipoParentesco menTipoParentesco;
    
    private String menPrimerNombre;

    private String menSegundoNombre;

    private String menPrimerApellido;

    private String menSegundoApellido;

    private SgSexo menSexo;

    private SgEstadoCivil menEstadoCivil;
    
    private SgNacionalidad menNacionalidad;
    
    private LocalDate menFechaNacimiento;
    
    private SgSede menSede;
    
    private SgMedioTransporte menMedioTransporte;
    
    private SgEncuestaEstudiante menEncuestaEstudiante;

    private LocalDateTime menUltModFecha;

    private String menUltModUsuario;

    private Integer menVersion;
    
    private Boolean menValidadoSIGES;
    
    private Long menPersonaFk;
    
    private Long menEstudianteFk;
    

    public SgMenorEncuestaEstudiante() {
        this.menValidadoSIGES = Boolean.FALSE;

    }
    
    @JsonIgnore
    public String getMenNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.menPrimerApellido != null) {
            s.append(this.menPrimerApellido).append(" ");
        }
        if (this.menSegundoApellido != null) {
            s.append(this.menSegundoApellido).append(" ");
        }
        if (s.toString().endsWith(" ")) {
            s.deleteCharAt(s.toString().length() - 1);
        }
        s.append(", ");
        if (this.menPrimerNombre != null) {
            s.append(this.menPrimerNombre).append(" ");
        }
        if (this.menSegundoNombre != null) {
            s.append(this.menSegundoNombre).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public Long getMenPk() {
        return menPk;
    }

    public void setMenPk(Long menPk) {
        this.menPk = menPk;
    }

    public Boolean getMenMatriculadoSiges() {
        return menMatriculadoSiges;
    }

    public void setMenMatriculadoSiges(Boolean menMatriculadoSiges) {
        this.menMatriculadoSiges = menMatriculadoSiges;
    }

    public Boolean getMenTieneNie() {
        return menTieneNie;
    }

    public void setMenTieneNie(Boolean menTieneNie) {
        this.menTieneNie = menTieneNie;
    }

    public Boolean getMenEstudia() {
        return menEstudia;
    }

    public void setMenEstudia(Boolean menEstudia) {
        this.menEstudia = menEstudia;
    }

    public SgSede getMenSede() {
        return menSede;
    }

    public void setMenSede(SgSede menSede) {
        this.menSede = menSede;
    }

    public SgMedioTransporte getMenMedioTransporte() {
        return menMedioTransporte;
    }

    public void setMenMedioTransporte(SgMedioTransporte menMedioTransporte) {
        this.menMedioTransporte = menMedioTransporte;
    }

    public LocalDateTime getMenUltModFecha() {
        return menUltModFecha;
    }

    public void setMenUltModFecha(LocalDateTime menUltModFecha) {
        this.menUltModFecha = menUltModFecha;
    }

    public String getMenUltModUsuario() {
        return menUltModUsuario;
    }

    public void setMenUltModUsuario(String menUltModUsuario) {
        this.menUltModUsuario = menUltModUsuario;
    }

    public Integer getMenVersion() {
        return menVersion;
    }

    public void setMenVersion(Integer menVersion) {
        this.menVersion = menVersion;
    }

    public SgEncuestaEstudiante getMenEncuestaEstudiante() {
        return menEncuestaEstudiante;
    }

    public void setMenEncuestaEstudiante(SgEncuestaEstudiante menEncuestaEstudiante) {
        this.menEncuestaEstudiante = menEncuestaEstudiante;
    }

    public Long getMenViewPk() {
        return menViewPk;
    }

    public void setMenViewPk(Long menViewPk) {
        this.menViewPk = menViewPk;
    }

    public Boolean getMenValidadoSIGES() {
        return menValidadoSIGES;
    }

    public void setMenValidadoSIGES(Boolean menValidadoSIGES) {
        this.menValidadoSIGES = menValidadoSIGES;
    }

    public Long getMenNie() {
        return menNie;
    }

    public void setMenNie(Long menNie) {
        this.menNie = menNie;
    }

    public Boolean getMenEsFamiliar() {
        return menEsFamiliar;
    }

    public void setMenEsFamiliar(Boolean menEsFamiliar) {
        this.menEsFamiliar = menEsFamiliar;
    }

    public SgTipoParentesco getMenTipoParentesco() {
        return menTipoParentesco;
    }

    public void setMenTipoParentesco(SgTipoParentesco menTipoParentesco) {
        this.menTipoParentesco = menTipoParentesco;
    }

    public String getMenPrimerNombre() {
        return menPrimerNombre;
    }

    public void setMenPrimerNombre(String menPrimerNombre) {
        this.menPrimerNombre = menPrimerNombre;
    }

    public String getMenSegundoNombre() {
        return menSegundoNombre;
    }

    public void setMenSegundoNombre(String menSegundoNombre) {
        this.menSegundoNombre = menSegundoNombre;
    }

    public String getMenPrimerApellido() {
        return menPrimerApellido;
    }

    public void setMenPrimerApellido(String menPrimerApellido) {
        this.menPrimerApellido = menPrimerApellido;
    }

    public String getMenSegundoApellido() {
        return menSegundoApellido;
    }

    public void setMenSegundoApellido(String menSegundoApellido) {
        this.menSegundoApellido = menSegundoApellido;
    }

    public SgSexo getMenSexo() {
        return menSexo;
    }

    public void setMenSexo(SgSexo menSexo) {
        this.menSexo = menSexo;
    }

    public SgEstadoCivil getMenEstadoCivil() {
        return menEstadoCivil;
    }

    public void setMenEstadoCivil(SgEstadoCivil menEstadoCivil) {
        this.menEstadoCivil = menEstadoCivil;
    }

    public SgNacionalidad getMenNacionalidad() {
        return menNacionalidad;
    }

    public void setMenNacionalidad(SgNacionalidad menNacionalidad) {
        this.menNacionalidad = menNacionalidad;
    }

    public LocalDate getMenFechaNacimiento() {
        return menFechaNacimiento;
    }

    public void setMenFechaNacimiento(LocalDate menFechaNacimiento) {
        this.menFechaNacimiento = menFechaNacimiento;
    }

    public Long getMenPersonaFk() {
        return menPersonaFk;
    }

    public void setMenPersonaFk(Long menPersonaFk) {
        this.menPersonaFk = menPersonaFk;
    }

    

    public Long getMenEstudianteFk() {
        return menEstudianteFk;
    }

    public void setMenEstudianteFk(Long menEstudianteFk) {
        this.menEstudianteFk = menEstudianteFk;
    }

    


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.menPk);
        hash = 47 * hash + Objects.hashCode(this.menViewPk);
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
        final SgMenorEncuestaEstudiante other = (SgMenorEncuestaEstudiante) obj;
        if (!Objects.equals(this.menPk, other.menPk)) {
            return false;
        }
        if (!Objects.equals(this.menViewPk, other.menViewPk)) {
            return false;
        }
        return true;
    }
        

    
    



}
