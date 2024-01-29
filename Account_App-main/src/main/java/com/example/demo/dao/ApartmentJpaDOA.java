package com.example.demo.dao;
import com.example.demo.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentJpaDOA extends JpaRepository<Apartment,Integer> {
     Optional<Apartment> findByApartmentCode(String apartmentCode);
     @Query(value = "SELECT * FROM apartment WHERE apartment_code = ?1 AND (?2 IS NULL OR status = ?2)", nativeQuery = true)
     Optional<Apartment> findByApartmentCodeAndStatus(String apartmentCode, String status);

     @Query(value = "SELECT * FROM apartment WHERE status = 'ACT'", nativeQuery = true)
     List<Apartment> findAllActiveApartments();

}
