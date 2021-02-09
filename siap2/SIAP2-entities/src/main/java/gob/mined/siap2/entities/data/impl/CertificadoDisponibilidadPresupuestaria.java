/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoCertificadoDispPresupuestaria;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_cert_disp_pres")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class CertificadoDisponibilidadPresupuestaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cert_disp_pres_gen")
    @SequenceGenerator(name = "seq_cert_disp_pres_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_cert_disp_pres", allocationSize = 1)
    @Column(name = "cer_id")
    protected Integer id;
    
    @Column(name = "cer_numero")
    protected Integer numero;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "cer_estado")
    private EstadoCertificadoDispPresupuestaria estado;
    
    @Column(name = "cer_fecha")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Column(name = "cer_fecha_certificado")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCertificado;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_cert_disp_pres_f",
            joinColumns = {
                @JoinColumn(name = "rel_cert_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_po_fuente_id")}
    )
    private List<POMontoFuenteInsumo> fuentes;
    
    
    @ManyToOne
    @JoinColumn(name = "cer_archivo")
    private Archivo archivo;
    
        
    @ManyToOne
    @JoinColumn(name = "cer_archivo_certificado")
    private Archivo archivoCertificado;
    
    
    
        
    @ManyToOne
    @JoinColumn(name = "cer_unidad_tecnica")
    private UnidadTecnica unidadTecnica;
    
    
    @OrderColumn(name = "com_indice")
    @OneToMany(mappedBy = "certificado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComentarioCertificadoDisponibilidadPresupuestaria> comentarios;
  
    
    
    
    
    //Auditoria
    @Column(name = "cer_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "cer_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "cer_version")
    @Version
    private Integer version;

    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EstadoCertificadoDispPresupuestaria getEstado() {
        return estado;
    }

    public void setEstado(EstadoCertificadoDispPresupuestaria estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setFuentes(List<POMontoFuenteInsumo> fuentes) {
        this.fuentes = fuentes;
    }

    public List<ComentarioCertificadoDisponibilidadPresupuestaria> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<ComentarioCertificadoDisponibilidadPresupuestaria> comentarios) {
        this.comentarios = comentarios;
    }

    public Archivo getArchivoCertificado() {
        return archivoCertificado;
    }

    public void setArchivoCertificado(Archivo archivoCertificado) {
        this.archivoCertificado = archivoCertificado;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public Date getFechaCertificado() {
        return fechaCertificado;
    }

    public void setFechaCertificado(Date fechaCertificado) {
        this.fechaCertificado = fechaCertificado;
    }
    
    

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    
    public String getUltUsuario() {
        return ultUsuario;
    }

    public Integer getNumero() {
        return numero;
    }

    public List<POMontoFuenteInsumo> getFuentes() {
        return fuentes;
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
        final CertificadoDisponibilidadPresupuestaria other = (CertificadoDisponibilidadPresupuestaria) obj;
        if ((this.id != null) && (other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }

}
