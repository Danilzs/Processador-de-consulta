package br.edu.processador.model;

import java.util.HashMap;
import java.util.Map;

public class DatabaseSchema {
    private Map<String, TableMetadata> tables;

    public DatabaseSchema() {
        this.tables = new HashMap<>();
        initializeSchema();
    }

    private void initializeSchema() {
        // Categoria
        TableMetadata categoria = new TableMetadata("Categoria");
        categoria.addColumn("idCategoria");
        categoria.addColumn("Descricao");
        tables.put("categoria", categoria);

        // Produto
        TableMetadata produto = new TableMetadata("Produto");
        produto.addColumn("idProduto");
        produto.addColumn("Nome");
        produto.addColumn("Descricao");
        produto.addColumn("Preco");
        produto.addColumn("QuantEstoque");
        produto.addColumn("Categoria_idCategoria");
        tables.put("produto", produto);

        // TipoCliente
        TableMetadata tipoCliente = new TableMetadata("TipoCliente");
        tipoCliente.addColumn("idTipoCliente");
        tipoCliente.addColumn("Descricao");
        tables.put("tipocliente", tipoCliente);

        // Cliente
        TableMetadata cliente = new TableMetadata("Cliente");
        cliente.addColumn("idCliente");
        cliente.addColumn("Nome");
        cliente.addColumn("Email");
        cliente.addColumn("Nascimento");
        cliente.addColumn("Senha");
        cliente.addColumn("TipoCliente_idTipoCliente");
        cliente.addColumn("DataRegistro");
        tables.put("cliente", cliente);

        // TipoEndereco
        TableMetadata tipoEndereco = new TableMetadata("TipoEndereco");
        tipoEndereco.addColumn("idTipoEndereco");
        tipoEndereco.addColumn("Descricao");
        tables.put("tipoendereco", tipoEndereco);

        // Endereco
        TableMetadata endereco = new TableMetadata("Endereco");
        endereco.addColumn("idEndereco");
        endereco.addColumn("EnderecoPadrao");
        endereco.addColumn("Logradouro");
        endereco.addColumn("Numero");
        endereco.addColumn("Complemento");
        endereco.addColumn("Bairro");
        endereco.addColumn("Cidade");
        endereco.addColumn("UF");
        endereco.addColumn("CEP");
        endereco.addColumn("TipoEndereco_idTipoEndereco");
        endereco.addColumn("Cliente_idCliente");
        tables.put("endereco", endereco);

        // Telefone
        TableMetadata telefone = new TableMetadata("Telefone");
        telefone.addColumn("Numero");
        telefone.addColumn("Cliente_idCliente");
        tables.put("telefone", telefone);

        // Status
        TableMetadata status = new TableMetadata("Status");
        status.addColumn("idStatus");
        status.addColumn("Descricao");
        tables.put("status", status);

        // Pedido
        TableMetadata pedido = new TableMetadata("Pedido");
        pedido.addColumn("idPedido");
        pedido.addColumn("Status_idStatus");
        pedido.addColumn("DataPedido");
        pedido.addColumn("ValorTotalPedido");
        pedido.addColumn("Cliente_idCliente");
        tables.put("pedido", pedido);

        // Pedido_has_Produto
        TableMetadata pedidoProduto = new TableMetadata("Pedido_has_Produto");
        pedidoProduto.addColumn("idPedidoProduto");
        pedidoProduto.addColumn("Pedido_idPedido");
        pedidoProduto.addColumn("Produto_idProduto");
        pedidoProduto.addColumn("Quantidade");
        pedidoProduto.addColumn("PrecoUnitario");
        tables.put("pedido_has_produto", pedidoProduto);
    }

    public TableMetadata getTable(String tableName) {
        return tables.get(tableName.toLowerCase());
    }

    public boolean hasTable(String tableName) {
        return tables.containsKey(tableName.toLowerCase());
    }
}
