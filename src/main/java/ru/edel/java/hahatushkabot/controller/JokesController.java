package ru.edel.java.hahatushkabot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.edel.java.hahatushkabot.model.JokesModel;
import ru.edel.java.hahatushkabot.server.JokesService;


@RestController
@RequestMapping("/jokes")
@RequiredArgsConstructor
public class JokesController {

    private final JokesService service;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<String> addJokes(@RequestBody JokesModel jok){
        if(jok.getJok() == null){
            return ResponseEntity.badRequest().body("Шутка в том, что шутки нет :(");
        }
        service.addJok(jok);
        return ResponseEntity.ok("Уважаемый, хахатушка добавлена!");
    }

    @GetMapping
    ResponseEntity<Page<JokesModel>> getJokes(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getJokes(page, size));
    }

    @GetMapping("/{id}")
    ResponseEntity<JokesModel> getJokesId(@PathVariable Long id) {
        return service.getJokesId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    ResponseEntity<String> updateJoke(@PathVariable Long id, @RequestBody JokesModel updatedJoke) {
        if (service.updateJoke(id, updatedJoke)) {
            return ResponseEntity.ok("Уважаемые, хахатушка обновилась!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    ResponseEntity<String> deleteJoke(@PathVariable Long id) {
        if (service.deleteJoke(id)) {
            return ResponseEntity.ok("Мы не хотели этого, но хахатушку удалили :(");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
