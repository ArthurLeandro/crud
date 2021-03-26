package Indexes;

/**
 * Classe container dos dados necessários para indexar um modelo do tipo @Book
 */
public class BookIndex {
  public byte m_isActive; // se o indice é utilizado para algo valido
  public int m_id; // id do objeto armazenado
  public int m_pointer; // pónteiro da memoria

  public BookIndex(int m_id, int m_pointer) {
    this.m_id = m_id;
    this.m_pointer = m_pointer;
    m_isActive = 1;
  }
}
