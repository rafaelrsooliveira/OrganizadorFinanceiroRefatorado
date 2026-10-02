
import organizadorfinanceiro.model.*;
import organizadorfinanceiro.service.*;


/**
 * Classe de testes do sistema.
 * Executa os principais fluxos sem abrir telas Swing,
 * comprovando que a camada de negócio está desacoplada da interface.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   TESTES DO SISTEMA ORGANIZADOR FINANCEIRO");
        System.out.println("=================================================");
        
        // Limpa os dados de teste antes de rodar
        try (java.sql.Connection conn = organizadorfinanceiro.dao.ConnectionFactory.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM usuario WHERE usuario = 'pedro'");
            System.out.println("\nBanco limpo. Iniciando testes...\n");
        } catch (Exception e) {
            System.out.println("Erro ao limpar: " + e.getMessage());
        }

        // Instancia os Services (usam os DAOs)
        UsuarioService usuarioService = new UsuarioService();
        DespesasService despesaService = new DespesasService();
        PoupancaService poupancaService = new PoupancaService();

        // 1) CADASTRO DE USUÁRIO 
        System.out.println("\n[1] Cadastro de usuário");
        try {
            Usuario user1 = new Usuario("Pedro Silva", 5000.00, "pedro", "123456");
            usuarioService.cadastrar(user1);
            System.out.println("    :) Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("    :( Erro: " + e.getMessage());
        }

        // 2) VALIDAÇÃO: LOGIN DUPLICADO
        System.out.println("\n[2] Tentativa de cadastro com login duplicado (deve falhar)");
        try {
            Usuario user2 = new Usuario("Pedro Borges", 3000.00, "pedro", "654321");
            usuarioService.cadastrar(user2);
            System.out.println("    :( ERRO: aceitou login duplicado!");
        } catch (IllegalArgumentException e) {
            System.out.println("    :) Bloqueado corretamente: " + e.getMessage());
        }

        // 3) VALIDAÇÃO: SENHA CURTA
        System.out.println("\n[3] Cadastro com senha curta (deve falhar)");
        try {
            Usuario user3 = new Usuario("Patrícia", 2000.00, "paty", "123");
            usuarioService.cadastrar(user3);
            System.out.println("    :( ERRO: aceitou senha curta!");
        } catch (IllegalArgumentException e) {
            System.out.println("    :) Bloqueado corretamente: " + e.getMessage());
        }

        // 4) AUTENTICAÇÃO
        System.out.println("\n[4] Autenticação");
        try {
            Usuario user4 = usuarioService.autenticar("pedro", "123456");
            System.out.println("    :) Bem-vindo(a), " + user4.getNome()
                    + " (renda: R$ " + user4.getRendaMensal() + ")");
        } catch (Exception e) {
            System.out.println("    :( Erro: " + e.getMessage());
        }

        // 5) AUTENTICAÇÃO INVÁLIDA
        System.out.println("\n[5] Autenticação com senha errada (deve falhar)");
        try {
            usuarioService.autenticar("pedro", "000000");
            System.out.println("    :( ERRO: aceitou senha inválida!");
        } catch (SecurityException e) {
            System.out.println("    :) Bloqueado corretamente: " + e.getMessage());
        }

        // 6) DESPESA RECORRENTE
        System.out.println("\n[6] Cadastrar despesa recorrente (Aluguel)");
        try {
            DespesasRecorrentes aluguel =
                    new DespesasRecorrentes("Aluguel", 1500.00, 1450.00, 1400.00);
            despesaService.cadastrarRecorrente("pedro", aluguel);
            System.out.println("    :) Despesa cadastrada. Média: R$ "
                    + String.format("%,.2f", aluguel.calcularMedia()));
        } catch (Exception e) {
            System.out.println("    :( Erro: " + e.getMessage());
        }

        // 7) DESPESA NÃO RECORRENTE
        System.out.println("\n[7] Cadastrar despesa não recorrente (Laptop em 10x)");
        try {
            DespesasNaoRecorrentes smartTV =
                    new DespesasNaoRecorrentes("Laptop", 340.00, (byte) 10);
            despesaService.cadastrarNaoRecorrente("pedro", smartTV);
            System.out.println("    :) Despesa cadastrada. Total: R$ "
                    + String.format("%,.2f", smartTV.calcularTotalParcelado()));
        } catch (Exception e) {
            System.out.println("    :( Erro: " + e.getMessage());
        }

        // 8) VALIDAÇÃO: VALOR NEGATIVO
        System.out.println("\n[8] Despesa com valor negativo (deve falhar)");
        try {
            DespesasRecorrentes agua =
                    new DespesasRecorrentes("Agua", -100.00, 120, 110);
            despesaService.cadastrarRecorrente("pedro", agua);
            System.out.println("    :( ERRO: aceitou valor negativo!");
        } catch (IllegalArgumentException e) {
            System.out.println("    :) Bloqueado corretamente: " + e.getMessage());
        }

        // 9) POUPANÇA
        System.out.println("\n[9] Configurar poupança (R$ 1500/mês por 12 meses a 12% ao ano)");
        try {
            double acumulado = poupancaService.salvar("pedro", 1500.00, 12.0);
            System.out.println("    :) Poupança salva. Acumulado em 12 meses: R$ "
                    + String.format("%,.2f", acumulado));
        } catch (Exception e) {
            System.out.println("    :( Erro: " + e.getMessage());
        }

        // 10) PREVISÃO FINANCEIRA
        System.out.println("\n[10] Previsão financeira dos próximos 12 meses");
        try {
            Object[][] dados = despesaService.calcularPrevisao("pedro");

            System.out.println("    ---------------------------------------------------------------"
                    + "-----------------------------------");
            System.out.printf("    %-3s %-5s %-14s %-14s %-14s %-12s %-14s%n",
                    "ID", "Mês", "Recorrente", "Não Rec.", "Total",
                    "Poupança", "Saldo");
            System.out.println("    ---------------------------------------------------------------"
                    + "-----------------------------------");

            for (Object[] linha : dados) {
                System.out.printf("    %-3s %-5s %-14s %-14s %-14s %-12s %-14s%n",
                        linha[0], linha[1], linha[2], linha[3],
                        linha[4], linha[5], linha[6]);
            }
            System.out.println("    ---------------------------------------------------------------"
                    + "-----------------------------------");
        } catch (Exception e) {
            System.out.println("    :( Erro: " + e.getMessage());
        }

        // 11) ATUALIZAÇÃO DE RENDA
        System.out.println("\n[11] Atualizar renda do usuário para R$ 6000");
        try {
            usuarioService.atualizarRenda("pedro", 6000.00);
            Usuario atualizado = usuarioService.buscarPorLogin("pedro");
            System.out.println("    :) Renda atualizada: R$ "
                    + String.format("%,.2f", atualizado.getRendaMensal()));
        } catch (Exception e) {
            System.out.println("    :( Erro: " + e.getMessage());
        }

        System.out.println("\n=================================================");
        System.out.println("                FIM DOS TESTES");
        System.out.println("=================================================");
    }
}
