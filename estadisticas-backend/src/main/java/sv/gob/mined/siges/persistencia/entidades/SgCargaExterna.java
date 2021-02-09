/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
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
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.enumerados.EnumTipoNumericoValorEstadistica;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_cargas_externas", schema = Constantes.SCHEMA_ESTADISTICAS)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carPk", scope = SgCargaExterna.class)
public class SgCargaExterna implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "car_pk", nullable = false)
    private Long carPk;

    @Column(name = "car_anio")
    private Integer carAnio;
    
    @ManyToOne
    @JoinColumn(name = "car_nombre_fk")
    private SgEstNombreExtraccion carNombre;
    
    @ManyToOne
    @JoinColumn(name = "car_indicador_fk")
    private SgEstIndicador carIndicador;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "car_desagregacion")
    private EnumDesagregacion carDesagregacion; //opcional
    
    @Enumerated(EnumType.STRING)
    @Column(name = "car_tipo_numerico_valor")
    private EnumTipoNumericoValorEstadistica carTipoNumerico;
    
    @Column(name = "car_descripcion")
    private String carDescripcion;
       
    @AtributoUltimaModificacion
    @Column(name = "car_ult_mod_fecha")
    private LocalDateTime carUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "car_ult_mod_usuario", length = 45)
    private String carUltmodUsuario;

    @Column(name = "car_version")
    @Version
    private Integer carVersion;
        
    @Transient
    private String carExcelTmpPath;
    
 
    public Long getCarPk() {
        return carPk;
    }

    public void setCarPk(Long carPk) {
        this.carPk = carPk;
    }

    public Integer getCarAnio() {
        return carAnio;
    }

    public void setCarAnio(Integer carAnio) {
        this.carAnio = carAnio;
    }

    public LocalDateTime getCarUltmodFecha() {
        return carUltmodFecha;
    }

    public void setCarUltmodFecha(LocalDateTime carUltmodFecha) {
        this.carUltmodFecha = carUltmodFecha;
    }

    public String getCarUltmodUsuario() {
        return carUltmodUsuario;
    }

    public void setCarUltmodUsuario(String carUltmodUsuario) {
        this.carUltmodUsuario = carUltmodUsuario;
    }

    public Integer getCarVersion() {
        return carVersion;
    }

    public void setCarVersion(Integer carVersion) {
        this.carVersion = carVersion;
    }


    public SgEstNombreExtraccion getCarNombre() {
        return carNombre;
    }

    public void setCarNombre(SgEstNombreExtraccion carNombre) {
        this.carNombre = carNombre;
    }


    public String getCarDescripcion() {
        return carDescripcion;
    }

    public void setCarDescripcion(String carDescripcion) {
        this.carDescripcion = carDescripcion;
    }

    public SgEstIndicador getCarIndicador() {
        return carIndicador;
    }

    public void setCarIndicador(SgEstIndicador carIndicador) {
        this.carIndicador = carIndicador;
    }

    public EnumDesagregacion getCarDesagregacion() {
        return carDesagregacion;
    }

    public void setCarDesagregacion(EnumDesagregacion carDesagregacion) {
        this.carDesagregacion = carDesagregacion;
    }

    public String getCarExcelTmpPath() {
        return carExcelTmpPath;
    }

    public void setCarExcelTmpPath(String carExcelTmpPath) {
        this.carExcelTmpPath = carExcelTmpPath;
    }

    public EnumTipoNumericoValorEstadistica getCarTipoNumerico() {
        return carTipoNumerico;
    }

    public void setCarTipoNumerico(EnumTipoNumericoValorEstadistica carTipoNumerico) {
        this.carTipoNumerico = carTipoNumerico;
    }
    
    
    
 

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.carPk);
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
        final SgCargaExterna other = (SgCargaExterna) obj;
        if (!Objects.equals(this.carPk, other.carPk)) {
            return false;
        }
        return true;
    }

}
