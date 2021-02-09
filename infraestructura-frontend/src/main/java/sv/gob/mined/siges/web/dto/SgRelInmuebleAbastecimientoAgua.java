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
import sv.gob.mined.siges.web.dto.catalogo.SgInfTratamientoAgua;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "iaaPk", scope = SgRelInmuebleAbastecimientoAgua.class)
public class SgRelInmuebleAbastecimientoAgua implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long iaaPk;

    private Integer iaaBueno;

    private Integer iaaMalo;

    private Integer iaaRegular;

    private String iaaDescripcion;

    private LocalDateTime iaaUltModFecha;

    private String iaaUltModUsuario;

    private Integer iaaVersion;

    private SgInmueblesSedes iaaInmuebleSede;
    
    private SgInfTratamientoAgua iaaAbastecimientoAgua;
    
    
    public SgRelInmuebleAbastecimientoAgua() {
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(iaaBueno!=null?iaaBueno:0);
        total=total+(iaaRegular!=null?iaaRegular:0);
        total=total+(iaaMalo!=null?iaaMalo:0);
        return total;
    }

    public Long getIaaPk() {
        return iaaPk;
    }

    public void setIaaPk(Long iaaPk) {
        this.iaaPk = iaaPk;
    }

    public Integer getIaaBueno() {
        return iaaBueno;
    }

    public void setIaaBueno(Integer iaaBueno) {
        this.iaaBueno = iaaBueno;
    }

    public Integer getIaaMalo() {
        return iaaMalo;
    }

    public void setIaaMalo(Integer iaaMalo) {
        this.iaaMalo = iaaMalo;
    }

    public Integer getIaaRegular() {
        return iaaRegular;
    }

    public void setIaaRegular(Integer iaaRegular) {
        this.iaaRegular = iaaRegular;
    }

    public String getIaaDescripcion() {
        return iaaDescripcion;
    }

    public void setIaaDescripcion(String iaaDescripcion) {
        this.iaaDescripcion = iaaDescripcion;
    }

  
    public LocalDateTime getIaaUltModFecha() {
        return iaaUltModFecha;
    }

    public void setIaaUltModFecha(LocalDateTime iaaUltModFecha) {
        this.iaaUltModFecha = iaaUltModFecha;
    }

    public String getIaaUltModUsuario() {
        return iaaUltModUsuario;
    }

    public void setIaaUltModUsuario(String iaaUltModUsuario) {
        this.iaaUltModUsuario = iaaUltModUsuario;
    }

    public Integer getIaaVersion() {
        return iaaVersion;
    }

    public void setIaaVersion(Integer iaaVersion) {
        this.iaaVersion = iaaVersion;
    }
    
    public SgInmueblesSedes getIaaInmuebleSede() {
        return iaaInmuebleSede;
    }

    public void setRisInmuebleSede(SgInmueblesSedes iaaInmuebleSede) {
        this.iaaInmuebleSede = iaaInmuebleSede;
    }

    public SgInfTratamientoAgua getIaaAbastecimientoAgua() {
        return iaaAbastecimientoAgua;
    }

    public void setIaaAbastecimientoAgua(SgInfTratamientoAgua iaaAbastecimientoAgua) {
        this.iaaAbastecimientoAgua = iaaAbastecimientoAgua;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iaaPk != null ? iaaPk.hashCode() : 0);
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
        final SgRelInmuebleAbastecimientoAgua other = (SgRelInmuebleAbastecimientoAgua) obj;
        if (!Objects.equals(this.iaaPk, other.iaaPk)) {
            return false;
        }
        if (!Objects.equals(this.iaaInmuebleSede, other.iaaInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.iaaAbastecimientoAgua, other.iaaAbastecimientoAgua)) {
            return false;
        }
        return true;
    }

   
	


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleAbastecimientoAgua[ iaaPk=" + iaaPk + " ]";
    }
    
}
