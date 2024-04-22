package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastrarUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/usuarios")
@RestController
// Definimos a classe Estado como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
public class UsuarioController {

    @Autowired // Injetamos a dependência
    private CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private UsuarioInputDisassembler usuarioInputDisassembler;


    @GetMapping // Mapeamos as requ HTTP do tipo GET para este método
    public List<UsuarioModel> listar() {

        List<Usuario> todosUsuarios = cadastrarUsuarioService.listar();

        return usuarioModelAssembler.toCollectionModel(todosUsuarios);
    }

    @GetMapping("/{usuarioId}")
    public UsuarioModel buscar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastrarUsuarioService.hasOrNot(usuarioId);

        return usuarioModelAssembler.toModel(usuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioComSenhaInput) {
        Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput);

        usuario = cadastrarUsuarioService.salvar(usuario);

        return usuarioModelAssembler.toModel(usuario);
    }

    @PutMapping("/{usuarioId}")
    public UsuarioModel atualizar(@PathVariable Long usuarioId,
                                 @RequestBody @Valid UsuarioInput usuarioInput) {

        Usuario usuarioAtual = cadastrarUsuarioService.hasOrNot(usuarioId);

        usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);

        usuarioAtual = cadastrarUsuarioService.salvar(usuarioAtual);

        return usuarioModelAssembler.toModel(usuarioAtual);
    }

    @DeleteMapping(value = "/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long usuarioId) {

        cadastrarUsuarioService.excluir(usuarioId);

    }

    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senhaInput) {

        cadastrarUsuarioService.alterarSenha(usuarioId, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
    }

}
