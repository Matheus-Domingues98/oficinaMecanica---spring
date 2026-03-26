package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.dto.ClienteRequestDto;
import com.projetoweb.oficinamecanica.dto.ClienteResponseDto;
import com.projetoweb.oficinamecanica.entities.Cliente;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponseDto> findAll() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponseDto::new)
                .collect(Collectors.toList());
    }

    public ClienteResponseDto findById(Long id) {
        Cliente entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
        return new ClienteResponseDto(entity);
    }

    public ClienteResponseDto findByDoc(String doc) {
        Cliente entity = clienteRepository.findByDoc(doc)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com documento: " + doc));
        return new ClienteResponseDto(entity);
    }

    @Transactional
    public ClienteResponseDto insert(ClienteRequestDto dto) {
        Cliente entity = new Cliente();
        entity.setNome(dto.nome());
        entity.setTelefone(dto.telefone());
        entity.setEmail(dto.email());
        entity.setDoc(dto.doc());
        return new ClienteResponseDto(clienteRepository.save(entity));
    }

    @Transactional
    public ClienteResponseDto update(Long id, ClienteRequestDto dto) {
        Cliente entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
        entity.setNome(dto.nome());
        entity.setTelefone(dto.telefone());
        entity.setEmail(dto.email());
        entity.setDoc(dto.doc());
        return new ClienteResponseDto(clienteRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}