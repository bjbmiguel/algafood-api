package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastrarUsuarioService {

    public static final String MSG_USUARIO_EM_USO = "Usuário de código %d não pode ser removido, pós está em uso";
    @Autowired //Injetamos o componente  EstadoRepository...
    UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscar(Long usuarioId) {

        try {
            return usuarioRepository.findById(usuarioId);

        } catch (EmptyResultDataAccessException e) {
            throw new UsuarioNaoEncontradoException(usuarioId);
        }
    }


    @Transactional
    public Usuario salvar(Usuario usuario) { //Vamos usar para salvar e actualizar

        usuarioRepository.detach(usuario); //desanexamos a entidade user do contexto de persistência...

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
            throw new NegocioException(
                    String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long usuario) {

        try {

            usuarioRepository.deleteById(usuario);
            //Para evitar a falha na captura da exceção DataIntegrityViolationException
            usuarioRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            //Not Found
            throw new UsuarioNaoEncontradoException(usuario);
        } catch (DataIntegrityViolationException e) {
            // Uma cozinha está em uso por um restaurante....
            throw new EntidadeEmUsoException(String.format(MSG_USUARIO_EM_USO, usuario));
        }
    }


    //Retorna um objecto do tipo cozinha na resposta, caso contrário uma resposta com código 404 será lançada...
    public Usuario findById(Long usuarioId) {

        return usuarioRepository.findById(usuarioId).orElseThrow(
                () -> new UsuarioNaoEncontradoException(
                        usuarioId));
    }

    public void checkIfExistsById(Long usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {

            throw new UsuarioNaoEncontradoException(usuarioId);

        }
    }

    @Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = findById(usuarioId);

        if(!passwordEncoder.matches(senhaAtual, usuario.getSenha())){
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }
        /*
        if (usuario.senhaNaoCoincideCom(senhaAtual)) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }

         */

        usuario.setSenha(passwordEncoder.encode(novaSenha));

    }

    @Transactional
    public void adicionarGrupo(Usuario usuario, Grupo grupo) {
        usuario.adicionarGrupo(grupo);
    }

    @Transactional
    public void removerGrupo(Usuario usuario, Grupo grupo) {
        usuario.removerGrupo(grupo);
    }
}
