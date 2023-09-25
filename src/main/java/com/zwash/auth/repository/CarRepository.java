package com.zwash.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zwash.auth.pojos.Car;
import com.zwash.auth.pojos.User;




@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	 @Query("select c from Car c where c.registerationPlate = ?1")
	  Car findByRegisterationPlate(String registerationPlate);

	 @Query("SELECT c FROM Car c WHERE c.user = ?1")
	  List<Car> findByUser(User user);

}

