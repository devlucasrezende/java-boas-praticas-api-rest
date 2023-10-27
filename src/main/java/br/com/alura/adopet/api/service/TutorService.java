package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.TutorDto;
import br.com.alura.adopet.api.exceptions.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<TutorDto> listar() {
        return tutorRepository.findAll()
                .stream()
                .map(TutorDto::new)
                .toList();
    }

    public void cadastrar(TutorDto dto) {
        boolean jaCadastrado = tutorRepository.existsByTelefoneOrEmail(dto.telefone(), dto.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro tutor!");
        }
        Tutor tutor = new Tutor(dto);
        tutorRepository.save(tutor);
    }

    public void atualizar(TutorDto dto) {
        if (dto.idTutor() == null) {
            throw new ValidacaoException("O id do tutor é necessário para a atualizaçao");
        }
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());
        tutor.atualizarTutor(dto);
    }
}
