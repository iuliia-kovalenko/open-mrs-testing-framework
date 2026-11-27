package models;

import generators.GeneratingRule;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VitalsData {
    @GeneratingRule(regex = "[6-8][0-9]")
    private double weight;

    @GeneratingRule(regex = "1[6-8][0-9]")
    private double height;

    @GeneratingRule(regex = "36\\.[1-9]")
    private double temperature;

    @GeneratingRule(regex = "1[12][0-9]")
    private double systolic;

    @GeneratingRule(regex = "[78][0-9]")
    private double diastolic;
}
