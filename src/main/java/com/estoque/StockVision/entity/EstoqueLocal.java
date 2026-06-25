package com.estoque.StockVision.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estoques_locais")
public class EstoqueLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    // Um estoque local pode ter vários materiais associados a ele
    @OneToMany(mappedBy = "estoqueLocal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materiais = new ArrayList<>();

    // Construtor padrão obrigatório para o Banco de Dados
    public EstoqueLocal() {}

    // Construtor para facilitar a criação de novos estoques
    public EstoqueLocal(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<Material> getMateriais() { return materiais; }
    public void setMateriais(List<Material> materiais) { this.materiais = materiais; }
}
