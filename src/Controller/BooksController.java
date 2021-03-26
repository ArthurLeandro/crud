package Controller;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import Indexes.BookIndex;
import Interface.EndProcess;
import Interface.IBuildIn;
import Interface.IIndexReader;
import Interface.IIndexer;
import Interface.ISchema;
import Interface.ISearchIn;
import Interface.UpdateDeleteProess;
import Model.Book;
import Utilitary.*;
import View.ConsoleView;

/**
 * Classe responsável por controlar os modelos do tipo @type=Book
 */
public class BooksController {

  private static BooksController m_instance = null; // instancia do Singleton
  private int m_lastId; // último id inserido
  private int m_lastPointer; // ultimo id do ponteiro inserido
  private boolean m_haveIndex; // Tem indice nessa tabela?
  private BookIndex[] m_indexes; // array dos indices em memoria

  // region SINGLETON & CTOR

  /**
   * Construtor da classe.
   */
  public BooksController() {
    try {
      String pathS = "./src/database/books/index.indx";
      Path path = Paths.get(pathS);
      m_haveIndex = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
    } catch (Exception e) {
      // TODO: handle exception
      m_haveIndex = false;
    }
    m_lastId = 0;
    m_lastPointer = 0;
    m_indexes = new BookIndex[100];
  }

  /**
   * Retorna a instância do Singleton
   * 
   * @return instância construída da classe
   */
  public static BooksController GetInstance() {
    if (m_instance == null) {
      m_instance = new BooksController();
    }
    return m_instance;
  }
  // endregion

  // region GETTERS & SETTERS

  /**
   * Pegar o ultimo id inserido
   * 
   * @return ultimo id inserido
   */
  public int getLastId() {
    return m_lastId;
  }

  /**
   * Seta o último id.
   * 
   * @param m_lastId valor a ser setado no último id
   */
  public void setLastId(int m_lastId) {
    this.m_lastId = m_lastId;
  }
  // endregion

  // region CRUD

  // region PUBLIC

  /**
   * Função utilizada para atualizar um modelo no banco de dados
   * 
   * @param p_book Modelo a ser atualizado
   * @return Foi atualizado?
   */
  public boolean Update(Book p_book) {
    UpdateDeleteProess<Book> delete = (ram, model) -> {
      try {
        ram.writeByte(ModelState.INACTIVE);
      } catch (Exception e) {
        // TODO: handle exception
      }
    };
    EndProcess endProcess = () -> {
      if (m_haveIndex) {
        // marcar no indice como excluido
        for (int i = 0; i < m_lastId; i++) {
          try {
            if (m_indexes[i].m_id == p_book.getM_id()) {
              m_indexes[i].m_isActive = 0;
            }
          } catch (Exception e) {
            // TODO: handle exception
            if (Debug.DEBUG)
              ConsoleView.PrintErrorMessage("Deu erro ao atualizar o indice");
          }
        }
        // criar um novo indice no array
        m_indexes[m_lastId] = new BookIndex(m_lastId, m_lastPointer); // cria um novo indice em memoria principal
        m_lastPointer += p_book.getM_arraySize(); // adiciona o ponteiro para o proximo indice com o tamanho do modelo
                                                  // atual
        this.GetInstance().setLastId(getLastId() + 1); // incrementa o id atual
      }
    };
    CRUDManager.Delete(DatabaseSchemaName.LIVRO, GetIDQuery(p_book.getM_id()), GetBuilder(), GetSchema(), delete,
        m_haveIndex, GetIndexPos(p_book));
    return (CRUDManager.Create(p_book.Writable(), DatabaseSchemaName.LIVRO, endProcess, null)) != -1;
  }

