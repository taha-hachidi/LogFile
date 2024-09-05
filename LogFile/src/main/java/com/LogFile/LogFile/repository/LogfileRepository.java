package com.LogFile.LogFile.repository;

import com.LogFile.LogFile.model.Logfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogfileRepository extends JpaRepository<Logfile,Long> {
    List<Logfile> findBySeverity(String severity);
    List<Logfile> findBySeverityOrderByDateDescTimeDesc(String severity);

    boolean existsByTime(String time);
}
