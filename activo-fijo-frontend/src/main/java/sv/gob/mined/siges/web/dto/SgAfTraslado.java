/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "traPk", scope = SgAfTraslado.class)
public class SgAfTraslado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long traPk;
    private String traCodigoTraslado;
    private LocalDate traFechaSolicitud;
    private LocalDateTime traFechaAutoriza;
    private LocalDate traFechaTraslado;
    private LocalDate traFechaRecepcion;
    private SgAfEstadosTraslado traTipoTrasladoFk;
    private String traNombreAutoriza;
    private String traNombreRecibe;
    private String traNombreReprAf;
    private String traCargoAutoriza;
    private String traCargoRecibe;
    private String traObservacion;
    private String[] traFechaRetonoEsperada;
    private LocalDate traFechaRetono;
    private String traUnidadDestino;
    private SgSede traSedeOrigenFk;
    private SgSede traSedeDestinoFk;
    private Integer traCorrelativo;
    private Integer traAnio;
    private SgAfUnidadesAdministrativas traUnidadAdmOrigenFk;
    private SgAfUnidadesAdministrativas traUnidadAdmDestinoFk;
    private SgAfEstadosBienes traEstadoFk;
    private LocalDateTime traFechaCreacion;
    private String traUsuarioCreacion;
    private LocalDateTime traUltModFecha;
    private String traUltModUsuario;
    private Integer traVersion;
    private List<SgAfTrasladoDetalle> sgAfTrasladosDetalle;
    private List<SgAfMotivoRechazoTraslado> sgAfMotivosRechazoTraslado;
    
    private SgAfEmpleados traEmpleadoBienes;
    private String traAsignadoABienes;
    
    public SgAfTraslado() {
        this.sgAfTrasladosDetalle = new ArrayList();
        this.sgAfMotivosRechazoTraslado = new ArrayList();
    }

    @JsonIgnore
    public String getTraNombreUnidadAdministrativaOrigen() {
        //if(traTipoTrasladoFk != null && traTipoTrasladoFk.getEtrCodigo() != null && traTipoTrasladoFk.getEtrCodigo().trim().equals(Constantes.CODIGO_TIPO_TRASLADO_DEFINITIVO)) {
            if(traUnidadAdmOrigenFk != null && traUnidadAdmOrigenFk.getUadPk() != null) {
                return traUnidadAdmOrigenFk.getCodigoNombre();
            } else if(traSedeOrigenFk != null && traSedeOrigenFk.getSedPk() != null) {
                return  traSedeOrigenFk.getSedCodigoNombre();
            } else {
               return traUnidadDestino != null && StringUtils.isNotBlank(traUnidadDestino) ? traUnidadDestino.trim().toUpperCase() : "";
            }
       // } else {
         //   return traUnidadDestino != null && StringUtils.isNotBlank(traUnidadDestino) ? traUnidadDestino.trim().toUpperCase() : "";
        //}
        
       // return "";
    }
    
    public String getTraNombreUnidadAdministrativaDestino() {
        if(traUnidadAdmDestinoFk != null && traUnidadAdmDestinoFk.getUadPk() != null) {
            return traUnidadAdmDestinoFk.getCodigoNombre();
        } else if(traSedeDestinoFk != null && traSedeDestinoFk.getSedPk() != null) {
            return  traSedeDestinoFk.getSedCodigoNombre();
        }
        return traUnidadDestino;
    }
    
    public Long getTraPk() {
        return traPk;
    }

    public void setTraPk(Long traPk) {
        this.traPk = traPk;
    }

    public String getTraCodigoTraslado() {
        return traCodigoTraslado;
    }

    public void setTraCodigoTraslado(String traCodigoTraslado) {
        this.traCodigoTraslado = traCodigoTraslado;
    }

    public LocalDate getTraFechaSolicitud() {
        return traFechaSolicitud;
    }

    public void setTraFechaSolicitud(LocalDate traFechaSolicitud) {
        this.traFechaSolicitud = traFechaSolicitud;
    }

    public LocalDateTime getTraFechaAutoriza() {
        return traFechaAutoriza;
    }

    public void setTraFechaAutoriza(LocalDateTime traFechaAutoriza) {
        this.traFechaAutoriza = traFechaAutoriza;
    }

    public LocalDate getTraFechaTraslado() {
        return traFechaTraslado;
    }

    public void setTraFechaTraslado(LocalDate traFechaTraslado) {
        this.traFechaTraslado = traFechaTraslado;
    }

    public LocalDate getTraFechaRecepcion() {
        return traFechaRecepcion;
    }

    public void setTraFechaRecepcion(LocalDate traFechaRecepcion) {
        this.traFechaRecepcion = traFechaRecepcion;
    }

    public SgAfEstadosTraslado getTraTipoTrasladoFk() {
        return traTipoTrasladoFk;
    }

    public void setTraTipoTrasladoFk(SgAfEstadosTraslado traTipoTrasladoFk) {
        this.traTipoTrasladoFk = traTipoTrasladoFk;
    }

    public String getTraNombreAutoriza() {
        return traNombreAutoriza;
    }

    public void setTraNombreAutoriza(String traNombreAutoriza) {
        this.traNombreAutoriza = traNombreAutoriza;
    }

    public String getTraNombreRecibe() {
        return traNombreRecibe;
    }

    public void setTraNombreRecibe(String traNombreRecibe) {
        this.traNombreRecibe = traNombreRecibe;
    }

    public String getTraNombreReprAf() {
        return traNombreReprAf;
    }

    public void setTraNombreReprAf(String traNombreReprAf) {
        this.traNombreReprAf = traNombreReprAf;
    }

    public String getTraCargoAutoriza() {
        return traCargoAutoriza;
    }

    public void setTraCargoAutoriza(String traCargoAutoriza) {
        this.traCargoAutoriza = traCargoAutoriza;
    }

    public String getTraCargoRecibe() {
        return traCargoRecibe;
    }

    public void setTraCargoRecibe(String traCargoRecibe) {
        this.traCargoRecibe = traCargoRecibe;
    }

    public String getTraObservacion() {
        return traObservacion;
    }

    public void setTraObservacion(String traObservacion) {
        this.traObservacion = traObservacion;
    }

    public SgSede getTraSedeOrigenFk() {
        return traSedeOrigenFk;
    }

    public void setTraSedeOrigenFk(SgSede traSedeOrigenFk) {
        this.traSedeOrigenFk = traSedeOrigenFk;
    }

    public SgSede getTraSedeDestinoFk() {
        return traSedeDestinoFk;
    }

    public void setTraSedeDestinoFk(SgSede traSedeDestinoFk) {
        this.traSedeDestinoFk = traSedeDestinoFk;
    }

    public SgAfUnidadesAdministrativas getTraUnidadAdmOrigenFk() {
        return traUnidadAdmOrigenFk;
    }

    public void setTraUnidadAdmOrigenFk(SgAfUnidadesAdministrativas traUnidadAdmOrigenFk) {
        this.traUnidadAdmOrigenFk = traUnidadAdmOrigenFk;
    }

    public SgAfUnidadesAdministrativas getTraUnidadAdmDestinoFk() {
        return traUnidadAdmDestinoFk;
    }

    public void setTraUnidadAdmDestinoFk(SgAfUnidadesAdministrativas traUnidadAdmDestinoFk) {
        this.traUnidadAdmDestinoFk = traUnidadAdmDestinoFk;
    }

    public SgAfEstadosBienes getTraEstadoFk() {
        return traEstadoFk;
    }

    public void setTraEstadoFk(SgAfEstadosBienes traEstadoFk) {
        this.traEstadoFk = traEstadoFk;
    }

    public LocalDateTime getTraFechaCreacion() {
        return traFechaCreacion;
    }

    public void setTraFechaCreacion(LocalDateTime traFechaCreacion) {
        this.traFechaCreacion = traFechaCreacion;
    }

    public String getTraUsuarioCreacion() {
        return traUsuarioCreacion;
    }

    public void setTraUsuarioCreacion(String traUsuarioCreacion) {
        this.traUsuarioCreacion = traUsuarioCreacion;
    }

    public LocalDateTime getTraUltModFecha() {
        return traUltModFecha;
    }

    public void setTraUltModFecha(LocalDateTime traUltModFecha) {
        this.traUltModFecha = traUltModFecha;
    }

    public String getTraUltModUsuario() {
        return traUltModUsuario;
    }

    public void setTraUltModUsuario(String traUltModUsuario) {
        this.traUltModUsuario = traUltModUsuario;
    }

    public Integer getTraVersion() {
        return traVersion;
    }

    public void setTraVersion(Integer traVersion) {
        this.traVersion = traVersion;
    }

    public List<SgAfTrasladoDetalle> getSgAfTrasladosDetalle() {
        return sgAfTrasladosDetalle;
    }

    public void setSgAfTrasladosDetalle(List<SgAfTrasladoDetalle> sgAfTrasladosDetalle) {
        this.sgAfTrasladosDetalle = sgAfTrasladosDetalle;
    }

    public String getTraUnidadDestino() {
        return traUnidadDestino;
    }

    public void setTraUnidadDestino(String traUnidadDestino) {
        this.traUnidadDestino = traUnidadDestino;
    }

    public LocalDate getTraFechaRetono() {
        return traFechaRetono;
    }

    public void setTraFechaRetono(LocalDate traFechaRetono) {
        this.traFechaRetono = traFechaRetono;
    }

    public String[] getTraFechaRetonoEsperada() {
        return traFechaRetonoEsperada;
    }

    public void setTraFechaRetonoEsperada(String[] traFechaRetonoEsperada) {
        this.traFechaRetonoEsperada = traFechaRetonoEsperada;
    }

    public List<SgAfMotivoRechazoTraslado> getSgAfMotivosRechazoTraslado() {
        return sgAfMotivosRechazoTraslado;
    }

    public void setSgAfMotivosRechazoTraslado(List<SgAfMotivoRechazoTraslado> sgAfMotivosRechazoTraslado) {
        this.sgAfMotivosRechazoTraslado = sgAfMotivosRechazoTraslado;
    }

    public Integer getTraCorrelativo() {
        return traCorrelativo;
    }

    public void setTraCorrelativo(Integer traCorrelativo) {
        this.traCorrelativo = traCorrelativo;
    }

    public Integer getTraAnio() {
        return traAnio;
    }

    public void setTraAnio(Integer traAnio) {
        this.traAnio = traAnio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traPk != null ? traPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfTraslado)) {
            return false;
        }
        SgAfTraslado other = (SgAfTraslado) object;
        if ((this.traPk == null && other.traPk != null) || (this.traPk != null && !this.traPk.equals(other.traPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfTraslados[ traPk=" + traPk + " ]";
    }

    public LocalDate getTraFechaMaximaRetornoBienes() {
        if(this.traFechaTraslado != null) {
            return this.traFechaTraslado.plusMonths(1); //1 mex maximo después de la fecha de traslado
        }
        return LocalDate.now();
    }

    public SgAfEmpleados getTraEmpleadoBienes() {
        return traEmpleadoBienes;
    }

    public void setTraEmpleadoBienes(SgAfEmpleados traEmpleadoBienes) {
        this.traEmpleadoBienes = traEmpleadoBienes;
    }

    public String getTraAsignadoABienes() {
        return traAsignadoABienes;
    }

    public void setTraAsignadoABienes(String traAsignadoABienes) {
        this.traAsignadoABienes = traAsignadoABienes;
    }
    
}
