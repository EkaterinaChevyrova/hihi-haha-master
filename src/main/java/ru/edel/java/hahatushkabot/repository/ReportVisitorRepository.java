package ru.edel.java.hahatushkabot.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.edel.java.hahatushkabot.model.ReportVisitor;

import java.util.List;

@Repository
public interface ReportVisitorRepository extends PagingAndSortingRepository<ReportVisitor, Long> {
    List<ReportVisitor> findAll();
}
