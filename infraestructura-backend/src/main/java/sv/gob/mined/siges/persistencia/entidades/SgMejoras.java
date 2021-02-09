/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoMejora;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_mejoras", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mejPk", scope = SgMejoras.class)
@Audited
public class SgMejoras implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mej_pk", nullable = false)
    private Long mejPk; 

    @Column(name = "mej_fecha")
    private LocalDate mejFecha;
    
    @Column(name = "mej_descripcion")
    private String mejDescripcion;
    
    @Column(name="mej_costo")
    private BigDecimal mejCosto;

    @Column(name = "mej_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mejUltModFecha;

    @Size(max = 45)
    @Column(name = "mej_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mejUltModUsuario;

    @Column(name = "mej_version")
    @Version
    private Integer mejVersion;
    
    @JoinColumn(name = "mej_inmueble", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes mejInmueble;    
    
    @JoinColumn(name = "mej_edificio", referencedColumnName = "edi_pk")
    @ManyToOne
    private SgEdificio mejEdificio;    

    
    @JoinColumn(name = "mej_tipo_mejora", referencedColumnName = "tme_pk")
    @ManyToOne
    private SgTipoMejora mejTipoMejora;
    
     @JoinColumn(name = "mej_fuente_financiamiento", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgFuenteFinanciamiento mejFuenteFinanciamiento;
    
    

    public SgMejoras() {
    }

    public Long getMejPk() {
        return mejPk;
    }

    public void setMejPk(Long mejPk) {
        this.mejPk = mejPk;
    }

    public LocalDateTime getMejUltModFecha() {
        return mejUltModFecha;
    }

    public void setMejUltModFecha(LocalDateTime mejUltModFecha) {
        this.mejUltModFecha = mejUltModFecha;
    }

    public String getMejUltModUsuario() {
        return mejUltModUsuario;
    }

    public void setMejUltModUsuario(String mejUltModUsuario) {
        this.mejUltModUsuario = mejUltModUsuario;
    }

    public Integer getMejVersion() {
        return mejVersion;
    }

    public void setMejVersion(Integer mejVersion) {
        this.mejVersion = mejVersion;
    }

    public LocalDate getMejFecha() {
        return mejFecha;
    }

    public void setMejFecha(LocalDate mejFecha) {
        this.mejFecha = mejFecha;
    }

    public String getMejDescripcion() {
        return mejDescripcion;
    }

    public void setMejDescripcion(String mejDescripcion) {
        this.mejDescripcion = mejDescripcion;
    }

    public BigDecimal getMejCosto() {
        return mejCosto;
    }

    public void setMejCosto(BigDecimal mejCosto) {
        this.mejCosto = mejCosto;
    }

    public SgEdificio getMejEdificio() {
        return mejEdificio;
    }

    public void setMejEdificio(SgEdificio mejEdificio) {
        this.mejEdificio = mejEdificio;
    }

    public SgTipoMejora getMejTipoMejora() {
        return mejTipoMejora;
    }

    public void setMejTipoMejora(SgTipoMejora mejTipoMejora) {
        this.mejTipoMejora = mejTipoMejora;
    }
    
    public SgInmueblesSedes getMejInmueble() {
        return mejInmueble;
    }

    public void setMejInmueble(SgInmueblesSedes mejInmueble) {
        this.mejInmueble = mejInmueble;
    }

    public SgFuenteFinanciamiento getMejFuenteFinanciamiento() {
        return mejFuenteFinanciamiento;
    }

    public void setMejFuenteFinanciamiento(SgFuenteFinanciamiento mejFuenteFinanciamiento) {
        this.mejFuenteFinanciamiento = mejFuenteFinanciamiento;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.mejPk);
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
        final SgMejoras other = (SgMejoras) obj;
        if (!Objects.equals(this.mejPk, other.mejPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMejoras{" + "mejPk=" + mejPk +  ", mejUltModFecha=" + mejUltModFecha + ", mejUltModUsuario=" + mejUltModUsuario + ", mejVersion=" + mejVersion + '}';
    }
    
    

}
