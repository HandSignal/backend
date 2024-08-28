package choijjyo.handsignal.service;

import choijjyo.handsignal.entity.HandCoordinate;
import choijjyo.handsignal.repository.HandCoordinateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandCoordinateService {
    @Autowired
    private HandCoordinateRepository repository;

    public HandCoordinate save(HandCoordinate handCoordinate) {
        return repository.save(handCoordinate);
    }

    public List<HandCoordinate> getAll() {
        return repository.findAll();
    }
}
