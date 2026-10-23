package farmacia;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Medicamento {

    // ---------------- ATRIBUTOS ----------------
    private String codigo;
    private String nome;
    private String descricao;
    private String principioAtivo;
    private LocalDate dataValidade;
    private int quantidadeEstoque;
    private BigDecimal preco;
    private boolean controlado;
    private Fornecedor fornecedor; // composição: o medicamento "tem um" fornecedor

    // ---------------- CONSTRUTOR ----------------
    // Valida os dados antes de criar o objeto; se algo estiver errado, mostra uma mensagem de erro.
    public Medicamento(String codigo, String nome, String descricao, String principioAtivo,
                       LocalDate dataValidade, int quantidadeEstoque, BigDecimal preco,
                       boolean controlado, Fornecedor fornecedor) {

        // Código: exatamente 7 caracteres, só letras e números
        if (!codigo.matches("[A-Za-z0-9]{7}")) {
            throw new IllegalArgumentException("Código deve ter 7 caracteres alfanuméricos.");
        }
        // Nome: mínimo de 3 caracteres
        if (nome.isBlank() || nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres.");
        }
        // Descrição e princípio ativo são obrigatórios
        if (descricao.isBlank() || principioAtivo.isBlank()) {
            throw new IllegalArgumentException("Descrição e princípio ativo são obrigatórios.");
        }
        // Estoque não pode ser negativo
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa.");
        }
        // Preço precisa ser maior que zero
        if (preco.signum() <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
        }

        this.codigo = codigo.toUpperCase(); // guarda o código sempre em maiúsculo
        this.nome = nome;
        this.descricao = descricao;
        this.principioAtivo = principioAtivo;
        this.dataValidade = dataValidade;
        this.quantidadeEstoque = quantidadeEstoque;
        this.preco = preco;
        this.controlado = controlado;
        this.fornecedor = fornecedor;
    }

    // ---------------- VALIDAÇÃO DA DATA ----------------

    // Chamado só no cadastro. Ao ler o CSV não validamos, pois um remédio
    // cadastrado pode vencer depois e não deve sumir da lista.
    public static void validarDataValidade(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Informe a data de validade.");
        }
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de validade não pode ser passada.");
        }
    }

    // ---------------- CÁLCULO ----------------

    // Valor total deste item no estoque (preço x quantidade). Usado no relatório.
    public BigDecimal valorEmEstoque() {
        return preco.multiply(BigDecimal.valueOf(quantidadeEstoque));
    }

    // ---------------- GETTERS ----------------

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public boolean isControlado() {
        return controlado;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    // Usado pela coluna "Fornecedor" da tabela
    public String getNomeFornecedor() {
        return fornecedor.getRazaoSocial();
    }
}