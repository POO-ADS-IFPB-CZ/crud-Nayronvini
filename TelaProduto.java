import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class TelaProduto extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ProdutoDao produtoDao;
    private List<Produto> produtos;

    public TelaProduto() {
        super("Cadastro de Produtos");

        produtoDao = new ProdutoDao();
        produtos = produtoDao.listarTodos();

        // Configuração da tabela
        modeloTabela = new DefaultTableModel(new String[]{"Código", "Descrição", "Preço"}, 0);
        tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);

        carregarTabela();

        // Botões
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);

        // Layout principal
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnRemover.addActionListener(e -> removerProduto());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[]{p.getCodigo(), p.getDescricao(), p.getPreco()});
        }
    }

    private void adicionarProduto() {
        JTextField campoCodigo = new JTextField();
        JTextField campoDescricao = new JTextField();
        JTextField campoPreco = new JTextField();

        JPanel painel = new JPanel(new GridLayout(3, 2));
        painel.add(new JLabel("Código:"));
        painel.add(campoCodigo);
        painel.add(new JLabel("Descrição:"));
        painel.add(campoDescricao);
        painel.add(new JLabel("Preço:"));
        painel.add(campoPreco);

        int resultado = JOptionPane.showConfirmDialog(this, painel, "Adicionar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int codigo = Integer.parseInt(campoCodigo.getText());
                String descricao = campoDescricao.getText();
                double preco = Double.parseDouble(campoPreco.getText());

                Produto novo = new Produto(codigo, descricao, preco);
                produtos.remove(novo); // remove se já existir com o mesmo código
                produtos.add(novo);
                produtoDao.salvarTodos(produtos);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    private void atualizarProduto() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para atualizar.");
            return;
        }

        Produto produto = produtos.get(linhaSelecionada);

        JTextField campoCodigo = new JTextField(String.valueOf(produto.getCodigo()));
        JTextField campoDescricao = new JTextField(produto.getDescricao());
        JTextField campoPreco = new JTextField(String.valueOf(produto.getPreco()));

        JPanel painel = new JPanel(new GridLayout(3, 2));
        painel.add(new JLabel("Código:"));
        painel.add(campoCodigo);
        painel.add(new JLabel("Descrição:"));
        painel.add(campoDescricao);
        painel.add(new JLabel("Preço:"));
        painel.add(campoPreco);

        int resultado = JOptionPane.showConfirmDialog(this, painel, "Atualizar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int codigo = Integer.parseInt(campoCodigo.getText());
                String descricao = campoDescricao.getText();
                double preco = Double.parseDouble(campoPreco.getText());

                Produto atualizado = new Produto(codigo, descricao, preco);
                produtos.set(linhaSelecionada, atualizado);
                produtoDao.salvarTodos(produtos);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    private void removerProduto() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
            return;
        }

        Produto produto = produtos.get(linhaSelecionada);
        produtos.remove(produto);
        try {
            produtoDao.salvarTodos(produtos);
            carregarTabela();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaProduto::new);
    }
}

