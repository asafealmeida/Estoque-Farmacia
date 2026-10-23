# Estoque Medicamentos

Sistema em Java com interface gráfica JavaFX para gerenciar o estoque de medicamentos de uma farmácia. Projeto da P1 de Programação Orientada a Objetos II – UNIFAN.

## Requisitos

- JDK 17 ou superior
- Maven (o IntelliJ já traz). A biblioteca JavaFX é baixada automaticamente pelo `pom.xml`.

## Como executar

1. Abra a pasta do projeto no IntelliJ IDEA (o `pom.xml` é reconhecido como projeto Maven).
2. Aguarde o Maven baixar as dependências.
3. Execute a classe `farmacia.Launcher` (botão de play ao lado do método `main`).

Os avisos `WARNING` em vermelho que aparecem no console sobre o JavaFX são normais e não afetam o programa.

## Estrutura do projeto

```
src/main/java/farmacia/
├── Launcher.java              # ponto de entrada (main)
├── Main.java                  # abre a janela JavaFX
├── MedicamentoController.java # monta as telas e trata os eventos
├── Medicamento.java           # modelo + validações
├── Fornecedor.java            # modelo + validação do CNPJ
├── ArquivoCsv.java            # leitura e gravação do CSV
└── Relatorio.java             # relatórios com Stream API
```

## Como usar

A janela tem um menu no topo com três botões. Cada botão troca a tela exibida:

- **Cadastro**: preencha os dados do medicamento e do fornecedor e clique em *Cadastrar*.
- **Medicamentos**: mostra a tabela. Digite um código e clique em *Consultar*, use *Listar todos* para ver tudo ou selecione uma linha e clique em *Excluir selecionado*.
- **Relatórios**: escolha um dos quatro relatórios (vencimento em 30 dias, estoque baixo, valor por fornecedor, controlados x não controlados). O resultado aparece em uma tabela, com o mesmo visual da tela de Medicamentos.

## Arquivo de dados

Os dados ficam em `medicamentos.csv`, com campos separados por `;`, na pasta raiz do projeto. O arquivo é criado no primeiro cadastro, lido ao iniciar o programa e regravado após cada cadastro ou exclusão.

## Regras de validação

| Campo | Regra |
|---|---|
| Código | 7 caracteres alfanuméricos, único |
| Nome | não vazio, mínimo de 3 caracteres |
| Data de validade | não pode ser passada (checada no cadastro) |
| Quantidade em estoque | não pode ser negativa |
| Preço | maior que zero |
| CNPJ | 14 dígitos (com ou sem máscara) e dígitos verificadores corretos |
| Dados do fornecedor | todos obrigatórios |