  /**
   * Função utilizada para criar um modelo no banco de dados
   * 
   * @param p_toWrite Modelo a ser críado no Banco de Dados
   * @return id do modelo críado
   */
  public int CreateBook(Book p_toWrite) {
    EndProcess endProcess = () -> {
      if (m_haveIndex) {
        m_indexes[m_lastId] = new BookIndex(m_lastId, m_lastPointer); // cria um novo indice em memoria principal
        m_lastPointer += p_toWrite.getM_arraySize(); // adiciona o ponteiro para o proximo indice com o tamanho do
                                                     // modelo atual
      }
      this.GetInstance().setLastId(getLastId() + 1); // incrementa o id atual
    };
    p_toWrite.setM_id(m_lastId);
    int varReturn = CRUDManager.Create(p_toWrite.Writable(), DatabaseSchemaName.LIVRO, endProcess, GetIndexCreate());
    if (varReturn != -1) {
      ConsoleView.PrintSuccessMessage("O Livro foi inserido com sucesso\n");
      varReturn = p_toWrite.getM_id();
      // setLastId(getLastId() + 1);
    } else {
      if (Debug.DEBUG)
        ConsoleView.PrintErrorMessage("Ocorreu um erro e o livro não será inserido, mais ");
    }
    return varReturn;
  }

  /**
   * Função utilizada para ler um modelo baseado em uma pesquisa por nome
   * 
   * @param p_bookName Nome do modelo a ser lido
   * @return modelo lido
   */
  public Book ReadBookName(String p_bookName) {
    return Read(GetNameQuery(p_bookName));
  }

  /**
   * Função utilizada para ler um modelo baseado em uma pesquisa por preço
   * 
   * @param p_bookPrice Preço do modelo a ser lido
   * @return modelo a ser lido
   */
  public Book ReadBookPrice(float p_bookPrice) {
    return Read(GetPriceQuery(p_bookPrice));
  }

  /**
   * Função utilizada para ler um modelo baseado em uma pesquisa por id
   * 
   * @param p_id Id do modelo a ser lido
   * @return modelo a ser lido
   */
  public Book ReadBookID(int p_id) {
    return Read(GetIDQuery(p_id));
  }

  /**
   * Função utilizada para excluir um modelo baseado em uma pesquisa por nome
   * 
   * @param p_bookName Nome do modelo a ser excluído
   * @return foi excluido?
   */
  public boolean DeleteBookByName(String p_name) {
    return Delete(GetNameQuery(p_name));
  }

  /**
   * Função utilizada para excluir um modelo baseado em uma pesquisa por preço
   * 
   * @param p_bookPrice Preço do modelo excluído
   * @return foi excluído?
   */
  public boolean DeleteBookPrice(float p_bookPrice) {
    return Delete(GetPriceQuery(p_bookPrice));
  }

  /**
   * Função utilizada para excluir um modelo baseado em uma pesquisa por ID
   * 
   * @param p_id Id do modelo excluído
   * @return foi excluído?
   */
  public boolean DeleteBookId(int p_id) {
    return Delete(GetIDQuery(p_id));
  }

  // endregion

  // region PRIVATE
  /**
   * Função privada utilizada para efetivamente excluir um modelo
   * 
   * @param p_query Consulta a ser utilizada dentro dessa exclusão
   * @return foi excluído?
   */
  private boolean Delete(ISearchIn p_query) {
    UpdateDeleteProess delete = (ram, model) -> {
      try {
        ram.writeByte(ModelState.INACTIVE);
      } catch (Exception e) {
        // TODO: handle exception
      }
    };
    return CRUDManager.Delete(DatabaseSchemaName.LIVRO, p_query, GetBuilder(), GetSchema(), delete, false, null);
  }

  /**
   * Função privada utilizada para efetivamente ler um modelo
   * 
   * @param p_query Consulta a ser utilizada dentro da leitura
   * @return o livro lido
   */
  private Book Read(ISearchIn p_query) {
    ISchema schema = (ram, dos) -> {
      try {
        dos.writeByte(ram.readByte());
        dos.writeShort(ram.readShort());
        dos.writeInt(ram.readInt());
        dos.writeUTF(ram.readUTF());
        dos.writeFloat(ram.readFloat());
      } catch (Exception e) {
        // TODO: handle exception
        if (Debug.DEBUG)
          ConsoleView.PrintErrorMessage("OPERAÇÂO NEGADA: \n" + e.getMessage());
      }
    };
    IBuildIn<Book> builder = (bytes) -> {
      try {
        return new Book().Readble(bytes, null);
      } catch (Exception e) {
        // TODO: handle exception
        if (Debug.DEBUG)
          ConsoleView.PrintErrorMessage("OPERAÇÂO NEGADA: \n" + e.getMessage());
        return null;
      }
    };
    Book returning = builder.Build(CRUDManager.Read(DatabaseSchemaName.LIVRO, p_query, builder, schema, false, null));
    if (returning != null)
      return returning;
    else
      return null;
  }
  // endregion

