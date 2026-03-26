package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.dto.CarroRequestDto;
import com.projetoweb.oficinamecanica.dto.CarroResponseDto;
import com.projetoweb.oficinamecanica.entities.Carro;
import com.projetoweb.oficinamecanica.entities.Cliente;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.CarroRepository;
import com.projetoweb.oficinamecanica.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CarroService {

    private final CarroRepository carroRepository;
    private final ClienteRepository clienteRepository;

    public CarroService(CarroRepository carroRepository, ClienteRepository clienteRepository) {
        this.carroRepository = carroRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<CarroResponseDto> findAll() {
        return carroRepository.findAll()
                .stream()
                .map(CarroResponseDto::new)
                .collect(Collectors.toList());
    }

    public CarroResponseDto findById(Long id) {
        Carro entity = carroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + id));
        return new CarroResponseDto(entity);
    }

    @Transactional
    public CarroResponseDto insert(CarroRequestDto dto) {
        if (dto.clienteId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório para cadastrar um carro");
        }
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.clienteId()));

        Carro entity = new Carro();
        entity.setModelo(dto.modelo());
        entity.setPlaca(dto.placa());
        entity.setCor(dto.cor());
        entity.setAnoFabricacao(dto.anoFabricacao());
        entity.setMarca(dto.marca());
        entity.setCliente(cliente);

        return new CarroResponseDto(carroRepository.save(entity));
    }

    @Transactional
    public CarroResponseDto update(Long id, CarroRequestDto dto) {
        Carro entity = carroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + id));
        entity.setModelo(dto.modelo());
        entity.setPlaca(dto.placa());
        entity.setCor(dto.cor());
        entity.setAnoFabricacao(dto.anoFabricacao());
        entity.setMarca(dto.marca());
        return new CarroResponseDto(carroRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!carroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Carro não encontrado com id: " + id);
        }
        carroRepository.deleteById(id);
    }
}