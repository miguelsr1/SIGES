
package uy.com.sofis.pfea.binarios;

import java.util.List;
import java.util.logging.Logger;
import uy.com.sofis.pfea.exceptions.TechnicalException;

/**
 * @author Sofis Solutions
 */
public abstract class Repositorio {

  protected static final Logger logger = Logger.getLogger(Repositorio.class.getName());
  
  public abstract byte[] obtenerBytesImagenNoDisponible() throws TechnicalException;
  
  public abstract List<byte[]> obtenerBytesDocumento(String nombreArchivo) throws TechnicalException;
  
  public abstract void guardarDocumento(String nombreArchivo, List<byte[]> bytes);
  
  public abstract void eliminarDocumento(String nombreArchivo);
}
