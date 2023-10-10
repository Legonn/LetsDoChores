package com.letschores.demo.repository;

import com.letschores.demo.model.Chore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoreRepository extends JpaRepository<Chore, Integer> {

    public Chore findById(int id);
}
