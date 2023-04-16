package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;


    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {

        List<Beer> beerList;

        if(StringUtils.hasText(beerName) && beerStyle == null){
            beerList = listBeersByName(beerName);
        }
        else if(!StringUtils.hasText(beerName) && beerStyle != null){
            beerList = listBeersByStyle(beerStyle);
        }
        else {
            beerList = beerRepository.findAll();
        }

        return beerList.stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    private List<Beer> listBeersByStyle(BeerStyle beerStyle) {
        return beerRepository.findAllByBeerStyle(beerStyle);
    }

    public List<Beer> listBeersByName(String beerName){
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBear(BeerDTO beer) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            atomicReference.set(Optional.of(
                    beerMapper.beerToBeerDto(
                            beerRepository.save(foundBeer)
                    )
            ));
        },() ->{
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID beerId) {
        if(beerRepository.existsById(beerId)){
            beerRepository.deleteById(beerId);
            return true;
        }

        return false;
    }

    @Override
    public Optional<BeerDTO> patchBearById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer ->{
            foundBeer.setBeerName((beer.getBeerName() != null)? beer.getBeerName() : foundBeer.getBeerName());
            foundBeer.setBeerStyle((beer.getBeerStyle() != null)? beer.getBeerStyle() : foundBeer.getBeerStyle());
            foundBeer.setUpc((beer.getUpc() != null)? beer.getUpc() : foundBeer.getUpc());
            foundBeer.setQuantityOnHand((beer.getQuantityOnHand() != null)? beer.getQuantityOnHand() : foundBeer.getQuantityOnHand());
            foundBeer.setUpdateDate(LocalDateTime.now());
            atomicReference.set(
                    Optional.of(beerMapper.beerToBeerDto(beerRepository.save(foundBeer)))
            );
        },()->{
            atomicReference.set(
                    Optional.empty()
            );
        });

        return atomicReference.get();
    }
}
