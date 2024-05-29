package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.model.UsuarioGrupo;
import com.algaworks.algafood.domain.service.CadastrarGrupoService;
import com.algaworks.algafood.domain.service.CadastrarUsuarioGrupoService;
import com.algaworks.algafood.domain.service.CadastrarUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/usuarios/{usuarioId}/grupos")
@RestController
public class UsuarioGrupoController {

    @Autowired
    CadastrarGrupoService cadastrarGrupoService;

    @Autowired
    CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    GrupoModelAssembler grupoModelAssembler;

    @Autowired
    GrupoInputDisassembler grupoInputDisassembler;

    @Autowired
    CadastrarUsuarioGrupoService cadastrarUsuarioGrupoService;

    @GetMapping
    public List<GrupoModel> listar(@PathVariable Long usuarioId) {

        Usuario usuario = cadastrarUsuarioService.findById(usuarioId);

        return grupoModelAssembler.toCollectionModel(usuario.getGrupos());
    }


    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId){

        Usuario usuario = cadastrarUsuarioService.findById(usuarioId);
        Grupo grupo = cadastrarGrupoService.findById(grupoId);

        cadastrarUsuarioService.adicionarGrupo(usuario, grupo);

    }

    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId){

        cadastrarUsuarioService.checkIfExistsById(usuarioId);
        UsuarioGrupo usuarioGrupo = cadastrarUsuarioGrupoService.findById(usuarioId, grupoId);
        cadastrarUsuarioService.removerGrupo(usuarioGrupo.getUsuario(), usuarioGrupo.getGrupo());
    }
}
