/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoImportado;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "accPk", scope = SgArchivoCalificaciones.class)
public class SgArchivoCalificaciones implements Serializable {

    private Long accPk;

    private String accDescripcion;       

    private EnumEstadoImportado accEstado;

    private LocalDateTime accUltmodFecha;

    private String accUltmodUsuario;

    private Integer accVersion;

    private SgArchivo accArchivo; 
    
    private SgComponentePlanEstudio accComponentePlanEstudio;    
    
    private SgRangoFecha accRangoFecha;

    private Long accSeccionPk;
  
    private LocalDate accFechaCalificado;
    
    private Boolean accArchivoBorrado;
    
    private String accErrorLog; 
    
    private String accMetodoImportacion;
    
    private SgEscalaCalificacion accEscalaCalificacion;

    public SgArchivoCalificaciones() {
        accEstado=EnumEstadoImportado.NO_PROCESADO;
        accArchivoBorrado=Boolean.FALSE;
    }

    public Long getAccPk() {
        return accPk;
    }

    public void setAccPk(Long accPk) {
        this.accPk = accPk;
    }

    public String getAccDescripcion() {
        return accDescripcion;
    }

    public void setAccDescripcion(String accDescripcion) {
        this.accDescripcion = accDescripcion;
    }

    public EnumEstadoImportado getAccEstado() {
        return accEstado;
    }

    public void setAccEstado(EnumEstadoImportado accEstado) {
        this.accEstado = accEstado;
    }

    public LocalDateTime getAccUltmodFecha() {
        return accUltmodFecha;
    }

    public void setAccUltmodFecha(LocalDateTime accUltmodFecha) {
        this.accUltmodFecha = accUltmodFecha;
    }

    public String getAccUltmodUsuario() {
        return accUltmodUsuario;
    }

    public void setAccUltmodUsuario(String accUltmodUsuario) {
        this.accUltmodUsuario = accUltmodUsuario;
    }

    public Integer getAccVersion() {
        return accVersion;
    }

    public void setAccVersion(Integer accVersion) {
        this.accVersion = accVersion;
    }

    public SgArchivo getAccArchivo() {
        return accArchivo;
    }

    public void setAccArchivo(SgArchivo accArchivo) {
        this.accArchivo = accArchivo;
    }

    public SgComponentePlanEstudio getAccComponentePlanEstudio() {
        return accComponentePlanEstudio;
    }

    public void setAccComponentePlanEstudio(SgComponentePlanEstudio accComponentePlanEstudio) {
        this.accComponentePlanEstudio = accComponentePlanEstudio;
    }

    public SgRangoFecha getAccRangoFecha() {
        return accRangoFecha;
    }

    public void setAccRangoFecha(SgRangoFecha accRangoFecha) {
        this.accRangoFecha = accRangoFecha;
    }

    public Long getAccSeccionPk() {
        return accSeccionPk;
    }

    public void setAccSeccionPk(Long accSeccionPk) {
        this.accSeccionPk = accSeccionPk;
    }
    
    

    public SgEscalaCalificacion getAccEscalaCalificacion() {
        return accEscalaCalificacion;
    }

    public void setAccEscalaCalificacion(SgEscalaCalificacion accEscalaCalificacion) {
        this.accEscalaCalificacion = accEscalaCalificacion;
    }

    public Boolean getAccArchivoBorrado() {
        return accArchivoBorrado;
    }

    public void setAccArchivoBorrado(Boolean accArchivoBorrado) {
        this.accArchivoBorrado = accArchivoBorrado;
    }

    public String getAccErrorLog() {
        return accErrorLog;
    }

    public void setAccErrorLog(String accErrorLog) {
        this.accErrorLog = accErrorLog;
    }

    public LocalDate getAccFechaCalificado() {
        return accFechaCalificado;
    }

    public void setAccFechaCalificado(LocalDate accFechaCalificado) {
        this.accFechaCalificado = accFechaCalificado;
    }

    public String getAccMetodoImportacion() {
        return accMetodoImportacion;
    }

    public void setAccMetodoImportacion(String accMetodoImportacion) {
        this.accMetodoImportacion = accMetodoImportacion;
    }

    @Override
    public String toString() {
        return "SgArchivoCalificaciones{" + "accPk=" + accPk + ", accDescripcion=" + accDescripcion + ", accEstado=" + accEstado + ", accUltmodFecha=" + accUltmodFecha + ", accUltmodUsuario=" + accUltmodUsuario + ", accVersion=" + accVersion + ", accArchivo=" + accArchivo + '}';
    }

 
}
