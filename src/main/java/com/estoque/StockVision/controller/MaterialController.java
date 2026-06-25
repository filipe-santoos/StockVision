package com.estoque.StockVision.controller;

import com.estoque.StockVision.entity.EstoqueLocal;
import com.estoque.StockVision.entity.Material;
import com.estoque.StockVision.repository.EstoqueLocalRepository;
import com.estoque.StockVision.repository.MaterialRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MaterialController {

    private final MaterialRepository materialRepository;
    private final EstoqueLocalRepository estoqueLocalRepository;

    // O Spring injeta automaticamente os repositories do banco de dados aqui
    public MaterialController(MaterialRepository materialRepository, EstoqueLocalRepository estoqueLocalRepository) {
        this.materialRepository = materialRepository;
        this.estoqueLocalRepository = estoqueLocalRepository;
    }

    // Rota Principal: Abre o site e lista os estoques no menu lateral
    @GetMapping("/estoque")
    public String listarMateriais(@RequestParam(value = "busca", required = false) String busca,
                                  @RequestParam(value = "estoqueId", required = false) Long estoqueId,
                                  Model model) {

        // Sempre carrega os estoques cadastrados para o menu lateral esquerdo
        List<EstoqueLocal> estoques = estoqueLocalRepository.findAll();
        model.addAttribute("estoques", estoques);

        // Caso 1: Usuário clicou em um estoque específico no menu lateral
        if (estoqueId != null) {
            Optional<EstoqueLocal> estoqueOpt = estoqueLocalRepository.findById(estoqueId);
            if (estoqueOpt.isPresent()) {
                model.addAttribute("estoqueSelecionado", estoqueOpt.get());
                model.addAttribute("materiais", estoqueOpt.get().getMateriais()); // Carrega os itens do estoque
            }
        }
        // Caso 2: Usuário usou a barra de pesquisa global (procura em tudo)
        else if (busca != null && !busca.trim().isEmpty()) {
            List<Material> resultados = materialRepository.findByNomeContainingIgnoreCaseOrEstoqueLocalNomeContainingIgnoreCase(busca, busca);
            model.addAttribute("materiais", resultados);
            model.addAttribute("busca", busca);
        }
        // Caso 3: Tela Inicial Limpa (Home) -> não adiciona o atributo "materiais", mantendo a barra centralizada

        return "index";
    }

    // Cria um Novo Estoque (Ex: Estoque da Sala, Cozinha) pelo menu lateral
    @PostMapping("/estoque/novo-local")
    public String criarNovoEstoque(@RequestParam("nomeEstoque") String nomeEstoque) {
        if (nomeEstoque != null && !nomeEstoque.trim().isEmpty()) {
            if (estoqueLocalRepository.findByNomeIgnoreCase(nomeEstoque) == null) {
                estoqueLocalRepository.save(new EstoqueLocal(nomeEstoque));
            }
        }
        return "redirect:/estoque";
    }

    // Cadastra um Novo Item dentro de um estoque específico
    @PostMapping("/estoque/adicionar-item")
    public String adicionarItem(@RequestParam("estoqueId") Long estoqueId,
                                @RequestParam("nome") String nome,
                                @RequestParam("quantidade") int quantidade,
                                @RequestParam("localizacaoExata") String localizacaoExata) { // Novo parâmetro aqui

        Optional<EstoqueLocal> estoqueOpt = estoqueLocalRepository.findById(estoqueId);

        if (estoqueOpt.isPresent()) {
            // Passa a localização exata para o construtor do Material
            Material novoMaterial = new Material(nome, quantidade, localizacaoExata, estoqueOpt.get());
            materialRepository.save(novoMaterial);
        }

        return "redirect:/estoque?estoqueId=" + estoqueId;
    }

    // Botão rápido de adicionar +1 unidade no item
    @PostMapping("/estoque/adicionar-unidade")
    public String adicionarUnidade(@RequestParam("id") Long id,
                                   @RequestParam(value = "estoqueId", required = false) Long estoqueId) {
        Optional<Material> materialOpt = materialRepository.findById(id);
        if (materialOpt.isPresent()) {
            Material m = materialOpt.get();
            m.setQuantidade(m.getQuantidade() + 1);
            materialRepository.save(m);
        }
        return (estoqueId != null && estoqueId > 0) ? "redirect:/estoque?estoqueId=" + estoqueId : "redirect:/estoque";
    }

    // Botão rápido de remover -1 unidade (dar baixa) no item
    @PostMapping("/estoque/baixa")
    public String darBaixa(@RequestParam("id") Long id,
                           @RequestParam(value = "estoqueId", required = false) Long estoqueId) {
        Optional<Material> materialOpt = materialRepository.findById(id);
        if (materialOpt.isPresent()) {
            Material m = materialOpt.get();
            if (m.getQuantidade() > 0) {
                m.setQuantidade(m.getQuantidade() - 1);
                materialRepository.save(m);
            }
        }
        return (estoqueId != null && estoqueId > 0) ? "redirect:/estoque?estoqueId=" + estoqueId : "redirect:/estoque";
    }
}
