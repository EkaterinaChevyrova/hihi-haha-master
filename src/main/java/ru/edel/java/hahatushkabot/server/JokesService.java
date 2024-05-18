package ru.edel.java.hahatushkabot.server;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.edel.java.hahatushkabot.model.JokesModel;
import ru.edel.java.hahatushkabot.model.ReportVisitor;
import ru.edel.java.hahatushkabot.repository.JokesRepository;
import ru.edel.java.hahatushkabot.repository.ReportVisitorRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class JokesService implements JokesInter {

    private final JokesRepository repository;
    private final ReportVisitorRepository visitorRepository;

    @Override
    public void addJok(JokesModel jok) {
        repository.save(jok);
    }

    @Override
    public Page<JokesModel> getJokes(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Optional<JokesModel> getJokesId(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean updateJoke(Long id, JokesModel updatedJoke) {
        Optional<JokesModel> existingJoke = repository.findById(id);
        if (existingJoke.isPresent()) {
            JokesModel jokeToUpdate = existingJoke.get();
            jokeToUpdate.setJok(updatedJoke.getJok());
            jokeToUpdate.setUpdateDate(LocalDateTime.now());
            repository.save(jokeToUpdate);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteJoke(Long id) {
        Optional<JokesModel> existingJoke = repository.findById(id);
        if (existingJoke.isPresent()) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


    public JokesModel getRandomJoke() {
        Page<JokesModel> jokesPage = getJokes(0, Integer.MAX_VALUE); // Получить все шутки
        List<JokesModel> jokes = jokesPage.getContent();
        Random random = new Random();
        return jokes.get(random.nextInt(jokes.size()));
    }

     @Override
    public void saveUserAction(Long visitorId, String action, JokesModel joke) {

        ReportVisitor visitor = new ReportVisitor();
        visitor.setVisitorId(visitorId);
        visitor.setDate(new Date());

        visitor.setJoke(joke);

        visitorRepository.save(visitor);
    }



    // Метод для подсчета количества раз, которое каждая шутка была отправлена
    public Map<JokesModel, Long> countJokesUsage() {
        List<ReportVisitor> allVisitors = visitorRepository.findAll();
        return allVisitors.stream()
                .filter(visitor -> visitor.getJoke() != null)
                .collect(Collectors.groupingBy(ReportVisitor::getJoke, Collectors.counting()));
    }
}
