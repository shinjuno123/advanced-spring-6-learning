package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;


    @Test
    void testDeleteBeer() {

        BeerDTO newDTO = BeerDTO.builder()
                .price(new BigDecimal(("10.99")))
                .beerName("Mongo Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newDTO);

        beerClient.deleteBeer(beerDTO.getId());

        assertThrows(HttpClientErrorException.class, ()->{
            beerClient.getBeerById(beerDTO.getId());
        });


    }

    @Test
    void testUpdateBeer() {

        BeerDTO newDTO = BeerDTO.builder()
                .price(new BigDecimal(("10.99")))
                .beerName("Mongo Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newDTO);

        final String newName = "Mango Bobs 3";
        beerDTO.setBeerName(newName);
        BeerDTO updateBeer = beerClient.updateBeer(beerDTO);

        assertEquals(newName, updateBeer.getBeerName());


    }

    @Test
    void testCreateBeer(){

        BeerDTO newDTO = BeerDTO.builder()
                .price(new BigDecimal(("10.99")))
                .beerName("Mongo Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO savedDTO = beerClient.createBeer(newDTO);
        assertNotNull(savedDTO);
    }

    @Test
    void getBeerById(){
        Page<BeerDTO> beerDTOPage = beerClient.listBeers();

        BeerDTO dto = beerDTOPage.getContent().get(0);

        BeerDTO byId = beerClient.getBeerById(dto.getId());

        assertNotNull(byId);
    }
    @Test
    void listBeersNoBeerName() {
        beerClient.listBeers();
    }


    @Test
    void listBeers() {
        beerClient.listBeers("ALE", BeerStyle.LAGER, null, 20, null);
    }
}