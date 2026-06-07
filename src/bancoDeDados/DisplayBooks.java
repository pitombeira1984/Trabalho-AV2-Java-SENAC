package bancoDeDados;

//Questão 24.2 - Aplicativo de consulta para o banco de dados books
//Equipe: Tiago Pitombeira e Priscila
//Capítulo 24 - Java: Como Programar (Deitel & Deitel)
//
//INSTRUÇÕES PARA RODAR NO ECLIPSE:
//1. Certifique-se de ter o Java DB (Apache Derby) configurado
//2. Adicione o derby.jar ao Build Path do projeto
// (Geralmente em: C:\Program Files\Java\jdk1.x\db\lib\derby.jar)
//3. Execute primeiro o script SQL para criar o banco 'books'
// (Consulte a Seção 24.5 do livro para criar o banco de dados)
//4. Rode esta classe como Java Application

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class DisplayBooks {

 public static void main(String[] args) {
     // Executa a interface gráfica na Event Dispatch Thread
     SwingUtilities.invokeLater(() -> {
         BooksQueryFrame frame = new BooksQueryFrame();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
     });
 }
}


