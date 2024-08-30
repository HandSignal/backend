package choijjyo.handsignal.repository;

import choijjyo.handsignal.entity.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntryRepository extends JpaRepository<UserEntry, Long> {
}
