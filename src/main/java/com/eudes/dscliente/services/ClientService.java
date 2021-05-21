package com.eudes.dscliente.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eudes.dscliente.dto.ClientDTO;
import com.eudes.dscliente.entities.Client;
import com.eudes.dscliente.repositories.ClientRepository;
import com.eudes.dscliente.services.exceptions.DatabaseException;
import com.eudes.dscliente.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(Pageable pageable) {
		
		Page<Client> page = repository.findAll(pageable);
		
		return page.map(client -> new ClientDTO(client));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findbyId(Long id) {
		
		Optional<Client> optClient = repository.findById(id);
		Client client = optClient.orElseThrow(() -> new ResourceNotFoundException("Client " + id + " not found"));
		
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		
		Client client = new Client();
		client.setName(dto.getName());
		client.setCpf(dto.getCpf());
		client.setIncome(dto.getIncome());
		client.setBirthDate(dto.getBirthDate());
		client.setChildren(dto.getChildren());
		
		client = repository.save(client);
		
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		
		try {
			Client client = repository.getOne(id);
			client.setName(dto.getName());
			client.setCpf(dto.getCpf());
			client.setIncome(dto.getIncome());
			client.setBirthDate(dto.getBirthDate());
			client.setChildren(dto.getChildren());
			
			client = repository.save(client);
			
			return new ClientDTO(client);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found: " + id);
		}
	}

	public void delete(Long id) {
		
		try {
			
			repository.deleteById(id);
				
		} catch (EmptyResultDataAccessException e) {
			
			throw new ResourceNotFoundException("Id not found: " + id);
			
		} catch (DataIntegrityViolationException e) {
			
			throw new DatabaseException("Integrity violation");
			
		}
	}	
}
