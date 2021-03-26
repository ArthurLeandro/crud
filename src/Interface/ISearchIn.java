package Interface;

/**
 * Interface funcional para implementação de uma consulta generica
 */
@FunctionalInterface
public interface ISearchIn<T> {
  /**
   * Consulta generica a ser feita através de uma interface funcional onde T é um
   * objeto que possui um valor para ser utilizado. Essa interface foi
   * desenvolvida para ser usada dentro de uma função
   * 
   * @example ISearchIn<Book> query = (object) -> { return object.getField() ==
   *          fieldAsParamOfExternalFunction;};*
   * 
   * @param p_model objeto a ser comparado
   * @return resultado da comparação
   */
  public boolean Search(T p_model);
}
