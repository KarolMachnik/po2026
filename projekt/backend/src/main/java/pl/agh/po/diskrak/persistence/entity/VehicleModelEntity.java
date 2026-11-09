package pl.agh.po.diskrak.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicle_models")
public class VehicleModelEntity {

    @Id
    @Column(name = "code", nullable = false, length = 40)
    private String code;

    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName;

    @Column(name = "vehicle_category", nullable = false, length = 30)
    private String vehicleCategory;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "purchase_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "cost_per_km", nullable = false, precision = 8, scale = 2)
    private BigDecimal costPerKm;

    @Column(name = "eco_score", nullable = false)
    private double ecoScore;

    @Column(name = "has_autonomous_battery", nullable = false)
    private boolean autonomousBattery;

    @Column(name = "battery_capacity_kwh", nullable = false)
    private double batteryCapacityKwh;

    @Column(name = "energy_consumption_per_km", nullable = false)
    private double energyConsumptionPerKm;

    @Column(name = "fuel_tank_capacity_liters", nullable = false)
    private double fuelTankCapacityLiters;

    protected VehicleModelEntity() {
    }

    public VehicleModelEntity(String code, String modelName, String vehicleCategory, int maxCapacity,
                              BigDecimal purchasePrice, BigDecimal costPerKm, double ecoScore,
                              boolean autonomousBattery, double batteryCapacityKwh, double energyConsumptionPerKm,
                              double fuelTankCapacityLiters) {
        this.code = code;
        this.modelName = modelName;
        this.vehicleCategory = vehicleCategory;
        this.maxCapacity = maxCapacity;
        this.purchasePrice = purchasePrice;
        this.costPerKm = costPerKm;
        this.ecoScore = ecoScore;
        this.autonomousBattery = autonomousBattery;
        this.batteryCapacityKwh = batteryCapacityKwh;
        this.energyConsumptionPerKm = energyConsumptionPerKm;
        this.fuelTankCapacityLiters = fuelTankCapacityLiters;
    }

    public String getCode() {
        return code;
    }

    public String getModelName() {
        return modelName;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public BigDecimal getCostPerKm() {
        return costPerKm;
    }

    public double getEcoScore() {
        return ecoScore;
    }

    public boolean hasAutonomousBattery() {
        return autonomousBattery;
    }

    public double getBatteryCapacityKwh() {
        return batteryCapacityKwh;
    }

    public double getEnergyConsumptionPerKm() {
        return energyConsumptionPerKm;
    }

    public double getFuelTankCapacityLiters() {
        return fuelTankCapacityLiters;
    }
}
