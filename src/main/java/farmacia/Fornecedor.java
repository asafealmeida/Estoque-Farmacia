package farmacia;

public class Fornecedor {

    // ---------------- ATRIBUTOS ----------------
    private String cnpj;
    private String razaoSocial;
    private String telefone;
    private String email;
    private String cidade;
    private String estado;

    // ---------------- CONSTRUTOR ----------------
    // Só cria o objeto se os dados forem válidos; senão lança exceção.
    public Fornecedor(String cnpj, String razaoSocial, String telefone,
                      String email, String cidade, String estado) {

        // Valida o CNPJ (formato e dígitos verificadores)
        if (!cnpjValido(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido.");
        }

        // Todos os outros campos são obrigatórios
        if (razaoSocial.isBlank() || telefone.isBlank() || email.isBlank()
                || cidade.isBlank() || estado.isBlank()) {
            throw new IllegalArgumentException("Preencha todos os dados do fornecedor.");
        }

        // Guarda o CNPJ só com números (sem pontos, barra e traço)
        this.cnpj = cnpj.replaceAll("\\D", "");
        this.razaoSocial = razaoSocial;
        this.telefone = telefone;
        this.email = email;
        this.cidade = cidade;
        this.estado = estado;
    }

    // ---------------- VALIDAÇÃO DO CNPJ ----------------

    // Confere o tamanho e os dois dígitos verificadores do CNPJ
    private boolean cnpjValido(String cnpj) {
        // Remove tudo que não é número
        String numeros = cnpj.replaceAll("\\D", "");

        // Precisa ter exatamente 14 dígitos
        if (numeros.length() != 14) {
            return false;
        }
        // Recusa CNPJ com todos os dígitos iguais
        if (numeros.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Calcula os dois últimos dígitos e compara com os digitados
        int digito1 = calcularDigito(numeros, 12);
        int digito2 = calcularDigito(numeros, 13);

        return numeros.charAt(12) - '0' == digito1
                && numeros.charAt(13) - '0' == digito2;
    }

    // Calcula um dígito verificador usando os primeiros "quantidade" números
    private int calcularDigito(String numeros, int quantidade) {
        int soma = 0;
        int peso = quantidade - 7;

        for (int i = 0; i < quantidade; i++) {
            int digito = numeros.charAt(i) - '0';
            soma += digito * peso;
            peso--;
            if (peso < 2) {
                peso = 9;
            }
        }

        int resto = soma % 11;
        if (resto < 2) {
            return 0;
        }
        return 11 - resto;
    }

    // ---------------- GETTERS ----------------

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }
}