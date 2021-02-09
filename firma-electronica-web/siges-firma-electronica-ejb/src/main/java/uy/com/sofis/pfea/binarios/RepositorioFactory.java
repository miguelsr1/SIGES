
package uy.com.sofis.pfea.binarios;

/**
 * @author Sofis Solutions
 */
public class RepositorioFactory {

  public static Repositorio getRepositorio() {
    
    //return new RespositorioModeShape();
    
    return new RepositorioFileSystem();
  }
  
}
