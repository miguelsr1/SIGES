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
import org.apache.commons.lang3.BooleanUtils;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cacPk", scope = SgControlAsistenciaCabezal.class)
public class SgControlAsistenciaCabezal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cacPk;

    private SgSeccion cacSeccion;

    private LocalDate cacFecha;

    private Integer cacEstudiantesPresentes;

    private Integer cacEstudiantesEnLista;

    private Integer cacEstudiantesAusentesJustificados;

    private Integer cacEstudiantesAusentesSinJustificar;

    private LocalDateTime cacUltModFecha;

    private String cacUltModUsuario;

    private Integer cacVersion;

    private List<SgAsistencia> cacAsistencia;

    public SgControlAsistenciaCabezal() {
        this.cacAsistencia = new ArrayList<>();
        cacEstudiantesAusentesJustificados = 0;
        cacEstudiantesAusentesSinJustificar = 0;
    }

    public Long getCantidadEstudiantesPresentes() {
        if (this.cacAsistencia != null) {
            return this.cacAsistencia.stream().filter(a -> BooleanUtils.isFalse(a.getAsiInasistencia())).count();
        }
        return null;
    }

    public SgControlAsistenciaCabezal(Long cacPk) {
        this.cacPk = cacPk;
    }

    public Long getCacPk() {
        return cacPk;
    }

    public void setCacPk(Long cacPk) {
        this.cacPk = cacPk;
    }

    public LocalDate getCacFecha() {
        return cacFecha;
    }

    public void setCacFecha(LocalDate cacFecha) {
        this.cacFecha = cacFecha;
    }

    public Integer getCacEstudiantesPresentes() {
        return cacEstudiantesPresentes;
    }

    public void setCacEstudiantesPresentes(Integer cacEstudiantesPresentes) {
        this.cacEstudiantesPresentes = cacEstudiantesPresentes;
    }

    public Integer getCacEstudiantesEnLista() {
        return cacEstudiantesEnLista;
    }

    public void setCacEstudiantesEnLista(Integer cacEstudiantesEnLista) {
        this.cacEstudiantesEnLista = cacEstudiantesEnLista;
    }

    public Integer getCacEstudiantesAusentesJustificados() {
        return cacEstudiantesAusentesJustificados;
    }

    public void setCacEstudiantesAusentesJustificados(Integer cacEstudiantesAusentesJustificados) {
        this.cacEstudiantesAusentesJustificados = cacEstudiantesAusentesJustificados;
    }

    public Integer getCacEstudiantesAusentesSinJustificar() {
        return cacEstudiantesAusentesSinJustificar;
    }

    public void setCacEstudiantesAusentesSinJustificar(Integer cacEstudiantesAusentesSinJustificar) {
        this.cacEstudiantesAusentesSinJustificar = cacEstudiantesAusentesSinJustificar;
    }

    public LocalDateTime getCacUltModFecha() {
        return cacUltModFecha;
    }

    public void setCacUltModFecha(LocalDateTime cacUltModFecha) {
        this.cacUltModFecha = cacUltModFecha;
    }

    public String getCacUltModUsuario() {
        return cacUltModUsuario;
    }

    public void setCacUltModUsuario(String cacUltModUsuario) {
        this.cacUltModUsuario = cacUltModUsuario;
    }

    public Integer getCacVersion() {
        return cacVersion;
    }

    public void setCacVersion(Integer cacVersion) {
        this.cacVersion = cacVersion;
    }

    public SgSeccion getCacSeccion() {
        return cacSeccion;
    }

    public void setCacSeccion(SgSeccion cacSeccion) {
        this.cacSeccion = cacSeccion;
    }

    public List<SgAsistencia> getCacAsistencia() {
        return cacAsistencia;
    }

    public void setCacAsistencia(List<SgAsistencia> cacAsistencia) {
        this.cacAsistencia = cacAsistencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cacPk != null ? cacPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgControlAsistenciaCabezal)) {
            return false;
        }
        SgControlAsistenciaCabezal other = (SgControlAsistenciaCabezal) object;
        if ((this.cacPk == null && other.cacPk != null) || (this.cacPk != null && !this.cacPk.equals(other.cacPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaCabezal[ cacPk=" + cacPk + " ]";
    }

}
