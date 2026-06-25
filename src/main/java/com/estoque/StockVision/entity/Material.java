package com.estoque.StockVision.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "materiais")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int quantidade;

    // NOVO CAMPO: Para guardar a prateleira, gaveta, etc.
    private String localizacaoExata;

    @ManyToOne
    @JoinColumn(name = "estoque_local_id", nullable = false)
    private EstoqueLocal estoqueLocal;

    // Construtores
    public Material() {}

    public Material(String nome, int quantidade, String localizacaoExata, EstoqueLocal estoqueLocal) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.localizacaoExata = localizacaoExata;
        this.estoqueLocal = estoqueLocal;
    }

    // Getters e Setters (Adicione os do novo campo)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getLocalizacaoExata() { return localizacaoExata; }
    public void setLocalizacaoExata(String localizacaoExata) { this.localizacaoExata = localizacaoExata; }

    public EstoqueLocal getEstoqueLocal() { return estoqueLocal; }
    public void setEstoqueLocal(EstoqueLocal estoqueLocal) { this.estoqueLocal = estoqueLocal; }
}