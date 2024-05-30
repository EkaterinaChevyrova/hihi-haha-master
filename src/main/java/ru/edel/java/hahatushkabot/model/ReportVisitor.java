package ru.edel.java.hahatushkabot.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode
@AllArgsConstructor //Генерируем конструктор с параметрами
@NoArgsConstructor //Генерируем конструктор без параметров
@Getter //Генерируем геттеры
@Setter //Генерируем сеттеры
@Entity //Объявляем класс как сущность для работы с ним в БД и его имя
@Table(name = "visitors")

public class ReportVisitor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "your_sequence_generator")
    @SequenceGenerator(name="your_sequence_generator", sequenceName="your_sequence_name", allocationSize=1)
    private Long id;

    @Column(name = "visitor_id")
    private Long visitorId;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "joke_id", referencedColumnName = "id")
    private JokesModel joke;

    public void setJoke(JokesModel joke) {
        this.joke = joke;
    }
}
