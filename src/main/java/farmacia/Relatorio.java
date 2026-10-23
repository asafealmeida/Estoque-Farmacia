package farmacia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Relatórios feitos com a API Stream (filter, map, groupingBy, reducing).
// Cada método devolve as linhas da tabela (cada String[] é uma linha).
public class Relatorio {

    // ---------------- RELATÓRIO 1: PRÓXIMOS AO VENCIMENTO ----------------
    // Medicamentos que vencem entre hoje e daqui a 30 dias
    public List<String[]> proximosAoVencimento(List<Medicamento> lista) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);

        return lista.stream()
                .filter(m -> !m.getDataValidade().isBefore(hoje))   // tira os já vencidos
                .filter(m -> !m.getDataValidade().isAfter(limite))  // tira os que vencem depois de 30 dias
                .map(m -> new String[]{m.getCodigo(), m.getNome(), m.getDataValidade().toString()})
                .collect(Collectors.toList());
    }

    // ---------------- RELATÓRIO 2: ESTOQUE BAIXO ----------------
    // Medicamentos com menos de 5 unidades
    public List<String[]> estoqueBaixo(List<Medicamento> lista) {
        return lista.stream()
                .filter(m -> m.getQuantidadeEstoque() < 5)
                .map(m -> new String[]{m.getCodigo(), m.getNome(),
                        String.valueOf(m.getQuantidadeEstoque())})
                .collect(Collectors.toList());
    }

    // ---------------- RELATÓRIO 3: VALOR POR FORNECEDOR ----------------
    // Soma o valor em estoque (preço x quantidade) de cada fornecedor
    public List<String[]> valorPorFornecedor(List<Medicamento> lista) {
        Map<String, BigDecimal> totais = lista.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getFornecedor().getRazaoSocial(),     // agrupa pelo nome do fornecedor
                        Collectors.mapping(Medicamento::valorEmEstoque,  // cada remédio vira seu valor em estoque
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))); // soma tudo, começando do zero

        return totais.entrySet().stream()
                .map(e -> new String[]{e.getKey(), "R$ " + e.getValue().setScale(2)})
                .collect(Collectors.toList());
    }

    // ---------------- RELATÓRIO 4: CONTROLADOS X NÃO CONTROLADOS ----------------
    public List<String[]> controladosENaoControlados(List<Medicamento> lista) {
        return lista.stream()
                .map(m -> new String[]{m.getCodigo(), m.getNome(),
                        m.isControlado() ? "Controlado" : "Não controlado"})
                .collect(Collectors.toList());
    }
}