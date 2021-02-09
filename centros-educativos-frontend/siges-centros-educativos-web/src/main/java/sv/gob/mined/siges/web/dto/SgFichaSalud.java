/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import sv.gob.mined.siges.web.dto.catalogo.SgEnfermedadNoTransmisible;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "fsaPk", scope = SgFichaSalud.class)
public class SgFichaSalud implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long fsaPk;

    private Boolean fsaLactancia;

    private Boolean fsaPartoPrematuro;

    private Boolean fsaEsAlergicoMedicamento;

    private String fsaAlergicoMedicamentos;

    private Boolean fsaEsAlergicoAlimento;

    private String fsaAlergicoAlimentos;

    private String fsaMedicamentoPrescriptor;

    private String fsaEnfermedadesPadece;

    private String fsaMedicamentoPermanente;

    private String fsaSituacionesDesencadenantes;

    private String fsaManifestacionesFisicasPsicologicas;

    private String fsaTiemposComida;

    private Boolean fsaConsumeFrutasVerduras;

    private String fsaFrutasVerdurasDia;

    private Boolean fsaConsumeAgua;

    private String fsaAguaVasos;

    private Double fsaTiempoTv;

    private Double fsaTiempoTareas;

    private Double fsaTiempoDormir;

    private Double fsaTiempoFamilia;

    private Double fsaTiempoRecreacion;

    private Double fsaTiempoEjercicio;

    private String fsaTipoActividadFisica;

    private String fsaComentarios;

    private LocalDateTime fsaUltModFecha;

    private String fsaUltModUsuario;

    private Integer fsaVersion;

    private SgEstudiante fsaEstudiante;
    
    private List<SgEnfermedadNoTransmisible> fsaEnfermedadNoTransmitible;

    public SgFichaSalud() {
        fsaEsAlergicoAlimento=Boolean.FALSE;
        fsaEsAlergicoMedicamento=Boolean.FALSE;
    }
    
    @JsonIgnore
    public String getENTAsString() {
        if (this.fsaEnfermedadNoTransmitible != null) {
            return this.fsaEnfermedadNoTransmitible.stream().map(d -> d.getEntNombre()).collect(Collectors.joining(", "));
        }
        return null;
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
