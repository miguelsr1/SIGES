/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nivPk", scope = SgNivel.class)
public class SgNivel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nivPk;

    private String nivCodigo;

    private String nivNombre;

    private Integer nivOrden;

    private Boolean nivAdmiteCiclos;

    private Boolean nivObligatorio;

    private Boolean nivHabilitado;

    private SgOrganizacionCurricular nivOrganizacionCurricular;

    private List<SgCiclo> nivCiclos;

    private LocalDateTime nivUltModFecha;

    private String nivUltModUsuario;

    private Integer nivVersion;

    public SgNivel() {
        this.nivHabilitado = Boolean.TRUE;
        this.nivObligatorio = Boolean.TRUE;
        this.nivAdmiteCiclos = Boolean.TRUE;
    }

    public Long getNivPk() {
        return nivPk;
    }

    public void setNivPk(Long nivPk) {
        this.nivPk = nivPk;
    }

    public String getNivCodigo() {
        return nivCodigo;
    }

    public void setNivCodigo(String nivCodigo) {
        this.nivCodigo = nivCodigo;
    }

    public String getNivNombre() {
        return nivNombre;
    }

    public void setNivNombre(String nivNombre) {
        this.nivNombre = nivNombre;
    }

    public LocalDateTime getNivUltModFecha() {
        return nivUltModFecha;
    }

    public void setNivUltModFecha(LocalDateTime nivUltModFecha) {
        this.nivUltModFecha = nivUltModFecha;
    }

    public String getNivUltModUsuario() {
        return nivUltModUsuario;
    }

    public void setNivUltModUsuario(String nivUltModUsuario) {
        this.nivUltModUsuario = nivUltModUsuario;
    }

    public Integer getNivVersion() {
        return nivVersion;
    }

    public void setNivVersion(Integer nivVersion) {
        this.nivVersion = nivVersion;
    }

    public Boolean getNivHabilitado() {
        return nivHabilitado;
    }

    public void setNivHabilitado(Boolean nivHabilitado) {
        this.nivHabilitado = nivHabilitado;
    }

    public SgOrganizacionCurricular getNivOrganizacionCurricular() {
        return nivOrganizacionCurricular;
    }

    public void setNivOrganizacionCurricular(SgOrganizacionCurricular nivOrganizacionCurricular) {
        this.nivOrganizacionCurricular = nivOrganizacionCurricular;
    }

    public List<SgCiclo> getNivCiclos() {
        return nivCiclos;
    }

    public void setNivCiclos(List<SgCiclo> nivCiclos) {
        this.nivCiclos = nivCiclos;
    }

    public Integer getNivOrden() {
        return nivOrden;
    }

    public void setNivOrden(Integer nivOrden) {
        this.nivOrden = nivOrden;
    }

    public Boolean getNivAdmiteCiclos() {
        return nivAdmiteCiclos;
    }

    public void setNivAdmiteCiclos(Boolean nivAdmiteCiclos) {
        this.nivAdmiteCiclos = nivAdmiteCiclos;
    }

    public Boolean getNivObligatorio() {
        return nivObligatorio;
    }

    public String getNivelOrganizacion() {
        if (nivOrganizacionCurricular != null) {
            return nivOrganizacionCurricular.getOcuNombre() + " - " + nivNombre;
        }
        return nivNombre;
    }

    public void setNivObligatorio(Boolean nivObligatorio) {
        this.nivObligatorio = nivObligatorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nivPk != null ? nivPk.hashCode() : 0);
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
        final SgNivel other = (SgNivel) obj;
        if (!Objects.equals(this.nivPk, other.nivPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgNivel[ nivPk=" + nivPk + " ]";
    }

}
