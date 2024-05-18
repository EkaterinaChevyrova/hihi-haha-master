package ru.edel.java.hahatushkabot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.List;
import javax.persistence.*;
import java.time.LocalDateTime;


@AllArgsConstructor //Генерируем конструктор с параметрами
@NoArgsConstructor //Генерируем конструктор без параметров
@Getter //Генерируем геттеры
@Setter //Генерируем сеттеры
@ToString //Отдельный метод для toString
@Entity //Объявляем класс как сущность для работы с ним в БД и его имя
@Table(name = "jokes") //Помечаем, как называется таблица в БД

public class JokesModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "your_sequence_generator")
    @SequenceGenerator(name="your_sequence_generator", sequenceName="your_sequence_name", allocationSize=1)
    private Long id;

    @Column(name = "jok")
    private String jok;

    @Column(name = "creation_date")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "joke", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportVisitor> reportVisitors;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }

}

