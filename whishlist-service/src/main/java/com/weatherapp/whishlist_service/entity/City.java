package com.weatherapp.whishlist_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String cityName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="wishlist_id",nullable = false)
    private Wishlist wishlist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return city != null && city.equals(city.cityName);
    }

    @Override
    public int hashCode() {
        return cityName != null ? cityName.hashCode() : 0;
    }

}
