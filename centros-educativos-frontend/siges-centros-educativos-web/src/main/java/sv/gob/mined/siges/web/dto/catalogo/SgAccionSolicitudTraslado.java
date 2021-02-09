/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudTraslado;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "accPk", scope = SgAccionSolicitudTraslado.class) 
public class SgAccionSolicitudTraslado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long accPk;
    
    private Integer accNum;
    
    private EnumEstadoSolicitudTraslado accEstadoOrigen;
    
    private EnumEstadoSolicitudTraslado accEstadoDestino;
    
    private String accNombreAccion;
    
    private LocalDateTime accUltModFecha;
    
    private String accUltModUsuario;
    
    private Integer accVersion;

    public SgAccionSolicitudTraslado() {
    }

    public SgAccionSolicitudTraslado(Long accPk) {
        this.accPk = accPk;
    }

    public Long getAccPk() {
        return accPk;
    }

    public void setAccPk(Long accPk) {
        this.accPk = accPk;
    }

    public Integer getAccNum() {
        return accNum;
    }

    public void setAccNum(Integer accNum) {
        this.accNum = accNum;
    }

    public EnumEstadoSolicitudTraslado getAccEstadoOrigen() {
        return accEstadoOrigen;
    }

    public void setAccEstadoOrigen(EnumEstadoSolicitudTraslado accEstadoOrigen) {
        this.accEstadoOrigen = accEstadoOrigen;
    }

    public EnumEstadoSolicitudTraslado getAccEstadoDestino() {
        return accEstadoDestino;
    }

    public void setAccEstadoDestino(EnumEstadoSolicitudTraslado accEstadoDestino) {
        this.accEstadoDestino = accEstadoDestino;
    }

    public String getAccNombreAccion() {
        return accNombreAccion;
    }

    public void setAccNombreAccion(String accNombreAccion) {
        this.accNombreAccion = accNombreAccion;
    }

    public LocalDateTime getAccUltModFecha() {
        return accUltModFecha;
    }

    public void setAccUltModFecha(LocalDateTime accUltModFecha) {
        this.accUltModFecha = accUltModFecha;
    }

    public String getAccUltModUsuario() {
        return accUltModUsuario;
    }

    public void setAccUltModUsuario(String accUltModUsuario) {
        this.accUltModUsuario = accUltModUsuario;
    }

    public Integer getAccVersion() {
        return accVersion;
    }

    public void setAccVersion(Integer accVersion) {
        this.accVersion = accVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accPk != null ? accPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAccionSolicitudTraslado)) {
            return false;
        }
        SgAccionSolicitudTraslado other = (SgAccionSolicitudTraslado) object;
        if ((this.accPk == null && other.accPk != null) || (this.accPk != null && !this.accPk.equals(other.accPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAccion[ accPk=" + accPk + " ]";
    }
    
}
