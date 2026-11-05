package pl.agh.po.diskrak.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "stop_nodes")
public class StopNodeEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private DistrictEntity district;

    @Column(name = "lat", nullable = false)
    private double lat;

    @Column(name = "lng", nullable = false)
    private double lng;

    @Column(name = "is_terminal_loop", nullable = false)
    private boolean terminalLoop;

    @Column(name = "has_pantograph_charger", nullable = false)
    private boolean pantographCharger;

    @Column(name = "is_depot", nullable = false)
    private boolean depot;

    @Column(name = "depot_capacity", nullable = false)
    private int depotCapacity;

    @Column(name = "is_custom_built", nullable = false)
    private boolean customBuilt;

    @Column(name = "construction_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal constructionCost;

    protected StopNodeEntity() {
    }

    public StopNodeEntity(UUID id, String name, DistrictEntity district, double lat, double lng,
                          boolean terminalLoop, boolean pantographCharger, boolean depot, int depotCapacity,
                          boolean customBuilt, BigDecimal constructionCost) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.lat = lat;
        this.lng = lng;
        this.terminalLoop = terminalLoop;
        this.pantographCharger = pantographCharger;
        this.depot = depot;
        this.depotCapacity = depotCapacity;
        this.customBuilt = customBuilt;
        this.constructionCost = constructionCost;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DistrictEntity getDistrict() {
        return district;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public boolean isTerminalLoop() {
        return terminalLoop;
    }

    public boolean hasPantographCharger() {
        return pantographCharger;
    }

    public boolean isDepot() {
        return depot;
    }

    public int getDepotCapacity() {
        return depotCapacity;
    }

    public boolean isCustomBuilt() {
        return customBuilt;
    }

    public BigDecimal getConstructionCost() {
        return constructionCost;
    }
}
