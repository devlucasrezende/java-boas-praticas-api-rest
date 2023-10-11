package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exceptions.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.service.AdocaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

  private final AdocaoService adocaoService;

  public AdocaoController(AdocaoService adocaoService) {
    this.adocaoService = adocaoService;
  }

  @PostMapping
  @Transactional
  public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitacaoAdocaoDto dto) {
    try {
      adocaoService.solicitarAdocao(dto);
      return ResponseEntity.ok("Adoção solicitada com sucesso");
    } catch (ValidacaoException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/aprovar")
  @Transactional
  public ResponseEntity<String> aprovar(@RequestBody @Valid AprovacaoAdocaoDto dto) {
    adocaoService.aprovarAdocao(dto);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/reprovar")
  @Transactional
  public ResponseEntity<String> reprovar(@RequestBody @Valid ReprovacaoAdocaoDto dto) {
    adocaoService.reprovarAdocao(dto);

    return ResponseEntity.ok().build();
  }
}
