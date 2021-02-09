/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "peaPk", scope = SgPlanEscolarAnual.class)
public class SgPlanEscolarAnual implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long peaPk;

    private SgSede peaSede;

    private SgAnioLectivo peaAnioLectivo;

    private LocalDate peaFechaPresentado;

    private LocalDate peaFechaValido;

    private SgUsuario peaUsuarioValido;

    private LocalDateTime peaUltModFecha;

    private String peaUltModUsuario;

    private Integer peaVersion;

    private List<SgDetallePlanEscolar> peaDetallePlanEscolar;

    public SgPlanEscolarAnual() {
        this.peaDetallePlanEscolar = new ArrayList<>();
    }

    public SgPlanEscolarAnual(Long peaPk) {
        this.peaPk = peaPk;
    }

    public Long getPeaPk() {
        return peaPk;
    }

    public void setPeaPk(Long peaPk) {
        this.peaPk = peaPk;
    }

    public SgSede getPeaSede() {
        return peaSede;
    }

    public void setPeaSede(SgSede peaSede) {
        this.peaSede = peaSede;
    }

    public SgAnioLectivo getPeaAnioLectivo() {
        return peaAnioLectivo;
    }

    public void setPeaAnioLectivo(SgAnioLectivo peaAnioLectivo) {
        this.peaAnioLectivo = peaAnioLectivo;
    }

    public LocalDate getPeaFechaPresentado() {
        return peaFechaPresentado;
    }

    public void setPeaFechaPresentado(LocalDate peaFechaPresentado) {
        this.peaFechaPresentado = peaFechaPresentado;
    }

    public LocalDate getPeaFechaValido() {
        return peaFechaValido;
    }

    public void setPeaFechaValido(LocalDate peaFechaValido) {
        this.peaFechaValido = peaFechaValido;
    }

    public SgUsuario getPeaUsuarioValido() {
        return peaUsuarioValido;
    }

    public void setPeaUsuarioValido(SgUsuario peaUsuarioValido) {
        this.peaUsuarioValido = peaUsuarioValido;
    }

    public LocalDateTime getPeaUltModFecha() {
        return peaUltModFecha;
    }

    public void setPeaUltModFecha(LocalDateTime peaUltModFecha) {
        this.peaUltModFecha = peaUltModFecha;
    }

    public String getPeaUltModUsuario() {
        return peaUltModUsuario;
    }

    public void setPeaUltModUsuario(String peaUltModUsuario) {
        this.peaUltModUsuario = peaUltModUsuario;
    }

    public Integer getPeaVersion() {
        return peaVersion;
    }

    public void setPeaVersion(Integer peaVersion) {
        this.peaVersion = peaVersion;
    }

    public List<SgDetallePlanEscolar> getPeaDetallePlanEscolar() {
        return peaDetallePlanEscolar;
    }

    public void setPeaDetallePlanEscolar(List<SgDetallePlanEscolar> peaDetallePlanEscolar) {
        this.peaDetallePlanEscolar = peaDetallePlanEscolar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (peaPk != null ? peaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPlanEscolarAnual)) {
            return false;
        }
        SgPlanEscolarAnual other = (SgPlanEscolarAnual) object;
        if ((this.peaPk == null && other.peaPk != null) || (this.peaPk != null && !this.peaPk.equals(other.peaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPlanEscolarAnual[ peaPk=" + peaPk + " ]";
    }

}
