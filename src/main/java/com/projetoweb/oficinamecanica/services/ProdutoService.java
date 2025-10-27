package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.entities.Produto;
import com.projetoweb.oficinamecanica.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Produto findById(Long id) {
        Optional<Produto> obj = produtoRepository.findById(id);
        return obj.get();
    }

    public Produto insert(Produto obj) {
        return produtoRepository.save(obj);
    }

    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }

    public Produto update(Long id, Produto obj) {
        Produto entity = produtoRepository.findById(id).get();
        updateData(entity, obj);
        return produtoRepository.save(entity);
    }

    private void updateData(Produto entity, Produto obj) {
        entity.setNome(obj.getNome());
        entity.setPreco(obj.getPreco());
        entity.setQuantidade(obj.getQuantidade());
    }
}
