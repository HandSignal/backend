package choijjyo.handsignal.entity;

import java.util.List;
import java.util.Map;

public class HandCoordinateDTO {
    private List<List<Map<String, Object>>> poseKeypoints;
    private List<List<Map<String, Object>>> leftHandKeypoints;
    private List<List<Map<String, Object>>> rightHandKeypoints;

    public List<List<Map<String, Object>>> getPoseKeypoints() {
        return poseKeypoints;
    }

    public void setPoseKeypoints(List<List<Map<String, Object>>> poseKeypoints) {
        this.poseKeypoints = poseKeypoints;
    }

    public List<List<Map<String, Object>>> getLeftHandKeypoints() {
        return leftHandKeypoints;
    }

    public void setLeftHandKeypoints(List<List<Map<String, Object>>> leftHandKeypoints) {
        this.leftHandKeypoints = leftHandKeypoints;
    }

    public List<List<Map<String, Object>>> getRightHandKeypoints() {
        return rightHandKeypoints;
    }

    public void setRightHandKeypoints(List<List<Map<String, Object>>> rightHandKeypoints) {
        this.rightHandKeypoints = rightHandKeypoints;
    }
}

