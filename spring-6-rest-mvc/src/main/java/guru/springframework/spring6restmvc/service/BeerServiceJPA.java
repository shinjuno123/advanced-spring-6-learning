package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.data.domain.Pageable;
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

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                                   Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Beer> beerPage;

        if(StringUtils.hasText(beerName) && beerStyle == null){
            beerPage = listBeersByName(beerName, pageRequest);
        }
        else if(!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        } else if(StringUtils.hasText(beerName) && beerStyle != null){
            beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
        }
        else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if(showInventory != null && !showInventory){
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerToBeerDto);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if(pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber - 1;
        } else {
          queryPageNumber = DEFAULT_PAGE;
        }

        if(pageSize == null){
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if(pageSize > 1000){
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }


    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
    }

    private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    public Page<Beer> listBeersByName(String beerName, Pageable pageable){
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
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
        return Optional.of(beerMapper.beerToBeerDto(
                beerRepository.save(beerMapper.beerDtoToBeer(beer))
        ));

//        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
//
//        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
//            foundBeer.setBeerName(beer.getBeerName());
//            foundBeer.setBeerStyle(beer.getBeerStyle());
//            foundBeer.setUpc(beer.getUpc());
//            foundBeer.setPrice(beer.getPrice());
//            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
//            foundBeer.setVersion(beer.getVersion());
//            atomicReference.set(Optional.of(
//                    beerMapper.beerToBeerDto(
//                            beerRepository.save(foundBeer)
//                    )
//            ));
//        },() ->{
//            atomicReference.set(Optional.empty());
//        });
//
//        return atomicReference.get();
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
