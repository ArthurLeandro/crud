package Interface;

import java.io.RandomAccessFile;

public interface UpdateDeleteProess<T> {
  public void OnChangeModel(RandomAccessFile p_ram, T p_model);
}
