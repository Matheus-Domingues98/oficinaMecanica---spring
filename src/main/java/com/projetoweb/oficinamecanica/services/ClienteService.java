package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.entities.Cliente;
import com.projetoweb.oficinamecanica.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return  clienteRepository.findAll();
    }

    public Cliente findById(Long id) {
        Optional<Cliente> obj = clienteRepository.findById(id);
        return obj.get();
    }

    public Cliente insert(Cliente obj) {
        return clienteRepository.save(obj);
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente findByDoc(String doc) {
        Optional<Cliente> obj = clienteRepository.findByDoc(doc);
        return obj.get();
    }

    public Cliente update(Long id, Cliente obj) {
        Cliente entity = clienteRepository.findById(id).get();
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
