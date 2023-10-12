package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exceptions.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPetComAdocaoEmAndamento implements ValidacaoSolicitacaoAdocao {

    private final AdocaoRepository adocaoRepository;


    @Autowired
    public ValidacaoPetComAdocaoEmAndamento(AdocaoRepository adocaoRepository) {
        this.adocaoRepository = adocaoRepository;
    }

    public void validar(SolicitacaoAdocaoDto dto) {
        boolean petAguardandoAvaliacao = adocaoRepository.existsByPetIdAndStatus(dto.idPet(), StatusAdocao.AGUARDANDO_AVALIACAO);
        if (petAguardandoAvaliacao) {
            throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
        }
    }
}
