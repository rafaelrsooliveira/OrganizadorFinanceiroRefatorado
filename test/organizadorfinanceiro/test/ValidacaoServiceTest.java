
package organizadorfinanceiro.test;

import org.junit.Test;
import organizadorfinanceiro.model.DespesasNaoRecorrentes;
import organizadorfinanceiro.model.DespesasRecorrentes;
import organizadorfinanceiro.service.DespesasService;

/**
 * Testes de validações de regras de negócio.
 */
public class ValidacaoServiceTest {

    // Validações de Despesas Recorrentes
    @Test(expected = IllegalArgumentException.class)
    public void testDespesaRecorrenteComDescricaoVazia() {
        DespesasService despesa = new DespesasService();
        despesa.cadastrarRecorrente("pedro", new DespesasRecorrentes("", 100, 100, 100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDespesaRecorrenteComValorNegativo() {
        DespesasService despesa = new DespesasService();
        despesa.cadastrarRecorrente("pedro", new DespesasRecorrentes("Celular", -80.00, 102.50, 99.05));
    }
    
    // Validações de Despesas Não Recorrentes
    @Test(expected = IllegalArgumentException.class)
    public void testDespesaNaoRecorrenteComDescricaoVazia() {
        DespesasService despesa = new DespesasService();
        despesa.cadastrarNaoRecorrente("pedro", new DespesasNaoRecorrentes("", 100, (byte) 3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDespesaNaoRecorrenteComValorNegativo() {
        DespesasService despesa = new DespesasService();
        despesa.cadastrarNaoRecorrente("pedro", new DespesasNaoRecorrentes("Microondas", -90.75, (byte) 5));
    }
}