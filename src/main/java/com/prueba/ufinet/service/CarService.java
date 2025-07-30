package com.prueba.ufinet.service;

import com.prueba.ufinet.model.Car;
import com.prueba.ufinet.model.User;
import java.util.List;
import java.util.Optional;

public interface CarService {
    Car saveCar(Car car, User user);
    List<Car> getCarsByUser(User user);
    Optional<Car> getCarByIdAndUser(Long id, User user);
    Car updateCar(Car car, User user);
    void deleteCar(Long id, User user);
}

