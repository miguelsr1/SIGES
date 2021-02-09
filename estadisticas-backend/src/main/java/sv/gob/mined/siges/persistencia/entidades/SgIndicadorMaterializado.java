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
@Table(name = "sg_indicadores_materializados", schema = Constantes.SCHEMA_ESTADISTICAS)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "indPk", scope = SgIndicadorMaterializado.class)
public class SgIndicadorMaterializado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ind_pk", nullable = false)
    private Long indPk;

    @Column(name = "ind_anio")
    private Integer indAnio;
   
    @ManyToOne
    @JoinColumn(name = "ind_indicador_fk")
    private SgEstIndicador indIndicador;
    
    @ManyToOne
    @JoinColumn(name = "ind_nombre_fk")
    private SgEstNombreExtraccion indNombreExtraccion; //Utilizado para saber desde que fuente se hizo la materialización
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ind_desagregacion")
    private EnumDesagregacion indDesagregacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ind_tipo_numerico_valor")
    private EnumTipoNumericoValorEstadistica indTipoNumerico;
       
    @AtributoUltimaModificacion
    @Column(name = "ind_ult_mod_fecha")
    private LocalDateTime indUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "ind_ult_mod_usuario", length = 45)
    private String indUltmodUsuario;

    @Column(name = "ind_version")
    @Version
    private Integer indVersion;
        
 
    public Long getIndPk() {
        return indPk;
    }

    public void setIndPk(Long indPk) {
        this.indPk = indPk;
    }

    public Integer getIndAnio() {
        return indAnio;
    }

    public void setIndAnio(Integer indAnio) {
        this.indAnio = indAnio;
    }

    public LocalDateTime getIndUltmodFecha() {
        return indUltmodFecha;
    }

    public void setIndUltmodFecha(LocalDateTime indUltmodFecha) {
        this.indUltmodFecha = indUltmodFecha;
    }

    public String getIndUltmodUsuario() {
        return indUltmodUsuario;
    }

    public void setIndUltmodUsuario(String indUltmodUsuario) {
        this.indUltmodUsuario = indUltmodUsuario;
    }

    public Integer getIndVersion() {
        return indVersion;
    }

    public void setIndVersion(Integer indVersion) {
        this.indVersion = indVersion;
    }

    public SgEstIndicador getIndIndicador() {
        return indIndicador;
    }

    public void setIndIndicador(SgEstIndicador indIndicador) {
        this.indIndicador = indIndicador;
    }

    public EnumDesagregacion getIndDesagregacion() {
        return indDesagregacion;
    }

    public void setIndDesagregacion(EnumDesagregacion indDesagregacion) {
        this.indDesagregacion = indDesagregacion;
    }

    public EnumTipoNumericoValorEstadistica getIndTipoNumerico() {
        return indTipoNumerico;
    }

    public void setIndTipoNumerico(EnumTipoNumericoValorEstadistica indTipoNumerico) {
        this.indTipoNumerico = indTipoNumerico;
    }

    public SgEstNombreExtraccion getIndNombreExtraccion() {
        return indNombreExtraccion;
    }

    public void setIndNombreExtraccion(SgEstNombreExtraccion indNombreExtraccion) {
        this.indNombreExtraccion = indNombreExtraccion;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.indPk);
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
        final SgIndicadorMaterializado other = (SgIndicadorMaterializado) obj;
        if (!Objects.equals(this.indPk, other.indPk)) {
            return false;
        }
        return true;
    }

}
