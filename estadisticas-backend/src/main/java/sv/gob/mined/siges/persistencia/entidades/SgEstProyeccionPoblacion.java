/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_proyecciones_poblacion", schema = Constantes.SCHEMA_ESTADISTICAS)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "proPk", scope = SgEstProyeccionPoblacion.class)
public class SgEstProyeccionPoblacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pro_pk", nullable = false)
    private Long proPk;

    @Column(name = "pro_edad")
    private Integer proEdad;
    
    @Column(name = "pro_anio")
    private Integer proAnio;
    
    @Column(name = "pro_sexo_nom")
    private String proSexoNom;
    
    @Column(name = "pro_poblacion")
    private Integer proPoblacion;

    public Long getProPk() {
        return proPk;
    }

    public void setProPk(Long proPk) {
        this.proPk = proPk;
    }

    public Integer getProEdad() {
        return proEdad;
    }

    public void setProEdad(Integer proEdad) {
        this.proEdad = proEdad;
    }

    public Integer getProAnio() {
        return proAnio;
    }

    public void setProAnio(Integer proAnio) {
        this.proAnio = proAnio;
    }

    public String getProSexoNom() {
        return proSexoNom;
    }

    public void setProSexoNom(String proSexoNom) {
        this.proSexoNom = proSexoNom;
    }

    public Integer getProPoblacion() {
        return proPoblacion;
    }

    public void setProPoblacion(Integer proPoblacion) {
        this.proPoblacion = proPoblacion;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.proPk);
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
        final SgEstProyeccionPoblacion other = (SgEstProyeccionPoblacion) obj;
        if (!Objects.equals(this.proPk, other.proPk)) {
            return false;
        }
        return true;
    }

}
