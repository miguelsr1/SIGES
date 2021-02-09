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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_tuplas_indicador_materializado", schema = Constantes.SCHEMA_ESTADISTICAS)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tupPk", scope = SgTuplaIndicadorMaterializado.class)
public class SgTuplaIndicadorMaterializado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tup_pk", nullable = false)
    private Long tupPk;

    @ManyToOne
    @JoinColumn(name = "tup_indicador_materializado_fk")
    private SgIndicadorMaterializado tupIndicadorMaterializado;
    
    @Column(name = "tup_identificador")
    private String tupIdentificador;
    
    @Column(name = "tup_valor")
    private Double tupValor;
    
    @Column(name = "tup_desagregacion")
    private String tupDesagregacion;
    
    @Column(name = "tup_version")
    @Version
    private Integer tupVersion;
    
 
    public Long getTupPk() {
        return tupPk;
    }

    public void setTupPk(Long tupPk) {
        this.tupPk = tupPk;
    }

    public SgIndicadorMaterializado getTupIndicadorMaterializado() {
        return tupIndicadorMaterializado;
    }

    public void setTupIndicadorMaterializado(SgIndicadorMaterializado tupIndicadorMaterializado) {
        this.tupIndicadorMaterializado = tupIndicadorMaterializado;
    }

    public String getTupIdentificador() {
        return tupIdentificador;
    }

    public void setTupIdentificador(String tupIdentificador) {
        this.tupIdentificador = tupIdentificador;
    }

    public Double getTupValor() {
        return tupValor;
    }

    public void setTupValor(Double tupValor) {
        this.tupValor = tupValor;
    }

    public String getTupDesagregacion() {
        return tupDesagregacion;
    }

    public void setTupDesagregacion(String tupDesagregacion) {
        this.tupDesagregacion = tupDesagregacion;
    }

    public Integer getTupVersion() {
        return tupVersion;
    }

    public void setTupVersion(Integer tupVersion) {
        this.tupVersion = tupVersion;
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.tupPk);
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
        final SgTuplaIndicadorMaterializado other = (SgTuplaIndicadorMaterializado) obj;
        if (!Objects.equals(this.tupPk, other.tupPk)) {
            return false;
        }
        return true;
    }

}
