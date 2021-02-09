/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoImportado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_archivo_calificaciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "accPk", scope = SgArchivoCalificaciones.class)
public class SgArchivoCalificaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "acc_pk", nullable = false)
    private Long accPk;
   
    
    @Column(name = "acc_descripcion")
    private String accDescripcion;   
    
    @Column(name = "acc_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoImportado accEstado;

    @AtributoUltimaModificacion
    @Column(name = "acc_ult_mod_fecha")
    private LocalDateTime accUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "acc_ult_mod_usuario", length = 45)
    private String accUltmodUsuario;

    @Column(name = "acc_version")
    @Version
    private Integer accVersion;
    
    @JoinColumn(name = "acc_archivo", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgArchivo accArchivo;
    
    @JoinColumn(name = "acc_componente_plan_estudio_fk", referencedColumnName = "cpe_pk")
    @ManyToOne
    private SgComponentePlanEstudio accComponentePlanEstudio;

    
    @JoinColumn(name = "acc_rango_fecha_fk", referencedColumnName = "rfe_pk")
    @ManyToOne
    private SgRangoFecha accRangoFecha;

    @Column(name = "acc_seccion_fk")
    private Long accSeccionPk;

    @JoinColumn(name = "acc_escala_calificaciones_fk", referencedColumnName = "eca_pk")
    @ManyToOne
    private SgEscalaCalificacion accEscalaCalificacion;
    
    @Column(name = "acc_archivo_borrado")
    private Boolean accArchivoBorrado;
    
 
    @Column(name = "acc_error_log")
    private String accErrorLog;   

    @Column(name = "acc_fecha_calificado")
    private LocalDate accFechaCalificado;
    
    @Column(name = "acc_metodo_importacion")
    private String accMetodoImportacion;
    
    public SgArchivoCalificaciones() {
        accArchivoBorrado=Boolean.FALSE;
    }
    
    

    public Long getAchPk() {
        return accPk;
    }

    public void setAchPk(Long accPk) {
        this.accPk = accPk;
    }

    public String getAchDescripcion() {
        return accDescripcion;
    }

    public void setAchDescripcion(String accDescripcion) {
        this.accDescripcion = accDescripcion;
    }

    public LocalDateTime getAchUltmodFecha() {
        return accUltmodFecha;
    }

    public void setAchUltmodFecha(LocalDateTime accUltmodFecha) {
        this.accUltmodFecha = accUltmodFecha;
    }

    public String getAchUltmodUsuario() {
        return accUltmodUsuario;
    }

    public void setAchUltmodUsuario(String accUltmodUsuario) {
        this.accUltmodUsuario = accUltmodUsuario;
    }

    public Integer getAchVersion() {
        return accVersion;
    }

    public void setAchVersion(Integer accVersion) {
        this.accVersion = accVersion;
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

    public EnumEstadoImportado getAccEstado() {
        return accEstado;
    }

    public void setAccEstado(EnumEstadoImportado accEstado) {
        this.accEstado = accEstado;
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
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.accPk);
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
        final SgArchivoCalificaciones other = (SgArchivoCalificaciones) obj;
        if (!Objects.equals(this.accPk, other.accPk)) {
            return false;
        }
        return true;
    }

}
