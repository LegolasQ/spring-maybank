package com.zqlim.spring.Repo;

import com.zqlim.spring.Model.Car;
import com.zqlim.spring.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CarRepo extends JpaRepository<Car, Long> {
    List<Car> findByBrandAndModelAndClient(String brand, String model, Client client);
}
