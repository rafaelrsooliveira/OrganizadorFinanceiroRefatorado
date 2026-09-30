
package organizadorfinanceiro.service;

import organizadorfinanceiro.dao.UsuarioDAO;
import organizadorfinanceiro.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();

    /** Cadastra usuário aplicando regras de negócio. */
    public void cadastrar(Usuario usuario) throws IllegalArgumentException {
        if (usuario.getUsuario() == null || usuario.getUsuario().trim().isEmpty())
            throw new IllegalArgumentException("É obrigatório informar o Usuário.");
        if (usuario.getSenha() == null || usuario.getSenha().length() < 6)
            throw new IllegalArgumentException("Senha deve ter ao menos 6 caracteres.");
        if (usuario.getRendaMensal() < 0)
            throw new IllegalArgumentException("Renda não pode ser menor que zero.");
        if (dao.loginJaExiste(usuario.getUsuario()))
            throw new IllegalArgumentException("Este login já está em uso.");
        if (!dao.inserir(usuario))
            throw new RuntimeException("Erro ao salvar usuário no banco.");
    }

    /** Autentica e retorna o Usuario ou lança exceção. */
    public Usuario autenticar(String login, String senha) {
        if (login == null || senha == null || login.isEmpty() || senha.isEmpty())
            throw new IllegalArgumentException("Preencha os campos usuário e senha.");
        Usuario usuario = dao.buscarPorLoginESenha(login, senha);
        if (usuario == null) throw new SecurityException("Usuário ou senha inválidos.");
        return usuario;
    }

    public void atualizarRenda(String login, double novaRenda) {
        if (novaRenda < 0) throw new IllegalArgumentException("Renda inválida: Informe um valor acima de zero.");
        if (!dao.atualizarRenda(login, novaRenda))
            throw new RuntimeException("Usuário não encontrado.");
    }

    public Usuario buscarPorLogin(String login) {
        return dao.buscarPorLogin(login);
    }
}
