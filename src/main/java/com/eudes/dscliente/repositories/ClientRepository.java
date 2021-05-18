package com.eudes.dscliente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eudes.dscliente.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
