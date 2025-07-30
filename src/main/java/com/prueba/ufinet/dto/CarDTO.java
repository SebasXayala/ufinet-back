package com.prueba.ufinet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CarDTO {
    @NotBlank(message = "El modelo es obligatorio")
    private String model;

    @NotBlank(message = "La marca es obligatoria")
    private String brand;

    @NotBlank(message = "El color es obligatorio")
    private String color;

    @Size(min = 4, max = 4, message = "El año debe tener 4 dígitos")
    private String year;

    @NotBlank(message = "La placa es obligatoria")
    private String plateNumber;

    // Getters y setters
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
}
