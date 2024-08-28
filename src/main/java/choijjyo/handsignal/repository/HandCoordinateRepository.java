package choijjyo.handsignal.repository;

import choijjyo.handsignal.entity.HandCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandCoordinateRepository extends JpaRepository<HandCoordinate, Long> {
}
