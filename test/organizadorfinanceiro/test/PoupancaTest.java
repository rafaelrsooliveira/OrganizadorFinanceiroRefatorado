
package organizadorfinanceiro.test;

import static org.junit.Assert.*;
import org.junit.Test;
import organizadorfinanceiro.model.Poupanca;

/**
 * Testa o cálculo da projeção de poupança com juros compostos em 12 meses.
 */
public class PoupancaTest {
    

    @Test
    public void testProjecaoSemJurosAnual() {
        // Sem juros, o acumulado deve ser valor mensal x 12
        Poupanca poupanca = new Poupanca(500.00, 0.0);
        poupanca.projetarPoupanca();

        // Total esperado: 500 × 12 = 6000
        assertEquals(6000.0, poupanca.getAcumulado(), 0.01);
    }

    @Test
    public void testProjecaoComJurosAnual() {
        // Aplicação mensal de R$ 500 à taxa de juros de 12% ao ano 
        Poupanca poupanca = new Poupanca(500.00, 12.0);
        poupanca.projetarPoupanca();

        // Total esperado: 6383.2490
        assertEquals(6383.25, poupanca.getAcumulado(), 0.01);
    }

    @Test
    public void testProjecaoRetornaMatriz12Meses() {
        // Validação do tamanho da matriz
        Poupanca poupanca = new Poupanca(500.00, 9.0);
        double[] projecao = poupanca.projetarPoupanca();

        // A matriz deve ter exatamente 12 posições (12 meses)
        assertEquals(12, projecao.length);
    }
}