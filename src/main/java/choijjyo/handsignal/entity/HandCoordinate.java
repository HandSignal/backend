package choijjyo.handsignal.entity;

import choijjyo.handsignal.converter.JpaConverterJson;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
public class HandCoordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Convert(converter = JpaConverterJson.class)
    private List<List<Map<String, Object>>> poseKeypoints;

    @Lob
    @Convert(converter = JpaConverterJson.class)
    private List<List<Map<String, Object>>> leftHandKeypoints;

    @Lob
    @Convert(converter = JpaConverterJson.class)
    private List<List<Map<String, Object>>> rightHandKeypoints;

    private Date timestamp = new Date();

}
