package choijjyo.handsignal.controller;

import choijjyo.handsignal.entity.HandCoordinate;
import choijjyo.handsignal.service.HandCoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hand-coordinates")
public class HandCoordinateController {
    @Autowired
    private HandCoordinateService service;

    @PostMapping
    public HandCoordinate saveHandCoordinate(@RequestBody HandCoordinate handCoordinate) {
        return service.save(handCoordinate);
    }

    @GetMapping
    public List<HandCoordinate> getAllHandCoordinates() {
        return service.getAll();
    }
}
