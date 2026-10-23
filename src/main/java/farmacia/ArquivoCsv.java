package farmacia;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ArquivoCsv {

    // O arquivo fica na pasta raiz do projeto
    private final Path arquivo = Path.of("medicamentos.csv");

    // ---------------- GRAVAR ----------------

    public void salvar(List<Medicamento> lista) throws IOException {
        List<String> linhas = new ArrayList<>();

        // Primeira linha: cabeçalho com o nome das colunas
        linhas.add("codigo;nome;descricao;principioAtivo;dataValidade;quantidade;preco;controlado;"
                + "cnpj;razaoSocial;telefone;email;cidade;estado");

        // Uma linha por medicamento; os dados do fornecedor vão nas últimas 6 colunas
        for (Medicamento m : lista) {
            Fornecedor f = m.getFornecedor();
            linhas.add(String.join(";",
                    m.getCodigo(), m.getNome(), m.getDescricao(), m.getPrincipioAtivo(),
                    m.getDataValidade().toString(), String.valueOf(m.getQuantidadeEstoque()),
                    m.getPreco().toPlainString(), String.valueOf(m.isControlado()),
                    f.getCnpj(), f.getRazaoSocial(), f.getTelefone(),
                    f.getEmail(), f.getCidade(), f.getEstado()));
        }

        // Escreve tudo no arquivo (substitui o conteúdo anterior)
        Files.write(arquivo, linhas);
    }

    // ---------------- LER ----------------

    public List<Medicamento> carregar() throws IOException {
        List<Medicamento> lista = new ArrayList<>();

        // Primeira execução: o arquivo ainda não existe, devolve lista vazia
        if (!Files.exists(arquivo)) {
            return lista;
        }

        List<String> linhas = Files.readAllLines(arquivo);

        // i = 1 pula o cabeçalho
        for (int i = 1; i < linhas.size(); i++) {
            if (linhas.get(i).isBlank()) {
                continue; // ignora linhas em branco
            }

            // Quebra a linha nos ";"
            String[] c = linhas.get(i).split(";");

            // Recria o fornecedor (colunas 8 a 13) e o medicamento (colunas 0 a 7)
            Fornecedor f = new Fornecedor(c[8], c[9], c[10], c[11], c[12], c[13]);
            Medicamento m = new Medicamento(c[0], c[1], c[2], c[3], LocalDate.parse(c[4]),
                    Integer.parseInt(c[5]), new BigDecimal(c[6]), Boolean.parseBoolean(c[7]), f);

            lista.add(m);
        }

        return lista;
    }
}