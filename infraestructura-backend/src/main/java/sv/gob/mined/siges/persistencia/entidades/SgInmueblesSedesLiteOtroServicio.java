/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
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
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "sg_inmuebles_sedes", schema = Constantes.SCHEMA_INFRAESTRUCTURA,uniqueConstraints = {
    @UniqueConstraint(name = "tis_codigo_uk", columnNames = {"tis_codigo"})})
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "tisPk", scope = SgInmueblesSedesLiteOtroServicio.class)
@Audited
public class SgInmueblesSedesLiteOtroServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tis_pk")
    private Long tisPk;

    
    @Column(name = "tis_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tisUltModFecha;
    
    @Column(name = "tis_version")
    @Version
    private Integer tisVersion;
    
    @OneToMany(mappedBy = "risInmuebleSede", cascade = CascadeType.ALL, orphanRemoval = true)  
    private List<SgRelInmuebleServicioBasico> tisServicioBasico; 
   
    
    public SgInmueblesSedesLiteOtroServicio() {
    }

    public Long getTisPk() {
        return tisPk;
    }

    public void setTisPk(Long tisPk) {
        this.tisPk = tisPk;
    }

    public List<SgRelInmuebleServicioBasico> getTisServicioBasico() {
        return tisServicioBasico;
    }

    public void setTisServicioBasico(List<SgRelInmuebleServicioBasico> tisServicioBasico) {
        this.tisServicioBasico = tisServicioBasico;
    }


    public LocalDateTime getTisUltModFecha() {
        return tisUltModFecha;
    }

    public void setTisUltModFecha(LocalDateTime tisUltModFecha) {
        this.tisUltModFecha = tisUltModFecha;
    }

    public Integer getTisVersion() {
        return tisVersion;
    }

    public void setTisVersion(Integer tisVersion) {
        this.tisVersion = tisVersion;
    }


    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.tisPk);
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
        final SgInmueblesSedesLiteOtroServicio other = (SgInmueblesSedesLiteOtroServicio) obj;
        if (!Objects.equals(this.tisPk, other.tisPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes[ tisPk=" + tisPk + " ]";
    }

  
    
}
