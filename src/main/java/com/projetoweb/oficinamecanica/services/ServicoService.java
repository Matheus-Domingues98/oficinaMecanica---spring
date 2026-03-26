package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.dto.ServicoRequestDto;
import com.projetoweb.oficinamecanica.dto.ServicoResponseDto;
import com.projetoweb.oficinamecanica.entities.Servico;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.ServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ServicoService {

    private final ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public List<ServicoResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(ServicoResponseDto::from)
                .collect(Collectors.toList());
    }

    public ServicoResponseDto findById(Long id) {
        Servico entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + id));
        return ServicoResponseDto.from(entity);
    }

    @Transactional
    public ServicoResponseDto insert(ServicoRequestDto dto) {
        Servico entity = new Servico();
        entity.setNome(dto.nome());
        entity.setPreco(dto.preco());
        entity.setDescricao(dto.descricao());
        entity.setDuracao(dto.duracao());
        return ServicoResponseDto.from(repository.save(entity));
    }

    @Transactional
    public ServicoResponseDto update(Long id, ServicoRequestDto dto) {
        Servico entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + id));
        entity.setNome(dto.nome());
        entity.setPreco(dto.preco());
        entity.setDescricao(dto.descricao());
        entity.setDuracao(dto.duracao());
        return ServicoResponseDto.from(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}