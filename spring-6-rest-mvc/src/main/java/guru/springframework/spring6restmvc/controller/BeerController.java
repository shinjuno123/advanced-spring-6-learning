package guru.springframework.spring6restmvc.controller;


import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity PatchById(@PathVariable("beerId") UUID beerId,@RequestBody BeerDTO beer){

        if(beerService.patchBearById(beerId, beer).isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById( @PathVariable UUID beerId){

        if(!beerService.deleteById(beerId)){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId,@Validated @RequestBody BeerDTO beer){

        if(beerService.updateBeerById(beerId, beer).isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PostMapping(BEER_PATH)
    public ResponseEntity<BeerDTO> handlePost(@Validated @RequestBody BeerDTO beer){
        BeerDTO savedBeer = beerService.saveNewBear(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",BEER_PATH +"/" + savedBeer.getId().toString());

        return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
    }

    @GetMapping( BEER_PATH)
    public List<BeerDTO> listBears(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false) BeerStyle beerStyle){
        return beerService.listBeers(beerName, beerStyle);
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
