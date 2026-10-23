# Relatório – Estoque Medicamentos

**Disciplina:** Programação Orientada a Objetos II
**Avaliação:** P1
**Aluno:** Asafe Passos de Almeida

## Funcionalidades implementadas

- Cadastro de medicamento junto com os dados do fornecedor.
- Exclusão de um medicamento selecionado na tabela.
- Consulta por código.
- Listagem de todos os medicamentos cadastrados.
- Persistência em CSV separado por ponto-e-vírgula: o arquivo é lido ao iniciar e regravado após cada cadastro ou exclusão.
- Validação de dados: código (7 caracteres alfanuméricos e único), nome (mínimo de 3 caracteres), data de validade (não pode ser passada), quantidade (não negativa), preço (positivo) e CNPJ (formato e dígitos verificadores).
- Tratamento de exceções: erros de validação e de leitura/gravação do arquivo são capturados e mostrados ao usuário em uma janela de alerta.
- Quatro relatórios com Stream API, exibidos em tabela na tela de Relatórios:
  - Próximos ao vencimento (30 dias): `filter` e `map`.
  - Estoque baixo (menos de 5 unidades): `filter` e `map`.
  - Valor total do estoque por fornecedor: `groupingBy`, `mapping` e `reducing`.
  - Controlados x não controlados: `map`, com uma coluna "Tipo" que indica se o medicamento é controlado ou não.

## Decisões de design

Poucas classes, cada uma com uma função:

- `Fornecedor` e `Medicamento` formam o modelo de negócio.
- `ArquivoCsv` cuida da persistência.
- `Relatorio` concentra os relatórios.
- `MedicamentoController` monta a tela e trata os eventos.
- `Main` abre a janela e `Launcher` contém o método `main`.

Demais decisões:

- **Composição:** `Medicamento` possui um objeto `Fornecedor`. No CSV, os dados do fornecedor ficam na mesma linha do medicamento.
- **Validação no construtor:** os objetos só são criados com dados válidos. Quando algo está errado, o construtor lança `IllegalArgumentException` com uma mensagem clara.
- **Validade checada só no cadastro:** a data de validade é verificada apenas ao cadastrar (método `validarDataValidade`). Ao carregar o CSV não há essa verificação, para que um medicamento que venceu depois de cadastrado continue aparecendo na lista e nos relatórios.
- **BigDecimal e LocalDate:** `BigDecimal` para preço e `LocalDate` para datas, evitando erros de arredondamento e facilitando comparações.
- **Interface com menu no topo:** um `BorderPane` mantém o título "Estoque Medicamentos" e três botões (Cadastro, Medicamentos, Relatórios) sempre visíveis. Cada botão troca a tela do centro. As telas são criadas uma única vez, então o que foi digitado ou listado não se perde ao alternar.
- **Relatórios em tabela:** a tela de Relatórios usa um `TableView`, o mesmo componente da tela de Medicamentos, com a mesma margem e o mesmo visual. Os métodos da classe `Relatorio` devolvem listas de linhas (`List<String[]>`), e o controlador monta as colunas de cada relatório no método `mostrarRelatorio`. Assim a classe `Relatorio` continua sem nenhum código de interface.
- **Tela sem regra de negócio:** os botões só chamam as classes de modelo, de arquivo e de relatório, mantendo as responsabilidades separadas.
- **Launcher separado de Main:** evita o erro de componentes JavaFX ausentes ao executar pela IDE em projeto Maven sem módulos.
