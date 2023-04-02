package guru.springframework.spring6restmvc.controller;


import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity updateBeerPatchById(@PathVariable("beerId") UUID beerId,@RequestBody BeerDTO beer){

        beerService.patchBearById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable UUID beerId){

        beerService.deleteById(beerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId,@RequestBody BeerDTO beer){

        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PostMapping(BEER_PATH)
    public ResponseEntity<BeerDTO> handlePost(@RequestBody BeerDTO beer){
        BeerDTO savedBeer = beerService.saveNewBear(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",BEER_PATH + savedBeer.getId().toString());

        return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
    }

    @GetMapping( BEER_PATH)
    public List<BeerDTO> listBears(){
        return beerService.listBeers();
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        System.out.println("In exception Handler");

        return ResponseEntity.notFound().build();
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId){

        log.debug("Get Beer by Id - in controller - 23");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }


}
