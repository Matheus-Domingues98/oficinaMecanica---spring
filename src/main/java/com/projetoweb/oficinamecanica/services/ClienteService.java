package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.entities.Cliente;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> findAll() {
        return  clienteRepository.findAll();
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
    }

    @Transactional
    public Cliente insert(Cliente obj) {
        return clienteRepository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
    }

    public Cliente findByDoc(String doc) {
        return clienteRepository.findByDoc(doc)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com documento: " + doc));
    }

    @Transactional
    public Cliente update(Long id, Cliente obj) {
        Cliente entity = clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
        updateData(entity, obj);
        return clienteRepository.save(entity);
    }

    private void updateData(Cliente entity, Cliente obj) {
        entity.setNome(obj.getNome());
        entity.setDoc(obj.getDoc());
        entity.setEmail(obj.getEmail());
        entity.setTelefone(obj.getTelefone());
    }
}
