/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgGradoRiesgo;
import sv.gob.mined.siges.web.dto.catalogo.SgTiposRiesgo;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "friPk", scope = SgFactorRiesgoSede.class)
public class SgFactorRiesgoSede implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long friPk;
    
    private SgSede friSede;
    
    private SgTiposRiesgo friTipoRiesgo;
    
    private SgGradoRiesgo friGradoRiesgo;
    
    private LocalDate friFechaInicio;
    
    private LocalDate friFechaFin;
    
    private LocalDateTime friUltModFecha;
    
    private String friUltModUsuario;
    
    private Integer friVersion;

    public SgFactorRiesgoSede() {
    }
    
    public String getFriPeriodo(){
        return this.friFechaInicio+" - "+this.friFechaFin;
    }

    public Long getFriPk() {
        return friPk;
    }

    public void setFriPk(Long friPk) {
        this.friPk = friPk;
    }

    public SgSede getFriSede() {
        return friSede;
    }

    public void setFriSede(SgSede friSede) {
        this.friSede = friSede;
    }

    public SgTiposRiesgo getFriTipoRiesgo() {
        return friTipoRiesgo;
    }

    public void setFriTipoRiesgo(SgTiposRiesgo friTipoRiesgo) {
        this.friTipoRiesgo = friTipoRiesgo;
    }

    public SgGradoRiesgo getFriGradoRiesgo() {
        return friGradoRiesgo;
    }

    public void setFriGradoRiesgo(SgGradoRiesgo friGradoRiesgo) {
        this.friGradoRiesgo = friGradoRiesgo;
    }

    public LocalDate getFriFechaInicio() {
        return friFechaInicio;
    }

    public void setFriFechaInicio(LocalDate friFechaInicio) {
        this.friFechaInicio = friFechaInicio;
    }

    public LocalDate getFriFechaFin() {
        return friFechaFin;
    }

    public void setFriFechaFin(LocalDate friFechaFin) {
        this.friFechaFin = friFechaFin;
    }

    public LocalDateTime getFriUltModFecha() {
        return friUltModFecha;
    }

    public void setFriUltModFecha(LocalDateTime friUltModFecha) {
        this.friUltModFecha = friUltModFecha;
    }

    public String getFriUltModUsuario() {
        return friUltModUsuario;
    }

    public void setFriUltModUsuario(String friUltModUsuario) {
        this.friUltModUsuario = friUltModUsuario;
    }

    public Integer getFriVersion() {
        return friVersion;
    }

    public void setFriVersion(Integer friVersion) {
        this.friVersion = friVersion;
    }







    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.friPk);
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
        final SgFactorRiesgoSede other = (SgFactorRiesgoSede) obj;
        if (!Objects.equals(this.friPk, other.friPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgFactorRiesgoSede{" + "friPk=" + friPk +", friUltModFecha=" + friUltModFecha + ", friUltModUsuario=" + friUltModUsuario + ", friVersion=" + friVersion + '}';
    }
    
    

}
