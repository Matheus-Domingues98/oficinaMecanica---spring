package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.entities.Carro;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public List<Carro> findAll() {
        return  carroRepository.findAll();
    }

    public Carro findById(Long id) {
        return carroRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + id));
    }

    @Transactional
    public Carro insert(Carro obj) {
        return carroRepository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        if (!carroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Carro não encontrado com id: " + id);
        }
        carroRepository.deleteById(id);
    }

    @Transactional
    public Carro update(Long id, Carro obj) {
        Carro entity = carroRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + id));
        updateData(entity, obj);
        return carroRepository.save(entity);
    }

    private void updateData(Carro entity, Carro obj) {
        entity.setMarca(obj.getMarca());
        entity.setModelo(obj.getModelo());
        entity.setPlaca(obj.getPlaca());
        entity.setCor(obj.getCor());
        entity.setAnoFabricacao(obj.getAnoFabricacao());
    }
}
