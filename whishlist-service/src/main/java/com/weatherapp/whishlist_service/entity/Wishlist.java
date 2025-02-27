package com.weatherapp.whishlist_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wishlist",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    @OneToMany(mappedBy = "wishlist",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<City> cities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return email != null && email.equals(wishlist.email);
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

}
