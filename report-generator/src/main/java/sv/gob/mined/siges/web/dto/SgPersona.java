/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.web.dto.catalogo.SgEscolaridad;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgNacionalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.dto.catalogo.SgTrastornoAprendizaje;
import sv.gob.mined.siges.web.enumerados.EnumEstadoPersona;
import sv.gob.mined.siges.web.mensajes.Etiquetas;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "perPk", scope = SgPersona.class)
public class SgPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long perPk;

    private String perPrimerNombre;

    private String perSegundoNombre;

    private String perTercerNombre;

    private String perPrimerApellido;

    private String perSegundoApellido;

    private String perTercerApellido;

    private String perPrimerNombreBusqueda;

    private String perSegundoNombreBusqueda;

    private String perTercerNombreBusqueda;

    private String perPrimerApellidoBusqueda;

    private String perSegundoApellidoBusqueda;

    private String perTercerApellidoBusqueda;

    private LocalDate perFechaNacimiento;

    private SgArchivo perFoto;

    private String perEmail;

    private Integer perCantidadHijos;

    private Boolean perHabilitado;

    private LocalDateTime perUltModFecha;

    private String perUltModUsuario;

    private Integer perVersion;

    private SgEstudiante perEstudiante;

    private String perDui;

    private Long perCun;

    private Long perNie;

    private Long perNip;

    private String perNit;

    private String perInpep;

    private String perIsss;

    private String perNup;

    private Boolean perNaturalizada;

    private Long perPartidaNacimiento;

    private String perPartidaNacimientoFolio;

    private String perPartidaNacimientoLibro;

    private Boolean perPartidaNacimientoPresenta;

    private String perNombreBusqueda;

    private String perLugarTrabajo;

    private Boolean perTrabaja;

    private Boolean perEmbarazo;

    private LocalDate perFechaParto;

    private String perJornadaLaboral;

    private Double perSalario;

    private String perPropiedadVivienda;

    private String perServiciosBasicos;

    private Boolean perAccesoInternet;

    private Boolean perRecibeRemesas;

    private Integer perFamiliaresEmigrados;

    private String perNombrePartidaNacimiento;

    private SgNacionalidad perNacionalidad;

    private List<SgDiscapacidad> perDiscapacidades;

    private List<SgTrastornoAprendizaje> perTrastornosAprendizaje;

    private SgEstadoCivil perEstadoCivil;

    private SgSexo perSexo;

    private List<SgAllegado> perAllegados;

    private List<SgTelefono> perTelefonos;

    private List<SgIdentificacion> perIdentificaciones;

    private SgOcupacion perOcupacion;

    private SgDireccion perDireccion;

    private SgProfesion perProfesion;

    private SgEscolaridad perEscolaridad;

    private EnumEstadoPersona perEstado;

    public SgPersona() {
        this.perPartidaNacimientoPresenta = Boolean.FALSE;
        this.perNaturalizada = Boolean.TRUE;
        this.perTrabaja = Boolean.FALSE;
        this.perEmbarazo = Boolean.FALSE;
        this.perAccesoInternet = Boolean.FALSE;
        this.perRecibeRemesas = Boolean.FALSE;
    }

    public SgPersona(Long perPk) {
        this.perPk = perPk;
    }

    public String getPerNombreApellido() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerApellido != null) {
            s.append(this.perPrimerApellido);
        }
        if (this.perPrimerNombre != null) {
            s.append(", ").append(this.perPrimerNombre);
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public String getPerNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerApellido != null) {
            s.append(this.perPrimerApellido);
        }
        if (this.perSegundoApellido != null) {
            s.append(" ").append(this.perSegundoApellido);
        }
        if (this.perPrimerNombre != null) {
            s.append(", ").append(this.perPrimerNombre);
        }
        if (this.perSegundoNombre != null) {
            s.append(" ").append(this.perSegundoNombre);
        }
        if (this.perTercerNombre != null) {
            s.append(" ").append(this.perTercerNombre);
        }

        return SofisStringUtils.normalizarString(s.toString());
    }

    @JsonIgnore
    public String getPerNombreCompletoNP2() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerNombre != null) {
            s.append(this.perPrimerNombre).append(" ");
        }
        if (this.perSegundoNombre != null) {
            s.append(this.perSegundoNombre).append(" ");
        }
        if (this.perTercerNombre != null) {
            s.append(this.perTercerNombre).append(" ");
        }
        if (this.perPrimerApellido != null) {
            s.append(this.perPrimerApellido).append(" ");
        }
        if (this.perSegundoApellido != null) {
            s.append(this.perSegundoApellido).append(" ");
        }
        if (this.perTercerApellido != null) {
            s.append(this.perTercerApellido).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public String getPerNombreCompletoNP() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerNombre != null) {
            s.append(this.perPrimerNombre);
        }
        if (this.perSegundoNombre != null) {
            s.append(" ").append(this.perSegundoNombre);
        }
        if (this.perTercerNombre != null) {
            s.append(" ").append(this.perTercerNombre);
        }
        if (this.perPrimerApellido != null) {
            s.append(" ").append(this.perPrimerApellido);
        }
        if (this.perSegundoApellido != null) {
            s.append(" ").append(this.perSegundoApellido);
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public String getPerIdentificacionesAsString() {
        StringBuilder s = new StringBuilder();
        if (this.perNie != null) {
            s.append(Etiquetas.getValue("nie")).append(" ").append(this.perNie).append(".").append(System.lineSeparator());
        }
        if (this.perCun != null) {
            s.append(Etiquetas.getValue("cun")).append(" ").append(this.perCun).append(".").append(System.lineSeparator());
        }
        if (!StringUtils.isBlank(this.perDui)) {
            s.append(Etiquetas.getValue("dui")).append(" ").append(this.perDui).append(".").append(System.lineSeparator());
        }
        if (this.perNip != null) {
            s.append(Etiquetas.getValue("nip")).append(" ").append(this.perNip).append(".").append(System.lineSeparator());
        }
        if (!StringUtils.isBlank(this.perNit)) {
            s.append(Etiquetas.getValue("nit")).append(" ").append(this.perNit).append(".").append(System.lineSeparator());
        }
        if (!StringUtils.isBlank(this.perNup)) {
            s.append(Etiquetas.getValue("nup")).append(" ").append(this.perNup).append(".").append(System.lineSeparator());
        }
        if (!StringUtils.isBlank(this.perIsss)) {
            s.append(Etiquetas.getValue("isss")).append(" ").append(this.perIsss).append(".").append(System.lineSeparator());
        }
        if (!StringUtils.isBlank(this.perInpep)) {
            s.append(Etiquetas.getValue("inpep")).append(" ").append(this.perInpep).append(".").append(System.lineSeparator());
        }
        if (this.perPartidaNacimiento != null) {
            s.append(Etiquetas.getValue("partidaNacimiento")).append(" ").append(this.perPartidaNacimiento).append(". ");
        }
        if (this.perPartidaNacimientoFolio != null) {
            s.append(Etiquetas.getValue("folioPartida")).append(" ").append(this.perPartidaNacimientoFolio).append(". ");
        }
        if (this.perPartidaNacimientoLibro != null) {
            s.append(Etiquetas.getValue("libroPartida")).append(" ").append(this.perPartidaNacimientoLibro).append(". ");
        }
        if (this.perPartidaNacimiento != null || this.perPartidaNacimientoFolio != null || this.perPartidaNacimientoLibro != null) {
            s.append("\n");
        }
//        if (this.perIdentificaciones != null && !this.perIdentificaciones.isEmpty()) {
//            for (SgIdentificacion i : this.perIdentificaciones) {
//                s.append(Etiquetas.getValue("tipoDocumento")).append(" ").append(i.getIdeTipoDocumento().getTinNombre()).append(", ");
//                s.append(Etiquetas.getValue("paisEmisor")).append(" ").append(i.getIdePaisEmisor().getPaiNombre()).append(", ");
//                s.append(Etiquetas.getValue("hnumeroDocumento")).append(" ").append(i.getIdeNumeroDocumento()).append(".").append(System.lineSeparator());
//            }
//        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    @JsonIgnore
    public String getDiscapacidadesAsString() {
        if (this.perDiscapacidades != null) {
            return this.perDiscapacidades.stream().map(d -> d.getDisNombre()).collect(Collectors.joining(", "));
        }
        return null;
    }

    @JsonIgnore
    public String getTrastornosAprendizajeAsString() {
        if (this.perTrastornosAprendizaje != null) {
            return this.perTrastornosAprendizaje.stream().map(d -> d.getTraNombre()).collect(Collectors.joining(", "));
        }
        return null;
    }

    @JsonIgnore
    public String getTelefonosAsString() {
        if (this.perTelefonos != null) {
            return this.perTelefonos.stream().map(d -> d.getTelTelefono()).collect(Collectors.joining(", "));
        }
        return null;
    }

    public Long getPerPk() {
        return perPk;
    }

    public void setPerPk(Long perPk) {
        this.perPk = perPk;
    }

    public String getPerPrimerNombre() {
        return perPrimerNombre;
    }

    public void setPerPrimerNombre(String perPrimerNombre) {
        this.perPrimerNombre = perPrimerNombre;
    }

    public String getPerSegundoNombre() {
        return perSegundoNombre;
    }

    public void setPerSegundoNombre(String perSegundoNombre) {
        this.perSegundoNombre = perSegundoNombre;
    }

    public String getPerTercerNombre() {
        return perTercerNombre;
    }

    public void setPerTercerNombre(String perTercerNombre) {
        this.perTercerNombre = perTercerNombre;
    }

    public String getPerPrimerApellido() {
        return perPrimerApellido;
    }

    public void setPerPrimerApellido(String perPrimerApellido) {
        this.perPrimerApellido = perPrimerApellido;
    }

    public String getPerSegundoApellido() {
        return perSegundoApellido;
    }

    public void setPerSegundoApellido(String perSegundoApellido) {
        this.perSegundoApellido = perSegundoApellido;
    }

    public String getPerTercerApellido() {
        return perTercerApellido;
    }

    public void setPerTercerApellido(String perTercerApellido) {
        this.perTercerApellido = perTercerApellido;
    }

    public String getPerPrimerNombreBusqueda() {
        return perPrimerNombreBusqueda;
    }

    public void setPerPrimerNombreBusqueda(String perPrimerNombreBusqueda) {
        this.perPrimerNombreBusqueda = perPrimerNombreBusqueda;
    }

    public String getPerSegundoNombreBusqueda() {
        return perSegundoNombreBusqueda;
    }

    public void setPerSegundoNombreBusqueda(String perSegundoNombreBusqueda) {
        this.perSegundoNombreBusqueda = perSegundoNombreBusqueda;
    }

    public String getPerTercerNombreBusqueda() {
        return perTercerNombreBusqueda;
    }

    public void setPerTercerNombreBusqueda(String perTercerNombreBusqueda) {
        this.perTercerNombreBusqueda = perTercerNombreBusqueda;
    }

    public String getPerPrimerApellidoBusqueda() {
        return perPrimerApellidoBusqueda;
    }

    public void setPerPrimerApellidoBusqueda(String perPrimerApellidoBusqueda) {
        this.perPrimerApellidoBusqueda = perPrimerApellidoBusqueda;
    }

    public String getPerSegundoApellidoBusqueda() {
        return perSegundoApellidoBusqueda;
    }

    public void setPerSegundoApellidoBusqueda(String perSegundoApellidoBusqueda) {
        this.perSegundoApellidoBusqueda = perSegundoApellidoBusqueda;
    }

    public String getPerTercerApellidoBusqueda() {
        return perTercerApellidoBusqueda;
    }

    public void setPerTercerApellidoBusqueda(String perTercerApellidoBusqueda) {
        this.perTercerApellidoBusqueda = perTercerApellidoBusqueda;
    }

    public LocalDate getPerFechaNacimiento() {
        return perFechaNacimiento;
    }

    public void setPerFechaNacimiento(LocalDate perFechaNacimiento) {
        this.perFechaNacimiento = perFechaNacimiento;
    }

    public String getPerEmail() {
        return perEmail;
    }

    public void setPerEmail(String perEmail) {
        this.perEmail = perEmail;
    }

    public Integer getPerCantidadHijos() {
        return perCantidadHijos;
    }

    public void setPerCantidadHijos(Integer perCantidadHijos) {
        this.perCantidadHijos = perCantidadHijos;
    }

    public Boolean getPerHabilitado() {
        return perHabilitado;
    }

    public void setPerHabilitado(Boolean perHabilitado) {
        this.perHabilitado = perHabilitado;
    }

    public LocalDateTime getPerUltModFecha() {
        return perUltModFecha;
    }

    public void setPerUltModFecha(LocalDateTime perUltModFecha) {
        this.perUltModFecha = perUltModFecha;
    }

    public String getPerUltModUsuario() {
        return perUltModUsuario;
    }

    public void setPerUltModUsuario(String perUltModUsuario) {
        this.perUltModUsuario = perUltModUsuario;
    }

    public Integer getPerVersion() {
        return perVersion;
    }

    public void setPerVersion(Integer perVersion) {
        this.perVersion = perVersion;
    }

    public SgEstudiante getPerEstudiante() {
        return perEstudiante;
    }

    public void setPerEstudiante(SgEstudiante perEstudiante) {
        this.perEstudiante = perEstudiante;
    }

    public String getPerDui() {
        return perDui;
    }

    public void setPerDui(String perDui) {
        this.perDui = perDui;
    }

    public Long getPerCun() {
        return perCun;
    }

    public void setPerCun(Long perCun) {
        this.perCun = perCun;
    }

    public Long getPerNie() {
        return perNie;
    }

    public void setPerNie(Long perNie) {
        this.perNie = perNie;
    }

    public Long getPerNip() {
        return perNip;
    }

    public void setPerNip(Long perNip) {
        this.perNip = perNip;
    }

    public Long getPerPartidaNacimiento() {
        return perPartidaNacimiento;
    }

    public void setPerPartidaNacimiento(Long perPartidaNacimiento) {
        this.perPartidaNacimiento = perPartidaNacimiento;
    }

    public String getPerPartidaNacimientoFolio() {
        return perPartidaNacimientoFolio;
    }

    public void setPerPartidaNacimientoFolio(String perPartidaNacimientoFolio) {
        this.perPartidaNacimientoFolio = perPartidaNacimientoFolio;
    }

    public String getPerPartidaNacimientoLibro() {
        return perPartidaNacimientoLibro;
    }

    public void setPerPartidaNacimientoLibro(String perPartidaNacimientoLibro) {
        this.perPartidaNacimientoLibro = perPartidaNacimientoLibro;
    }

    public String getPerLugarTrabajo() {
        return perLugarTrabajo;
    }

    public void setPerLugarTrabajo(String perLugarTrabajo) {
        this.perLugarTrabajo = perLugarTrabajo;
    }

    public String getPerNombreBusqueda() {
        return perNombreBusqueda;
    }

    public void setPerNombreBusqueda(String perNombreBusqueda) {
        this.perNombreBusqueda = perNombreBusqueda;
    }

    public Boolean getPerPartidaNacimientoPresenta() {
        return perPartidaNacimientoPresenta;
    }

    public void setPerPartidaNacimientoPresenta(Boolean perPartidaNacimientoPresenta) {
        this.perPartidaNacimientoPresenta = perPartidaNacimientoPresenta;
    }

    public String getPerNit() {
        return perNit;
    }

    public void setPerNit(String perNit) {
        this.perNit = perNit;
    }

    public Boolean getPerNaturalizada() {
        return perNaturalizada;
    }

    public void setPerNaturalizada(Boolean perNaturalizada) {
        this.perNaturalizada = perNaturalizada;
    }

    public String getPerInpep() {
        return perInpep;
    }

    public void setPerInpep(String perInpep) {
        this.perInpep = perInpep;
    }

    public String getPerIsss() {
        return perIsss;
    }

    public void setPerIsss(String perIsss) {
        this.perIsss = perIsss;
    }

    public String getPerNup() {
        return perNup;
    }

    public void setPerNup(String perNup) {
        this.perNup = perNup;
    }

    public Boolean getPerEmbarazo() {
        return perEmbarazo;
    }

    public void setPerEmbarazo(Boolean perEmbarazo) {
        this.perEmbarazo = perEmbarazo;
    }

    public LocalDate getPerFechaParto() {
        return perFechaParto;
    }

    public void setPerFechaParto(LocalDate perFechaParto) {
        this.perFechaParto = perFechaParto;
    }

    public String getPerJornadaLaboral() {
        return perJornadaLaboral;
    }

    public void setPerJornadaLaboral(String perJornadaLaboral) {
        this.perJornadaLaboral = perJornadaLaboral;
    }

    public Double getPerSalario() {
        return perSalario;
    }

    public void setPerSalario(Double perSalario) {
        this.perSalario = perSalario;
    }

    public String getPerPropiedadVivienda() {
        return perPropiedadVivienda;
    }

    public void setPerPropiedadVivienda(String perPropiedadVivienda) {
        this.perPropiedadVivienda = perPropiedadVivienda;
    }

    public String getPerServiciosBasicos() {
        return perServiciosBasicos;
    }

    public void setPerServiciosBasicos(String perServiciosBasicos) {
        this.perServiciosBasicos = perServiciosBasicos;
    }

    public Boolean getPerAccesoInternet() {
        return perAccesoInternet;
    }

    public void setPerAccesoInternet(Boolean perAccesoInternet) {
        this.perAccesoInternet = perAccesoInternet;
    }

    public Boolean getPerRecibeRemesas() {
        return perRecibeRemesas;
    }

    public void setPerRecibeRemesas(Boolean perRecibeRemesas) {
        this.perRecibeRemesas = perRecibeRemesas;
    }

    public Boolean getPerTrabaja() {
        return perTrabaja;
    }

    public void setPerTrabaja(Boolean perTrabaja) {
        this.perTrabaja = perTrabaja;
    }

    public Integer getPerFamiliaresEmigrados() {
        return perFamiliaresEmigrados;
    }

    public void setPerFamiliaresEmigrados(Integer perFamiliaresEmigrados) {
        this.perFamiliaresEmigrados = perFamiliaresEmigrados;
    }

    public String getPerNombrePartidaNacimiento() {
        return perNombrePartidaNacimiento;
    }

    public void setPerNombrePartidaNacimiento(String perNombrePartidaNacimiento) {
        this.perNombrePartidaNacimiento = perNombrePartidaNacimiento;
    }

    public List<SgAllegado> getPerAllegados() {
        return perAllegados;
    }

    public void setPerAllegados(List<SgAllegado> perAllegados) {
        this.perAllegados = perAllegados;
    }

    public SgArchivo getPerFoto() {
        return perFoto;
    }

    public void setPerFoto(SgArchivo perFoto) {
        this.perFoto = perFoto;
    }

    public SgOcupacion getPerOcupacion() {
        return perOcupacion;
    }

    public void setPerOcupacion(SgOcupacion perOcupacion) {
        this.perOcupacion = perOcupacion;
    }

    public SgDireccion getPerDireccion() {
        return perDireccion;
    }

    public void setPerDireccion(SgDireccion perDireccion) {
        this.perDireccion = perDireccion;
    }

    public SgProfesion getPerProfesion() {
        return perProfesion;
    }

    public void setPerProfesion(SgProfesion perProfesion) {
        this.perProfesion = perProfesion;
    }

    public List<SgIdentificacion> getPerIdentificaciones() {
        return perIdentificaciones;
    }

    public void setPerIdentificaciones(List<SgIdentificacion> perIdentificaciones) {
        this.perIdentificaciones = perIdentificaciones;
    }

    public SgNacionalidad getPerNacionalidad() {
        return perNacionalidad;
    }

    public void setPerNacionalidad(SgNacionalidad perNacionalidad) {
        this.perNacionalidad = perNacionalidad;
    }

    public SgEstadoCivil getPerEstadoCivil() {
        return perEstadoCivil;
    }

    public void setPerEstadoCivil(SgEstadoCivil perEstadoCivil) {
        this.perEstadoCivil = perEstadoCivil;
    }

    public List<SgDiscapacidad> getPerDiscapacidades() {
        return perDiscapacidades;
    }

    public void setPerDiscapacidades(List<SgDiscapacidad> perDiscapacidades) {
        this.perDiscapacidades = perDiscapacidades;
    }

    public EnumEstadoPersona getPerEstado() {
        return perEstado;
    }

    public void setPerEstado(EnumEstadoPersona perEstado) {
        this.perEstado = perEstado;
    }

    public SgSexo getPerSexo() {
        return perSexo;
    }

    public void setPerSexo(SgSexo perSexo) {
        this.perSexo = perSexo;
    }

    public List<SgTelefono> getPerTelefonos() {
        return perTelefonos;
    }

    public void setPerTelefonos(List<SgTelefono> perTelefonos) {
        this.perTelefonos = perTelefonos;
    }

    public SgEscolaridad getPerEscolaridad() {
        return perEscolaridad;
    }

    public void setPerEscolaridad(SgEscolaridad perEscolaridad) {
        this.perEscolaridad = perEscolaridad;
    }

    public List<SgTrastornoAprendizaje> getPerTrastornosAprendizaje() {
        return perTrastornosAprendizaje;
    }

    public void setPerTrastornosAprendizaje(List<SgTrastornoAprendizaje> perTrastornosAprendizaje) {
        this.perTrastornosAprendizaje = perTrastornosAprendizaje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perPk != null ? perPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgPersona other = (SgPersona) obj;
        return this.perPk != null && this.perPk.equals(other.perPk);
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesPersona[ perPk=" + perPk + " ]";
    }

}
