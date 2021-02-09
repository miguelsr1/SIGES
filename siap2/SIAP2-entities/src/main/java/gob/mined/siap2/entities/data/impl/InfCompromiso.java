package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author sofis
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_inf_compromiso")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class InfCompromiso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inf_compromiso_gen")
    @SequenceGenerator(name = "seq_inf_compromiso_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_inf_compromiso", allocationSize = 1)
    @Column(name = "inf_id")
    private Integer id;

    @Transient
    private DataFile tempUploadedFile;
    
    @OneToOne
    @JoinColumn(name = "inf_archivo")
    private Archivo archivo;
    
    @OneToMany(mappedBy = "compromiso", cascade = CascadeType.ALL)
    @OrderColumn(name = "orden")
    private List<InfComprometido> comprometidos;
    
    @OneToMany(mappedBy = "compromiso", cascade = CascadeType.ALL)
    @OrderColumn(name = "orden")
    private List<InfDevengado> devengados;
    
    @OneToMany(mappedBy = "compromiso", cascade = CascadeType.ALL)
    @OrderColumn(name = "orden")
    private List<InfPagado> pagados;
    
    @Column(name = "inf_cmtido_f_genera")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date comprometidoFechaGenera;
    
    @Column(name = "inf_cmtido_ejercicio")
    private String comprometidoEjercicio;
    
    @Column(name = "inf_cmtido_institucion")
    private String comprometidoInstitucion;
    
    @Column(name = "inf_cmtido_unidad")
    private String comprometidoUnidad;
    
    @Column(name = "inf_pagado_f_periodo")
    private String pagadoPeriodo;
    
    @Column(name = "inf_pagado_f_genera")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date pagadoFechaGenera;
    
    @Column(name = "inf_pagado_ejercicio")
    private String pagadoEjercicio;
    
    @Column(name = "inf_pagado_institucion")
    private String pagadoInstitucion;
    
    @Column(name = "inf_pagado_unidad")
    private String pagadoUnidad;
    
    @Column(name = "inf_devengado_f_periodo")
    private String devengadoPeriodo;
    
    @Column(name = "inf_devengado_f_genera")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date devengadoFechaGenera;
    
    @Column(name = "inf_devengado_ejercicio")
    private String devengadoEjercicio;
    
    @Column(name = "inf_devengado_institucion")
    private String devengadoInstitucion;
    
    @Column(name = "inf_devengado_unidad")
    private String devengadoUnidad;
    
    
    //Auditoria
    @Column(name = "inf_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "inf_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "inf_version")
    @Version
    private Integer version;
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevengadoPeriodo() {
        return devengadoPeriodo;
    }

    public void setDevengadoPeriodo(String devengadoPeriodo) {
        this.devengadoPeriodo = devengadoPeriodo;
    }

    public Date getDevengadoFechaGenera() {
        return devengadoFechaGenera;
    }

    public void setDevengadoFechaGenera(Date devengadoFechaGenera) {
        this.devengadoFechaGenera = devengadoFechaGenera;
    }

    public String getDevengadoEjercicio() {
        return devengadoEjercicio;
    }

    public void setDevengadoEjercicio(String devengadoEjercicio) {
        this.devengadoEjercicio = devengadoEjercicio;
    }

    public String getDevengadoInstitucion() {
        return devengadoInstitucion;
    }

    public void setDevengadoInstitucion(String devengadoInstitucion) {
        this.devengadoInstitucion = devengadoInstitucion;
    }

    public String getDevengadoUnidad() {
        return devengadoUnidad;
    }

    public void setDevengadoUnidad(String devengadoUnidad) {
        this.devengadoUnidad = devengadoUnidad;
    }
    
    

    public Date getComprometidoFechaGenera() {
        return comprometidoFechaGenera;
    }

    public void setComprometidoFechaGenera(Date comprometidoFechaGenera) {
        this.comprometidoFechaGenera = comprometidoFechaGenera;
    }

    public String getComprometidoEjercicio() {
        return comprometidoEjercicio;
    }

    public void setComprometidoEjercicio(String comprometidoEjercicio) {
        this.comprometidoEjercicio = comprometidoEjercicio;
    }

    public String getComprometidoInstitucion() {
        return comprometidoInstitucion;
    }

    public void setComprometidoInstitucion(String comprometidoInstitucion) {
        this.comprometidoInstitucion = comprometidoInstitucion;
    }

    public String getComprometidoUnidad() {
        return comprometidoUnidad;
    }

    public void setComprometidoUnidad(String comprometidoUnidad) {
        this.comprometidoUnidad = comprometidoUnidad;
    }

    public String getPagadoPeriodo() {
        return pagadoPeriodo;
    }

    public void setPagadoPeriodo(String pagadoPeriodo) {
        this.pagadoPeriodo = pagadoPeriodo;
    }

    public Date getPagadoFechaGenera() {
        return pagadoFechaGenera;
    }

    public void setPagadoFechaGenera(Date pagadoFechaGenera) {
        this.pagadoFechaGenera = pagadoFechaGenera;
    }

    public String getPagadoEjercicio() {
        return pagadoEjercicio;
    }

    public void setPagadoEjercicio(String pagadoEjercicio) {
        this.pagadoEjercicio = pagadoEjercicio;
    }

    public String getPagadoInstitucion() {
        return pagadoInstitucion;
    }

    public void setPagadoInstitucion(String pagadoInstitucion) {
        this.pagadoInstitucion = pagadoInstitucion;
    }

    public String getPagadoUnidad() {
        return pagadoUnidad;
    }

    public void setPagadoUnidad(String pagadoUnidad) {
        this.pagadoUnidad = pagadoUnidad;
    }
    
    

    public List<InfComprometido> getComprometidos() {
        return comprometidos;
    }

    public void setComprometidos(List<InfComprometido> comprometidos) {
        this.comprometidos = comprometidos;
    }

    public List<InfDevengado> getDevengados() {
        return devengados;
    }

    public void setDevengados(List<InfDevengado> devengados) {
        this.devengados = devengados;
    }

    public List<InfPagado> getPagados() {
        return pagados;
    }

    public void setPagados(List<InfPagado> pagados) {
        this.pagados = pagados;
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
    
    public DataFile getTempUploadedFile() {
        return tempUploadedFile;
    }
    
    public void setTempUploadedFile(DataFile tempUploadedFile) {
        this.tempUploadedFile = tempUploadedFile;
    }
    
    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
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
        final InfCompromiso other = (InfCompromiso) obj;
        if (this.id!=null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
        return (this== other);
    }    
    
}
