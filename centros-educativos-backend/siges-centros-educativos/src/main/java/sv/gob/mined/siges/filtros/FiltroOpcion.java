package sv.gob.mined.siges.filtros;

import java.io.Serializable;
/**
 *
 * @author usuario
 */
public class FiltroOpcion implements Serializable {
    
    private Long opcModalidadPk;
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String nombreExacto;
    private String descripcion;
    private Boolean habilitado;
    private Long opcSectorEconomicoPk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private Boolean inicializarProgramaEducativo;
    
    public FiltroOpcion() {
        inicializarProgramaEducativo=Boolean.TRUE;
    }

    public Long getOpcModalidadPk() {
        return opcModalidadPk;
    }

    public void setOpcModalidadPk(Long opcModalidadPk) {
        this.opcModalidadPk = opcModalidadPk;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoExacto() {
        return codigoExacto;
    }

    public void setCodigoExacto(String codigoExacto) {
        this.codigoExacto = codigoExacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreExacto() {
        return nombreExacto;
    }

    public void setNombreExacto(String nombreExacto) {
        this.nombreExacto = nombreExacto;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getOpcSectorEconomicoPk() {
        return opcSectorEconomicoPk;
    }

    public void setOpcSectorEconomicoPk(Long opcSectorEconomicoPk) {
        this.opcSectorEconomicoPk = opcSectorEconomicoPk;
    }
    
    
    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public Boolean getInicializarProgramaEducativo() {
        return inicializarProgramaEducativo;
    }

    public void setInicializarProgramaEducativo(Boolean inicializarProgramaEducativo) {
        this.inicializarProgramaEducativo = inicializarProgramaEducativo;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    }
