package com.zqlim.spring.Service;

import com.zqlim.spring.Exception.ResourceNotFoundException;
import com.zqlim.spring.Model.Car;
import com.zqlim.spring.Model.Client;
import com.zqlim.spring.Repo.CarRepo;
import com.zqlim.spring.Repo.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private RestTemplate restTemplate;

    //3rd Party API
    private static final String apiUrl = "https://jsonplaceholder.typicode.com/albums";


    public List<Client> getAllClient(){
       return clientRepo.findAll();
    }

    public ResponseEntity<Client> getClientById(Long id) throws ResourceNotFoundException{
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No client with Id: " + id));
        return ResponseEntity.ok().body(client);
    }

    public ResponseEntity<Client> getClientByName(String name) throws ResourceNotFoundException{
        Client client = clientRepo.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("No client with name: " + name));

        return ResponseEntity.ok().body(client);
    }

    public Page<Client> getClientWithPaging(int offset, int pageSize){
        return clientRepo.findAll(PageRequest.of(offset,pageSize));
    }

    public Page<Client> getClientWithPaging10(int offset){
        return clientRepo.findAll(PageRequest.of(offset,10));
    }

    public List<Car> getAllCars(){
        return carRepo.findAll();
    }

    public ResponseEntity<String> saveClient(Client client){
        clientRepo.save(client);
        return ResponseEntity.ok("Client " + client.getName() + " saved.");
    }

    public ResponseEntity<String> updateClient(Long id, Client client) throws ResourceNotFoundException{
        Client updateClient = clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No client with id: " + id));

        if(client.getAge() != null)
            updateClient.setAge(client.getAge());
        if(StringUtils.hasText(client.getName()))
            updateClient.setName(client.getName());
        if(StringUtils.hasText(client.getGender()))
            updateClient.setGender(client.getGender());
        clientRepo.save(updateClient);

        return ResponseEntity.ok("Client " + updateClient.getName() + " updated.");
    }

    public ResponseEntity<String> saveClientCar(Long id, Car car) throws ResourceNotFoundException{
        Client client = clientRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No client with id: " + id));
        client.setCarCount(client.getCarCount() + 1);

        clientRepo.save(client);

        Car newCar = new Car();
        newCar.setBrand(car.getBrand());
        newCar.setModel(car.getModel());
        newCar.setClient(client);

        carRepo.save(newCar);

        return ResponseEntity.ok("Car " + car.getBrand() + " " + car.getModel() + " saved for " + client.getName() + ".");
    }

    public ResponseEntity<String> deleteClientCar(Long id, Car carToDelete) throws ResourceNotFoundException{
        Client client = clientRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No client with name: " + id));
        List<Car> cars = carRepo.findByBrandAndModelAndClient(carToDelete.getBrand(), carToDelete.getModel(), client);

        if(cars.isEmpty()){
            throw new ResourceNotFoundException("Car does not exist");
        }
        else{
            client.setCarCount(client.getCarCount() - cars.size());
            clientRepo.save(client);

            for (Car car : cars) {
                Car deletedCar = new Car();
                deletedCar.setId(car.getId());

                carRepo.delete(deletedCar);
            }
        }

       return ResponseEntity.ok("Car(s) deleted for " + client.getName() + ".");
    }

    public List<?> getAllAlbums(){
        return this.restTemplate.getForObject(apiUrl,List.class);
    }
}
