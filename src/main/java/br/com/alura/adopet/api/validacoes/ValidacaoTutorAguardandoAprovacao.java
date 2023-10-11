package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exceptions.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoTutorAguardandoAprovacao implements ValidacaoSolicitacaoAdocao {

  private final AdocaoRepository adocaoRepository;
  private final TutorRepository tutorRepository;

  @Autowired
  public ValidacaoTutorAguardandoAprovacao(
      AdocaoRepository adocaoRepository, TutorRepository tutorRepository) {
    this.adocaoRepository = adocaoRepository;
    this.tutorRepository = tutorRepository;
  }

  public void validar(SolicitacaoAdocaoDto dto) {
    Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());
    List<Adocao> allValidacoes = adocaoRepository.findAll();
    for (Adocao validacao : allValidacoes) {
      if (validacao.getTutor() == tutor
          && validacao.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
        throw new ValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
      }
    }
  }
}