  // endregion

  // region INTERFACES

  /**
   * Implementação da Consulta funcional baseado em id
   * 
   * @param p_id id utilizada
   * @return Consulta implementada
   */
  private ISearchIn<Book> GetIDQuery(int p_id) {
    ISearchIn<Book> query = (book) -> {
      return book.getM_id() == p_id && book.getM_isActive() == ModelState.ACTIVE;
    };
    return query;
  }

  /**
   * Implementação da consulta funcional em nome
   * 
   * @param p_name nome utilizado na consulta
   * @return Consulta implementada
   */
  private ISearchIn<Book> GetNameQuery(String p_name) {
    ISearchIn<Book> query = (book) -> {
      return book.getM_name().equals(p_name) && book.getM_isActive() == ModelState.ACTIVE;
    };
    return query;
  }

  /**
   * Implementação da consulta funcional em preço
   * 
   * @param p_price preço utilizado na consulta
   * @return consulta implementada
   */
  private ISearchIn<Book> GetPriceQuery(float p_price) {
    ISearchIn<Book> query = (book) -> {
      return (book.getM_price() == p_price) && book.getM_isActive() == ModelState.ACTIVE;
    };
    return query;
  }

  /**
   * Esquema utilizado para construção do modelo. Mapeamento de bytes lidos para o
   * array que deverá ser devolvido
   * 
   * @return esquema implementado
   */
  private ISchema GetSchema() {
    ISchema schema = (ram, dos) -> {
      try {
        dos.writeByte(ram.readByte());
        dos.writeShort(ram.readShort());
        dos.writeInt(ram.readInt());
        dos.writeUTF(ram.readUTF());
        dos.writeFloat(ram.readFloat());
      } catch (Exception e) {
        // TODO: handle exception
        if (Debug.DEBUG)
          ConsoleView.PrintErrorMessage("OPERAÇÂO NEGADA: \n" + e.getMessage());
      }
    };
    return schema;
  }

  /**
   * Pega a interface funcional necessária para indexar um modelo
   * 
   * @return indexação construída
   */
  private IIndexer GetIndexCreate() {
    IIndexer indexer = (vector, stream) -> {
      try {
        Book model = new Book().Readble(vector, null);
        stream.write(model.getM_isActive());
        stream.write(model.getM_id());
        stream.write(vector.length);
      } catch (Exception e) {
        if (Debug.DEBUG) {
          ConsoleView.PrintErrorMessage("IINDEXER Index() - Erro ao indexar item:\n" + e.getMessage());
        }
      }
    };
    return indexer;
  }

  /**
   * Interface funcional utilizada para transformar bytes do modelo desejado no
   * modelo utilizado
   * 
   * @return interface construída
   */
  private IBuildIn<Book> GetBuilder() {
    IBuildIn<Book> builder = (bytes) -> {
      try {
        return new Book().Readble(bytes, null);
      } catch (Exception e) {
        // TODO: handle exception
        if (Debug.DEBUG)
          ConsoleView.PrintErrorMessage("OPERAÇÂO NEGADA: Erro ao construir objeto\n" + e.getMessage());
        return null;
      }
    };
    return builder;
  }

  /**
   * Interface funcional utilizada para pegar a posição de escrita do indice do
   * modelo
   * 
   * @param p_model modelo a ser indexado
   * @return interface construída
   */
  private IIndexReader GetIndexPos(Book p_model) {
    IIndexReader posGetter = () -> {
      return p_model.getM_id() % 100;
    };
    return posGetter;
  }
  // endregion

}
