package com.weatherapp.whishlist_service.repository;

import com.weatherapp.whishlist_service.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City,Integer> {
}
