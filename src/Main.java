import Controller.BooksController;
import Model.Book;
import View.ConsoleView;

public class Main {
  public static void main(String[] args) {
    // Livros de exemplo
    Book l1 = new Book(-1, "Eu, Robô", 14.9F);
    Book l2 = new Book(-1, "Eu Sou A Lenda", 21.99F);
    Book l3 = new Book(-1, "Número Zero", 34.11F);
    int id1, id2, id3;
    BooksController controller = BooksController.GetInstance();

    try {

      // Abre (cria) o arquivo de livros

      // Insere os três livros
      id1 = controller.CreateBook(l1);
      l1.setM_id(id1);
      id2 = controller.CreateBook(l2);
      l2.setM_id(id2);
      id3 = controller.CreateBook(l3);
      l3.setM_id(id3);

      // Busca por dois livros
      controller.ReadBookID(id3).Stringfy();
      controller.ReadBookID(id1).Stringfy();
      ConsoleView.ReturnWhiteColor();

      // Altera um livro para um tamanho maior e exibe o resultado
      l2.setM_name("APAguei sen querer");
      controller.Update(l2);
      controller.ReadBookID(l2.getM_id()).Stringfy();

      // Altera um livro para um tamanho menor e exibe o resultado
      l1.setM_name("I. Asimov");
      controller.Update(l1);
      controller.ReadBookID(l1.getM_id()).Stringfy();

      // Excluir um livro e mostra que não existe mais
      controller.DeleteBookId(id3);
      Book l = controller.ReadBookID(l3.getM_id());
      if (l == null)
        System.out.println("Livro excluído");
      else
        l.Stringfy();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
