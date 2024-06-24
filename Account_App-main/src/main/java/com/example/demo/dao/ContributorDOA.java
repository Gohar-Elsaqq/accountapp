package com.example.demo.dao;

import com.example.demo.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorDOA extends JpaRepository<Contributor,Integer> {

}
