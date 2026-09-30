
package organizadorfinanceiro.dao;

import java.sql.*;

/**
 * DAO (Data Access Object) da Poupança.
 * Contém todos os métodos que acessam o banco de dados para a entidade Poupanca.
 */
public class PoupancaDAO {

    /**
     * Salva ou atualiza a poupança do usuário (upsert).
     * Cada usuário tem apenas um registro de poupança.
     */
    public boolean salvar(String login, double mensal, double taxa, double acumulado) {
        String sql = "INSERT INTO poupanca (usuario, poupanca_mensal, taxa_juros_anual, acumulado) "
                   + "VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE poupanca_mensal = VALUES(poupanca_mensal), "
                   + "taxa_juros_anual = VALUES(taxa_juros_anual), acumulado = VALUES(acumulado)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setDouble(2, mensal);
            stmt.setDouble(3, taxa);
            stmt.setDouble(4, acumulado);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar poupança: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca a poupança do usuário. Retorna array [poupancaMensal, taxaJurosAnual, acumulado]
     * ou null se não houver registro.
     */
    public double[] buscar(String login) {
        String sql = "SELECT poupanca_mensal, taxa_juros_anual, acumulado FROM poupanca WHERE usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new double[]{rs.getDouble(1), rs.getDouble(2), rs.getDouble(3)};
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar poupança: " + e.getMessage());
        }
        return null;
    }
}