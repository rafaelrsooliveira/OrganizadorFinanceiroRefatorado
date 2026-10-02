
package organizadorfinanceiro.test;

import static org.junit.Assert.*;
import org.junit.Test;
import organizadorfinanceiro.model.DespesasNaoRecorrentes;

/** 
 * Testa o cálculo do valor total (valor mensal × número de parcelas).
 */
public class DespesasNaoRecorrentesTest {

    @Test
    public void testTotalParceladoSemValorDecimal() {
        // Parcelamento em 12 vezes sem casas decimais
        DespesasNaoRecorrentes despesa = new DespesasNaoRecorrentes("Laptop", 300.00, (byte) 12);

        // Total esperado: 300 × 12 = 3600
        assertEquals(3600.0, despesa.calcularTotalParcelado(), 0.001);
    }
    
    @Test
    public void testTotalParceladoComValorDecimal() {
        // Parcelamento em 5 vezes com casas decimais
        DespesasNaoRecorrentes despesa = new DespesasNaoRecorrentes("Jaqueta", 49.99, (byte) 5);

        // Total esperado: 49.99 × 5 = 249.95
        // Tolerância de 1 centavo para casos de arredondamento
        assertEquals(249.95, despesa.calcularTotalParcelado(), 0.01);
    }

    @Test
    public void testTotalParceladoUmaVez() {
        // Compra à vista (1 parcela)
        DespesasNaoRecorrentes despesa = new DespesasNaoRecorrentes("Livro", 79.90, (byte) 1);

        // Total esperado: 79.90 x 1 = 79.90
        assertEquals(79.90, despesa.calcularTotalParcelado(), 0.001);
    }
}