package dania.app.web.repository;

import dania.app.web.entities.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

    CalendarEntity findCalendarEntityById(String id);

    Optional<CalendarEntity> findFirstByEopGreaterThanEqual(LocalDate endOfPeriod);

}

