package com.luizalabs.whitelist.controller;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizalabs.whitelist.enumeration.IntegrationResponseEnum;
import com.luizalabs.whitelist.integration.IntegrationResponse;
import com.luizalabs.whitelist.mongo.ClientDto;
import com.luizalabs.whitelist.mongo.Product;
import com.luizalabs.whitelist.service.WishListService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = WishListController.class)
public class WishListControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WishListService wishListService;

	@SpyBean
	private WishListController wishListController;

	@Test
	  public void saveTest() throws Exception {
		IntegrationResponse response = new IntegrationResponse(IntegrationResponseEnum.SUCCESS, null);
		when(wishListService.createOrUpdate(any(ClientDto.class))).thenReturn(response);
		ClientDto clientDto = getClientDto();

		Product Product = getProduct();

		List<Product> listProduct = new ArrayList<>();

		listProduct.add(Product);
		clientDto.setProdutos(listProduct);

	    mockMvc.perform(post("/client")
	        .contentType(MediaType.APPLICATION_JSON)
	        .content(convertObjectToJsonString(clientDto)))
	        .andDo(print())
	        .andExpect(status().isCreated());
//	        .andExpect(jsonPath("$.description").value(seasonalSchema.getDescription()))
//	        .andExpect(jsonPath("$.site_id").value(seasonalSchema.getSiteId()))
//	        .andExpect(jsonPath("$.color").value(seasonalSchema.getColor()))
//	        .andExpect(jsonPath("$.icon").value(seasonalSchema.getIcon()))
//	        .andExpect(jsonPath("$.tag_text").value(seasonalSchema.getTagText()))
//	        .andExpect(jsonPath("$.timer").value(false))
//	        .andExpect(jsonPath("$.end_date").value(format(seasonalSchema.getEndDate(), DateUtils.PATTERN_YYYY_MM_DD_T_HH_MM_SS)))
//	        .andExpect(jsonPath("$.start_date").value(format(seasonalSchema.getStartDate(), DateUtils.PATTERN_YYYY_MM_DD_T_HH_MM_SS)));
		}

  @Test
  public void addListProductTest() throws Exception {
    when(wishListService.addListProduct(anyLong(), any())).thenReturn(1L);
    Product Product = getProduct();

    List<Product> listProduct = new ArrayList<>();

    listProduct.add(Product);

    mockMvc.perform(post("/client/product")
            .param("idClient", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonString(listProduct)))
        .andDo(print())
        .andExpect(status().isCreated());
//	        .andExpect(jsonPath("$.description").value(seasonalSchema.getDescription()))
//	        .andExpect(jsonPath("$.site_id").value(seasonalSchema.getSiteId()))
//	        .andExpect(jsonPath("$.color").value(seasonalSchema.getColor()))
//	        .andExpect(jsonPath("$.icon").value(seasonalSchema.getIcon()))
//	        .andExpect(jsonPath("$.tag_text").value(seasonalSchema.getTagText()))
//	        .andExpect(jsonPath("$.timer").value(false))
//	        .andExpect(jsonPath("$.end_date").value(format(seasonalSchema.getEndDate(), DateUtils.PATTERN_YYYY_MM_DD_T_HH_MM_SS)))
//	        .andExpect(jsonPath("$.start_date").value(format(seasonalSchema.getStartDate(), DateUtils.PATTERN_YYYY_MM_DD_T_HH_MM_SS)));
  }



	@Test
	  public void deleteProductTest() throws Exception {
	    when(wishListService.deleteProduct(anyLong(), anyLong())).thenReturn(1L);

	    mockMvc.perform(delete("/client/product")
	        .param("idClient", "1")
			.param("idProduct", "10")
            .contentType(MediaType.APPLICATION_JSON))
	        .andDo(print())
            .andExpect(status().isOk());

	    verify(wishListService).deleteProduct(1L, 10L);

	}

  @Test
  public void deleteTest() throws Exception {
    when(wishListService.delete(anyLong())).thenReturn(1L);

    mockMvc.perform(delete("/client")
            .param("id", "1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(wishListService).delete("1");

  }

	@Test
	public void listAllTest() throws Exception {
		ClientDto clientDto = getClientDto();

		when(wishListService.listFull()).thenReturn(List.of(clientDto));

		mockMvc.perform(get("/client")
						.contentType(MediaType.APPLICATION_JSON)
						.param("page","1")
						.param("searchType", "FULL")
						.param("idClient", "1"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$[0].idClient", is("1")).value(clientDto.getIdClient()));
	}

	@Test
	public void findByIdTest() throws Exception {
		ClientDto clientDto = getClientDto();

		when(wishListService.findById(anyString())).thenReturn(clientDto);

		mockMvc.perform(get("/client/id")
						.contentType(MediaType.APPLICATION_JSON)
						.param("id","1"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.idClient", is("1")).value(clientDto.getIdClient()));
	}

	@Test
	public void findProductByIdProductTest() throws Exception {
		Product product = getProduct();

		when(wishListService.findProductByIdProduct(anyLong(), anyLong())).thenReturn(product);

		mockMvc.perform(get("/client/find/keys-client-product")
						.contentType(MediaType.APPLICATION_JSON)
						.param("idClient", "1")
						.param("idProduct", "10"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.nome", is("1")).value(product.getNome()));
	}

	private static String convertObjectToJsonString(Object object) throws IOException {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	    return mapper.writeValueAsString(object);
	}

	private static Product getProduct() {
		Product Product = new Product();
		Product.setIdProduct(10L);
		Product.setNome("PS5");
		Product.setDescricao("Playstation 5");
		Product.setPerco(4000.0);
		return Product;
	}

	private static ClientDto getClientDto() {
		ClientDto clientDto = new ClientDto();

		clientDto.setIdClient(1L);
		clientDto.setNome("Daniel Moura");
		clientDto.setUsuario("DANIEL.RMOURA");
		clientDto.setUsuarioReg("DANIEL.RMOURA");
		return clientDto;
	}

}
