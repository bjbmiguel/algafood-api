package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastrarUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/v1/usuarios")
@RestController
// Definimos a classe Estado como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
public class UsuarioController implements UsuarioControllerOpenApi {

    @Autowired // Injetamos a dependência
    private CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private UsuarioInputDisassembler usuarioInputDisassembler;



    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping // Mapeamos as requ HTTP do tipo GET para este método
    public CollectionModel<UsuarioModel> listar() {

        List<Usuario> todosUsuarios = cadastrarUsuarioService.listar();

        return usuarioModelAssembler.toCollectionModel(todosUsuarios);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping("/{usuarioId}")
    public UsuarioModel buscar(@PathVariable Long usuarioId) {
        var usuario = cadastrarUsuarioService.findById(usuarioId);
        return usuarioModelAssembler.toModel(usuario);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioComSenhaInput) {
        Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput);

        usuario = cadastrarUsuarioService.salvar(usuario);

        return usuarioModelAssembler.toModel(usuario);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{usuarioId}")
    public UsuarioModel atualizar(@PathVariable Long usuarioId,
                                  @RequestBody @Valid UsuarioInput usuarioInput) {

        Usuario usuarioAtual = cadastrarUsuarioService.findById(usuarioId);

        usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);

        usuarioAtual = cadastrarUsuarioService.salvar(usuarioAtual);

        return usuarioModelAssembler.toModel(usuarioAtual);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping(value = "/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long usuarioId) {

        cadastrarUsuarioService.excluir(usuarioId);

    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senhaInput) {
        cadastrarUsuarioService.alterarSenha(usuarioId, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
        return ResponseEntity.noContent().build();
    }

}
