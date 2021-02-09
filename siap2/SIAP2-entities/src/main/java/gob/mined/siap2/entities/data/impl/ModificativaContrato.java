/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoModificativa;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_modificativa_contrato")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ModificativaContrato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_modificativa_contrato")
    @SequenceGenerator(name = "seq_modificativa_contrato", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_modificativa_contrato", allocationSize = 1)
    @Column(name = "mod_id")
    private Integer id;
    
    @Column(name = "mod_nro")
    private Integer numero;
    
    @Column(name = "mod_fech")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "mod_estado")
    private EstadoModificativa estado;

    @ManyToOne 
    @JoinColumn(name = "mod_contrato_oc")
    private ContratoOC contratoOC;
     
    @OneToMany(mappedBy = "modificativa")
    private List<POInsumos> poInsumos;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_modi_fluj_caj_anio",
            joinColumns = {
                @JoinColumn(name = "rel_modific")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_fluj_caj")}
    )
    private Set<FlujoCajaAnio> programacionPagos;
   
    @OneToOne(mappedBy ="modificativaContrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private CompromisoPresupuestarioModificativa compromisoPresupuestario;
    
    @Lob
    @Column(name = "mod_comentarios")
    private String comentarios;
    
    //Auditoria
    @Column(name = "con_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "con_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "con_version")
    @Version
    private Integer version;
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    

    public EstadoModificativa getEstado() {
        return estado;
    }

    public void setEstado(EstadoModificativa estado) {
        this.estado = estado;
    }

    public ContratoOC getContratoOC() {
        return contratoOC;
    }

    public void setContratoOC(ContratoOC contratoOC) {
        this.contratoOC = contratoOC;
    }

    public List<POInsumos> getPoInsumos() {
        return poInsumos;
    }

    public void setPoInsumos(List<POInsumos> poInsumos) {
        this.poInsumos = poInsumos;
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
    
    public Set<FlujoCajaAnio> getProgramacionPagos() {
        return programacionPagos;
    }

    public void setProgramacionPagos(Set<FlujoCajaAnio> programacionPagos) {
        this.programacionPagos = programacionPagos;
    }

    public CompromisoPresupuestarioModificativa getCompromisoPresupuestario() {
        return compromisoPresupuestario;
    }

    public void setCompromisoPresupuestario(CompromisoPresupuestarioModificativa compromisoPresupuestario) {
        this.compromisoPresupuestario = compromisoPresupuestario;
    }
    
    
    
    public String getComentarios() {
        return comentarios;
    }
    
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    // </editor-fold>

    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.getId());
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
        final ModificativaContrato other = (ModificativaContrato) obj;
        if (this.id!= null && other.id!= null) {
            return Objects.equals(this.id, other.id);
        }
        return this == other;
    }


}
