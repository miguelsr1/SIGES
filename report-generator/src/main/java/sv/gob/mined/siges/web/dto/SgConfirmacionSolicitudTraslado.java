/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;



@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sotPk", scope = SgConfirmacionSolicitudTraslado.class)
public class SgConfirmacionSolicitudTraslado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sotPk;

    private Long sotSolicitudTrasladoPk;
    
    private LocalDate sotFechaTraslado;

    private SgSeccion sotSeccion;
    
    private String sotResolucion;

    private String sotFirmaTransactionId; 
    
    private LocalDateTime sotUltModFecha;

    private String sotUltModUsuario;
 
    private Integer sotVersion;
   


    public SgConfirmacionSolicitudTraslado() {
    }

    public SgConfirmacionSolicitudTraslado(Long sotPk) {
        this.sotPk = sotPk;
    }

    public Long getSotPk() {
        return sotPk;
    }

    public void setSotPk(Long sotPk) {
        this.sotPk = sotPk;
    }

    public Long getSotSolicitudTrasladoPk() {
        return sotSolicitudTrasladoPk;
    }

    public void setSotSolicitudTrasladoPk(Long sotSolicitudTrasladoPk) {
        this.sotSolicitudTrasladoPk = sotSolicitudTrasladoPk;
    }

    public LocalDate getSotFechaTraslado() {
        return sotFechaTraslado;
    }

    public void setSotFechaTraslado(LocalDate sotFechaTraslado) {
        this.sotFechaTraslado = sotFechaTraslado;
    }

    public SgSeccion getSotSeccion() {
        return sotSeccion;
    }

    public void setSotSeccion(SgSeccion sotSeccion) {
        this.sotSeccion = sotSeccion;
    }

    public String getSotResolucion() {
        return sotResolucion;
    }

    public void setSotResolucion(String sotResolucion) {
        this.sotResolucion = sotResolucion;
    }

    public String getSotFirmaTransactionId() {
        return sotFirmaTransactionId;
    }

    public void setSotFirmaTransactionId(String sotFirmaTransactionId) {
        this.sotFirmaTransactionId = sotFirmaTransactionId;
    }

    public Integer getSotVersion() {
        return sotVersion;
    }

    public void setSotVersion(Integer sotVersion) {
        this.sotVersion = sotVersion;
    }

    public LocalDateTime getSotUltModFecha() {
        return sotUltModFecha;
    }

    public void setSotUltModFecha(LocalDateTime sotUltModFecha) {
        this.sotUltModFecha = sotUltModFecha;
    }

    public String getSotUltModUsuario() {
        return sotUltModUsuario;
    }

    public void setSotUltModUsuario(String sotUltModUsuario) {
        this.sotUltModUsuario = sotUltModUsuario;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sotPk != null ? sotPk.hashCode() : 0);
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
        final SgConfirmacionSolicitudTraslado other = (SgConfirmacionSolicitudTraslado) obj;
        if (!Objects.equals(this.sotPk, other.sotPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudTraslado[ sotPk=" + sotPk + " ]";
    }

}
