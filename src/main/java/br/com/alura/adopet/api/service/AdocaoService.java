package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdocaoService {

  private final AdocaoRepository adocaoRepository;
  private final PetRepository petRepository;
  private final TutorRepository tutorRepository;
  private final EmailService emailService;
  private final List<ValidacaoSolicitacaoAdocao> validacoes;

  @Autowired
  public AdocaoService(
      AdocaoRepository adocaoRepository,
      EmailService emailService,
      PetRepository petRepository,
      TutorRepository tutorRepository,
      List<ValidacaoSolicitacaoAdocao> validacoes) {
    this.adocaoRepository = adocaoRepository;
    this.emailService = emailService;
    this.tutorRepository = tutorRepository;
    this.petRepository = petRepository;
    this.validacoes = validacoes;
  }

  public void solicitarAdocao(SolicitacaoAdocaoDto dto) {
    Pet pet = petRepository.getReferenceById(dto.idPet());
    Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

    validacoes.forEach(v -> v.validar(dto));

    Adocao adocao = new Adocao();
    adocao.setData(LocalDateTime.now());
    adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
    adocao.setPet(pet);
    adocao.setTutor(tutor);
    adocao.setMotivo(dto.motivo());
    adocaoRepository.save(adocao);

    emailService.dispararEmail(
        adocao.getPet().getAbrigo().getEmail(),
        "Solicitação de adoção",
        "Olá "
            + adocao.getPet().getAbrigo().getNome()
            + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: "
            + adocao.getPet().getNome()
            + ". \nFavor avaliar para aprovação ou reprovação.");
  }

  public void aprovarAdocao(AprovacaoAdocaoDto dto) {
    Adocao adocao = adocaoRepository.getReferenceById(dto.idAdocao());
    adocao.setStatus(StatusAdocao.APROVADO);
    emailService.dispararEmail(
        adocao.getTutor().getEmail(),
        "Adoção aprovada",
        "Parabéns "
            + adocao.getTutor().getNome()
            + "!\n\nSua adoção do pet "
            + adocao.getPet().getNome()
            + ", solicitada em "
            + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            + ", foi aprovada.\nFavor entrar em contato com o abrigo "
            + adocao.getPet().getAbrigo().getNome()
            + " para agendar a busca do seu pet.");
  }

  public void reprovarAdocao(ReprovacaoAdocaoDto dto) {
    Adocao adocao = adocaoRepository.getReferenceById(dto.idAdocao());
    adocao.setStatus(StatusAdocao.REPROVADO);
    adocao.setJustificativaStatus(dto.justificativa());
    emailService.dispararEmail(
        adocao.getTutor().getEmail(),
        "Adoção reprovada",
        "Olá "
            + adocao.getTutor().getNome()
            + "!\n\nInfelizmente sua adoção do pet "
            + adocao.getPet().getNome()
            + ", solicitada em "
            + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            + ", foi reprovada pelo abrigo "
            + adocao.getPet().getAbrigo().getNome()
            + " com a seguinte justificativa: "
            + adocao.getJustificativaStatus());
  }
}
