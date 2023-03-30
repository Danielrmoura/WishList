package com.luizalabs.whitelist.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.luizalabs.whitelist.business.service.IService;
import com.luizalabs.whitelist.enumeration.IntegrationResponseEnum;
import com.luizalabs.whitelist.mongo.ClientDto;
import com.luizalabs.whitelist.mongo.Product;
import com.luizalabs.whitelist.dao.mongo.WishListMongoDao;
import com.luizalabs.whitelist.integration.IntegrationResponse;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishListService implements IService<ClientDto>{

    private static final Class<ClientDto> DOMAIN_CLASS = ClientDto.class;
    private static final String MONGO_COLLECTION = "client";
    private static final String pageSize = "10";
	
    private final WishListMongoDao clientMongoDao;

	
    public List<ClientDto> listAllPageableAsMap(Integer page, Long idClient) {
    	log.info("ClientService: listAllPageableAsMap, {}, {}",page ,idClient);
		
		return clientMongoDao.listAllPageable(DOMAIN_CLASS, page, Integer.valueOf(pageSize), MONGO_COLLECTION, idClient);

	} 
    
    public List<ClientDto> listFull() {
        return clientMongoDao.listAll(DOMAIN_CLASS, MONGO_COLLECTION);
    }    
    
	@Override
	public ClientDto findById(Object id) {
		log.info("ClientService: findById, {}", id);
		return clientMongoDao.findById(id, DOMAIN_CLASS, MONGO_COLLECTION);
	} 
	
	public Product findProductByIdProduct(Long idClient, Long idProduct) {
		log.info("ClientService: findProdutoByIdProduct, {}, {}", idClient, idProduct);
		return clientMongoDao.findProductByIdProduct(idClient, idProduct);
	}

	@Override
	public ClientDto insert(ClientDto client) {
		log.info("ClientService: insert, {}", client);
		return clientMongoDao.insert(client, MONGO_COLLECTION);
	}

	@Override
	public long update(ClientDto client) {
		log.info("ClientService: update, {}", client);
		UpdateResult updateResult = clientMongoDao.update(client, MONGO_COLLECTION);

		return updateResult.getModifiedCount();
	}
	
	public long deleteProduct(Long idClient, Long idProduct) {
		log.info("ClientService: deleteProduct, {}, {}", idClient, idProduct);
		UpdateResult updateResult = clientMongoDao.deleteProduct(idClient, idProduct, MONGO_COLLECTION);

		return updateResult.getModifiedCount();
	}
	
	
	public long addListProduct(Long idClient, List<Product> product) {
		log.info("ClientService: ListaProdutos, {}, {}", idClient, product);
		UpdateResult updateResult = clientMongoDao.addListProduct(idClient, product,  MONGO_COLLECTION);

		return updateResult.getModifiedCount();
	}	

	@Override
	public long delete(Object id) {
		log.info("ClientService: delete, {}", id);
		DeleteResult deleteResult = clientMongoDao.deleteById(id, MONGO_COLLECTION);

		return deleteResult.getDeletedCount();
	}
	
	public IntegrationResponse createOrUpdate(ClientDto client) {
		log.info("ClientService: createOrUpdate, {}", client);

			if (clientMongoDao.findByKey(client.getIdClient(), DOMAIN_CLASS, MONGO_COLLECTION) != null) {
				update(client);
			} else {
				Date now = Calendar.getInstance().getTime();
				client.setDatahora(now);
				client.setDatahoraReg(now);
				client.setUsuarioReg(client.getUsuario());
				insert(client);
			}

			return createResponse(IntegrationResponseEnum.SUCCESS);

	}

	protected IntegrationResponse createResponse(IntegrationResponseEnum status) {
		IntegrationResponse response = new IntegrationResponse();
		response.setStatus(status);
		
		return response;
	}
}
