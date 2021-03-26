package Interface;

import java.io.FileOutputStream;

/**
 * Interface funcional para tratar dos aspectos de indexação de um modelo
 * genérico.
 */
@FunctionalInterface
public interface IIndexer {
  public void Index(byte[] p_model, FileOutputStream p_stream);
}
