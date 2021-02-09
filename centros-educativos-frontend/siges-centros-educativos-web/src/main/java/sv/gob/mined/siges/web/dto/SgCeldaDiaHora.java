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
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumCeldaDiaHora;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdhPk", scope = SgCeldaDiaHora.class)
public class SgCeldaDiaHora implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cdhPk;

    private EnumCeldaDiaHora cdhDia;

    private Integer hesHora;

    private LocalDateTime cdhUltModFecha;

    private String cdhUltModUsuario;

    private Integer cdhVersion;

    private SgComponentePlanGrado cdhComponentePlanGrado;

    private SgHorarioEscolar cdhHorarioEscolar;

    public SgCeldaDiaHora() {

    }

    public Long getCdhPk() {
        return cdhPk;
    }

    public void setCdhPk(Long cdhPk) {
        this.cdhPk = cdhPk;
    }

    public EnumCeldaDiaHora getCdhDia() {
        return cdhDia;
    }

    public void setCdhDia(EnumCeldaDiaHora cdhDia) {
        this.cdhDia = cdhDia;
    }

    public Integer getHesHora() {
        return hesHora;
    }

    public void setHesHora(Integer hesHora) {
        this.hesHora = hesHora;
    }

    public SgComponentePlanGrado getCdhComponentePlanGrado() {
        return cdhComponentePlanGrado;
    }

    public void setCdhComponentePlanGrado(SgComponentePlanGrado cdhComponentePlanGrado) {
        this.cdhComponentePlanGrado = cdhComponentePlanGrado;
    }

    public LocalDateTime getCdhUltModFecha() {
        return cdhUltModFecha;
    }

    public void setCdhUltModFecha(LocalDateTime cdhUltModFecha) {
        this.cdhUltModFecha = cdhUltModFecha;
    }

    public String getCdhUltModUsuario() {
        return cdhUltModUsuario;
    }

    public void setCdhUltModUsuario(String cdhUltModUsuario) {
        this.cdhUltModUsuario = cdhUltModUsuario;
    }

    public Integer getCdhVersion() {
        return cdhVersion;
    }

    public void setCdhVersion(Integer cdhVersion) {
        this.cdhVersion = cdhVersion;
    }

    public SgHorarioEscolar getCdhHorarioEscolar() {
        return cdhHorarioEscolar;
    }

    public void setCdhHorarioEscolar(SgHorarioEscolar cdhHorarioEscolar) {
        this.cdhHorarioEscolar = cdhHorarioEscolar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdhPk != null ? cdhPk.hashCode() : 0);
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
        final SgCeldaDiaHora other = (SgCeldaDiaHora) obj;
        if (!Objects.equals(this.cdhPk, other.cdhPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCeldaDiaHora[ cdhPk=" + cdhPk + " ]";
    }

}
