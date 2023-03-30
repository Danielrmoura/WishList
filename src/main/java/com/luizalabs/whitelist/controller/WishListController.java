package com.luizalabs.whitelist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.luizalabs.whitelist.enumeration.IntegrationResponseEnum;
import com.luizalabs.whitelist.enumeration.SearchTypeFilterEnum;
import com.luizalabs.whitelist.integration.IntegrationResponse;
import com.luizalabs.whitelist.mongo.ClientDto;
import com.luizalabs.whitelist.mongo.Product;
import com.luizalabs.whitelist.service.WishListService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/client")
@Slf4j 
@RequiredArgsConstructor
public class WishListController {

	private final WishListService clientService;

    @PostMapping
	public ResponseEntity<Object> createOrUpdate(
			@RequestBody ClientDto client) {
    	log.info("ClientController: createOrUpdate,{}", client);

		IntegrationResponse response = clientService.createOrUpdate(client);

		if (IntegrationResponseEnum.SUCCESS.equals(response.getStatus()))
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		else
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}	
	
    @PostMapping(value = "/product")
	public ResponseEntity<Object> addListProduct(
			@RequestParam(value = "idClient") Long idClient,
			@RequestBody List<Product> product){
    	log.info("ClientController: save-product, {}", idClient, product);

		return new ResponseEntity<>(clientService.addListProduct(idClient, product), HttpStatus.CREATED);

	}   

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestParam(value = "id") String id) {
    	log.info("ClientController: delete, {}", id);

        return new ResponseEntity<>(clientService.delete(id), HttpStatus.OK);
    } 
    
 
    
    @DeleteMapping(value = "/product")
	public ResponseEntity<Object> deleteProduct(
			@RequestParam(value = "idClient") Long idClient,
            @RequestParam(value = "idProduct") Long idProduct) {
    	log.info("ClientController: deleteProduct, {}, {}",idClient, idProduct);

		return new ResponseEntity<>(clientService.deleteProduct(idClient,idProduct), HttpStatus.OK);

	}      
    
	@GetMapping
    public ResponseEntity<List<ClientDto>> listAll(@RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "searchType", required = false) String searchType,
                                          @RequestParam(value = "idClient", required = false) Long idClient) {
		log.info("ClientController: listAll, {}, {}, {}", page, searchType,idClient);

        if(searchType != null && SearchTypeFilterEnum.FULL.name().equals(searchType.toUpperCase())) {
            return new ResponseEntity<>(clientService.listFull(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(clientService.listAllPageableAsMap(page, idClient), HttpStatus.OK);

        }
    }
 
    @GetMapping(value = "/id")
    public ResponseEntity<ClientDto> findById(@RequestParam(value = "id") String id) {
    	log.info("ClientController: findById, {}", id);

        return new ResponseEntity<>(clientService.findById(id), HttpStatus.OK);
    }
    
    @GetMapping(value = "/find/keys-client-product")
    public ResponseEntity<Product> findProductByIdProduct(@RequestParam(value = "idClient") Long idClient,
            											  @RequestParam(value = "idProduct") Long idProduct) {
    	log.info("ClientController: findProductByIdProduct, {}, {}", idClient,idProduct);
    	
    	Product product = clientService.findProductByIdProduct(idClient, idProduct);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }    
 
}
