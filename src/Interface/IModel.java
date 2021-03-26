package Interface;

public interface IModel<T> {
  public byte[] Writable();

  public T Readble(byte[] p_dataInBytes, EndProcess p_onComplete);

  public void Stringfy();
}
