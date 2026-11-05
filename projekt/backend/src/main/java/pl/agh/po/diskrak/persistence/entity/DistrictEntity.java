package pl.agh.po.diskrak.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "districts")
public class DistrictEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "population_weight", nullable = false)
    private double populationWeight;

    @Column(name = "base_satisfaction", nullable = false)
    private double baseSatisfaction;

    @Column(name = "punctuality_weight", nullable = false)
    private double punctualityWeight;

    @Column(name = "eco_fleet_weight", nullable = false)
    private double ecoFleetWeight;

    @Column(name = "overcrowding_weight", nullable = false)
    private double overcrowdingWeight;

    @Column(name = "replacement_speed_weight", nullable = false)
    private double replacementSpeedWeight;

    protected DistrictEntity() {
    }

    public DistrictEntity(UUID id, String name, double populationWeight, double baseSatisfaction,
                          double punctualityWeight, double ecoFleetWeight, double overcrowdingWeight,
                          double replacementSpeedWeight) {
        this.id = id;
        this.name = name;
        this.populationWeight = populationWeight;
        this.baseSatisfaction = baseSatisfaction;
        this.punctualityWeight = punctualityWeight;
        this.ecoFleetWeight = ecoFleetWeight;
        this.overcrowdingWeight = overcrowdingWeight;
        this.replacementSpeedWeight = replacementSpeedWeight;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPopulationWeight() {
        return populationWeight;
    }

    public double getBaseSatisfaction() {
        return baseSatisfaction;
    }

    public double getPunctualityWeight() {
        return punctualityWeight;
    }

    public double getEcoFleetWeight() {
        return ecoFleetWeight;
    }

    public double getOvercrowdingWeight() {
        return overcrowdingWeight;
    }

    public double getReplacementSpeedWeight() {
        return replacementSpeedWeight;
    }
}
