package Interface;

import java.io.RandomAccessFile;

/**
 * Interface funcional para construção de um objeto a partir de um vetor de
 * bytes. É uma implementação do Readable do modelo de modo obrigatória quando o
 * mesmo não foi efetuada. generica
 */
@FunctionalInterface
public interface IBuildIn<T> {
  /**
   * Construtor de um objeto genérico a partir de um vetor de byte.
   * 
   * @example : IBuildIn<Book> builder = (bytes) -> { return new
   *          T().Readable(bytes,null);
   * @param p_model array de vetor que possui os dados
   * @return objeto criado
   */
  public T Build(byte[] p_model);
}
