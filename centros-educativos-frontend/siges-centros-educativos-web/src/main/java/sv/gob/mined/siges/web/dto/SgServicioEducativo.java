/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.enumerados.EnumServicioEducativoEstado;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sduPk", scope = SgServicioEducativo.class)
public class SgServicioEducativo implements Serializable {

    private Long sduPk;

    private SgGrado sduGrado;

    private EnumServicioEducativoEstado sduEstado;

    private LocalDate sduFechaHabilitado;

    private LocalDate sduFechaSolicitada;

    private String sduNumeroTramite;

    private SgSede sduSede;

    private LocalDateTime sduUltModFecha;

    private String sduUltModUsuario;

    private Integer sduVersion;

    private SgOpcion sduOpcion;

    private SgProgramaEducativo sduProgramaEducativo;

    private List<SgSeccion> sduSeccion;

    public SgServicioEducativo() {
    }

    public SgServicioEducativo(Long sduPk) {
        this.sduPk = sduPk;
    }

    @JsonIgnore
    public String getSduNombre() {
        StringBuilder s = new StringBuilder();
        s.append(this.sduGrado.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivNombre()).append(" - ");
        s.append(this.sduGrado.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNombre()).append(" - ");
        s.append(this.sduGrado.getGraNombre());
        return SofisStringUtils.normalizarString(s.toString());
    }
    
    @JsonIgnore
    public String listaSecciones(Long anio){
        StringBuilder lista = new StringBuilder();
        if(sduSeccion != null){
            for(SgSeccion sec: sduSeccion){
                if(sec.getSecAnioLectivo()!=null){
                    if((anio != null && sec.getSecAnioLectivo().getAlePk().equals(anio)) || anio == null){
                        lista.append(sec.getSecAnioLectivo().getAleAnio()).append("-");

                        lista.append(sec.getSecNombre());
                        if(sec.getSecJornadaLaboral()!=null){
                            lista.append("-").append(sec.getSecJornadaLaboral().getJlaNombre());
                        }
                        lista.append(System.lineSeparator());
                    }
                }
            }
        }
        return lista.toString();
    }

    public Long getSduPk() {
        return sduPk;
    }

    public void setSduPk(Long sduPk) {
        this.sduPk = sduPk;
    }

    public EnumServicioEducativoEstado getSduEstado() {
        return sduEstado;
    }

    public void setSduEstado(EnumServicioEducativoEstado sduEstado) {
        this.sduEstado = sduEstado;
    }

    public LocalDate getSduFechaHabilitado() {
        return sduFechaHabilitado;
    }

    public void setSduFechaHabilitado(LocalDate sduFechaHabilitado) {
        this.sduFechaHabilitado = sduFechaHabilitado;
    }

    public LocalDate getSduFechaSolicitada() {
        return sduFechaSolicitada;
    }

    public void setSduFechaSolicitada(LocalDate sduFechaSolicitada) {
        this.sduFechaSolicitada = sduFechaSolicitada;
    }

    public String getSduNumeroTramite() {
        return sduNumeroTramite;
    }

    public void setSduNumeroTramite(String sduNumeroTramite) {
        this.sduNumeroTramite = sduNumeroTramite;
    }

    public LocalDateTime getSduUltModFecha() {
        return sduUltModFecha;
    }

    public void setSduUltModFecha(LocalDateTime sduUltModFecha) {
        this.sduUltModFecha = sduUltModFecha;
    }

    public String getSduUltModUsuario() {
        return sduUltModUsuario;
    }

    public void setSduUltModUsuario(String sduUltModUsuario) {
        this.sduUltModUsuario = sduUltModUsuario;
    }

    public Integer getSduVersion() {
        return sduVersion;
    }

    public void setSduVersion(Integer sduVersion) {
        this.sduVersion = sduVersion;
    }

    public SgGrado getSduGrado() {
        return sduGrado;
    }

    public void setSduGrado(SgGrado sduGrado) {
        this.sduGrado = sduGrado;
    }

    public SgSede getSduSede() {
        return sduSede;
    }

    public void setSduSede(SgSede sduSede) {
        this.sduSede = sduSede;
    }

    public SgOpcion getSduOpcion() {
        return sduOpcion;
    }

    public void setSduOpcion(SgOpcion sduOpcion) {
        this.sduOpcion = sduOpcion;
    }

    public SgProgramaEducativo getSduProgramaEducativo() {
        return sduProgramaEducativo;
    }

    public void setSduProgramaEducativo(SgProgramaEducativo sduProgramaEducativo) {
        this.sduProgramaEducativo = sduProgramaEducativo;
    }

    public List<SgSeccion> getSduSeccion() {
        return sduSeccion;
    }

    public void setSduSeccion(List<SgSeccion> sduSeccion) {
        this.sduSeccion = sduSeccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sduPk != null ? sduPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgServicioEducativo)) {
            return false;
        }
        SgServicioEducativo other = (SgServicioEducativo) object;
        if ((this.sduPk == null && other.sduPk != null) || (this.sduPk != null && !this.sduPk.equals(other.sduPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgServicioEducativo[ sduPk=" + sduPk + " ]";
    }

}
