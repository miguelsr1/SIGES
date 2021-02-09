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
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoManejoDesechos;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "imdPk", scope = SgRelInmuebleManejoDesechos.class)
public class SgRelInmuebleManejoDesechos implements Serializable {
    
    private static final long serimdVersionUID = 1L;
	
    private Long imdPk;

    private Integer imdBueno;

    private Integer imdMalo;

    private Integer imdRegular;

    private String imdDescripcion;

    private LocalDateTime imdUltModFecha;

    private String imdUltModUsuario;

    private Integer imdVersion;

    private SgInmueblesSedes imdInmuebleSede;
    
    private SgInfTipoManejoDesechos imdManejoDesechos;
    
    public SgRelInmuebleManejoDesechos() {
 
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(imdBueno!=null?imdBueno:0);
        total=total+(imdRegular!=null?imdRegular:0);
        total=total+(imdMalo!=null?imdMalo:0);
        return total;
    }

    public Long getImdPk() {
        return imdPk;
    }

    public void setImdPk(Long imdPk) {
        this.imdPk = imdPk;
    }

    public Integer getImdBueno() {
        return imdBueno;
    }

    public void setImdBueno(Integer imdBueno) {
        this.imdBueno = imdBueno;
    }

    public Integer getImdMalo() {
        return imdMalo;
    }

    public void setImdMalo(Integer imdMalo) {
        this.imdMalo = imdMalo;
    }

    public Integer getImdRegular() {
        return imdRegular;
    }

    public void setImdRegular(Integer imdRegular) {
        this.imdRegular = imdRegular;
    }

    public String getImdDescripcion() {
        return imdDescripcion;
    }

    public void setImdDescripcion(String imdDescripcion) {
        this.imdDescripcion = imdDescripcion;
    }

  
    public LocalDateTime getImdUltModFecha() {
        return imdUltModFecha;
    }

    public void setImdUltModFecha(LocalDateTime imdUltModFecha) {
        this.imdUltModFecha = imdUltModFecha;
    }

    public String getImdUltModUsuario() {
        return imdUltModUsuario;
    }

    public void setImdUltModUsuario(String imdUltModUsuario) {
        this.imdUltModUsuario = imdUltModUsuario;
    }

    public Integer getImdVersion() {
        return imdVersion;
    }

    public void setImdVersion(Integer imdVersion) {
        this.imdVersion = imdVersion;
    }
    
    public SgInmueblesSedes getImdInmuebleSede() {
        return imdInmuebleSede;
    }

    public void setRisInmuebleSede(SgInmueblesSedes imdInmuebleSede) {
        this.imdInmuebleSede = imdInmuebleSede;
    }

    public SgInfTipoManejoDesechos getImdManejoDesechos() {
        return imdManejoDesechos;
    }

    public void setImdManejoDesechos(SgInfTipoManejoDesechos imdManejoDesechos) {
        this.imdManejoDesechos = imdManejoDesechos;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imdPk != null ? imdPk.hashCode() : 0);
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
        final SgRelInmuebleManejoDesechos other = (SgRelInmuebleManejoDesechos) obj;
        if (!Objects.equals(this.imdPk, other.imdPk)) {
            return false;
        }
        if (!Objects.equals(this.imdInmuebleSede, other.imdInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.imdManejoDesechos, other.imdManejoDesechos)) {
            return false;
        }
        return true;
    }

	
	


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleManejoDesechos[ imdPk=" + imdPk + " ]";
    }
    
}
