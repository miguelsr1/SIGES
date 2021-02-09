/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.enums.TipoPOAPAC;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 * Objeto PAC - Plan Anual Contratación
 *
 * @author Sofis Solutions
 */


@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pac")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class PAC implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pac_gen")
    @SequenceGenerator(name = "seq_pac_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pac", allocationSize = 1)
    @Column(name = "pac_id")
    private Integer id;

    @Column(name = "pac_nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "pac_estado")
    private EstadoPAC estado;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_pac_poa",
            joinColumns = {
                @JoinColumn(name = "poa_consolidado")},
            inverseJoinColumns = {
                @JoinColumn(name = "poa_poa")}
    )
    private List<GeneralPOA> poas;

    @Enumerated(EnumType.STRING)
    @Column(name = "pac_tipoPOA")
    private TipoPOAPAC tipoPOA;

    @Column(name = "pac_iniciado")
    private Boolean iniciado;

    @OneToMany(mappedBy = "pac", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PACGrupo> grupos;

    @Column(name = "pac_anio")
    private Integer anio;

    @Column(name = "pac_fecha_inicio")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "pac_fecha_fin")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFin;

    @OneToOne
    @JoinColumn(name = "pac_reporte")
    private Archivo reportePAC;

    @OneToOne
    @JoinColumn(name = "pac_reporte_borrador")
    private Archivo borradorPAC;

    @OneToOne
    @JoinColumn(name = "pep_reporte")
    private Archivo reportePEP;

    @OneToOne
    @JoinColumn(name = "pep_reporte_borrador")
    private Archivo borradorPEP;

    //Auditoria
    @Column(name = "pac_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pac_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pac_version")
    @Version
    private Integer version;

    /**
     * atributo calculado para que quede mas facil el pac *
     */
    @Transient
    private List<POInsumos> insumos = null;

    public void initInsumos() {
        List minsumos = new LinkedList();
        for (GeneralPOA poa : poas) {
            if (poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                POAConActividades pa = (POAConActividades) poa;
                for (POActividadBase a : pa.getActividades()) {
                    for (POInsumos i : a.getInsumos()) {
                        if(i.getHabilitado() != null && i.getHabilitado()) {//Solo se cargan los insumos habilitados
                            //se marca como uaci por defecto
                            if (i.getNoUACI() == null) {
                                i.setNoUACI(false);
                            }
                            if (!i.getNoUACI()) {
                                minsumos.add(i);                     
                            }
                        }
                    }
                }
            } else if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
                POAProyecto pa = (POAProyecto) poa;
                for (POLinea l : pa.getLineas()) {
                    for (POActividadBase a : l.getActividades()) {
                        for (POInsumos i : a.getInsumos()) {
                            if(i.getHabilitado() != null && i.getHabilitado()) {//Solo se cargan los insumos habilitados
                               //se marca como uaci por defecto
                                if (i.getNoUACI() == null) {
                                    i.setNoUACI(false);
                                }
                                if (!i.getNoUACI()) {
                                    minsumos.add(i);
                                } 
                            }
                        }
                    }
                }
            }
        }
        insumos = minsumos;
    }

    public List<POInsumos> getInsumos() {
        return insumos;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setInsumos(List<POInsumos> insumos) {
        this.insumos = insumos;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getIniciado() {
        return iniciado;
    }

    public void setIniciado(Boolean iniciado) {
        this.iniciado = iniciado;
    }

    public List<PACGrupo> getGrupos() {
        return grupos;
    }

    public Integer getAnio() {
        return anio;
    }

    public Archivo getReportePAC() {
        return reportePAC;
    }

    public void setReportePAC(Archivo reportePAC) {
        this.reportePAC = reportePAC;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public void setGrupos(List<PACGrupo> grupos) {
        this.grupos = grupos;
    }

    public Archivo getReportePEP() {
        return reportePEP;
    }

    public void setReportePEP(Archivo reportePEP) {
        this.reportePEP = reportePEP;
    }

    public Archivo getBorradorPEP() {
        return borradorPEP;
    }

    public void setBorradorPEP(Archivo borradorPEP) {
        this.borradorPEP = borradorPEP;
    }

    public TipoPOAPAC getTipoPOA() {
        return tipoPOA;
    }

    public void setTipoPOA(TipoPOAPAC tipoPOA) {
        this.tipoPOA = tipoPOA;
    }

    public List<GeneralPOA> getPoas() {
        return poas;
    }

    public void setPoas(List<GeneralPOA> poas) {
        this.poas = poas;
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

    public EstadoPAC getEstado() {
        return estado;
    }

    public void setEstado(EstadoPAC estado) {
        this.estado = estado;
    }

    public Archivo getBorradorPAC() {
        return borradorPAC;
    }

    public void setBorradorPAC(Archivo borradorPAC) {
        this.borradorPAC = borradorPAC;
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
        final PAC other = (PAC) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
