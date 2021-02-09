/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.EnumEstadoImportado;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "abmPk", scope = SgArchivoBitMatricula.class)
public class SgArchivoBitMatricula implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long abmPk;
    
    private SgSede abmSede;
    
    private Integer abmDiaIngreso;
    
    private Integer abmMesIngreso;
    
    private Integer abmDiaEgreso;
    
    private Integer abmMesEgreso;
    
    private SgArchivo abmArchivo;

    private EnumEstadoImportado abmEstado;
    
    private String abmDescripcion; 
    
    private LocalDateTime abmUltModFecha;
    
    private String abmUltModUsuario;
    
    private Integer abmVersion;

    public SgArchivoBitMatricula() {
    }

    public SgArchivoBitMatricula(Long abmPk) {
        this.abmPk = abmPk;
    }

    public Long getAbmPk() {
        return abmPk;
    }

    public void setAbmPk(Long abmPk) {
        this.abmPk = abmPk;
    }

    public SgSede getAbmSede() {
        return abmSede;
    }

    public void setAbmSede(SgSede abmSede) {
        this.abmSede = abmSede;
    }

    public Integer getAbmDiaIngreso() {
        return abmDiaIngreso;
    }

    public void setAbmDiaIngreso(Integer abmDiaIngreso) {
        this.abmDiaIngreso = abmDiaIngreso;
    }

    public Integer getAbmMesIngreso() {
        return abmMesIngreso;
    }

    public void setAbmMesIngreso(Integer abmMesIngreso) {
        this.abmMesIngreso = abmMesIngreso;
    }

    public Integer getAbmDiaEgreso() {
        return abmDiaEgreso;
    }

    public void setAbmDiaEgreso(Integer abmDiaEgreso) {
        this.abmDiaEgreso = abmDiaEgreso;
    }

    public Integer getAbmMesEgreso() {
        return abmMesEgreso;
    }

    public void setAbmMesEgreso(Integer abmMesEgreso) {
        this.abmMesEgreso = abmMesEgreso;
    }

    public SgArchivo getAbmArchivo() {
        return abmArchivo;
    }

    public void setAbmArchivo(SgArchivo abmArchivo) {
        this.abmArchivo = abmArchivo;
    }

    public EnumEstadoImportado getAbmEstado() {
        return abmEstado;
    }

    public void setAbmEstado(EnumEstadoImportado abmEstado) {
        this.abmEstado = abmEstado;
    }

    public String getAbmDescripcion() {
        return abmDescripcion;
    }

    public void setAbmDescripcion(String abmDescripcion) {
        this.abmDescripcion = abmDescripcion;
    }

    public LocalDateTime getAbmUltModFecha() {
        return abmUltModFecha;
    }

    public void setAbmUltModFecha(LocalDateTime abmUltModFecha) {
        this.abmUltModFecha = abmUltModFecha;
    }

    public String getAbmUltModUsuario() {
        return abmUltModUsuario;
    }

    public void setAbmUltModUsuario(String abmUltModUsuario) {
        this.abmUltModUsuario = abmUltModUsuario;
    }

    public Integer getAbmVersion() {
        return abmVersion;
    }

    public void setAbmVersion(Integer abmVersion) {
        this.abmVersion = abmVersion;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (abmPk != null ? abmPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgArchivoBitMatricula)) {
            return false;
        }
        SgArchivoBitMatricula other = (SgArchivoBitMatricula) object;
        if ((this.abmPk == null && other.abmPk != null) || (this.abmPk != null && !this.abmPk.equals(other.abmPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgArchivoBitMatricula[ abmPk=" + abmPk + " ]";
    }
    
}
