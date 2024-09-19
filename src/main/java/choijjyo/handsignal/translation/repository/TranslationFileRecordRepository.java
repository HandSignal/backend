package choijjyo.handsignal.translation.repository;

import choijjyo.handsignal.translation.entity.TranslationFileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationFileRecordRepository extends JpaRepository<TranslationFileRecord, Long> {
}
