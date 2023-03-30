package com.luizalabs.whitelist.dao.mongo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.luizalabs.whitelist.mongo.ClientDto;
import com.luizalabs.whitelist.mongo.Product;
import com.mongodb.client.result.UpdateResult;



@Repository
public class WishListMongoDao extends abstracMongoDao<ClientDto, Long> {
	
	public List<ClientDto> listAllPageable(Class<ClientDto> entity, Integer page, Integer pageSize, String collection,
			  Long idClient) {
		Query query = getQueryKey(idClient);
		query.with(PageRequest.of(page == null ? 0 : page, pageSize));

		return mongoOperations.find(query, entity, collection);
	}
		
    public Product findProductByIdProduct(Long idClient, Long idProduct) {
    	Query query = new Query();
    	query.addCriteria(Criteria.where("idClient").is(idClient));
    	query.addCriteria(Criteria.where("produtos.idProduct").is(idProduct));
    	query.fields().elemMatch("produtos", Criteria.where("idProduct").is(idProduct));
        ClientDto client = mongoOperations.findOne(query, ClientDto.class);
        if (client != null && client.getProdutos() != null && !client.getProdutos().isEmpty()) {
            return client.getProdutos().get(0);
        } else {
            return null;
        }
    }
	
	public UpdateResult update(ClientDto client, String collection) {

		Update update = new Update();

		if(client.getNome() != null) update.set("nome", client.getNome());
		if(client.getProdutos() != null) update.set("produtos", client.getProdutos());
		
		update.set("usuario", client.getUsuario());
		update.set("datahora",Calendar.getInstance().getTime());

		return mongoOperations.upsert(getQueryKey(client.getIdClient()), update, collection);
	}
	
	public UpdateResult deleteProduct(Long idClient, Long idProduct, String collection) {

		Query query = new Query();
		query.addCriteria(Criteria.where("idClient").is(idClient));
		Update update = new Update().pull("produtos", new Document("idProduct", idProduct));
		

		return mongoOperations.updateFirst(query, update, collection);
	}
	

	public UpdateResult addListProduct(Long idClient, List<Product> product, String collection) {

	    Query query = new Query();
	    query.addCriteria(Criteria.where("idClient").is(idClient));
	    ClientDto client = findByKey(idClient, ClientDto.class, collection);
	    Update update = new Update();
	    List<Document> ListaProdutos = new ArrayList<>();
        if (client.getProdutos().size() + product.size() >= 20) {
	        throw new RuntimeException("A lista de produtos ja contem o numero maximo de elementos permitidos (20). Nao e possivel adicionar mais produtos.");
	    } else {
	        for (Product produtos : product) {
	            Document novoProduto = new Document("idProduct", produtos.getIdProduct())
	                    .append("nome", produtos.getNome())
	                    .append("descricao", produtos.getDescricao())
	                    .append("preco", produtos.getPerco());
	            ListaProdutos.add(novoProduto);
	        }
	        update.push("produtos").each(ListaProdutos.toArray(new Document[ListaProdutos.size()]));
	    }

        try {
            return mongoOperations.updateFirst(query, update, collection);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar a lista de produtos: " + e.getMessage(), e);
        }
	}
		

	private Query getQueryKey(Long idClient) {
		Query query = new Query();
		query.addCriteria(Criteria.where("idClient").is(idClient));
		
		return query;
	}
	
	public ClientDto findByKey(Long idClient, Class<ClientDto> entity, String collection) {
		Query query = new Query();
		query.addCriteria(Criteria.where("idClient").is(idClient));
		
		return mongoOperations.findOne(query, entity, collection);
	}
	

}
