/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "retPk", scope = SgRelEdificioServicioSanitario.class)
public class SgRelEdificioServicioSanitario implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long retPk;

    private Integer retBueno;
 
    private Integer retMalo;

    private Integer retRegular;
 
    private Integer retNinos;

    private Integer retNinas;

    private Integer retMaestros;

    private Integer retAdministrativos;

    private LocalDateTime retUltModFecha;

    private String retUltModUsuario;

    private Integer retVersion;

    private SgEdificio retEdificio;

    private SgTipoServicioSanitario retServicioSanitario;
    
    
    public SgRelEdificioServicioSanitario() {
    }
    
    @JsonIgnore
    public Integer getTotalCondiciones(){
        Integer total=0;
        total=total+(retBueno!=null?retBueno:0);
        total=total+(retRegular!=null?retRegular:0);
        total=total+(retMalo!=null?retMalo:0);
        return total;
    }
    
    @JsonIgnore
    public Integer getTotalTipoUsuario(){
        Integer total=0;
        total=total+(retNinos!=null?retNinos:0);
        total=total+(retNinas!=null?retNinas:0);
        total=total+(retMaestros!=null?retMaestros:0);
        total=total+(retAdministrativos!=null?retAdministrativos:0);
        return total;
    }


    public Long getRetPk() {
        return retPk;
    }

    public void setRetPk(Long retPk) {
        this.retPk = retPk;
    }

    public Integer getRetBueno() {
        return retBueno;
    }

    public void setRetBueno(Integer retBueno) {
        this.retBueno = retBueno;
    }

    public Integer getRetMalo() {
        return retMalo;
    }

    public void setRetMalo(Integer retMalo) {
        this.retMalo = retMalo;
    }

    public Integer getRetRegular() {
        return retRegular;
    }

    public void setRetRegular(Integer retRegular) {
        this.retRegular = retRegular;
    }

    public Integer getRetNinos() {
        return retNinos;
    }

    public void setRetNinos(Integer retNinos) {
        this.retNinos = retNinos;
    }

    public Integer getRetNinas() {
        return retNinas;
    }

    public void setRetNinas(Integer retNinas) {
        this.retNinas = retNinas;
    }

    public Integer getRetMaestros() {
        return retMaestros;
    }

    public void setRetMaestros(Integer retMaestros) {
        this.retMaestros = retMaestros;
    }

    public Integer getRetAdministrativos() {
        return retAdministrativos;
    }

    public void setRetAdministrativos(Integer retAdministrativos) {
        this.retAdministrativos = retAdministrativos;
    }

    public LocalDateTime getRetUltModFecha() {
        return retUltModFecha;
    }

    public void setRetUltModFecha(LocalDateTime retUltModFecha) {
        this.retUltModFecha = retUltModFecha;
    }

    public String getRetUltModUsuario() {
        return retUltModUsuario;
    }

    public void setRetUltModUsuario(String retUltModUsuario) {
        this.retUltModUsuario = retUltModUsuario;
    }

    public Integer getRetVersion() {
        return retVersion;
    }

    public void setRetVersion(Integer retVersion) {
        this.retVersion = retVersion;
    }

    public SgEdificio getRetEdificio() {
        return retEdificio;
    }

    public void setRetEdificio(SgEdificio retEdificio) {
        this.retEdificio = retEdificio;
    }

    public SgTipoServicioSanitario getRetServicioSanitario() {
        return retServicioSanitario;
    }

    public void setRetServicioSanitario(SgTipoServicioSanitario retServicioSanitario) {
        this.retServicioSanitario = retServicioSanitario;
    }

    

   


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (retPk != null ? retPk.hashCode() : 0);
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
        final SgRelEdificioServicioSanitario other = (SgRelEdificioServicioSanitario) obj;
        if (!Objects.equals(this.retPk, other.retPk)) {
            return false;
        }
        if (!Objects.equals(this.retEdificio, other.retEdificio)) {
            return false;
        }
        if (!Objects.equals(this.retServicioSanitario, other.retServicioSanitario)) {
            return false;
        }
        return true;
    }
	


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelEdificioServicio[ resPk=" + retPk + " ]";
    }
    
}
