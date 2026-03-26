package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.dto.ProdutoRequestDto;
import com.projetoweb.oficinamecanica.dto.ProdutoResponseDto;
import com.projetoweb.oficinamecanica.entities.Produto;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<ProdutoResponseDto> findAll() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponseDto::from)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDto findById(Long id) {
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
        return ProdutoResponseDto.from(entity);
    }

    @Transactional
    public ProdutoResponseDto insert(ProdutoRequestDto dto) {
        Produto entity = new Produto();
        entity.setNome(dto.nome());
        entity.setPreco(dto.preco());
        entity.setQuantidade(dto.quantidade());
        return ProdutoResponseDto.from(produtoRepository.save(entity));
    }

    @Transactional
    public ProdutoResponseDto update(Long id, ProdutoRequestDto dto) {
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
        entity.setNome(dto.nome());
        entity.setPreco(dto.preco());
        entity.setQuantidade(dto.quantidade());
        return ProdutoResponseDto.from(produtoRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com id: " + id);
        }
        produtoRepository.deleteById(id);
    }
}