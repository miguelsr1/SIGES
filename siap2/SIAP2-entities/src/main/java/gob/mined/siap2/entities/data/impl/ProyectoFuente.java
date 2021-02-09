/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_fuente_proyecto")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public  class ProyectoFuente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fuente_proy_gen")
    @SequenceGenerator(name = "seq_fuente_proy_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_fuente_proy", allocationSize = 1)
    @Column(name = "fue_id")
    protected Integer id;

      
    @ManyToOne
    @JoinColumn(name = "fue_proyecto")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "fue_financiamiento")
    private FuenteFinanciamiento fuenteFinanciamiento;

    @ManyToOne
    @JoinColumn(name = "fue_recursos")
    private FuenteRecursos fuenteRecursos;


    @Column(name = "fue_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;
    
    
    @Column(name = "fue_posicion")
    protected Integer posicion;

        
        
    /**
     * la lista de techos asociada al aporte
     */
    @OneToMany(mappedBy = "aporte",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoAporteTechoPresupuestarioAnio> techos;    
    
    
    
    //Auditoria
    @Column(name = "fue_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "fue_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "fue_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }


    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }


    public String getUltUsuario() {
        return ultUsuario;
    }

    public FuenteFinanciamiento getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(FuenteFinanciamiento fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }
    
    

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
    }

    public List<ProyectoAporteTechoPresupuestarioAnio> getTechos() {
        return techos;
    }

    public void setTechos(List<ProyectoAporteTechoPresupuestarioAnio> techos) {
        this.techos = techos;
    }
    
    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // </editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProyectoFuente other = (ProyectoFuente) obj;
        if ((this.id != null) && (other.id != null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }
    
    

}
