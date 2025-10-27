package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.entities.Carro;
import com.projetoweb.oficinamecanica.repositories.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public List<Carro> findAll() {
        return  carroRepository.findAll();
    }

    public Carro findById(Long id) {
        Optional <Carro> obj = carroRepository.findById(id);
        return obj.get();
    }

    public Carro insert(Carro obj) {
        return carroRepository.save(obj);
    }

    public void delete(Long id) {
        carroRepository.deleteById(id);
    }

    public Carro update(Long id, Carro obj) {
        Carro entity = carroRepository.findById(id).get();
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
