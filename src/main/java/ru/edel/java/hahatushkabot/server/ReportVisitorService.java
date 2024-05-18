package ru.edel.java.hahatushkabot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.edel.java.hahatushkabot.model.ReportVisitor;
import ru.edel.java.hahatushkabot.repository.ReportVisitorRepository;

@Service
public class ReportVisitorService {

    @Autowired
    private ReportVisitorRepository reportVisitorRepository;

    public ReportVisitor addVisitor(ReportVisitor reportVisitor) {
        return reportVisitorRepository.save(reportVisitor);
    }
}

