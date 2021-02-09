/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
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
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEspacioComun;

@Entity
@Table(name = "sg_rel_inmueble_espacio_comun", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "riePk", scope = SgRelInmuebleEspacioComun.class)
@Audited
public class SgRelInmuebleEspacioComun implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rie_pk")
    private Long riePk;
  
    @Size(max = 45)
    @Column(name = "rie_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String rieUltModUsuario;

    
    @Column(name = "rie_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rieUltModFecha;
    
    @Column(name = "rie_version")
    @Version
    private Integer rieVersion;
    
    @JoinColumn(name = "rie_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes rieInmuebleSede;
    
    @JoinColumn(name = "rie_espacio_comun_fk", referencedColumnName = "eco_pk")
    @ManyToOne
    private SgEspacioComun rieEspacioComun;
    
    @Column(name = "rie_bueno")
    private Integer rieBueno;
    
    @Column(name = "rie_regular")
    private Integer rieRegular;
    
    @Column(name = "rie_malo")
    private Integer rieMalo;
    
    @Column(name = "rie_descripcion")
    private String rieDescripcion;
 
    
    public SgRelInmuebleEspacioComun() {
    }

    public Long getRiePk() {
        return riePk;
    }

    public void setRiePk(Long riePk) {
        this.riePk = riePk;
    }

    public String getRieUltModUsuario() {
        return rieUltModUsuario;
    }

    public void setRieUltModUsuario(String rieUltModUsuario) {
        this.rieUltModUsuario = rieUltModUsuario;
    }

    public LocalDateTime getRieUltModFecha() {
        return rieUltModFecha;
    }

    public void setRieUltModFecha(LocalDateTime rieUltModFecha) {
        this.rieUltModFecha = rieUltModFecha;
    }

    public Integer getRieVersion() {
        return rieVersion;
    }

    public void setRieVersion(Integer rieVersion) {
        this.rieVersion = rieVersion;
    }

    public SgInmueblesSedes getRieInmuebleSede() {
        return rieInmuebleSede;
    }

    public void setRieInmuebleSede(SgInmueblesSedes rieInmuebleSede) {
        this.rieInmuebleSede = rieInmuebleSede;
    }

    public SgEspacioComun getRieEspacioComun() {
        return rieEspacioComun;
    }

    public void setRieEspacioComun(SgEspacioComun rieEspacioComun) {
        this.rieEspacioComun = rieEspacioComun;
    }
    
    public Integer getRieBueno() {
        return rieBueno;
    }

    public void setRieBueno(Integer rieBueno) {
        this.rieBueno = rieBueno;
    }

    public Integer getRieRegular() {
        return rieRegular;
    }

    public void setRieRegular(Integer rieRegular) {
        this.rieRegular = rieRegular;
    }

    public Integer getRieMalo() {
        return rieMalo;
    }

    public void setRieMalo(Integer rieMalo) {
        this.rieMalo = rieMalo;
    }

    public String getRieDescripcion() {
        return rieDescripcion;
    }

    public void setRieDescripcion(String rieDescripcion) {
        this.rieDescripcion = rieDescripcion;
    }

    

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.riePk);
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
        final SgRelInmuebleEspacioComun other = (SgRelInmuebleEspacioComun) obj;
        if (!Objects.equals(this.riePk, other.riePk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes[ tisPk=" + riePk + " ]";
    }

 
  
    
}
