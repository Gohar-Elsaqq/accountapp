package com.example.demo.dao;

import com.example.demo.entity.ActApartmentsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ActApartmentsViewDOA extends JpaRepository<ActApartmentsView,Long> {
   List<ActApartmentsView> findByApartmentCode(String apartmentCode);
}
