package com.prueba.ufinet.service;

import com.prueba.ufinet.exception.CarValidationException;
import com.prueba.ufinet.model.Car;
import com.prueba.ufinet.model.User;
import com.prueba.ufinet.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Car saveCar(Car car, User user) {
        if (car.getBrand() == null || car.getBrand().trim().isEmpty()) {
            throw new CarValidationException("La marca es obligatoria");
        }
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new CarValidationException("El modelo es obligatorio");
        }
        if (car.getYear() == null || car.getYear() < 1900 || car.getYear() > 2100) {
            throw new CarValidationException("El año es inválido");
        }
        if (car.getPlateNumber() == null || car.getPlateNumber().trim().isEmpty()) {
            throw new CarValidationException("La placa es obligatoria");
        }
        car.setUser(user);
        carRepository.save(car);
        return carRepository.findById(car.getId()).orElse(null);
    }

    @Override
    public List<Car> getCarsByUser(User user) {
        return carRepository.findByUser(user);
    }

    @Override
    public Optional<Car> getCarByIdAndUser(Long id, User user) {
        return carRepository.findByIdAndUser(id, user);
    }

    @Override
    public Car updateCar(Car car, User user) {
        Optional<Car> existing = carRepository.findByIdAndUser(car.getId(), user);
        if (existing.isPresent()) {
            if (car.getBrand() == null || car.getBrand().trim().isEmpty()) {
                throw new CarValidationException("La marca es obligatoria");
            }
            if (car.getModel() == null || car.getModel().trim().isEmpty()) {
                throw new CarValidationException("El modelo es obligatorio");
            }
            if (car.getYear() == null || car.getYear() < 1900 || car.getYear() > 2100) {
                throw new CarValidationException("El año es inválido");
            }
            if (car.getPlateNumber() == null || car.getPlateNumber().trim().isEmpty()) {
                throw new CarValidationException("La placa es obligatoria");
            }
            Car toUpdate = existing.get();
            toUpdate.setBrand(car.getBrand());
            toUpdate.setModel(car.getModel());
            toUpdate.setYear(car.getYear());
            toUpdate.setPlateNumber(car.getPlateNumber());
            toUpdate.setColor(car.getColor());
            return carRepository.save(toUpdate);
        }
        throw new CarValidationException("Carro no encontrado o no autorizado");
    }

    @Override
    public void deleteCar(Long id, User user) {
        Optional<Car> car = carRepository.findByIdAndUser(id, user);
        car.ifPresent(carRepository::delete);
    }
}
