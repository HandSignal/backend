package choijjyo.handsignal.videoCall.repository;

import choijjyo.handsignal.videoCall.entity.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntryRepository extends JpaRepository<UserEntry, Long> {
}
