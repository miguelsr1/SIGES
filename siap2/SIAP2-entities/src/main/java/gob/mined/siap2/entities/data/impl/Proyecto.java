/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoProyecto;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 * Objecto Proyecto
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proyecto")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Proyecto implements Serializable {

    /**
     * maximo 31 caractres para nombre de la secuencia
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proyecto_gen")
    @SequenceGenerator(name = "seq_proyecto_gen", sequenceName = Constantes.SCHEMA_SIAP2+".seq_proyecto", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;
 
    @Column(name = "pro_estado")
    @Enumerated(EnumType.STRING)
    private EstadoProyecto estado;
        
    @Column(name = "pro_paso_actual")
    private Integer pasoActual;
    
    @Column(name = "pro_nombre")
    private String nombre;

    @Lob
    @Column(name = "pro_descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "pro_tipo_proyecto")
    private TipoProyecto tipoProyecto;

    @Column(name = "pro_inicio")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicio;

    @Column(name = "pro_fin")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fin;

    @ManyToOne
    @JoinColumn(name = "pro_prog_pres")
    private ProgramaPresupuestario programaPresupuestario;

    @ManyToOne
    @JoinColumn(name = "pro_prog_inst")
    private ProgramaInstitucional programaInstitucional;

    @Column(name = "pro_codigo_siip")
    private String codigoSIIP;

    @Column(name = "pro_pep")
    private Boolean pep;

    
    @Column(name = "pro_tiene_pog")
    private Boolean tienePOG;
    
    @Lob
    @Column(name = "pro_objetivo")
    private String objetivo;

    @Column(name = "pro_monto_global", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoGlobal;
    @Column(name = "pro_temp_monto_global", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoGlobalEnConstruccion;  
    
    @ManyToOne
    @JoinColumn(name = "pro_conv")
    private Convenio convenio;
    
    @Column(name = "pro_tipo_est_comp")
    private boolean tipoEstructuraComponente;
    @Column(name = "pro_tipo_est_macro")
    private boolean tipoEstructuraMacroactividad;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoProrroga> prorrogas;
    
    @OrderColumn(name = "fue_posicion")
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoFuente> fuentesProyecto;
        
    @OrderColumn(name = "cat_posicion")
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoCategoriaConvenio> distribuccionCategorias;
        
     @OrderColumn(name = "fue_posicion")
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoAporte> aportesProyecto;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoDesembolso> proyectoDesembolso;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoEnmienda> proyectoEnmienda;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoCoEjecutora> proyectoCoEjecutoras;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoDocumentos> proyectoDocumentos;

    //estan tods los componentes, padres eh hijos
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoComponente> proyectoComponentes;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoMacroActividad> proyectoMacroactividad;
    
    @OneToMany(mappedBy = "proyecto", orphanRemoval = false)
    private List<POAProyecto> pOAProyectos;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_proyecto_indicador",
            joinColumns = {
                @JoinColumn(name = "rel_proy_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_ind_id")}
    )
    private List<ProgramaIndicador> indicadoresAsociados;
    
    //la referencia a su pog
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pro_pog")
    private POGProyecto pog;

    /**
     * la lista de techos asociada al proyecto
     */
    @OneToMany(mappedBy = "proyecto",  cascade = CascadeType.ALL)
    private List<ProyectoTechoPresupuestarioAnio> techos;    
    
    @ManyToOne
    @JoinColumn(name = "pro_pac")
    private PAC pac;
    
    @Column(name = "pro_codigo")
    private Integer codigo;
        

    //Auditoria
    @Column(name = "pro_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pro_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pro_version")
    @Version
    private Integer version;

    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    
    public String getNombreCorto() {
        String nombreCorto="";
        if (codigoSIIP!=null) {
            nombreCorto=codigoSIIP+"-";
        }
        nombreCorto=nombreCorto+TextUtils.ellipsize(nombre, 80);
        return nombreCorto;
    }
    
    
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

    public Integer getPasoActual() {
        return pasoActual;
    }

    public void setPasoActual(Integer pasoActual) {
        this.pasoActual = pasoActual;
    }

    public List<ProyectoCategoriaConvenio> getDistribuccionCategorias() {
        return distribuccionCategorias;
    }

    public void setDistribuccionCategorias(List<ProyectoCategoriaConvenio> distribuccionCategorias) {
        this.distribuccionCategorias = distribuccionCategorias;
    }

    public Boolean getTienePOG() {
        return tienePOG;
    }

    public List<ProyectoTechoPresupuestarioAnio> getTechos() {
        return techos;
    }

    public BigDecimal getMontoGlobal() {
        return montoGlobal;
    }

    public void setMontoGlobal(BigDecimal montoGlobal) {
        this.montoGlobal = montoGlobal;
    }

    public boolean isTipoEstructuraComponente() {
        return tipoEstructuraComponente;
    }

    public void setTipoEstructuraComponente(boolean tipoEstructuraComponente) {
        this.tipoEstructuraComponente = tipoEstructuraComponente;
    }

    public boolean isTipoEstructuraMacroactividad() {
        return tipoEstructuraMacroactividad;
    }

    public void setTipoEstructuraMacroactividad(boolean tipoEstructuraMacroactividad) {
        this.tipoEstructuraMacroactividad = tipoEstructuraMacroactividad;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public List<ProyectoFuente> getFuentesProyecto() {
        return fuentesProyecto;
    }

    public void setFuentesProyecto(List<ProyectoFuente> fuentesProyecto) {
        this.fuentesProyecto = fuentesProyecto;
    }


    public void setTechos(List<ProyectoTechoPresupuestarioAnio> techos) {
        this.techos = techos;
    }    

    public void setTienePOG(Boolean tienePOG) {
        this.tienePOG = tienePOG;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public List<ProgramaIndicador> getIndicadoresAsociados() {
        return indicadoresAsociados;
    }

    public void setIndicadoresAsociados(List<ProgramaIndicador> indicadoresAsociados) {
        this.indicadoresAsociados = indicadoresAsociados;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public List<ProyectoComponente> getProyectoComponentes() {
        return proyectoComponentes;
    }

    public POGProyecto getPog() {
        return pog;
    }

    public void setPog(POGProyecto pog) {
        this.pog = pog;
    }

    public void setProyectoComponentes(List<ProyectoComponente> proyectoComponentes) {
        this.proyectoComponentes = proyectoComponentes;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Boolean getTipoEstructuraComponente() {
        return tipoEstructuraComponente;
    }

    public void setTipoEstructuraComponente(Boolean tipoEstructuraComponente) {
        this.tipoEstructuraComponente = tipoEstructuraComponente;
    }

    public Boolean getTipoEstructuraMacroactividad() {
        return tipoEstructuraMacroactividad;
    }

    public void setTipoEstructuraMacroactividad(Boolean tipoEstructuraMacroactividad) {
        this.tipoEstructuraMacroactividad = tipoEstructuraMacroactividad;
    }

    public List<ProyectoMacroActividad> getProyectoMacroactividad() {
        return proyectoMacroactividad;
    }

    public void setProyectoMacroactividad(List<ProyectoMacroActividad> proyectoMacroactividad) {
        this.proyectoMacroactividad = proyectoMacroactividad;
    }

    public List<POAProyecto> getpOAProyectos() {
        return pOAProyectos;
    }

    public void setpOAProyectos(List<POAProyecto> pOAProyectos) {
        this.pOAProyectos = pOAProyectos;
    }

    public String getCodigoSIIP() {
        return codigoSIIP;
    }

    public List<ProyectoDocumentos> getProyectoDocumentos() {
        return proyectoDocumentos;
    }

    public void setProyectoDocumentos(List<ProyectoDocumentos> proyectoDocumentos) {
        this.proyectoDocumentos = proyectoDocumentos;
    }


    public List<ProyectoDesembolso> getProyectoDesembolso() {
        return proyectoDesembolso;
    }

    public List<ProyectoEnmienda> getProyectoEnmienda() {
        return proyectoEnmienda;
    }

    
    public void setProyectoEnmienda(List<ProyectoEnmienda> proyectoEnmienda) {
        this.proyectoEnmienda = proyectoEnmienda;
    }

    public void setProyectoDesembolso(List<ProyectoDesembolso> proyectoDesembolso) {
        this.proyectoDesembolso = proyectoDesembolso;
    }

    public void setCodigoSIIP(String codigoSIIP) {
        this.codigoSIIP = codigoSIIP;
    }

    public List<ProyectoAporte> getAportesProyecto() {
        return aportesProyecto;
    }

    public void setAportesProyecto(List<ProyectoAporte> aportesProyecto) {
        this.aportesProyecto = aportesProyecto;
    }

    public List<ProyectoCoEjecutora> getProyectoCoEjecutoras() {
        return proyectoCoEjecutoras;
    }

    public void setProyectoCoEjecutoras(List<ProyectoCoEjecutora> proyectoCoEjecutoras) {
        this.proyectoCoEjecutoras = proyectoCoEjecutoras;
    }

    public Boolean getPep() {
        return pep;
    }

    public void setPep(Boolean pep) {
        this.pep = pep;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public BigDecimal getMontoGlobalEnConstruccion() {
        return montoGlobalEnConstruccion;
    }

    public void setMontoGlobalEnConstruccion(BigDecimal montoGlobalEnConstruccion) {
        this.montoGlobalEnConstruccion = montoGlobalEnConstruccion;
    }

    
    public List<ProyectoProrroga> getProrrogas() {
        return prorrogas;
    }

    public void setProrrogas(List<ProyectoProrroga> prorrogas) {
        this.prorrogas = prorrogas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoProyecto getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(TipoProyecto tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public PAC getPac() {
        return pac;
    }

    public void setPac(PAC pac) {
        this.pac = pac;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public ProgramaPresupuestario getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(ProgramaPresupuestario programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public ProgramaInstitucional getProgramaInstitucional() {
        return programaInstitucional;
    }

    public void setProgramaInstitucional(ProgramaInstitucional programaInstitucional) {
        this.programaInstitucional = programaInstitucional;
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
    
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }    

    // </editor-fold>
    
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Proyecto other = (Proyecto) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
    
    /****
     * Agregado por Gustavo
     */
    
    public Integer getImporteTotal() {
        Integer total =0;
        for(ProyectoAporte a: aportesProyecto) {
            if (a.getMonto()!=null) {
            total=total+a.getMonto().intValue();
            }
        }
        return total;
    }
}
