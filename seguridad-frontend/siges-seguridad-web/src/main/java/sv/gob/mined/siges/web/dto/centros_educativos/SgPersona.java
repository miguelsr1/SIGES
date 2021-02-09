/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgEscolaridad;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgEtnia;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgOcupacion;
import sv.gob.mined.siges.web.dto.catalogo.SgProfesion;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoSangre;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTrabajo;
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

    private String perEmail;

    private SgEtnia perEtnia;

    private SgEstadoCivil perEstadoCivil;

    private SgSexo perSexo;

    private SgTipoSangre perTipoSangre;

    private Integer perCantidadHijos;

    private Boolean perHabilitado;

    private LocalDateTime perUltModFecha;

    private String perUltModUsuario;

    private Integer perVersion;

    private SgDireccion perDireccion;

    private SgEstudiante perEstudiante;

    private List<SgTelefono> perTelefonos;

    private List<SgIdentificacion> perIdentificaciones;

    private String perDui;

    private Long perCun;

    private Long perNie;

    private String perNip;

    private String perNit;

    private String perInpep;

    private String perIsss;

    private String perNup;

    private Boolean perNaturalizada;

    private Long perPartidaNacimiento;
    
    private Integer perPartidaNacimientoAnio;

    private String perPartidaNacimientoFolio;

    private String perPartidaNacimientoLibro;

    private String perPartidaNacimientoComplemento;
    
    private SgArchivo perPartidaNacimientoArchivo;

    private Boolean perPartidaNacimientoPresenta;
    
    private SgDepartamento perPartidaDepartamento;
    
    private SgMunicipio perPartidaMunicipio;

    private SgDepartamento perDepartamentoNacimento;

    private SgMunicipio perMunicipioNacimiento;

    private String perNombreBusqueda;

    private String perLugarTrabajo;

    private SgArchivo perFoto;

    private SgNacionalidad perNacionalidad;

    private List<SgAllegado> perAllegados;

    private SgProfesion perProfesion;

    private SgOcupacion perOcupacion;

    private SgEscolaridad perEscolaridad;
    
    private Boolean perTrabaja;

    private SgTipoTrabajo perTipoTrabajo;

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

    private SgAllegado perResponsable;
    
    private Boolean perTieneIdentificacion;
    
    private Long perUsuarioPk;

    //View Objects
    private Boolean perSeBuscoEnBd; //Se buscó en bd
    private Boolean perSeEncontroIdentificacion; //Se encontró persona en bd
    private Boolean perSeIngresoNuevaIdentificacion;

    public SgPersona() {
        this.perHabilitado = Boolean.TRUE;
        this.perTelefonos = new ArrayList<>();
        this.perIdentificaciones = new ArrayList<>();
        this.perDireccion = new SgDireccion();
        this.perPartidaNacimientoPresenta = Boolean.FALSE;
        this.perAllegados = new ArrayList();
        this.perNaturalizada = Boolean.TRUE;
        this.perTrabaja = Boolean.FALSE;
        this.perSeBuscoEnBd = Boolean.FALSE;
        this.perSeEncontroIdentificacion = Boolean.FALSE;
        this.perEmbarazo = Boolean.FALSE;
        this.perAccesoInternet = Boolean.FALSE;
        this.perRecibeRemesas = Boolean.FALSE;
        this.perSeIngresoNuevaIdentificacion = Boolean.FALSE;
        this.perTieneIdentificacion = Boolean.FALSE;
    }

    public SgPersona(Long perPk) {
        this.perPk = perPk;
    }

    @JsonIgnore
    public void normalizarDatos() {
        if (this.perDireccion == null) {
            this.perDireccion = new SgDireccion();
        }
        if (this.perPartidaNacimientoPresenta == null) {
            perPartidaNacimientoPresenta = Boolean.FALSE;
        }
    }
    
    @JsonIgnore
    public Boolean getPerEsMayorDeEdad(){
        if (this.perFechaNacimiento != null){    
            if (Period.between(perFechaNacimiento, LocalDate.now()).getYears() >= Constantes.EDAD_PERSONA_MAYOR){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @JsonIgnore
    public String getPerNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerApellido != null) {
            s.append(this.perPrimerApellido).append(" ");
        }
        if (this.perSegundoApellido != null) {
            s.append(this.perSegundoApellido).append(" ");
        }
        if (this.perTercerApellido != null) {
            s.append(this.perTercerApellido).append(" ");
        }
        if (this.perPrimerNombre != null) {
            s.append(this.perPrimerNombre).append(" ");
        }
        if (this.perSegundoNombre != null) {
            s.append(this.perSegundoNombre).append(" ");
        }
        if (this.perTercerNombre != null) {
            s.append(this.perTercerNombre).append(" ");
        }        
        return SofisStringUtils.normalizarString(s.toString());
    }
    
    @JsonIgnore
    public String getPerDuiNombreCompleto(){
        StringBuilder s = new StringBuilder();
        if (this.perDui != null) {
            s.append(this.perDui).append(" - ");
        }
        s.append(this.getPerNombreCompleto());
        return s.toString();
    }

    @JsonIgnore
    public String getPerNombreCompletoOrder() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerApellido != null) {
            s.append(this.perPrimerApellido).append(" ");
        }
        if (this.perSegundoApellido != null) {
            s.append(this.perSegundoApellido).append(" ");
        }
        if (this.perTercerApellido != null) {
            s.append(this.perTercerApellido).append(" ");
        }
        if (this.perPrimerNombre != null) {
            s.append(this.perPrimerNombre).append(" ");
        }
        if (this.perSegundoNombre != null) {
            s.append(this.perSegundoNombre).append(" ");
        }
        if (this.perTercerNombre != null) {
            s.append(this.perTercerNombre).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    @JsonIgnore
    public String getPerPartidaNacimientoCompleta() {
        StringBuilder s = new StringBuilder();
        if (this.perPartidaNacimiento != null) {
            s.append("N:");
            s.append(this.perPartidaNacimiento).append(" ");

            if (!StringUtils.isBlank(this.perPartidaNacimientoFolio)) {
                s.append("F:");
                s.append(this.perPartidaNacimientoFolio).append(" ");
            }
            if (!StringUtils.isBlank(this.perPartidaNacimientoLibro)) {
                s.append("L:");
                s.append(this.perPartidaNacimientoLibro).append(" ");
            }
            if (!StringUtils.isBlank(this.perPartidaNacimientoComplemento)) {
                s.append("C:");
                s.append(this.perPartidaNacimientoComplemento).append(" ");
            }
            if (this.perPartidaNacimientoAnio != null) {
                s.append("A:");
                s.append(this.perPartidaNacimientoAnio).append(" ");
            }
            if (this.perPartidaDepartamento != null){
                s.append(this.perPartidaDepartamento.getDepNombre()).append(" ");
            }
            if (this.perPartidaMunicipio != null){
                s.append(this.perPartidaMunicipio.getMunNombre());
            }
        }
        return s.toString();
    }

    @JsonIgnore
    public Boolean getPerEsNacionalidadSalvador() {
        if (this.perNacionalidad != null) {
            return this.perNacionalidad.getNacCodigo().equalsIgnoreCase(Constantes.CODIGO_NACIONALIDAD_EL_SALVADOR);
        }
        return Boolean.FALSE;
    }

    /*
        Las identificaciones de la base de datos tienen precedencia sobre las ingresadas.
        Si se ingresan nuevas identificaciones, la persona retornada de la base es actualizada con ellas.
    */
    @JsonIgnore
    public void actualizarIdentificaciones(SgPersona pers) {
        this.perSeIngresoNuevaIdentificacion = Boolean.FALSE;

        if (this.perNie == null) {
            this.perNie = pers.getPerNie();
            if (this.perNie != null) {
                this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
            }
        }

        if (this.perDui == null) {
            this.perDui = pers.getPerDui();
            if (this.perDui != null) {
                this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
            }
        }

        if (this.perCun == null) {
            this.perCun = pers.getPerCun();
            if (this.perCun != null) {
                this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
            }
        }

        if (this.perNip == null) {
            this.perNip = pers.getPerNip();
            if (this.perNip != null) {
                this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
            }
        }

        if (this.perNit == null) {
            this.perNit = pers.getPerNit();
            if (this.perNit != null) {
                this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
            }
        }

        if (this.perInpep == null) {
            this.perInpep = pers.getPerInpep();
            if (this.perInpep != null) {
                this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
            }
        }

        if (this.perIsss == null) {
            this.perIsss = pers.getPerIsss();
            if (this.perIsss != null) {
                this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
            }
        }

        if (this.perNup == null) {
            this.perNup = pers.getPerNup();
            if (this.perNup != null) {
                this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
            }
        }

        if (this.perPartidaNacimiento == null) {
            this.perPartidaNacimiento = pers.getPerPartidaNacimiento();
        }

        if (this.perPartidaNacimientoFolio == null) {
            this.perPartidaNacimientoFolio = pers.getPerPartidaNacimientoFolio();
        }

        if (this.perPartidaNacimientoLibro == null) {
            this.perPartidaNacimientoLibro = pers.getPerPartidaNacimientoLibro();
        }

        if (BooleanUtils.isTrue(pers.getPerPartidaNacimientoPresenta())) {
            this.perPartidaNacimientoPresenta = Boolean.TRUE;
        }

        if (pers.getPerIdentificaciones() != null && !pers.getPerIdentificaciones().isEmpty()) {
            if (this.perIdentificaciones == null) {
                this.perIdentificaciones = new ArrayList<>();
            }
            for (SgIdentificacion i : pers.getPerIdentificaciones()) {
                Boolean existe = Boolean.FALSE;
                for (SgIdentificacion id : this.perIdentificaciones) {
                    if (id.equalsByDocumento(i)) {
                        existe = Boolean.TRUE;
                    }
                }
                if (!existe) {
                    this.perSeIngresoNuevaIdentificacion = Boolean.TRUE;
                    this.perIdentificaciones.add(i);
                }
            }
        }
    }
    
    public String getNieNombre(){
        return this.perNie+" - "+this.getPerNombreCompleto();
    }
    
    
    @JsonIgnore
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
        if (this.perIdentificaciones != null && !this.perIdentificaciones.isEmpty()) {
            for (SgIdentificacion i : this.perIdentificaciones) {
                s.append(Etiquetas.getValue("tipoDocumento")).append(" ").append(i.getIdeTipoDocumento().getTinNombre()).append(", ");
                s.append(Etiquetas.getValue("paisEmisor")).append(" ").append(i.getIdePaisEmisor().getPaiNombre()).append(", ");
                s.append(Etiquetas.getValue("hnumeroDocumento")).append(" ").append(i.getIdeNumeroDocumento()).append(".").append(System.lineSeparator());
            }
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    @JsonIgnore
    public Boolean getPerIngresoAlgunaIdentificacion() {
        return this.perCun != null
                || this.perNie != null
                || !StringUtils.isBlank(this.perDui)
                || !StringUtils.isBlank(this.perNit)
                || !StringUtils.isBlank(this.perNup)
                || !StringUtils.isBlank(this.perIsss)
                || !StringUtils.isBlank(this.perInpep)
                || this.perNip != null
                || (this.perIdentificaciones != null && !this.perIdentificaciones.isEmpty());
    }

    @JsonIgnore
    public Boolean getPerIngresoIdentificacionObligatoriaPersonalSede() {
        return !StringUtils.isBlank(this.perNit);
    }

    @JsonIgnore
    public Boolean getRenderBuscarPersona() {
        return this.getPerPk() == null
                && BooleanUtils.isTrue(this.getPerTieneIdentificacion())
                && BooleanUtils.isFalse(this.getPerSeBuscoEnBd());
    }

    public Long getPerPk() {
        return perPk;
    }

    public void setPerPk(Long perPk) {
        this.perPk = perPk;
    }

    public Boolean getPerTieneIdentificacion() {
        return perTieneIdentificacion;
    }

    public void setPerTieneIdentificacion(Boolean perTieneIdentificacion) {
        this.perTieneIdentificacion = perTieneIdentificacion;
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

    public SgEtnia getPerEtnia() {
        return perEtnia;
    }

    public void setPerEtnia(SgEtnia perEtnia) {
        this.perEtnia = perEtnia;
    }

    public SgEstadoCivil getPerEstadoCivil() {
        return perEstadoCivil;
    }

    public void setPerEstadoCivil(SgEstadoCivil perEstadoCivil) {
        this.perEstadoCivil = perEstadoCivil;
    }

    public SgSexo getPerSexo() {
        return perSexo;
    }

    public void setPerSexo(SgSexo perSexo) {
        this.perSexo = perSexo;
    }

    public SgTipoSangre getPerTipoSangre() {
        return perTipoSangre;
    }

    public void setPerTipoSangre(SgTipoSangre perTipoSangre) {
        this.perTipoSangre = perTipoSangre;
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

    public SgDireccion getPerDireccion() {
        return perDireccion;
    }

    public void setPerDireccion(SgDireccion perDireccion) {
        this.perDireccion = perDireccion;
    }

    public SgEstudiante getPerEstudiante() {
        return perEstudiante;
    }

    public void setPerEstudiante(SgEstudiante perEstudiante) {
        this.perEstudiante = perEstudiante;
    }

    public List<SgTelefono> getPerTelefonos() {
        return perTelefonos;
    }

    public void setPerTelefonos(List<SgTelefono> perTelefonos) {
        this.perTelefonos = perTelefonos;
    }

    public List<SgIdentificacion> getPerIdentificaciones() {
        return perIdentificaciones;
    }

    public void setPerIdentificaciones(List<SgIdentificacion> perIdentificaciones) {
        this.perIdentificaciones = perIdentificaciones;
    }

    public String getPerDui() {
        return perDui;
    }

    public void setPerDui(String perDui) {
        if (!StringUtils.isEmpty(perDui)) {
            perDui = StringUtils.leftPad(perDui, 9, "0");
        }
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

    public String getPerNip() {
        return perNip;
    }

    public void setPerNip(String perNip) {
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

    public SgDepartamento getPerDepartamentoNacimento() {
        return perDepartamentoNacimento;
    }

    public void setPerDepartamentoNacimento(SgDepartamento perDepartamentoNacimento) {
        this.perDepartamentoNacimento = perDepartamentoNacimento;
    }

    public SgMunicipio getPerMunicipioNacimiento() {
        return perMunicipioNacimiento;
    }

    public void setPerMunicipioNacimiento(SgMunicipio perMunicipioNacimiento) {
        this.perMunicipioNacimiento = perMunicipioNacimiento;
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

    public SgArchivo getPerFoto() {
        return perFoto;
    }

    public void setPerFoto(SgArchivo perFoto) {
        this.perFoto = perFoto;
    }

    public SgNacionalidad getPerNacionalidad() {
        return perNacionalidad;
    }

    public void setPerNacionalidad(SgNacionalidad perNacionalidad) {
        this.perNacionalidad = perNacionalidad;
    }

    public Boolean getPerPartidaNacimientoPresenta() {
        return perPartidaNacimientoPresenta;
    }

    public void setPerPartidaNacimientoPresenta(Boolean perPartidaNacimientoPresenta) {
        this.perPartidaNacimientoPresenta = perPartidaNacimientoPresenta;
    }

    public List<SgAllegado> getPerAllegados() {
        return perAllegados;
    }

    public void setPerAllegados(List<SgAllegado> perAllegados) {
        this.perAllegados = perAllegados;
    }

    public Boolean getPerSeBuscoEnBd() {
        return perSeBuscoEnBd;
    }

    public void setPerSeBuscoEnBd(Boolean perSeBuscoEnBd) {
        this.perSeBuscoEnBd = perSeBuscoEnBd;
    }

    public SgProfesion getPerProfesion() {
        return perProfesion;
    }

    public void setPerProfesion(SgProfesion perProfesion) {
        this.perProfesion = perProfesion;
    }

    public SgEscolaridad getPerEscolaridad() {
        return perEscolaridad;
    }

    public void setPerEscolaridad(SgEscolaridad perEscolaridad) {
        this.perEscolaridad = perEscolaridad;
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

    public SgOcupacion getPerOcupacion() {
        return perOcupacion;
    }

    public void setPerOcupacion(SgOcupacion perOcupacion) {
        this.perOcupacion = perOcupacion;
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

    public SgTipoTrabajo getPerTipoTrabajo() {
        return perTipoTrabajo;
    }

    public void setPerTipoTrabajo(SgTipoTrabajo perTipoTrabajo) {
        this.perTipoTrabajo = perTipoTrabajo;
    }
    
    public Integer getPerFamiliaresEmigrados() {
        return perFamiliaresEmigrados;
    }

    public void setPerFamiliaresEmigrados(Integer perFamiliaresEmigrados) {
        this.perFamiliaresEmigrados = perFamiliaresEmigrados;
    }

    public Boolean getPerSeEncontroIdentificacion() {
        return perSeEncontroIdentificacion;
    }

    public void setPerSeEncontroIdentificacion(Boolean perSeEncontroIdentificacion) {
        this.perSeEncontroIdentificacion = perSeEncontroIdentificacion;
    }

    public String getPerNombrePartidaNacimiento() {
        return perNombrePartidaNacimiento;
    }

    public void setPerNombrePartidaNacimiento(String perNombrePartidaNacimiento) {
        this.perNombrePartidaNacimiento = perNombrePartidaNacimiento;
    }

    public SgAllegado getPerResponsable() {
        return perResponsable;
    }

    public void setPerResponsable(SgAllegado perResponsable) {
        this.perResponsable = perResponsable;
    }

    public Boolean getPerSeIngresoNuevaIdentificacion() {
        return perSeIngresoNuevaIdentificacion;
    }

    public void setPerSeIngresoNuevaIdentificacion(Boolean perSeIngresoNuevaIdentificacion) {
        this.perSeIngresoNuevaIdentificacion = perSeIngresoNuevaIdentificacion;
    }

    public String getPerPartidaNacimientoComplemento() {
        return perPartidaNacimientoComplemento;
    }

    public void setPerPartidaNacimientoComplemento(String perPartidaNacimientoComplemento) {
        this.perPartidaNacimientoComplemento = perPartidaNacimientoComplemento;
    }

    public Integer getPerPartidaNacimientoAnio() {
        return perPartidaNacimientoAnio;
    }

    public void setPerPartidaNacimientoAnio(Integer perPartidaNacimientoAnio) {
        this.perPartidaNacimientoAnio = perPartidaNacimientoAnio;
    }

    public SgArchivo getPerPartidaNacimientoArchivo() {
        return perPartidaNacimientoArchivo;
    }

    public void setPerPartidaNacimientoArchivo(SgArchivo perPartidaNacimientoArchivo) {
        this.perPartidaNacimientoArchivo = perPartidaNacimientoArchivo;
    }

    public SgDepartamento getPerPartidaDepartamento() {
        return perPartidaDepartamento;
    }

    public void setPerPartidaDepartamento(SgDepartamento perPartidaDepartamento) {
        this.perPartidaDepartamento = perPartidaDepartamento;
    }

    public SgMunicipio getPerPartidaMunicipio() {
        return perPartidaMunicipio;
    }

    public void setPerPartidaMunicipio(SgMunicipio perPartidaMunicipio) {
        this.perPartidaMunicipio = perPartidaMunicipio;
    }

    public Long getPerUsuarioPk() {
        return perUsuarioPk;
    }

    public void setPerUsuarioPk(Long perUsuarioPk) {
        this.perUsuarioPk = perUsuarioPk;
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
