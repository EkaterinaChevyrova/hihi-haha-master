
package ru.edel.java.hahatushkabot.server;

import org.springframework.data.domain.Page;
import ru.edel.java.hahatushkabot.model.JokesModel;
import ru.edel.java.hahatushkabot.model.ReportVisitor;

import java.util.Optional;

public interface JokesInter {

    void addJok(JokesModel jok);

    Page<JokesModel> getJokes(int page, int size);
    Optional<JokesModel> getJokesId(Long id);

    boolean updateJoke(Long id, JokesModel updatedJoke);
    boolean deleteJoke(Long id);
    void saveUserAction(Long visitorId, String action, JokesModel joke) ;
}


