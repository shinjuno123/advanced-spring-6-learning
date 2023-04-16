package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File file) throws FileNotFoundException;
}
