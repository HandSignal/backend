package choijjyo.handsignal.entity;

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
    private String poseKeypoints;

    @Lob
    private String leftHandKeypoints;

    @Lob
    private String rightHandKeypoints;

    private Date timestamp = new Date();

}
