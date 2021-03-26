package Utilitary;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import Interface.EndProcess;
import Interface.IBuildIn;
import Interface.IIndexReader;
import Interface.IIndexer;
import Interface.ISchema;
import Interface.ISearchIn;
import Interface.UpdateDeleteProess;
import View.ConsoleView;

public class FileManager {

  private static final String m_fileName = "database.db";
  private static final String m_indexName = "index.indx";

  /**
   * Função usada para determinar se um diretório existe
   * 
   * @param p_pathToDirectory caminho ate o diretorio em questãoo
   * @return Diretório existe? Retorna o resultado da pergunta em booleano.
   */
  public static boolean DirectoryExists(String p_pathToDirectory) {
    Path path = Paths.get(p_pathToDirectory);
    return Files.exists(path, LinkOption.NOFOLLOW_LINKS);
  }

  /**
   * Função usada para escrever um vetor de bytes em um arquivo
   * 
   * @param p_toWrite   Vetor de bytes para serem escritos
   * @param p_whichFile Em qual arquivo deve ser escrito, valor pego
   *                    do @DataBaseSchemaName
   * @return Escreveu? Retorna a resposta dessa pergunta
   */
  public static boolean WriteInFile(byte[] p_toWrite, String p_whichFile, IIndexer p_index) {
    boolean valueToReturn = false;
    try {
      // open file
      String filePath = "./src/database/";
      switch (p_whichFile) {
      case "book/":
        filePath += DatabaseSchemaName.LIVRO;
        break;
      default:
        filePath += DatabaseSchemaName.USUARIO;
        break;
      }
      File f = new File(filePath + m_fileName);
      FileOutputStream stream = new FileOutputStream(f, true);
      stream.write(p_toWrite);
      if (p_index != null) {
        File fileIndex = new File(filePath + m_indexName);
        FileOutputStream streamIndex = new FileOutputStream(fileIndex, true);
        p_index.Index(p_toWrite, streamIndex);
      }
      valueToReturn = true;
    } catch (Exception e) {
      // TODO: handle exception
      if (Debug.DEBUG) {
        ConsoleView.PrintErrorMessage("FILEMANAGER WrieInFile()- ");
        e.printStackTrace();
      }
    }
    return valueToReturn;
  }

  /**
   * Função que lê de um arquivo já existente e retorna os bytes referentes a ele
   * 
   * @param p_pathToFile caminho ate o arquivo, pego na store @DatabaseSchemeName
   * @param p_query      Interface da consulta a ser realizada, qual parametro
   *                     estou buscando
   * @param p_builder    Interface de como consruir o objeto para a consulta
   * @param p_schema     Interface de como entender o objeto a partir de um vetor
   *                     de bytes
   * @return Objeto genérico da minha consulta em vetor de bytes
   */
  public static byte[] ReadFromFile(String p_pathToFile, ISearchIn p_query, IBuildIn p_builder, ISchema p_schema,
      UpdateDeleteProess p_onComplete, boolean p_hasIndex, IIndexReader p_readerIndex) {
    String filePath = "./src" + DatabaseSchemaName.BASE_FOLDER + p_pathToFile;
    byte[] vectorObject = null;
    if (FileManager.DirectoryExists(filePath)) {
      try {
        RandomAccessFile reader = new RandomAccessFile(new File(filePath + m_fileName), "rw");
        boolean shouldKeepOnReading = true;
        if (!p_hasIndex) {
          while (shouldKeepOnReading) {
            vectorObject = FileManager.GetAllBytesFromThisLine(reader, p_schema);
            if (p_query.Search(p_builder.Build(vectorObject))) {
              shouldKeepOnReading = false;
            }
          }
        } else {
          reader.skipBytes(p_readerIndex.ReadPosFromIndex());
          vectorObject = FileManager.GetAllBytesFromThisLine(reader, p_schema);
        }
        if (p_onComplete != null) {
          reader.seek(reader.getFilePointer() - vectorObject.length); // retorna ao inicio desse arquivo
          p_onComplete.OnChangeModel(reader, p_builder.Build(vectorObject)); // constroi o modelo e altera
        }
      } catch (Exception e) {
        // TODO: handle exception
        if (Debug.DEBUG)
          ConsoleView.PrintErrorMessage("FILEMANAGER ReadFrom: \n" + e.getMessage());
      }
    }
    return vectorObject;
  }

  private static byte[] GetAllBytesFromThisLine(RandomAccessFile p_fileReader, ISchema p_schema) {
    ByteArrayOutputStream array = new ByteArrayOutputStream();
    DataOutputStream aux = new DataOutputStream(array);
    p_schema.Build(p_fileReader, aux);
    return array.toByteArray();
  }

}
