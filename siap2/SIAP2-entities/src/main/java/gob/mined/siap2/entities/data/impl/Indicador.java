/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_indicador")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Indicador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_indicador_gen")
    @SequenceGenerator(name = "seq_indicador_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_indicador", allocationSize = 1)
    @Column(name = "ind_id")
    private Integer id;

    @Column(name = "ind_nombre")
    private String nombre;
    
    @Column(name = "ind_nombre_busqueda")
    private String nombreBusqueda;

    @Enumerated(EnumType.STRING)
    @Column(name = "ind_estado")
    private EstadoComun estado;

    @ManyToOne
    @JoinColumn(name = "ind_tipo")
    private Categoria tipo;

    @Lob
    @Column(name = "ind_descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "ind_form_med")
    private IndicadorFormaMedicion formaMedicion;

    
    @ManyToOne
    @JoinColumn(name = "ind_unidad_id")
    private UnidadDeMedida unidadDeMedida;

    @Lob
    @Column(name = "ind_metodo")
    private String metodoMedicion;

    
    // cosas agregadas de producto
    
    @Column(name = "ind_fin_tol_verde", columnDefinition = "Decimal(20,2)")
    private BigDecimal finToleranciaVerde;
    
        
    @Column(name = "ind_fin_tol_amariila", columnDefinition = "Decimal(20,2)")
    private BigDecimal finToleranciaAmarillo;
     
    @OneToMany(mappedBy = "indicador")
    private List<ProgramaIndicador> programasIndicador;

    //Auditoria
    @Column(name = "ind_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "ind_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ind_version")
    @Version
    private Integer version;

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }

    
    @PrePersist
    @PreUpdate
    public void beforeSave() {
        this.nombreBusqueda = this.nombre != null ? this.nombre.toLowerCase() : "";
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UnidadDeMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(UnidadDeMedida unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public String getMetodoMedicion() {
        return metodoMedicion;
    }

    public List<ProgramaIndicador> getProgramasIndicador() {
        return programasIndicador;
    }

    public void setProgramasIndicador(List<ProgramaIndicador> programasIndicador) {
        this.programasIndicador = programasIndicador;
    }

    public Categoria getTipo() {
        return tipo;
    }

    public BigDecimal getFinToleranciaVerde() {
        return finToleranciaVerde;
    }

    public void setFinToleranciaVerde(BigDecimal finToleranciaVerde) {
        this.finToleranciaVerde = finToleranciaVerde;
    }

    public BigDecimal getFinToleranciaAmarillo() {
        return finToleranciaAmarillo;
    }

    public void setFinToleranciaAmarillo(BigDecimal finToleranciaAmarillo) {
        this.finToleranciaAmarillo = finToleranciaAmarillo;
    }
    
    

    public void setTipo(Categoria tipo) {
        this.tipo = tipo;
    }

    public void setMetodoMedicion(String metodoMedicion) {
        this.metodoMedicion = metodoMedicion;
    }

    public IndicadorFormaMedicion getFormaMedicion() {
        return formaMedicion;
    }

    public void setFormaMedicion(IndicadorFormaMedicion formaMedicion) {
        this.formaMedicion = formaMedicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EstadoComun getEstado() {
        return estado;
    }

    public void setEstado(EstadoComun estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUltUsuario() {
        return ultUsuario;
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
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Indicador other = (Indicador) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
