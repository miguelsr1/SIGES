/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.to.AreasInversionArchivoTO;
import gob.mined.siap2.business.CatalogoInsumosBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.RelacionAreasInversionInsumo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jboss.ejb3.annotation.TransactionTimeout;

@LocalBean
@Stateless(name = "generacionInsumoAreasInversionDesdeArchivoBean")
public class GeneracionInsumoAreasInversionDesdeArchivoBean {
    private static final Logger LOGGER = Logger.getLogger(GeneracionInsumoAreasInversionDesdeArchivoBean.class.getName());

    @Inject
    private AreasInversionBean areasInversionBean;

    @Inject
    private CatalogoInsumosBean catalogoInsumosBean;
    
    @Inject 
    private RelacionAreasInversionInsumoBean areasInversionInsumoBean;
    
    @TransactionTimeout(unit=TimeUnit.MINUTES, value=30)
    public void crearDesdeArchivo(AreasInversionArchivoTO archivo) {
        try {
            BusinessException be = new BusinessException();
            if (archivo.getArea() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_AREA_INVERSION);
            }
            
            if (archivo.getSubArea() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_SUB_AREA_INVERSION);
            }            
            if (archivo.getFile() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_ARCHIVO);
            }
            
            if (!be.getErrores().isEmpty()) {
                throw be;
            }
            List<RelacionAreasInversionInsumo> relaciones = areasInversionInsumoBean.getRelacionByAreaInversionId(archivo.getSubArea().getId());
            for(RelacionAreasInversionInsumo rels : relaciones) {
                rels.setExistente(Boolean.TRUE);
            }
            
            //List<RelacionAreasInversionInsumo> relacionesAgregar = new ArrayList();
            org.apache.poi.ss.usermodel.Workbook myWorkBook = WorkbookFactory.create(archivo.getFile().getInputStream());
            org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    if (row.getRowNum() > 1) {
                        Iterator<Cell> cellIterator = row.cellIterator();
                        if(cellIterator != null) {
                            whileInsumos:
                                while (cellIterator.hasNext()) {
                                     //insumo
                                    String insumoCodigo = areasInversionBean.getValor(cellIterator.next());
                                    Insumo insumo = null;
                                    Boolean agregar = true;
                                    if (!StringUtils.isBlank(insumoCodigo)) {
                                        
                                        if(!existeEnLista(relaciones, insumoCodigo)) {
                                            insumo = catalogoInsumosBean.getInsumoByCodigo(insumoCodigo.trim());
                                            if (insumo == null) {
                                                be.addError(ConstantesErrores.ERR_CODIGO_INSUMO_INEXISTENTE, new String[]{insumoCodigo});
                                                throw be;
                                            } 
                                        } else {
                                            agregar = false;
                                        }
                                        
                                        if(agregar) {
                                            RelacionAreasInversionInsumo relInsumo = new RelacionAreasInversionInsumo();
                                            relInsumo.setInsumo(insumo);
                                            relInsumo.setAreaInversion(archivo.getSubArea() );
                                            relInsumo.setExistente(Boolean.FALSE);
                                            relaciones.add(relInsumo);
                                        }

                                    } else {
                                        break whileInsumos;
                                    }
                                }
                            
                        }
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error al leer la celda", e);
                }
            }
            
            if(relaciones != null && !relaciones.isEmpty() && relaciones.size() > 0) {
                for(RelacionAreasInversionInsumo rels : relaciones) {
                    if(rels.getExistente() == null || !rels.getExistente()) {
                        areasInversionInsumoBean.crearActualizar(rels);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    private Boolean existeEnLista(List<RelacionAreasInversionInsumo> listado, String codigo) {
        Boolean existente = Boolean.FALSE;
        for(RelacionAreasInversionInsumo rels : listado) {
            if(rels.getInsumo().getCodigo().trim().equals(codigo.trim())) {
                return Boolean.TRUE;
            }
        }
        return existente;
    }
}
