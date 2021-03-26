package Utilitary;

import Interface.EndProcess;
import Interface.IBuildIn;
import Interface.IIndexReader;
import Interface.IIndexer;
import Interface.ISchema;
import Interface.ISearchIn;
import Interface.UpdateDeleteProess;
import View.ConsoleView;

public class CRUDManager {
  /**
   * API para salvar um objeto no DB
   * 
   * @param p_toWrite objeto em bytes
   * @param p_path    caminho para armazenar o objeto, pego na
   *                  loja @DatabaseSchemaName
   * @param p_onEnd   Callback para ser chamado no fim da execução
   * @param p_indexer Callback para ser chamado caso a tabela seja indexada
   * @return um inteiro positivo quando for positivo a operação, negativo quando
   *         houver algum erro
   */
  public static int Create(byte[] p_toWrite, String p_path, EndProcess p_onEnd, IIndexer p_indexer) {
    int response = -1;
    try {
      FileManager.WriteInFile(p_toWrite, p_path, p_indexer);
      if (p_onEnd != null) {
        p_onEnd.OnEndProcess();
      }
      response = 0;
    } catch (Exception e) {
      if (Debug.DEBUG)
        ConsoleView.PrintErrorMessage("OPERAÇÂO NEGADA:\n " + e.getMessage());
    }
    return response;
  }

  public static boolean Delete(String p_pathToFile, ISearchIn p_query, IBuildIn p_builder, ISchema p_schema,
      UpdateDeleteProess p_update, boolean p_hasIndex, IIndexReader p_reader) {
    boolean valueTOReturn = false;
    try {
      FileManager.ReadFromFile(p_pathToFile, p_query, p_builder, p_schema, p_update, p_hasIndex, p_reader);
      valueTOReturn = true;
    } catch (Exception e) {
      valueTOReturn = false;
    }
    return valueTOReturn;
  }

  /**
   * Função estatíca para ler um objeto e retornar os bytes relativos a ele
   * 
   * @param p_pathToFile caminho ate o arquivo
   * @param p_query      interface da consulta
   * @param p_builder    interface do construtor do objeto
   * @param p_schema     interface do tradutor de bytes para objeto
   * @return retornar os bytes relativos a ele
   */
  public static byte[] Read(String p_pathToFile, ISearchIn p_query, IBuildIn p_builder, ISchema p_schema,
      boolean p_hasIndex, IIndexReader p_reader) {
    byte[] toReturn = null;
    try {
      toReturn = FileManager.ReadFromFile(p_pathToFile, p_query, p_builder, p_schema, null, p_hasIndex, p_reader);
    } catch (Exception e) {
      if (Debug.DEBUG)
        ConsoleView.PrintErrorMessage("OPERAÇÂO NEGADA: \n" + e.getMessage());
    }
    return toReturn;
  }

  public static boolean Update(String p_pathToFile, byte[] p_toUpdate, ISearchIn p_query, IBuildIn p_builder,
      ISchema p_schema, UpdateDeleteProess p_update, boolean p_hasIndex, IIndexReader p_reader) {
    boolean valueTOReturn = false;
    try {
      FileManager.ReadFromFile(p_pathToFile, p_query, p_builder, p_schema, p_update, p_hasIndex, p_reader);
    } catch (Exception e) {
      valueTOReturn = false;
    }
    return valueTOReturn;
  }
}
