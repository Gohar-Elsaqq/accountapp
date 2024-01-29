package com.example.demo.dao;


import com.example.demo.entity.DetailsApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsApartmentDAO extends JpaRepository<DetailsApartment,Integer> {

    @Query(value = "SELECT MAX(da.totalcost) FROM details_apartment da WHERE da.apartment_id = (SELECT id FROM apartment WHERE apartment_code = :apartmentCode)", nativeQuery = true)
    Double findMaxTotalCostByApartmentCode(String apartmentCode);

}


