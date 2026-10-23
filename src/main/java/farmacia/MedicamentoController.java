package farmacia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

// Monta as telas e trata os cliques dos botões.
// Não tem regra de negócio: chama Medicamento, Fornecedor, ArquivoCsv e Relatorio.
public class MedicamentoController {

    // ---------------- DADOS ----------------
    private ArquivoCsv arquivo = new ArquivoCsv();
    private Relatorio relatorio = new Relatorio();

    // Lista "de verdade" (a que vai para o CSV)
    private List<Medicamento> medicamentos = new ArrayList<>();

    // Lista que a tabela mostra no momento; a tabela se atualiza sozinha quando ela muda
    private ObservableList<Medicamento> dadosTabela = FXCollections.observableArrayList();

    // ---------------- COMPONENTES: TELA DE CADASTRO ----------------
    private TextField txtCodigo = new TextField();
    private TextField txtNome = new TextField();
    private TextField txtDescricao = new TextField();
    private TextField txtPrincipioAtivo = new TextField();
    private DatePicker dpValidade = new DatePicker();
    private TextField txtQuantidade = new TextField();
    private TextField txtPreco = new TextField();
    private CheckBox chkControlado = new CheckBox("Controlado");
    private TextField txtCnpj = new TextField();
    private TextField txtRazaoSocial = new TextField();
    private TextField txtTelefone = new TextField();
    private TextField txtEmail = new TextField();
    private TextField txtCidade = new TextField();
    private TextField txtEstado = new TextField();

    // ---------------- COMPONENTES: TELA DE LISTAGEM ----------------
    private TableView<Medicamento> tabela = new TableView<>();
    private TextField txtBusca = new TextField();

    // ---------------- COMPONENTES: TELA DE RELATÓRIOS ----------------
    private TableView<String[]> tabelaRelatorio = new TableView<>();

    // ---------------- CONSTRUTOR ----------------
    // Ao abrir o programa, lê o CSV e mostra os dados na tabela
    public MedicamentoController() {
        try {
            medicamentos = arquivo.carregar();
        } catch (IOException | RuntimeException e) {
            mostrarErro("Não foi possível ler o arquivo: " + e.getMessage());
        }
        dadosTabela.setAll(medicamentos);
    }

    // ==================================================================
    //                          MONTAGEM DA TELA
    // ==================================================================

    //título e botões no topo
    public BorderPane criarTela() {
        // As três telas são criadas uma única vez (assim não perdem o que foi digitado)
        Node telaCadastro = criarFormulario();
        Node telaLista = criarLista();
        Node telaRelatorios = criarRelatorios();

        // Título
        Label titulo = new Label("Estoque Medicamento");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        // Botões do menu
        Button btnCadastro = new Button("Cadastro");
        Button btnLista = new Button("Medicamentos");
        Button btnRelatorios = new Button("Relatórios");

        HBox menu = new HBox(10, btnCadastro, btnLista, btnRelatorios);
        VBox topo = new VBox(10, titulo, menu);
        topo.setPadding(new Insets(15));

        // Painel principal: topo fixo e centro que muda
        BorderPane raiz = new BorderPane();
        raiz.setTop(topo);
        raiz.setCenter(telaCadastro);

        // Cada botão troca a tela do centro
        btnCadastro.setOnAction(e -> raiz.setCenter(telaCadastro));
        btnLista.setOnAction(e -> raiz.setCenter(telaLista));
        btnRelatorios.setOnAction(e -> raiz.setCenter(telaRelatorios));

        return raiz;
    }

    // ---------------- TELA: CADASTRO ----------------
    private GridPane criarFormulario() {
        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(8);
        g.setPadding(new Insets(15));

        // Cada linha: rótulo, campo, rótulo, campo
        g.addRow(0, new Label("Código (7 caracteres)"), txtCodigo, new Label("Nome"), txtNome);
        g.addRow(1, new Label("Descrição"), txtDescricao, new Label("Princípio ativo"), txtPrincipioAtivo);
        g.addRow(2, new Label("Validade"), dpValidade, new Label("Quantidade"), txtQuantidade);
        g.addRow(3, new Label("Preço (ex: 12,50)"), txtPreco, chkControlado);
        g.addRow(4, new Label("CNPJ"), txtCnpj, new Label("Razão social"), txtRazaoSocial);
        g.addRow(5, new Label("Telefone"), txtTelefone, new Label("E-mail"), txtEmail);
        g.addRow(6, new Label("Cidade"), txtCidade, new Label("Estado (UF)"), txtEstado);

        // Botão que chama o método cadastrar()
        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setOnAction(e -> cadastrar());
        g.add(btnCadastrar, 1, 7);

        return g;
    }

