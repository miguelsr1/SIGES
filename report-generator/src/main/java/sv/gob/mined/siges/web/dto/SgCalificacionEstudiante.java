/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.utils.SpanishConverter;
import sv.gob.mined.siges.web.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumResolucionCalificacion;
import sv.gob.mined.siges.web.utilidades.NumberFormatUtils;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "caePk", scope = SgCalificacionEstudiante.class)
public class SgCalificacionEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long caePk;

    private SgCalificacionCE caeCalificacion;

    private SgEstudiante caeEstudiante;

    private String caeCalificacionNumericaEstudiante;

    private EnumPromovidoCalificacion caePromovidoCalificacion;

    private SgCalificacion caeCalificacionConceptualEstudiante;

    private EnumResolucionCalificacion caeResolucion;

    private String caeObservacion;

    private LocalDate caeFechaRealizado;

    private LocalDateTime caeUltModFecha;

    private String caeUltModUsuario;

    private Integer caeVersion;

    public SgCalificacionEstudiante() {

    }

    public String getCaeCalificacionValor(Integer precision) {
        if (caeCalificacionConceptualEstudiante != null) {
            return caeCalificacionConceptualEstudiante.getCalValor();
        }
        if (!StringUtils.isBlank(caeCalificacionNumericaEstudiante)) {
            Double d = Double.parseDouble(caeCalificacionNumericaEstudiante);
            return NumberFormatUtils.formatDouble(d, precision);
        }
        return "";
    }


    public String getCaeCalificacionValorPromocion() {
        if (caeCalificacionConceptualEstudiante != null) {
            return caeCalificacionConceptualEstudiante.getCalValor();
        }
        if (!StringUtils.isBlank(caeCalificacionNumericaEstudiante)) {
            Double notaPromocion = Double.parseDouble(caeCalificacionNumericaEstudiante);
            DecimalFormat df = new DecimalFormat("#");
            String nota = df.format(notaPromocion);
            return nota;
        }
        return "";
    }

    public String getCaeCalificacionFormatoEscrito() {
        if (caeCalificacionConceptualEstudiante != null) {
            return !StringUtils.isBlank(caeCalificacionConceptualEstudiante.getCalValorEnCertificado()) ? caeCalificacionConceptualEstudiante.getCalValorEnCertificado() : null;
        } else {
            Double notaPromocion = Double.parseDouble(caeCalificacionNumericaEstudiante);
            DecimalFormat df = new DecimalFormat("#");
            SpanishConverter sc = new SpanishConverter();
            String nota = df.format(notaPromocion);
            return sc.convert(Long.parseLong(nota)).toUpperCase();
        }
    }

    public Long getCaePk() {
        return caePk;
    }

    public void setCaePk(Long caePk) {
        this.caePk = caePk;
    }

    public SgCalificacionCE getCaeCalificacion() {
        return caeCalificacion;
    }

    public void setCaeCalificacion(SgCalificacionCE caeCalificacion) {
        this.caeCalificacion = caeCalificacion;
    }

    public SgEstudiante getCaeEstudiante() {
        return caeEstudiante;
    }

    public void setCaeEstudiante(SgEstudiante caeEstudiante) {
        this.caeEstudiante = caeEstudiante;
    }

    public String getCaeCalificacionNumericaEstudiante() {
        return caeCalificacionNumericaEstudiante;
    }

    public void setCaeCalificacionNumericaEstudiante(String caeCalificacionNumericaEstudiante) {
        this.caeCalificacionNumericaEstudiante = caeCalificacionNumericaEstudiante;
    }

    public SgCalificacion getCaeCalificacionConceptualEstudiante() {
        return caeCalificacionConceptualEstudiante;
    }

    public void setCaeCalificacionConceptualEstudiante(SgCalificacion caeCalificacionConceptualEstudiante) {
        this.caeCalificacionConceptualEstudiante = caeCalificacionConceptualEstudiante;
    }

    public String getCaeObservacion() {
        return caeObservacion;
    }

    public void setCaeObservacion(String caeObservacion) {
        this.caeObservacion = caeObservacion;
    }

    public LocalDate getCaeFechaRealizado() {
        return caeFechaRealizado;
    }

    public void setCaeFechaRealizado(LocalDate caeFechaRealizado) {
        this.caeFechaRealizado = caeFechaRealizado;
    }

    public LocalDateTime getCaeUltModFecha() {
        return caeUltModFecha;
    }

    public void setCaeUltModFecha(LocalDateTime caeUltModFecha) {
        this.caeUltModFecha = caeUltModFecha;
    }

    public String getCaeUltModUsuario() {
        return caeUltModUsuario;
    }

    public void setCaeUltModUsuario(String caeUltModUsuario) {
        this.caeUltModUsuario = caeUltModUsuario;
    }

    public Integer getCaeVersion() {
        return caeVersion;
    }

    public void setCaeVersion(Integer caeVersion) {
        this.caeVersion = caeVersion;
    }

    public EnumResolucionCalificacion getCaeResolucion() {
        return caeResolucion;
    }

    public void setCaeResolucion(EnumResolucionCalificacion caeResolucion) {
        this.caeResolucion = caeResolucion;
    }

    public EnumPromovidoCalificacion getCaePromovidoCalificacion() {
        return caePromovidoCalificacion;
    }

    public void setCaePromovidoCalificacion(EnumPromovidoCalificacion caePromovidoCalificacion) {
        this.caePromovidoCalificacion = caePromovidoCalificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (caePk != null ? caePk.hashCode() : 0);
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
        final SgCalificacionEstudiante other = (SgCalificacionEstudiante) obj;
        if (!Objects.equals(this.caePk, other.caePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCalificacionEstudiante[ caePk=" + caePk + " ]";
    }

}
