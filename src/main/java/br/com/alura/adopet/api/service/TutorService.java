package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public void cadastrar(Tutor tutor) {
        tutorRepository.save(tutor);
    }

    public boolean podeCadastrar(Tutor tutor) {

        boolean telefoneJaCadastrado = tutorRepository.existsByTelefone(tutor.getTelefone());
        boolean emailJaCadastrado = tutorRepository.existsByEmail(tutor.getEmail());

        return !telefoneJaCadastrado && !emailJaCadastrado;
    }

    public void atualizar(Tutor tutor) {
        Tutor tutorBanco;
        if (tutorRepository.findById(tutor.getId()).isPresent()) {
            tutorBanco = tutorRepository.findById(tutor.getId()).get();
            tutorBanco.setEmail(tutor.getEmail());
            tutorBanco.setNome(tutor.getNome());
            tutorBanco.setTelefone(tutorBanco.getTelefone());
            tutorBanco.setAdocoes(tutor.getAdocoes());
        } else {
            throw new EntityNotFoundException("Tutor não encontrado");
        }
    }
}
