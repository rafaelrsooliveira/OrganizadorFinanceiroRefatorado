
package organizadorfinanceiro.dao;

import java.sql.*;
import organizadorfinanceiro.model.Usuario;

/**
 * DAO (Data Access Object) do Usuário.
 * Contém todos os métodos que acessam o banco de dados para a entidade Usuario.
 */
public class UsuarioDAO {
    /**
     * Insere um novo usuário no banco de dados.
     *
     * @param usuario objeto Usuario a ser salvo
     * @return true se inserido com sucesso, false se o login já existe
     */
    public boolean inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (usuario, senha, nome, renda_mensal) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getNome());
            stmt.setDouble(4, usuario.getRendaMensal());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Código 1062 = entrada duplicada (login já existe)
            if (e.getErrorCode() == 1062) {
                return false;
            }
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }
    
    /**
     * Verifica se um login já está cadastrado no banco.
     *
     * @param login nome de usuário a verificar
     * @return true se já existe, false se está disponível
     */
    public boolean loginJaExiste(String login) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE usuario = ?";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar login: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn);
        }

        return false;
    }

    /**
     * Busca um usuário pelo login e senha (usado na autenticação).
     *
     * @param login  nome de usuário
     * @param senha  senha do usuário
     * @return objeto Usuario se encontrado, ou null se credenciais inválidas
     */
    public Usuario buscarPorLoginESenha(String login, String senha) {
        String sql = "SELECT * FROM usuario WHERE usuario = ? AND senha = ?";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, login);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Monta o objeto Usuario com os dados vindos do banco
                return new Usuario(
                    rs.getString("nome"),
                    rs.getDouble("renda_mensal"),
                    rs.getString("usuario"),
                    rs.getString("senha")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn);
        }

        return null; // não encontrado
    }

    /**
     * Busca um usuário apenas pelo login (sem senha).
     * Útil para recuperar dados do usuário já autenticado.
     *
     * @param login nome de usuário
     * @return objeto Usuario se encontrado, ou null caso contrário
     */
    public Usuario buscarPorLogin(String login) {
        String sql = "SELECT * FROM usuario WHERE usuario = ?";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getString("nome"),
                    rs.getDouble("renda_mensal"),
                    rs.getString("usuario"),
                    rs.getString("senha")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por login: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn);
        }

        return null;
    }

    /**
     * Atualiza a renda mensal de um usuário no banco de dados.
     *
     * @param login       nome de usuário a atualizar
     * @param novaRenda   novo valor da renda mensal
     * @return true se atualizado com sucesso, false caso contrário
     */
    public boolean atualizarRenda(String login, double novaRenda) {
        String sql = "UPDATE usuario SET renda_mensal = ? WHERE usuario = ?";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setDouble(1, novaRenda);
            stmt.setString(2, login);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; // true se encontrou e atualizou o usuário

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar renda: " + e.getMessage());
            return false;
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }
}