package com.prueba.ufinet.dto;

public class CarResponseDTO {
    private Long id;
    private String brand;
    private String model;
    private int year;
    private String plateNumber;
    private String color;
    private UserDTO user;

    public CarResponseDTO() {}

    public CarResponseDTO(Long id, String brand, String model, int year, String plateNumber, String color, UserDTO user) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.plateNumber = plateNumber;
        this.color = color;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}

