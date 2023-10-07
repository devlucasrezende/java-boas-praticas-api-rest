package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.service.AbrigoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {


    private final AbrigoService abrigoService;

    public AbrigoController(AbrigoService abrigoService) {
        this.abrigoService = abrigoService;
    }

    @GetMapping
    public ResponseEntity<List<Abrigo>> listar() {
        return ResponseEntity.ok(abrigoService.listAllAbrigos());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Abrigo abrigo) {
        if (!abrigoService.verificaSePodeCadastrar(abrigo)) {
            return ResponseEntity.badRequest().body("Dados já cadastrados para outro abrigo!");
        } else {
            abrigoService.cadastrar(abrigo);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{idOuNome}/pets")
    public ResponseEntity<List<Pet>> listarPetsPorAbrigo(@PathVariable String idOuNome) {
        try {
            List<Pet> pets = abrigoService.listarPets(idOuNome);
            return ResponseEntity.ok(pets);
        } catch (EntityNotFoundException enfe) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            try {
                List<Pet> pets = abrigoService.listarPetsByNome(idOuNome);
                return ResponseEntity.ok(pets);
            } catch (EntityNotFoundException enfe) {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PostMapping("/{idOuNome}/pets")
    @Transactional
    public ResponseEntity<String> cadastrarPetNoAbrigo(@PathVariable String idOuNome, @RequestBody @Valid Pet pet) {
        abrigoService.cadastrarPetNoAbrigo(idOuNome, pet);
        return ResponseEntity.ok().build();
    }

}
