package com.estoque.StockVision.repository;

import com.estoque.StockVision.entity.EstoqueLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueLocalRepository extends JpaRepository<EstoqueLocal, Long> {
    // Método customizado para achar um estoque pelo nome idêntico (ignorando maiúsculas/minúsculas)
    EstoqueLocal findByNomeIgnoreCase(String nome);
}