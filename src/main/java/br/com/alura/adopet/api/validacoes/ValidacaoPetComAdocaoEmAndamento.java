package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exceptions.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPetComAdocaoEmAndamento implements ValidacaoSolicitacaoAdocao{

  private final AdocaoRepository adocaoRepository;
  private final PetRepository petRepository;

  @Autowired
  public ValidacaoPetComAdocaoEmAndamento(
      AdocaoRepository adocaoRepository, PetRepository petRepository) {
    this.adocaoRepository = adocaoRepository;
    this.petRepository = petRepository;
  }

  public void validar(SolicitacaoAdocaoDto dto) {
    Pet pet = petRepository.getReferenceById(dto.idPet());
    List<Adocao> allValidacoes = adocaoRepository.findAll();

    for (Adocao validacao : allValidacoes) {
      if (validacao.getPet() == pet && validacao.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
        throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
      }
    }
  }
}
