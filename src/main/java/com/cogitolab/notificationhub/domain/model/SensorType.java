package com.cogitolab.notificationhub.domain.model;

public enum SensorType {
    AIR_TEMPERATURE("C"),
    AIR_HUMIDITY("%"),
    SOIL_MOISTURE("%"),
    WATER_RESERVOIR_LEVEL("%"),
    SILO_LEVEL("%"),
    EQUIPMENT_STATUS(null);

    private final String defaultUnit;

    SensorType(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }
}
