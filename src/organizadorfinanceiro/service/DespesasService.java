
package organizadorfinanceiro.service;

import java.time.LocalDate;
import java.util.List;
import organizadorfinanceiro.dao.*;
import organizadorfinanceiro.model.*;

public class DespesasService {

    private DespesasRecorrentesDAO recDAO = new DespesasRecorrentesDAO();
    private DespesasNaoRecorrentesDAO naoRecDAO = new DespesasNaoRecorrentesDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private PoupancaDAO poupancaDAO = new PoupancaDAO();

    // Cadastros
    public void cadastrarRecorrente(String login, DespesasRecorrentes rec) {
        if (rec.getDescricao() == null || rec.getDescricao().trim().isEmpty())
            throw new IllegalArgumentException("Descrição é obrigatória.");
        if (rec.getValorAtual() <= 0)
            throw new IllegalArgumentException("Valor deve ser positivo.");
        if (!recDAO.inserir(login, rec))
            throw new RuntimeException("Erro ao salvar no banco.");
    }

    public void cadastrarNaoRecorrente(String login, DespesasNaoRecorrentes naoRec) {
        if (naoRec.getDescricao() == null || naoRec.getDescricao().trim().isEmpty())
            throw new IllegalArgumentException("Descrição é obrigatória.");
        if (naoRec.getValorMensal() <= 0)
            throw new IllegalArgumentException("Valor deve ser positivo.");
        if (naoRec.getQtdeParcelas() <= 0)
            throw new IllegalArgumentException("Quantidade de parcelas deve ser maior que zero.");
        if (!naoRecDAO.inserir(login, naoRec))
            throw new RuntimeException("Erro ao salvar no banco.");
    }

    // Consultas
    public List<DespesasRecorrentes> listarRecorrentes(String login) {
        return recDAO.buscarPorUsuario(login);
    }

    public List<DespesasNaoRecorrentes> listarNaoRecorrentes(String login) {
        return naoRecDAO.buscarPorUsuario(login);
    }

    public void excluirRecorrente(String login, int indice) {
        recDAO.excluirPorIndice(login, indice);
    }

    public void excluirNaoRecorrente(String login, int indice) {
        naoRecDAO.excluirPorIndice(login, indice);
    }

    // Regra de negócio principal
    /** Retorna matriz com a previsão financeira. */
    public Object[][] calcularPrevisao(String login) {
        Usuario usuario = usuarioDAO.buscarPorLogin(login);
        if (usuario == null) throw new RuntimeException("Usuário não encontrado.");
        double renda = usuario.getRendaMensal();

        double totalRecorrente = 0;
        for (DespesasRecorrentes rec : recDAO.buscarPorUsuario(login))
            totalRecorrente += rec.calcularMedia();

        double[] naoRecPorMes = new double[12];
        for (DespesasNaoRecorrentes naoRec : naoRecDAO.buscarPorUsuario(login))
            for (int i = 0; i < naoRec.getQtdeParcelas() && i < 12; i++)
                naoRecPorMes[i] += naoRec.getValorMensal();

        double poupanca = 0;
        double[] dadosP = poupancaDAO.buscar(login);
        if (dadosP != null) poupanca = dadosP[0];

        String[] meses = {"JAN","FEV","MAR","ABR","MAI","JUN",
                          "JUL","AGO","SET","OUT","NOV","DEZ"};
        int mesAtual = LocalDate.now().getMonthValue() - 1;

        Object[][] resultado = new Object[12][7];
        for (int i = 0; i < 12; i++) {
            int idx = (mesAtual + 1 + i) % 12;
            double total = totalRecorrente + naoRecPorMes[i];
            double saldo = renda - total - poupanca;
            resultado[i] = new Object[]{
                i + 1, meses[idx],
                String.format("%,.2f", totalRecorrente),
                String.format("%,.2f", naoRecPorMes[i]),
                String.format("%,.2f", total),
                String.format("%,.2f", poupanca),
                String.format("%,.2f", saldo)
            };
        }
        return resultado;
    }
}