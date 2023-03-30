package com.luizalabs.whitelist.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizalabs.whitelist.dao.mongo.WishListMongoDao;
import com.luizalabs.whitelist.mongo.ClientDto;
import com.luizalabs.whitelist.mongo.Product;
import com.mongodb.client.result.UpdateResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WishListService.class)
public class WishListServiceTest {
	
    @MockBean
    private WishListMongoDao wishListMongoDao;

    @Mock
    private UpdateResult updateResult;
    
    private WishListService clientService;
    
    private static final String MONGO_COLLECTION = "client";
    private static final Class<ClientDto> DOMAIN_CLASS = ClientDto.class;
    private static final String pageSize = "10";
    
	@Test
	void testListAllPageableAsMap() {
	    
	    Long idClient = 1L;
	    Integer page = 0;

	    ClientDto ClientDto1 = new ClientDto();
		ClientDto1.setIdClient(1l);
		ClientDto1.setNome("Daniel Moura");
		Product product = getProduct();
		List<Product> listProduct = new ArrayList<>();
		listProduct.add(product);
		ClientDto1.setProdutos(listProduct);
		
		ClientDto ClientDto2 = new ClientDto();
		ClientDto2.setIdClient(2l);
		ClientDto1.setNome("Flavio Moura");
		List<Product> listProduct2 = new ArrayList<>();
		listProduct.add(product);
		ClientDto1.setProdutos(listProduct2);
		
	    List<ClientDto> clientDtosMock = Arrays.asList(
	    		ClientDto1,
				ClientDto2
	    );
	    when(wishListMongoDao.listAllPageable(DOMAIN_CLASS, page, Integer.valueOf(pageSize), MONGO_COLLECTION, idClient))
	            .thenReturn(clientDtosMock);

	    List<ClientDto> result = clientService.listAllPageableAsMap(page, idClient);

	    Assertions.assertEquals(clientDtosMock, result);
	    verify(wishListMongoDao, times(1)).listAllPageable(eq(DOMAIN_CLASS), eq(page), eq(Integer.valueOf(pageSize)), eq(MONGO_COLLECTION), eq(idClient));
	}
	
	@Test
	void testListFull() {
		
		ClientDto ClientDto1 = new ClientDto();
		ClientDto1.setIdClient(1l);
		ClientDto1.setNome("Daniel Moura");
		Product product = getProduct();
		List<Product> listProduct = new ArrayList<>();
		listProduct.add(product);
		ClientDto1.setProdutos(listProduct);
		
		ClientDto ClientDto2 = new ClientDto();
		ClientDto2.setIdClient(2l);
		ClientDto1.setNome("Flavio Moura");
		List<Product> listProduct2 = new ArrayList<>();
		listProduct.add(product);
		ClientDto1.setProdutos(listProduct2);
		
		List<ClientDto> clientDtosMock = Arrays.asList(
				ClientDto1,
				ClientDto2
	    );
	    when(wishListMongoDao.listAll(DOMAIN_CLASS, MONGO_COLLECTION)).thenReturn(clientDtosMock);

	    List<ClientDto> result = clientService.listFull();

	    Assertions.assertEquals(clientDtosMock, result);
	    verify(wishListMongoDao, times(1)).listAll(eq(DOMAIN_CLASS), eq(MONGO_COLLECTION));
	}
	
	@Test
	void testFindById() {
	    ObjectId id = new ObjectId("60624f8d36d2c122984742eb");
	    ClientDto ClientDto1 = new ClientDto();
		ClientDto1.setIdClient(1l);
		ClientDto1.setNome("Daniel Moura");
		Product product = getProduct();
		List<Product> listProduct = new ArrayList<>();
		listProduct.add(product);
		ClientDto1.setProdutos(listProduct);
	    
	    when(wishListMongoDao.findById(eq(id), eq(DOMAIN_CLASS), eq(MONGO_COLLECTION))).thenReturn(ClientDto1);

	    ClientDto result = clientService.findById(id);

	    Assertions.assertEquals(ClientDto1, result);
	    verify(wishListMongoDao, times(1)).findById(eq(id), eq(DOMAIN_CLASS), eq(MONGO_COLLECTION));
	}

	
	@Test
	void testFindProductByIdProduct() {
	    
		Long idClient = 1L;
	    Long idProduct = 12l;

	    Product product1 = getProduct();
	    when(clientService.findProductByIdProduct(idClient, idProduct)).thenReturn(product1);

	    Product result = clientService.findProductByIdProduct(idClient, idProduct);

	    Assertions.assertEquals(product1, result);
	    verify(wishListMongoDao, times(1)).findProductByIdProduct(eq(idClient), eq(idProduct));
	}
	
	@Test
	void testInsert() {
		
		ClientDto ClientDto1 = new ClientDto();
		ClientDto1.setIdClient(1l);
		ClientDto1.setNome("Daniel Moura");
		Product product = getProduct();
		List<Product> listProduct = new ArrayList<>();
		listProduct.add(product);
		ClientDto1.setProdutos(listProduct);
		when(wishListMongoDao.insert(eq(ClientDto1), eq(MONGO_COLLECTION))).thenReturn(ClientDto1);

	    ClientDto result = clientService.insert(ClientDto1);

	    Assertions.assertEquals(ClientDto1, result);
	    verify(wishListMongoDao, times(1)).insert(eq(ClientDto1), eq(MONGO_COLLECTION));
	}
	
    
    @Test
    public void testDeleteProduct() {
        
    	Long idClient = 1L;
        Long idProduct = 12L;

        when(wishListMongoDao.deleteProduct(idClient, idProduct, MONGO_COLLECTION)).thenReturn(updateResult);
        when(updateResult.getModifiedCount()).thenReturn(1L);

        long modifiedCount = wishListMongoDao.deleteProduct(idClient, idProduct, MONGO_COLLECTION).getModifiedCount();

        assertEquals(1L, modifiedCount);
    }
    
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		clientService = new WishListService(wishListMongoDao);
	}
	
	@Test
	void testAddListProduct() {
	    
		Long idClient = 1l;
		Product product1 = getProduct();
		
	    Product product2 = new Product();
	    product2.setIdProduct(13L);
	    product2.setNome("Controle branco");
	    product2.setDescricao("Controle DualSense - Branco");
	    product2.setPerco(400.0);
	    List<Product> products = Arrays.asList(product1, product2);

	    UpdateResult updateResultMock = mock(UpdateResult.class);
	    when(updateResultMock.getModifiedCount()).thenReturn(2L);

	    when(wishListMongoDao.addListProduct(idClient, products, MONGO_COLLECTION))
	            .thenReturn(updateResultMock);

	    long result = clientService.addListProduct(idClient, products);
	    Assertions.assertEquals(2L, result);
	    verify(wishListMongoDao, times(1)).addListProduct(eq(idClient), eq(products), eq(MONGO_COLLECTION));
	}
	
	private static Product getProduct() {
		Product Product = new Product();
		Product.setIdProduct(10L);
		Product.setNome("PS5");
		Product.setDescricao("Playstation 5");
		Product.setPerco(4000.0);
		return Product;
	}	
}
