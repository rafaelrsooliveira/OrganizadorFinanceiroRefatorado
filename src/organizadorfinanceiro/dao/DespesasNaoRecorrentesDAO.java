
package organizadorfinanceiro.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import organizadorfinanceiro.model.DespesasNaoRecorrentes;

/**
 * DAO (Data Access Object) das Despesas Não Recorrentes.
 * Contém todos os métodos que acessam o banco de dados para a entidade DespesasNaoRcorrentes.
 */
public class DespesasNaoRecorrentesDAO {

    /**     
     * Insere uma despesa não recorrente para o usuário.
     */
    public boolean inserir(String login, DespesasNaoRecorrentes despesa) {
        String sql = "INSERT INTO despesa_nao_recorrente (usuario, descricao, valor_mensal, qtde_parcelas) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, despesa.getDescricao());
            stmt.setDouble(3, despesa.getValorMensal());
            stmt.setByte(4, despesa.getQtdeParcelas());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir: " + e.getMessage());
            return false;
        }
    }
    /**
     * Busca todas as despesas não recorrentes do usuário.
     */
    public List<DespesasNaoRecorrentes> buscarPorUsuario(String login) {
        String sql = "SELECT descricao, valor_mensal, qtde_parcelas FROM despesa_nao_recorrente WHERE usuario = ?";
        List<DespesasNaoRecorrentes> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DespesasNaoRecorrentes(
                        rs.getString("descricao"),
                        rs.getDouble("valor_mensal"),
                        rs.getByte("qtde_parcelas")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar: " + e.getMessage());
        }
        return lista;
    }
    /**
     * Exclui uma despesa não recorrente pelo índice (posição na lista do usuário).
     */
    public boolean excluirPorIndice(String login, int indice) {
        String sqlBusca = "SELECT id FROM despesa_nao_recorrente WHERE usuario = ? ORDER BY id LIMIT 1 OFFSET ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBusca)) {
            stmt.setString(1, login);
            stmt.setInt(2, indice);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement del = conn.prepareStatement(
                            "DELETE FROM despesa_nao_recorrente WHERE id = ?")) {
                        del.setInt(1, rs.getInt("id"));
                        return del.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir: " + e.getMessage());
        }
        return false;
    }
}