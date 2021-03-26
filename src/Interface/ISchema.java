package Interface;

import java.io.DataOutputStream;
import java.io.RandomAccessFile;

/**
 * Interface funcional para implementação de um esquema do modelo. generica
 */
@FunctionalInterface
public interface ISchema {
  /**
   * Construção do esquema de um modelo. É onde a operação do Writable do modelo é
   * desfeita na mesma ordem, o dado é lido da memória que é fornecida
   * pelo @FileManager e é escrito no DataOutputStream
   * 
   * @param p_model  Leitor do arquivo, ofertado pelo @@FileManager
   * @param p_output escritor no byte que necessário para o @FileManager
   */
  public void Build(RandomAccessFile p_model, DataOutputStream p_output);
}
