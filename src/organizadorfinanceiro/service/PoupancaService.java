
package organizadorfinanceiro.service;

import organizadorfinanceiro.dao.PoupancaDAO;
import organizadorfinanceiro.model.Poupanca;

public class PoupancaService {

    private final PoupancaDAO dao = new PoupancaDAO();

    /** Salva a poupança calculando o acumulado em 12 meses. */
    public double salvar(String login, double mensal, double taxaAnual) {
        if (mensal < 0 || taxaAnual < 0)
            throw new IllegalArgumentException("Valores não podem ser abaixo de zero.");

        Poupanca poupanca = new Poupanca(mensal, taxaAnual);
        poupanca.projetarPoupanca();
        double acumulado = poupanca.getAcumulado();

        if (!dao.salvar(login, mensal, taxaAnual, acumulado))
            throw new RuntimeException("Erro ao salvar poupança.");

        return acumulado;
    }

    public double[] buscar(String login) {
        return dao.buscar(login);
    }
}