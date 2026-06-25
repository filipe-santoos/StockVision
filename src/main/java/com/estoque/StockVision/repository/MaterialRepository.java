package com.estoque.StockVision.repository;

import com.estoque.StockVision.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    // Método para fazer a busca por texto na barra de pesquisa (busca no nome do item ou no nome do local)
    List<Material> findByNomeContainingIgnoreCaseOrEstoqueLocalNomeContainingIgnoreCase(String nome, String localNome);
}
