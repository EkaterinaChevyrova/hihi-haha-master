package ru.edel.java.hahatushkabot.server;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.edel.java.hahatushkabot.model.JokesModel;
import ru.edel.java.hahatushkabot.model.ReportVisitor;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TelegramBotService {
    private final TelegramBot telegramBot;
    private final JokesService jokesService;

    public TelegramBotService(@Autowired TelegramBot telegramBot, JokesService jokesService) {
        this.telegramBot = telegramBot;
        this.jokesService = jokesService;
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }

    private void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String messageText = update.message().text().trim();
            if (messageText.equals("/start")) {
                sendStartMessage(update.message().chat().id());
            } else if (messageText.equals("/help")) {
                sendHelpMessage(update.message().chat().id());
            } else if (messageText.equals("Топ 5 популярных шуток")) { // Обработка нажатия на новую кнопку
                sendTopJokesMessage(update.message().chat().id());
            } else {
                // Передаем аргументы для получения всех шуток
                getterJokes(update, 0, Integer.MAX_VALUE);
                // Опционально, передаем аргументы для получения случайной шутки
                getterJoke(update);
            }
        }
    }

    private void sendStartMessage(Long chatId) {
        String startMessage = "Бот готов принять команду!";
        SendMessage request = new SendMessage(chatId, startMessage)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton("Хочу хахатушку"),
                        new KeyboardButton("Все хахатушки"),
                        new KeyboardButton("Топ 5 популярных шуток")
                }
        );

        // Устанавливаем параметры для клавиатуры
        keyboardMarkup.resizeKeyboard(true); // Масштабирует клавиатуру по размеру экрана

        // Присваиваем клавиатуру к запросу
        request.replyMarkup(keyboardMarkup);

        this.telegramBot.execute(request);
    }


    private void sendHelpMessage(Long chatId) {
        String helpMessage = "Доступные команды:\n" +
                "/start - Начать использование бота\n" +
                "/help - Список доступных команд\n" +
                "Хочу хахатушку - Получить случайную хахатушку\n" +
                "Все хахатушки - Получить список всех хахатушек";
        SendMessage request = new SendMessage(chatId, helpMessage)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
        this.telegramBot.execute(request);
    }


    private void getterJoke(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String messageText = update.message().text().trim();
            if (messageText.equalsIgnoreCase("Хочу хахатушку")) {
                Long chatId = update.message().chat().id();
                // Получаем случайную шутку
                JokesModel randomJoke = jokesService.getRandomJoke();
                // Формируем сообщение с шуткой и датой
                String jokeMessage = "Пользователь " + chatId + " захотел хахатушку!\n\n"
                        +"Вот, пожалуйста, хихикайте:\n"
                        + randomJoke.getJok() + "\n\n"
                        + "Отправлено " + new Date();
                SendMessage request = new SendMessage(chatId, jokeMessage)
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true)
                        .replyToMessageId(update.message().messageId());
                this.telegramBot.execute(request);
                // Сохраняем информацию о пользователе и его действии в базе данных
                saveUserAction(chatId, "Хочу хахатушку", randomJoke);
            }
        }
    }

    void getterJokes(Update update, int page, int size) {
        if (update.message() != null && update.message().text() != null) {
            String messageText = update.message().text().trim();
            if (messageText.equalsIgnoreCase("Все хахатушки")) {
                Long chatId = update.message().chat().id();
                // Получаем все шутки на запрошенной странице
                Page<JokesModel> jokesPage = jokesService.getJokes(page, size);
                // Формируем ответ
                StringBuilder responseText = new StringBuilder();
                int counter = page * size + 1;
                for (JokesModel joke : jokesPage.getContent()) {
                    responseText.append(counter).append(". ").append(joke.getJok()).append("\n");
                    counter++;
                }

                SendMessage request = new SendMessage(chatId, responseText.toString() + "\nОтправлено " + new Date())
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true)
                        .replyToMessageId(update.message().messageId());
                this.telegramBot.execute(request);
                // Сохраняем информацию о пользователе и его действии в базе данных
                saveUserAction(chatId, "Все хахатушки", null);
            }
        }
    }


    private void sendTopJokesMessage(Long chatId) {
        // Получаем топ-5 популярных шуток
        Map<JokesModel, Long> topJokes = jokesService.countJokesUsage().entrySet().stream()
                .sorted(Map.Entry.<JokesModel, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        StringBuilder messageBuilder = new StringBuilder("Топ 5 популярных шуток:\n");
        int rank = 1;
        for (Map.Entry<JokesModel, Long> entry : topJokes.entrySet()) {
            messageBuilder.append(rank).append(". ").append(entry.getKey().getJok()).append(" - ").append(entry.getValue()).append(" раз\n");
            rank++;
        }

        SendMessage request = new SendMessage(chatId, messageBuilder.toString())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
        this.telegramBot.execute(request);
    }


    private void saveUserAction(Long chatId, String action, JokesModel joke) {
        ReportVisitor visitor = new ReportVisitor();
        visitor.setVisitorId(chatId);
        visitor.setDate(new Date());
        visitor.setJoke(joke);
    }
}
