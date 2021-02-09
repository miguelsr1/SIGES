/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumTipoBeneficio;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bnfPk", scope = SgBeneficio.class)
public class SgBeneficio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long bnfPk;

    private SgProyectoInstitucional bnfProyectoInstitucional;

    private String bnfNombre;

    private String bnfDescripcion;

    private String bnfObjetivos;

    private EnumTipoBeneficio bnfTipoBeneficio;

    private Integer bnfCantidadAnual;

    private LocalDateTime bnfUltModFecha;

    private String bnfUltModUsuario;

    private Integer bnfVersion;
    
    
    public SgBeneficio() {
    }

    public Long getBnfPk() {
        return bnfPk;
    }

    public void setBnfPk(Long bnfPk) {
        this.bnfPk = bnfPk;
    }

    public SgProyectoInstitucional getBnfProyectoInstitucional() {
        return bnfProyectoInstitucional;
    }

    public void setBnfProyectoInstitucional(SgProyectoInstitucional bnfProyectoInstitucional) {
        this.bnfProyectoInstitucional = bnfProyectoInstitucional;
    }

    public String getBnfNombre() {
        return bnfNombre;
    }

    public void setBnfNombre(String bnfNombre) {
        this.bnfNombre = bnfNombre;
    }

    public String getBnfDescripcion() {
        return bnfDescripcion;
    }

    public void setBnfDescripcion(String bnfDescripcion) {
        this.bnfDescripcion = bnfDescripcion;
    }

    public String getBnfObjetivos() {
        return bnfObjetivos;
    }

    public void setBnfObjetivos(String bnfObjetivos) {
        this.bnfObjetivos = bnfObjetivos;
    }

    public EnumTipoBeneficio getBnfTipoBeneficio() {
        return bnfTipoBeneficio;
    }

    public void setBnfTipoBeneficio(EnumTipoBeneficio bnfTipoBeneficio) {
        this.bnfTipoBeneficio = bnfTipoBeneficio;
    }

    public Integer getBnfCantidadAnual() {
        return bnfCantidadAnual;
    }

    public void setBnfCantidadAnual(Integer bnfCantidadAnual) {
        this.bnfCantidadAnual = bnfCantidadAnual;
    }

    public LocalDateTime getBnfUltModFecha() {
        return bnfUltModFecha;
    }

    public void setBnfUltModFecha(LocalDateTime bnfUltModFecha) {
        this.bnfUltModFecha = bnfUltModFecha;
    }

    public String getBnfUltModUsuario() {
        return bnfUltModUsuario;
    }

    public void setBnfUltModUsuario(String bnfUltModUsuario) {
        this.bnfUltModUsuario = bnfUltModUsuario;
    }

    public Integer getBnfVersion() {
        return bnfVersion;
    }

    public void setBnfVersion(Integer bnfVersion) {
        this.bnfVersion = bnfVersion;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bnfPk != null ? bnfPk.hashCode() : 0);
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
        final SgBeneficio other = (SgBeneficio) obj;
        if (!Objects.equals(this.bnfPk, other.bnfPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgBeneficio[ bnfPk=" + bnfPk + " ]";
    }
    
}
