package bancoDeDados;

//Questão 24.2 - BooksQueryFrame.java
//Interface gráfica principal com JComboBox para consultas predefinidas
//e área de texto para consultas personalizadas.
//Equipe: Tiago Pitombeira e Priscila

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class BooksQueryFrame extends JFrame {

 private static final long serialVersionUID = 1L;

 // Nomes das consultas predefinidas exibidas no JComboBox
 private static final String[] QUERY_NAMES = {
     "-- Selecione uma consulta predefinida --",
     "(a) Selecionar todos os autores",
     "(b) Livros por autor específico",
     "(c) Autores por título específico"
 };

 private BooksQueries booksQueries;

 // Componentes da interface
 private JComboBox<String> queryComboBox;
 private JPanel paramPanel;
 private JLabel param1Label;
 private JTextField param1Field;
 private JLabel param2Label;
 private JTextField param2Field;
 private JButton executeButton;
 private JTable resultTable;
 private DefaultTableModel tableModel;
 private JTextArea customQueryArea;
 private JButton customQueryButton;
 private JLabel statusLabel;

 // Construtor principal
 public BooksQueryFrame() {
     super("Questão 24.2 - Consulta ao Banco de Dados Books");

     // Conecta ao banco de dados
     booksQueries = new BooksQueries();

     // Fecha a conexão ao encerrar a janela
     addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
             booksQueries.close();
         }
     });

     buildGUI();
     setSize(850, 650);
     setLocationRelativeTo(null); // Centraliza na tela
 }

 // Constrói toda a interface gráfica
 private void buildGUI() {
     setLayout(new BorderLayout(10, 10));

     // Painel superior: título
     JLabel titleLabel = new JLabel("Aplicativo de Consulta - Banco de Dados Books", SwingConstants.CENTER);
     titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
     titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
     add(titleLabel, BorderLayout.NORTH);

     // Painel central: controles de consulta + resultado
     JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
     centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

     // Painel de consultas predefinidas (topo do painel central)
     JPanel predefinedPanel = buildPredefinedQueryPanel();
     centerPanel.add(predefinedPanel, BorderLayout.NORTH);

     // Tabela de resultados (centro)
     JPanel resultPanel = buildResultPanel();
     centerPanel.add(resultPanel, BorderLayout.CENTER);

     add(centerPanel, BorderLayout.CENTER);

     // Painel inferior: consultas personalizadas
     JPanel customPanel = buildCustomQueryPanel();
     add(customPanel, BorderLayout.SOUTH);
 }

 // Painel com JComboBox de consultas predefinidas e parâmetros
 private JPanel buildPredefinedQueryPanel() {
     JPanel panel = new JPanel(new GridBagLayout());
     panel.setBorder(new TitledBorder("Consultas Predefinidas"));
     GridBagConstraints gbc = new GridBagConstraints();
     gbc.insets = new Insets(5, 8, 5, 8);
     gbc.fill = GridBagConstraints.HORIZONTAL;

     // JComboBox com as consultas predefinidas
     gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
     gbc.weightx = 1.0;
     queryComboBox = new JComboBox<>(QUERY_NAMES);
     queryComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
     queryComboBox.addActionListener(e -> onQuerySelected());
     panel.add(queryComboBox, gbc);

     // Painel de parâmetros dinâmicos (visível apenas quando necessário)
     gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
     param1Label = new JLabel("Parâmetro:");
     param1Label.setVisible(false);
     panel.add(param1Label, gbc);

     gbc.gridx = 1; gbc.weightx = 0.4;
     param1Field = new JTextField(15);
     param1Field.setVisible(false);
     panel.add(param1Field, gbc);

     gbc.gridx = 2; gbc.weightx = 0;
     param2Label = new JLabel("");
     param2Label.setVisible(false);
     panel.add(param2Label, gbc);

     gbc.gridx = 3; gbc.weightx = 0.4;
     param2Field = new JTextField(15);
     param2Field.setVisible(false);
     panel.add(param2Field, gbc);

     // Botão executar
     gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
     gbc.fill = GridBagConstraints.NONE;
     gbc.anchor = GridBagConstraints.CENTER;
     executeButton = new JButton("  Executar Consulta  ");
     executeButton.setFont(new Font("Arial", Font.BOLD, 13));
     executeButton.setBackground(new Color(0, 102, 204));
     executeButton.setForeground(Color.WHITE);
     executeButton.setOpaque(true);
     executeButton.addActionListener(e -> executePredefinedQuery());
     panel.add(executeButton, gbc);

     return panel;
 }

 // Painel com JTable para exibir resultados
 private JPanel buildResultPanel() {
     JPanel panel = new JPanel(new BorderLayout(5, 5));
     panel.setBorder(new TitledBorder("Resultado da Consulta"));

     tableModel = new DefaultTableModel();
     resultTable = new JTable(tableModel);
     resultTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
     resultTable.setRowHeight(22);
     resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     resultTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
     resultTable.getTableHeader().setBackground(new Color(0, 102, 204));
     resultTable.getTableHeader().setForeground(Color.WHITE);
     resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

     JScrollPane scrollPane = new JScrollPane(resultTable);
     scrollPane.setPreferredSize(new Dimension(800, 200));
     panel.add(scrollPane, BorderLayout.CENTER);

     // Rótulo de status (quantidade de registros retornados)
     statusLabel = new JLabel("Nenhuma consulta executada.");
     statusLabel.setFont(new Font("Arial", Font.ITALIC, 11));
     statusLabel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
     panel.add(statusLabel, BorderLayout.SOUTH);

     return panel;
 }

 // Painel inferior para consultas SQL personalizadas
 private JPanel buildCustomQueryPanel() {
     JPanel panel = new JPanel(new BorderLayout(5, 5));
     panel.setBorder(new TitledBorder("Consulta SQL Personalizada"));
     panel.setPreferredSize(new Dimension(850, 130));

     customQueryArea = new JTextArea(3, 60);
     customQueryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
     customQueryArea.setLineWrap(true);
     customQueryArea.setText("SELECT * FROM Authors");
     JScrollPane scrollPane = new JScrollPane(customQueryArea);
     panel.add(scrollPane, BorderLayout.CENTER);

     JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     customQueryButton = new JButton("  Executar SQL Personalizado  ");
     customQueryButton.setFont(new Font("Arial", Font.BOLD, 12));
     customQueryButton.setBackground(new Color(34, 139, 34));
     customQueryButton.setForeground(Color.WHITE);
     customQueryButton.setOpaque(true);
     customQueryButton.addActionListener(e -> executeCustomQuery());
     btnPanel.add(customQueryButton);
     panel.add(btnPanel, BorderLayout.SOUTH);

     return panel;
 }

 // Atualiza os campos de parâmetro conforme a consulta selecionada
 private void onQuerySelected() {
     int index = queryComboBox.getSelectedIndex();

     // Oculta todos os parâmetros por padrão
     param1Label.setVisible(false);
     param1Field.setVisible(false);
     param2Label.setVisible(false);
     param2Field.setVisible(false);
     param1Field.setText("");
     param2Field.setText("");

     switch (index) {
         case 1: // (a) Todos os autores - sem parâmetros
             break;

         case 2: // (b) Livros por autor - pede FirstName e LastName
             param1Label.setText("Primeiro Nome:");
             param1Label.setVisible(true);
             param1Field.setVisible(true);
             param2Label.setText("Sobrenome:");
             param2Label.setVisible(true);
             param2Field.setVisible(true);
             break;

         case 3: // (c) Autores por título - pede o título
             param1Label.setText("Título do Livro:");
             param1Label.setVisible(true);
             param1Field.setVisible(true);
             break;
     }

     revalidate();
     repaint();
 }

 // Executa a consulta predefinida selecionada no JComboBox
 private void executePredefinedQuery() {
     int index = queryComboBox.getSelectedIndex();

     if (index == 0) {
         JOptionPane.showMessageDialog(this,
             "Por favor, selecione uma consulta predefinida.",
             "Nenhuma consulta selecionada",
             JOptionPane.WARNING_MESSAGE);
         return;
     }

     clearTable();

     switch (index) {
         case 1: // (a) Selecionar todos os autores
             executeQueryA();
             break;

         case 2: // (b) Livros por autor específico
             executeQueryB();
             break;

         case 3: // (c) Autores por título específico
             executeQueryC();
             break;
     }
 }

 // (a) Seleciona todos os autores da tabela Authors
 private void executeQueryA() {
     List<String[]> results = booksQueries.getAllAuthors();

     String[] columns = {"ID", "Primeiro Nome", "Sobrenome"};
     tableModel.setColumnIdentifiers(columns);

     for (String[] row : results) {
         tableModel.addRow(row);
     }

     statusLabel.setText(results.size() + " autor(es) encontrado(s).");
 }

 // (b) Seleciona livros de um autor específico
 private void executeQueryB() {
     String firstName = param1Field.getText().trim();
     String lastName  = param2Field.getText().trim();

     if (firstName.isEmpty() || lastName.isEmpty()) {
         JOptionPane.showMessageDialog(this,
             "Informe o Primeiro Nome e o Sobrenome do autor.",
             "Parâmetros incompletos", JOptionPane.WARNING_MESSAGE);
         return;
     }

     List<String[]> results = booksQueries.getBooksByAuthor(firstName, lastName);

     String[] columns = {"Título", "Ano (Copyright)", "ISBN"};
     tableModel.setColumnIdentifiers(columns);

     for (String[] row : results) {
         tableModel.addRow(row);
     }

     if (results.isEmpty()) {
         statusLabel.setText("Nenhum livro encontrado para: " + firstName + " " + lastName);
     } else {
         statusLabel.setText(results.size() + " livro(s) encontrado(s) para: " +
             firstName + " " + lastName + ".");
     }
 }

 // (c) Seleciona autores de um título específico
 private void executeQueryC() {
     String title = param1Field.getText().trim();

     if (title.isEmpty()) {
         JOptionPane.showMessageDialog(this,
             "Informe o título do livro.",
             "Parâmetro incompleto", JOptionPane.WARNING_MESSAGE);
         return;
     }

     List<String[]> results = booksQueries.getAuthorsByTitle(title);

     String[] columns = {"ID", "Primeiro Nome", "Sobrenome"};
     tableModel.setColumnIdentifiers(columns);

     for (String[] row : results) {
         tableModel.addRow(row);
     }

     if (results.isEmpty()) {
         statusLabel.setText("Nenhum autor encontrado para o título: \"" + title + "\"");
     } else {
         statusLabel.setText(results.size() + " autor(es) encontrado(s) para: \"" + title + "\".");
     }
 }

 // Executa uma consulta SQL personalizada digitada pelo usuário
 private void executeCustomQuery() {
     String sql = customQueryArea.getText().trim();

     if (sql.isEmpty()) {
         JOptionPane.showMessageDialog(this,
             "Digite uma instrução SQL antes de executar.",
             "SQL vazio", JOptionPane.WARNING_MESSAGE);
         return;
     }

     clearTable();

     BooksQueries.QueryResult qr = null;
     try {
         qr = booksQueries.executeCustomQuery(sql);

         // Lê os metadados para criar colunas na tabela
         ResultSetMetaData metaData = qr.resultSet.getMetaData();
         int columnCount = metaData.getColumnCount();
         Vector<String> columnNames = new Vector<>();
         for (int i = 1; i <= columnCount; i++) {
             columnNames.add(metaData.getColumnName(i));
         }
         tableModel.setColumnIdentifiers(columnNames);

         // Preenche as linhas
         int rowCount = 0;
         while (qr.resultSet.next()) {
             Vector<Object> row = new Vector<>();
             for (int i = 1; i <= columnCount; i++) {
                 row.add(qr.resultSet.getObject(i));
             }
             tableModel.addRow(row);
             rowCount++;
         }

         statusLabel.setText(rowCount + " registro(s) retornado(s).");

     } catch (SQLException e) {
         JOptionPane.showMessageDialog(this,
             "Erro ao executar SQL:\n" + e.getMessage(),
             "Erro de SQL", JOptionPane.ERROR_MESSAGE);
         statusLabel.setText("Erro na consulta personalizada.");
     } finally {
         if (qr != null) qr.close();
     }
 }

 // Limpa a tabela de resultados
 private void clearTable() {
     tableModel.setRowCount(0);
     tableModel.setColumnCount(0);
 }
}
