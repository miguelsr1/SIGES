/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAntecedenteParto;
import sv.gob.mined.siges.enumerados.EnumEdadGestacional;
import sv.gob.mined.siges.enumerados.EnumVacunas;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEnfermedadNoTransmisible;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_fichas_salud", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "fsaPk", scope = SgFichaSalud.class)
@Audited
public class SgFichaSalud implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fsa_pk")
    private Long fsaPk;
    
    @Column(name = "fsa_vacunas_completo")
    @Enumerated(value = EnumType.STRING)
    private EnumVacunas fsaVacunasCompleto;
    
    @Column(name = "fsa_antecedente_parto")
    @Enumerated(value = EnumType.STRING)
    private EnumAntecedenteParto fsaAntecedenteParto;
    
    @Column(name = "fsa_lactancia")
    private Boolean fsaLactancia;
    
    @Column(name = "fsa_parto_prematuro")
    private Boolean fsaPartoPrematuro;
    
    @Column(name = "fsa_edad_gestacional")
    @Enumerated(value = EnumType.STRING)
    private EnumEdadGestacional fsaEdadGestacional;
    
    @Column(name = "fsa_es_alergico_medicamento")
    private Boolean fsaEsAlergicoMedicamento;
    
    @Size(max = 255)
    @Column(name = "fsa_alergico_medicamentos")
    private String fsaAlergicoMedicamentos;
    
    @Column(name = "fsa_es_alergico_alimento")
    private Boolean fsaEsAlergicoAlimento;
    
    @Size(max = 255)
    @Column(name = "fsa_alergico_alimentos")
    private String fsaAlergicoAlimentos;
    
    @Size(max = 500)
    @Column(name = "fsa_medicamento_prescriptor")
    private String fsaMedicamentoPrescriptor;
    
    @Size(max = 500)
    @Column(name = "fsa_enfermedades_padece")
    private String fsaEnfermedadesPadece;
    
    @Size(max = 500)
    @Column(name = "fsa_medicamento_permanente")
    private String fsaMedicamentoPermanente;
    
    @Size(max = 500)
    @Column(name = "fsa_situaciones_desencadenantes")
    private String fsaSituacionesDesencadenantes;
    
    @Size(max = 500)
    @Column(name = "fsa_manifestaciones_fisicas_psicologicas")
    private String fsaManifestacionesFisicasPsicologicas;
    
    @Size(max = 40)
    @Column(name = "fsa_tiempos_comida")
    private String fsaTiemposComida;
    
    @Column(name = "fsa_consume_frutas_verduras")
    private Boolean fsaConsumeFrutasVerduras;
    
    @Size(max = 40)
    @Column(name = "fsa_frutas_verduras_dia")
    private String fsaFrutasVerdurasDia;
    
    @Column(name = "fsa_consume_agua")
    private Boolean fsaConsumeAgua;
    
    @Size(max = 40)
    @Column(name = "fsa_agua_vasos")
    private String fsaAguaVasos;
    
    @Column(name = "fsa_tiempo_tv")
    private Double fsaTiempoTv;
    
    @Column(name = "fsa_tiempo_tareas")
    private Double fsaTiempoTareas;
    
    @Column(name = "fsa_tiempo_dormir")
    private Double fsaTiempoDormir;
    
    @Column(name = "fsa_tiempo_familia")
    private Double fsaTiempoFamilia;
    
    @Column(name = "fsa_tiempo_recreacion")
    private Double fsaTiempoRecreacion;
    
    @Column(name = "fsa_tiempo_ejercicio")
    private Double fsaTiempoEjercicio;
    
    @Size(max = 100)
    @Column(name = "fsa_tipo_actividad_fisica")
    private String fsaTipoActividadFisica;
    
    @Size(max = 255)
    @Column(name = "fsa_comentarios")
    private String fsaComentarios;
    
    @Column(name = "fsa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime fsaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "fsa_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String fsaUltModUsuario;
    
    @Column(name = "fsa_version")
    @Version
    private Integer fsaVersion;
    
    @JoinColumn(name = "fsa_estudiante", referencedColumnName = "est_pk")
    @OneToOne
    private SgEstudiante fsaEstudiante;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_ficha_salud_enfer_no_transm",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "fsa_pk"),
            inverseJoinColumns = @JoinColumn(name = "ent_pk"))
    @NotAudited
    private List<SgEnfermedadNoTransmisible> fsaEnfermedadNoTransmitible;

    public SgFichaSalud() {
    }

    public Long getFsaPk() {
        return fsaPk;
    }

    public void setFsaPk(Long fsaPk) {
        this.fsaPk = fsaPk;
    }

    public Boolean getFsaLactancia() {
        return fsaLactancia;
    }

    public void setFsaLactancia(Boolean fsaLactancia) {
        this.fsaLactancia = fsaLactancia;
    }

    public Boolean getFsaPartoPrematuro() {
        return fsaPartoPrematuro;
    }

    public void setFsaPartoPrematuro(Boolean fsaPartoPrematuro) {
        this.fsaPartoPrematuro = fsaPartoPrematuro;
    }

    public String getFsaAlergicoMedicamentos() {
        return fsaAlergicoMedicamentos;
    }

    public void setFsaAlergicoMedicamentos(String fsaAlergicoMedicamentos) {
        this.fsaAlergicoMedicamentos = fsaAlergicoMedicamentos;
    }

    public EnumVacunas getFsaVacunasCompleto() {
        return fsaVacunasCompleto;
    }

    public void setFsaVacunasCompleto(EnumVacunas fsaVacunasCompleto) {
        this.fsaVacunasCompleto = fsaVacunasCompleto;
    }

    public EnumAntecedenteParto getFsaAntecedenteParto() {
        return fsaAntecedenteParto;
    }

    public void setFsaAntecedenteParto(EnumAntecedenteParto fsaAntecedenteParto) {
        this.fsaAntecedenteParto = fsaAntecedenteParto;
    }

    public EnumEdadGestacional getFsaEdadGestacional() {
        return fsaEdadGestacional;
    }

    public void setFsaEdadGestacional(EnumEdadGestacional fsaEdadGestacional) {
        this.fsaEdadGestacional = fsaEdadGestacional;
    }

    public Boolean getFsaEsAlergicoMedicamento() {
        return fsaEsAlergicoMedicamento;
    }

    public void setFsaEsAlergicoMedicamento(Boolean fsaEsAlergicoMedicamento) {
        this.fsaEsAlergicoMedicamento = fsaEsAlergicoMedicamento;
    }

    public Boolean getFsaEsAlergicoAlimento() {
        return fsaEsAlergicoAlimento;
    }

    public void setFsaEsAlergicoAlimento(Boolean fsaEsAlergicoAlimento) {
        this.fsaEsAlergicoAlimento = fsaEsAlergicoAlimento;
    }
    
    

    public String getFsaAlergicoAlimentos() {
        return fsaAlergicoAlimentos;
    }

    public void setFsaAlergicoAlimentos(String fsaAlergicoAlimentos) {
        this.fsaAlergicoAlimentos = fsaAlergicoAlimentos;
    }

    public String getFsaMedicamentoPrescriptor() {
        return fsaMedicamentoPrescriptor;
    }

    public void setFsaMedicamentoPrescriptor(String fsaMedicamentoPrescriptor) {
        this.fsaMedicamentoPrescriptor = fsaMedicamentoPrescriptor;
    }

    public String getFsaEnfermedadesPadece() {
        return fsaEnfermedadesPadece;
    }

    public void setFsaEnfermedadesPadece(String fsaEnfermedadesPadece) {
        this.fsaEnfermedadesPadece = fsaEnfermedadesPadece;
    }

    public String getFsaMedicamentoPermanente() {
        return fsaMedicamentoPermanente;
    }

    public void setFsaMedicamentoPermanente(String fsaMedicamentoPermanente) {
        this.fsaMedicamentoPermanente = fsaMedicamentoPermanente;
    }

    public String getFsaSituacionesDesencadenantes() {
        return fsaSituacionesDesencadenantes;
    }

    public void setFsaSituacionesDesencadenantes(String fsaSituacionesDesencadenantes) {
        this.fsaSituacionesDesencadenantes = fsaSituacionesDesencadenantes;
    }

    public String getFsaManifestacionesFisicasPsicologicas() {
        return fsaManifestacionesFisicasPsicologicas;
    }

    public void setFsaManifestacionesFisicasPsicologicas(String fsaManifestacionesFisicasPsicologicas) {
        this.fsaManifestacionesFisicasPsicologicas = fsaManifestacionesFisicasPsicologicas;
    }

    public String getFsaTiemposComida() {
        return fsaTiemposComida;
    }

    public void setFsaTiemposComida(String fsaTiemposComida) {
        this.fsaTiemposComida = fsaTiemposComida;
    }

    public Boolean getFsaConsumeFrutasVerduras() {
        return fsaConsumeFrutasVerduras;
    }

    public void setFsaConsumeFrutasVerduras(Boolean fsaConsumeFrutasVerduras) {
        this.fsaConsumeFrutasVerduras = fsaConsumeFrutasVerduras;
    }

    public String getFsaFrutasVerdurasDia() {
        return fsaFrutasVerdurasDia;
    }

    public void setFsaFrutasVerdurasDia(String fsaFrutasVerdurasDia) {
        this.fsaFrutasVerdurasDia = fsaFrutasVerdurasDia;
    }

    public Boolean getFsaConsumeAgua() {
        return fsaConsumeAgua;
    }

    public void setFsaConsumeAgua(Boolean fsaConsumeAgua) {
        this.fsaConsumeAgua = fsaConsumeAgua;
    }

    public String getFsaAguaVasos() {
        return fsaAguaVasos;
    }

    public void setFsaAguaVasos(String fsaAguaVasos) {
        this.fsaAguaVasos = fsaAguaVasos;
    }

    public Double getFsaTiempoTv() {
        return fsaTiempoTv;
    }

    public void setFsaTiempoTv(Double fsaTiempoTv) {
        this.fsaTiempoTv = fsaTiempoTv;
    }

    public Double getFsaTiempoTareas() {
        return fsaTiempoTareas;
    }

    public void setFsaTiempoTareas(Double fsaTiempoTareas) {
        this.fsaTiempoTareas = fsaTiempoTareas;
    }

    public Double getFsaTiempoDormir() {
        return fsaTiempoDormir;
    }

    public void setFsaTiempoDormir(Double fsaTiempoDormir) {
        this.fsaTiempoDormir = fsaTiempoDormir;
    }

    public Double getFsaTiempoFamilia() {
        return fsaTiempoFamilia;
    }

    public void setFsaTiempoFamilia(Double fsaTiempoFamilia) {
        this.fsaTiempoFamilia = fsaTiempoFamilia;
    }

    public Double getFsaTiempoRecreacion() {
        return fsaTiempoRecreacion;
    }

    public void setFsaTiempoRecreacion(Double fsaTiempoRecreacion) {
        this.fsaTiempoRecreacion = fsaTiempoRecreacion;
    }

    public Double getFsaTiempoEjercicio() {
        return fsaTiempoEjercicio;
    }

    public void setFsaTiempoEjercicio(Double fsaTiempoEjercicio) {
        this.fsaTiempoEjercicio = fsaTiempoEjercicio;
    }

    public String getFsaTipoActividadFisica() {
        return fsaTipoActividadFisica;
    }

    public void setFsaTipoActividadFisica(String fsaTipoActividadFisica) {
        this.fsaTipoActividadFisica = fsaTipoActividadFisica;
    }

    public String getFsaComentarios() {
        return fsaComentarios;
    }

    public void setFsaComentarios(String fsaComentarios) {
        this.fsaComentarios = fsaComentarios;
    }

    public LocalDateTime getFsaUltModFecha() {
        return fsaUltModFecha;
    }

    public void setFsaUltModFecha(LocalDateTime fsaUltModFecha) {
        this.fsaUltModFecha = fsaUltModFecha;
    }

    public String getFsaUltModUsuario() {
        return fsaUltModUsuario;
    }

    public void setFsaUltModUsuario(String fsaUltModUsuario) {
        this.fsaUltModUsuario = fsaUltModUsuario;
    }

    public Integer getFsaVersion() {
        return fsaVersion;
    }

    public void setFsaVersion(Integer fsaVersion) {
        this.fsaVersion = fsaVersion;
    }

    public SgEstudiante getFsaEstudiante() {
        return fsaEstudiante;
    }

    public void setFsaEstudiante(SgEstudiante fsaEstudiante) {
        this.fsaEstudiante = fsaEstudiante;
    }

    public List<SgEnfermedadNoTransmisible> getFsaEnfermedadNoTransmitible() {
        return fsaEnfermedadNoTransmitible;
    }

    public void setFsaEnfermedadNoTransmitible(List<SgEnfermedadNoTransmisible> fsaEnfermedadNoTransmitible) {
        this.fsaEnfermedadNoTransmitible = fsaEnfermedadNoTransmitible;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fsaPk != null ? fsaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgFichaSalud)) {
            return false;
        }
        SgFichaSalud other = (SgFichaSalud) object;
        if ((this.fsaPk == null && other.fsaPk != null) || (this.fsaPk != null && !this.fsaPk.equals(other.fsaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgFichaSalud[ fsaPk=" + fsaPk + " ]";
    }
    
}
