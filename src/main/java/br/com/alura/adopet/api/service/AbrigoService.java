package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    private final AbrigoRepository abrigoRepository;

    @Autowired
    public AbrigoService(AbrigoRepository abrigoRepository) {
        this.abrigoRepository = abrigoRepository;
    }

    public List<Abrigo> listAllAbrigos() {
        return abrigoRepository.findAll();
    }

    public void cadastrar(Abrigo abrigo) {
        abrigoRepository.save(abrigo);
    }

    public boolean verificaSePodeCadastrar(Abrigo abrigo) {
        boolean nomeJaCadastrado = abrigoRepository.existsByNome(abrigo.getNome());
        boolean telefoneJaCadastrado = abrigoRepository.existsByTelefone(abrigo.getTelefone());
        boolean emailJaCadastrado = abrigoRepository.existsByEmail(abrigo.getEmail());

        return !nomeJaCadastrado && !telefoneJaCadastrado && !emailJaCadastrado;
    }

    public List<Pet> listarPets(String idOuNome) {
        Long id = Long.parseLong(idOuNome);
        return abrigoRepository.getReferenceById(id).getPets();
    }

    public List<Pet> listarPetsByNome(String nome) {
        return abrigoRepository.findByNome(nome).getPets();
    }

    public void cadastrarPetNoAbrigo(String idOuNome, Pet pet) {

        Abrigo abrigo;

        if (isNumeric(idOuNome)) {
            Long id = Long.parseLong(idOuNome);
            abrigo = abrigoRepository.getReferenceById(id);
        } else {
            abrigo = abrigoRepository.findByNome(idOuNome);
        }
        pet.setAbrigo(abrigo);
        pet.setAdotado(false);
        abrigo.getPets().add(pet);
        abrigoRepository.save(abrigo);
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
