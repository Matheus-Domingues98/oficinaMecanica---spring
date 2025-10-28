package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.entities.Servico;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ServicoService {

    @Autowired
    private ServicoRepository repository;

    public List<Servico> findAll() {
        return repository.findAll();
    }

    public Servico findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + id));
    }

    @Transactional
    public Servico insert(Servico obj) {
        return repository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public Servico update(Long id, Servico obj) {
        Servico entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + id));
        updateData(entity, obj);
        return repository.save(entity);
    }

    private void updateData(Servico entity, Servico obj) {
        entity.setNome(obj.getNome());
        entity.setPreco(obj.getPreco());
        entity.setDescricao(obj.getDescricao());
        entity.setDuracao(obj.getDuracao());
    }
}
