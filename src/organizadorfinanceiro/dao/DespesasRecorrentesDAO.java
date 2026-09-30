
package organizadorfinanceiro.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import organizadorfinanceiro.model.DespesasRecorrentes;

/**
 * DAO (Data Access Object) das Despesas Recorrentes.
 * Contém todos os métodos que acessam o banco de dados para a entidade DespesasRecorrentes.
 */
public class DespesasRecorrentesDAO {
    /**
     * Insere uma despesa recorrente para o usuário.
     */
    public boolean inserir(String login, DespesasRecorrentes despesa) {
        String sql = "INSERT INTO despesa_recorrente (usuario, descricao, valor_atual, valor_anterior, valor_dois_meses) VALUES (?,?,?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, despesa.getDescricao());
            stmt.setDouble(3, despesa.getValorAtual());
            stmt.setDouble(4, despesa.getValorAnterior());
            stmt.setDouble(5, despesa.getValorDoisMeses());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir despesa recorrente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca todas as despesas recorrentes do usuário.
     */
    public List<DespesasRecorrentes> buscarPorUsuario(String login) {
        String sql = "SELECT descricao, valor_atual, valor_anterior, valor_dois_meses FROM despesa_recorrente WHERE usuario = ?";
        List<DespesasRecorrentes> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DespesasRecorrentes(
                        rs.getString("descricao"),
                        rs.getDouble("valor_atual"),
                        rs.getDouble("valor_anterior"),
                        rs.getDouble("valor_dois_meses")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar despesas recorrentes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Exclui uma despesa recorrente pelo índice (posição na lista do usuário).
     * Busca o ID do registro na posição desejada e o remove.
     */
    public boolean excluirPorIndice(String login, int indice) {
        String sqlBusca = "SELECT id FROM despesa_recorrente WHERE usuario = ? ORDER BY id LIMIT 1 OFFSET ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBusca)) {
            stmt.setString(1, login);
            stmt.setInt(2, indice);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement del = conn.prepareStatement(
                            "DELETE FROM despesa_recorrente WHERE id = ?")) {
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