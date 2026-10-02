
package organizadorfinanceiro.test;

import static org.junit.Assert.*;
import org.junit.Test;
import organizadorfinanceiro.model.DespesasRecorrentes;

/**
 * Testa o cálculo da média dos últimos 3 meses.
 */
public class DespesasRecorrentesTest {
  
    @Test
    public void testCalcularMediaComValoresIguais() {
        // Cenário: 3 meses com o mesmo valor
        DespesasRecorrentes despesa = new DespesasRecorrentes("Aluguel", 1500, 1500, 1500);

        // A média deve ser exatamente 1500
        assertEquals(1500.0, despesa.calcularMedia(), 0.001);
    }

    @Test
    public void testCalcularMediaComValoresDiferentes() {
        // Cenário: valores diferentes nos 3 meses
        DespesasRecorrentes despesa = new DespesasRecorrentes("Água", 110, 130, 120);

        // Média esperada: (110 + 130 + 120) / 3 = 120
        assertEquals(120.0, despesa.calcularMedia(), 0.001);
    }

    @Test
    public void testCalcularMediaComValoresDecimais() {
        // Cenário com casas decimais
        DespesasRecorrentes despesa = new DespesasRecorrentes("Internet", 99.95, 105.50, 103.75);

        // Média esperada: (99.95 + 105.50 + 103.75) / 3 = 103.067
        // Tolerância de 1 centavo para casos de arredondamento
        assertEquals(103.06, despesa.calcularMedia(), 0.01);
    }
}