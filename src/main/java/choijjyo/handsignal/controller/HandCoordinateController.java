package choijjyo.handsignal.controller;

import choijjyo.handsignal.entity.HandCoordinate;
import choijjyo.handsignal.entity.HandCoordinateDTO;
import choijjyo.handsignal.service.HandCoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/translate")
public class HandCoordinateController {
    @Autowired
    private HandCoordinateService service;

    @PostMapping("/hand-coordinates")
    public HandCoordinate saveHandCoordinate(@RequestBody HandCoordinateDTO dto) {
        HandCoordinate handCoordinate = new HandCoordinate();
        handCoordinate.setPoseKeypoints(dto.getPoseKeypoints());
        handCoordinate.setLeftHandKeypoints(dto.getLeftHandKeypoints());
        handCoordinate.setRightHandKeypoints(dto.getRightHandKeypoints());
        return service.save(handCoordinate);
    }

    @GetMapping("/hand-coordinates")
    public List<HandCoordinate> getAllHandCoordinates() {
        return service.getAll();
    }
}
