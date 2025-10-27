package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.entities.Servico;
import com.projetoweb.oficinamecanica.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository repository;

    public List<Servico> findAll() {
        return repository.findAll();
    }

    public Servico findById(Long id) {
        return repository.findById(id).get();
    }

    public Servico insert(Servico obj) {
        return repository.save(obj);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Servico update(Long id, Servico obj) {
        Servico entity = repository.findById(id).get();
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
