/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_comen_cert_disp")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ComentarioCertificadoDisponibilidadPresupuestaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_comen_cert_disp_gen")
    @SequenceGenerator(name = "seq_comen_cert_disp_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_comen_cert_disp", allocationSize = 1)
    @Column(name = "com_id")
    protected Integer id;
    
    @Column(name = "com_indice")
    private Integer indice;
    
    
    @ManyToOne
    @JoinColumn(name = "com_certificado")
    private CertificadoDisponibilidadPresupuestaria certificado;
    
    
    @Column(name = "com_fecha")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    
    @ManyToOne
    @JoinColumn(name = "com_usuario")
    private SsUsuario usuario;
    
    @Lob
    @Column(name = "com_comentario")
    private String comentario;
    
    

    //Auditoria
    @Column(name = "com_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "com_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "com_version")
    @Version
    private Integer version;

    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CertificadoDisponibilidadPresupuestaria getCertificado() {
        return certificado;
    }

    public void setCertificado(CertificadoDisponibilidadPresupuestaria certificado) {
        this.certificado = certificado;
    }

    public SsUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(SsUsuario usuario) {
        this.usuario = usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    
  
    

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        final ComentarioCertificadoDisponibilidadPresupuestaria other = (ComentarioCertificadoDisponibilidadPresupuestaria) obj;
        if ((this.id != null) && (other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }

}
