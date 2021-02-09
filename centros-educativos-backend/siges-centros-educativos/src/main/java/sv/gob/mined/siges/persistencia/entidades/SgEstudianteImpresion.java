/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoAnulacionTitulo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;



@Entity
@Table(name = "sg_estudiante_impresion",  schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "eimPk", scope = SgEstudianteImpresion.class)
@Audited
public class SgEstudianteImpresion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eim_pk")
    private Long eimPk;
    
    @JoinColumn(name = "eim_solicitud_impresion_fk", referencedColumnName = "sim_pk")
    @ManyToOne
    private SgSolicitudImpresion eimSolicitudImpresion;
    
    @JoinColumn(name = "eim_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante eimEstudiante;
    
    @Column(name = "eim_anulada")
    private Boolean eimAnulada;
    
    @Column(name = "eim_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime eimUltModFecha;

    @Size(max = 45)
    @Column(name = "eim_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String eimUltModUsuario;

    @Column(name = "eim_version")
    @Version
    private Integer eimVersion;
    
    @Column(name = "eim_codigo_hash")
    private String codigoHash;
    
    @JoinColumn(name = "eim_motivo_anulacion_titulo_fk", referencedColumnName = "mat_pk")
    @ManyToOne
    private SgMotivoAnulacionTitulo eimMotivoAnulacion;
    
     
    @Column(name = "eim_observacion_anulada")
    private String eimObservacionAnulada;        
    
    public SgEstudianteImpresion(){
        eimAnulada=Boolean.FALSE;
    }


    public Long getEimPk() {
        return eimPk;
    }

    public void setEimPk(Long eimPk) {
        this.eimPk = eimPk;
    }

    public SgSolicitudImpresion getEimSolicitudImpresion() {
        return eimSolicitudImpresion;
    }

    public void setEimSolicitudImpresion(SgSolicitudImpresion eimSolicitudImpresion) {
        this.eimSolicitudImpresion = eimSolicitudImpresion;
    }


    public SgEstudiante getEimEstudiante() {
        return eimEstudiante;
    }

    public void setEimEstudiante(SgEstudiante eimEstudiante) {
        this.eimEstudiante = eimEstudiante;
    }

    public Boolean getEimAnulada() {
        return eimAnulada;
    }

    public void setEimAnulada(Boolean eimAnulada) {
        this.eimAnulada = eimAnulada;
    }

    public SgMotivoAnulacionTitulo getEimMotivoAnulacion() {
        return eimMotivoAnulacion;
    }

    public void setEimMotivoAnulacion(SgMotivoAnulacionTitulo eimMotivoAnulacion) {
        this.eimMotivoAnulacion = eimMotivoAnulacion;
    }    

    public LocalDateTime getEimUltModFecha() {
        return eimUltModFecha;
    }

    public void setEimUltModFecha(LocalDateTime eimUltModFecha) {
        this.eimUltModFecha = eimUltModFecha;
    }

    public String getEimUltModUsuario() {
        return eimUltModUsuario;
    }

    public void setEimUltModUsuario(String eimUltModUsuario) {
        this.eimUltModUsuario = eimUltModUsuario;
    }

    public Integer getEimVersion() {
        return eimVersion;
    }

    public void setEimVersion(Integer eimVersion) {
        this.eimVersion = eimVersion;
    }

    public String getCodigoHash() {
        return codigoHash;
    }

    public void setCodigoHash(String codigoHash) {
        this.codigoHash = codigoHash;
    }

    public String getEimObservacionAnulada() {
        return eimObservacionAnulada;
    }

    public void setEimObservacionAnulada(String eimObservacionAnulada) {
        this.eimObservacionAnulada = eimObservacionAnulada;
    }

    

    @Override
    public String toString() {
         return "SgEstudianteImpresion{" + "eimPk=" + eimPk + ", eimEstudiante=" + eimEstudiante.getEstPk().toString() +   '}';
    }
    
    

}
