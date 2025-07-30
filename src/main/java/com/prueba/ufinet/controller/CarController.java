package com.prueba.ufinet.controller;

import com.prueba.ufinet.dto.CarDTO;
import com.prueba.ufinet.dto.CarResponseDTO;
import com.prueba.ufinet.dto.UserDTO;
import com.prueba.ufinet.model.Car;
import com.prueba.ufinet.model.User;
import com.prueba.ufinet.service.CarService;
import com.prueba.ufinet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createCar(@Valid @RequestBody CarDTO carDto, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No autenticado");
            return ResponseEntity.status(401).body(error);
        }
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario no encontrado");
            return ResponseEntity.status(404).body(error);
        }
        if (carDto.getModel() == null || carDto.getModel().isEmpty() ||
            carDto.getBrand() == null || carDto.getBrand().isEmpty() ||
            carDto.getColor() == null || carDto.getColor().isEmpty() ||
            carDto.getYear() == null || carDto.getYear().isEmpty() ||
            carDto.getPlateNumber() == null || carDto.getPlateNumber().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Todos los campos del auto son obligatorios");
            return ResponseEntity.status(400).body(error);
        }
        try {
            int year = Integer.parseInt(carDto.getYear());
            if (year < 1900 || year > 2100) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El año debe estar entre 1900 y 2100");
                return ResponseEntity.status(400).body(error);
            }
        } catch (NumberFormatException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El año debe ser un número válido");
            return ResponseEntity.status(400).body(error);
        }
        if (!carDto.getPlateNumber().matches("^[A-Z]{3}[0-9]{3}$")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La placa debe tener el formato de 3 letras seguidas de 3 números, por ejemplo: ABC123");
            return ResponseEntity.status(400).body(error);
        }
        Car car = new Car();
        car.setModel(carDto.getModel());
        car.setBrand(carDto.getBrand());
        car.setColor(carDto.getColor());
        car.setYear(Integer.parseInt(carDto.getYear()));
        car.setPlateNumber(carDto.getPlateNumber());
        car.setUser(user.get());
        try {
            Car savedCar = carService.saveCar(car, user.get());
            CarResponseDTO response = new CarResponseDTO(
                savedCar.getId(),
                savedCar.getBrand(),
                savedCar.getModel(),
                savedCar.getYear(),
                savedCar.getPlateNumber(),
                savedCar.getColor(),
                new UserDTO(
                    savedCar.getUser().getId(),
                    savedCar.getUser().getUsername(),
                    savedCar.getUser().getEmail()
                )
            );
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            String msg = e.getMessage();
            Map<String, String> error = new HashMap<>();
            if (msg != null && msg.contains("constraint") && msg.contains("plate_number")) {
                error.put("error", "La placa ya está registrada. No se permiten duplicados.");
                return ResponseEntity.status(409).body(error);
            }
            error.put("error", "Error al guardar el auto: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCars(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No autenticado");
            return ResponseEntity.status(401).body(error);
        }
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario no encontrado");
            return ResponseEntity.status(404).body(error);
        }
        try {
            List<Car> cars = carService.getCarsByUser(user.get());
            if (cars.isEmpty()) {
                Map<String, String> msg = new HashMap<>();
                msg.put("message", "No hay autos registrados para este usuario");
                return ResponseEntity.status(200).body(msg);
            }
            List<CarResponseDTO> response = cars.stream().map(car -> new CarResponseDTO(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getPlateNumber(),
                car.getColor(),
                new UserDTO(
                    car.getUser().getId(),
                    car.getUser().getUsername(),
                    car.getUser().getEmail()
                )
            )).toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener los autos: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @Valid @RequestBody CarDTO carDto, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No autenticado");
            return ResponseEntity.status(401).body(error);
        }
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario no encontrado");
            return ResponseEntity.status(404).body(error);
        }
        if (carDto.getModel() == null || carDto.getModel().isEmpty() ||
            carDto.getBrand() == null || carDto.getBrand().isEmpty() ||
            carDto.getColor() == null || carDto.getColor().isEmpty() ||
            carDto.getYear() == null || carDto.getYear().isEmpty() ||
            carDto.getPlateNumber() == null || carDto.getPlateNumber().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Todos los campos del auto son obligatorios");
            return ResponseEntity.status(400).body(error);
        }
        try {
            int year = Integer.parseInt(carDto.getYear());
            if (year < 1900 || year > 2100) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El año debe estar entre 1900 y 2100");
                return ResponseEntity.status(400).body(error);
            }
        } catch (NumberFormatException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El año debe ser un número válido");
            return ResponseEntity.status(400).body(error);
        }
        if (!carDto.getPlateNumber().matches("^[A-Z]{3}[0-9]{3}$")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La placa debe tener el formato de 3 letras seguidas de 3 números, por ejemplo: ABC123");
            return ResponseEntity.status(400).body(error);
        }
        Car car = new Car();
        car.setId(id);
        car.setModel(carDto.getModel());
        car.setBrand(carDto.getBrand());
        car.setColor(carDto.getColor());
        car.setYear(Integer.parseInt(carDto.getYear()));
        car.setPlateNumber(carDto.getPlateNumber());
        car.setUser(user.get());
        try {
            Car updatedCar = carService.updateCar(car, user.get());
            CarResponseDTO response = new CarResponseDTO(
                updatedCar.getId(),
                updatedCar.getBrand(),
                updatedCar.getModel(),
                updatedCar.getYear(),
                updatedCar.getPlateNumber(),
                updatedCar.getColor(),
                new UserDTO(
                    updatedCar.getUser().getId(),
                    updatedCar.getUser().getUsername(),
                    updatedCar.getUser().getEmail()
                )
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String msg = e.getMessage();
            Map<String, String> error = new HashMap<>();
            if (msg != null && msg.contains("constraint") && msg.contains("plate_number")) {
                error.put("error", "La placa ya está registrada. No se permiten duplicados.");
                return ResponseEntity.status(409).body(error);
            }
            error.put("error", "Error al actualizar el auto: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No autenticado");
            return ResponseEntity.status(401).body(error);
        }
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario no encontrado");
            return ResponseEntity.status(404).body(error);
        }
        Long carId;
        try {
            carId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El id del auto debe ser un número válido");
            return ResponseEntity.status(400).body(error);
        }
        Optional<Car> carOpt = carService.getCarByIdAndUser(carId, user.get());
        if (carOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Auto no encontrado");
            return ResponseEntity.status(404).body(error);
        }
        carService.deleteCar(carId, user.get());
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Auto eliminado correctamente");
        return ResponseEntity.ok(msg);
    }
}