    // ---------------- TELA: LISTAGEM ----------------
    private VBox criarLista() {
        // Colunas da tabela: o 2º texto é o getter usado (ex.: "codigo" -> getCodigo())
        coluna("Código", "codigo");
        coluna("Nome", "nome");
        coluna("Princípio ativo", "principioAtivo");
        coluna("Validade", "dataValidade");
        coluna("Estoque", "quantidadeEstoque");
        coluna("Preço", "preco");
        coluna("Controlado", "controlado");
        coluna("Fornecedor", "nomeFornecedor");
        tabela.setItems(dadosTabela);

        // Barra de ações acima da tabela
        txtBusca.setPromptText("Código do medicamento");
        Button btnConsultar = new Button("Consultar");
        Button btnListar = new Button("Listar todos");
        Button btnExcluir = new Button("Excluir selecionado");
        btnConsultar.setOnAction(e -> consultar());
        btnListar.setOnAction(e -> dadosTabela.setAll(medicamentos));
        btnExcluir.setOnAction(e -> excluir());

        HBox barra = new HBox(10, txtBusca, btnConsultar, btnListar, btnExcluir);
        VBox caixa = new VBox(10, barra, tabela);
        caixa.setPadding(new Insets(15));

        // A tabela ocupa todo o espaço que sobrar na tela
        VBox.setVgrow(tabela, Priority.ALWAYS);
        return caixa;
    }

    // Cria uma coluna da tabela ligada a um getter do Medicamento
    private void coluna(String titulo, String propriedade) {
        TableColumn<Medicamento, Object> c = new TableColumn<>(titulo);
        c.setCellValueFactory(new PropertyValueFactory<>(propriedade));
        tabela.getColumns().add(c);
    }

    // ---------------- TELA: RELATÓRIOS ----------------
    private VBox criarRelatorios() {
        Button b1 = new Button("Próximos ao vencimento");
        Button b2 = new Button("Estoque baixo");
        Button b3 = new Button("Valor por fornecedor");
        Button b4 = new Button("Controlados x não controlados");

        // Cada botão define as colunas e preenche a tabela com as linhas do relatório
        b1.setOnAction(e -> mostrarRelatorio(
                new String[]{"Código", "Nome", "Validade"},
                relatorio.proximosAoVencimento(medicamentos)));
        b2.setOnAction(e -> mostrarRelatorio(
                new String[]{"Código", "Nome", "Estoque"},
                relatorio.estoqueBaixo(medicamentos)));
        b3.setOnAction(e -> mostrarRelatorio(
                new String[]{"Fornecedor", "Valor total em estoque"},
                relatorio.valorPorFornecedor(medicamentos)));
        b4.setOnAction(e -> mostrarRelatorio(
                new String[]{"Código", "Nome", "Tipo"},
                relatorio.controladosENaoControlados(medicamentos)));

        tabelaRelatorio.setPlaceholder(new Label("Escolha um relatório acima"));

        HBox barra = new HBox(10, b1, b2, b3, b4);
        VBox caixa = new VBox(10, barra, tabelaRelatorio);
        caixa.setPadding(new Insets(15)); // mesma margem da tela de Medicamentos

        // A tabela ocupa todo o espaço que sobrar, como na tela de Medicamentos
        VBox.setVgrow(tabelaRelatorio, Priority.ALWAYS);
        return caixa;
    }

    // Monta as colunas do relatório escolhido e preenche as linhas
    private void mostrarRelatorio(String[] titulos, List<String[]> linhas) {
        tabelaRelatorio.getColumns().clear();

        for (int i = 0; i < titulos.length; i++) {
            final int indice = i; // posição da coluna dentro de cada linha
            TableColumn<String[], String> c = new TableColumn<>(titulos[i]);
            c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[indice]));
            tabelaRelatorio.getColumns().add(c);
        }

        tabelaRelatorio.setItems(FXCollections.observableArrayList(linhas));
        tabelaRelatorio.setPlaceholder(new Label("(nenhum registro)"));
    }

    // ==================================================================
    //                              EVENTOS
    // ==================================================================

    // Botão "Cadastrar": lê os campos, valida, salva e atualiza a tela
    private void cadastrar() {
        try {
            // 1. Valida a data (regra só do cadastro)
            Medicamento.validarDataValidade(dpValidade.getValue());

            // 2. Cria o fornecedor (o construtor valida os dados)
            Fornecedor fornecedor = new Fornecedor(
                    txtCnpj.getText(), txtRazaoSocial.getText().trim(), txtTelefone.getText().trim(),
                    txtEmail.getText().trim(), txtCidade.getText().trim(), txtEstado.getText().trim());

            // 3. Cria o medicamento (o construtor valida os dados)
            Medicamento novo = new Medicamento(
                    txtCodigo.getText().trim(), txtNome.getText().trim(),
                    txtDescricao.getText().trim(), txtPrincipioAtivo.getText().trim(),
                    dpValidade.getValue(),
                    Integer.parseInt(txtQuantidade.getText().trim()),
                    new BigDecimal(txtPreco.getText().trim().replace(",", ".")), // aceita vírgula
                    chkControlado.isSelected(), fornecedor);

            // 4. Não permite código repetido
            if (buscar(novo.getCodigo()) != null) {
                throw new IllegalArgumentException("Já existe um medicamento com esse código.");
            }

            // 5. Adiciona na lista, grava no CSV e atualiza a tela
            medicamentos.add(novo);
            arquivo.salvar(medicamentos);
            dadosTabela.setAll(medicamentos);
            limparFormulario();
            new Alert(Alert.AlertType.INFORMATION, "Medicamento cadastrado!").showAndWait();

        } catch (NumberFormatException e) {
            // Quantidade ou preço com letras
            mostrarErro("Quantidade e preço devem ser números válidos.");
        } catch (IllegalArgumentException e) {
            // Qualquer validação dos construtores (a mensagem vem do throw)
            mostrarErro(e.getMessage());
        } catch (IOException e) {
            // Erro ao gravar o arquivo
            mostrarErro("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    // Botão "Excluir selecionado"
    private void excluir() {
        Medicamento selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarErro("Selecione um medicamento na tabela.");
            return;
        }
        try {
            medicamentos.remove(selecionado);
            arquivo.salvar(medicamentos);
            dadosTabela.setAll(medicamentos);
        } catch (IOException e) {
            mostrarErro("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    // Botão "Consultar"
    private void consultar() {
        Medicamento encontrado = buscar(txtBusca.getText().trim());
        if (encontrado == null) {
            mostrarErro("Medicamento não encontrado.");
        } else {
            dadosTabela.setAll(encontrado);
        }
    }

    // ==================================================================
    //                            AUXILIARES
    // ==================================================================

    // Procura um medicamento pelo código (devolve null se não achar)
    private Medicamento buscar(String codigo) {
        for (Medicamento m : medicamentos) {
            if (m.getCodigo().equalsIgnoreCase(codigo)) {
                return m;
            }
        }
        return null;
    }

    // Esvazia todos os campos do formulário
    private void limparFormulario() {
        txtCodigo.clear();
        txtNome.clear();
        txtDescricao.clear();
        txtPrincipioAtivo.clear();
        dpValidade.setValue(null);
        txtQuantidade.clear();
        txtPreco.clear();
        chkControlado.setSelected(false);
        txtCnpj.clear();
        txtRazaoSocial.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtCidade.clear();
        txtEstado.clear();
    }

    // Mostra uma janelinha de erro
    private void mostrarErro(String mensagem) {
        new Alert(Alert.AlertType.ERROR, mensagem).showAndWait();
    }
}