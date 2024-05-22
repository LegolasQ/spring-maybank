package com.zqlim.spring.Controller;

import com.zqlim.spring.Exception.ResourceNotFoundException;
import com.zqlim.spring.Model.Car;
import com.zqlim.spring.Model.Client;
import com.zqlim.spring.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping(value="/")
    public String getPage(){
        return "Hello World.";
    }

    //To get all client record
    @Transactional(readOnly = true)
    @GetMapping(value="/clients")
    public List<Client> getClient(){
        return clientService.getAllClient();
    }

    //To get client record by id
    @Transactional(readOnly = true)
    @GetMapping(value = "/clients/id/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException{
        return clientService.getClientById(id);
    }

    //To get client record by name
    @Transactional(readOnly = true)
    @GetMapping(value = "/clients/name/{name}")
    public ResponseEntity<Client> getClientByName(@PathVariable(value = "name") String name)
            throws ResourceNotFoundException {
        return clientService.getClientByName(name);
    }

    //Pagination API whereby user defines the page size
    @Transactional(readOnly = true)
    @GetMapping(value = "/clients/pageWithSize")
    public Page<Client> getClientWithPaging(@RequestParam int num, @RequestParam int size){
        return clientService.getClientWithPaging(num,size);
    }

    //Pagination API whereby page size is defaulted to 10
    @Transactional(readOnly = true)
    @GetMapping(value = "/clients/page")
    public Page<Client> getClientWithPaging10(@RequestParam int num){
        return clientService.getClientWithPaging10(num);
    }

    //To get all car record
    @Transactional(readOnly = true)
    @GetMapping(value = "/cars")
    public List<Car> getCars(){
        return clientService.getAllCars();
    }

    //To save client record
    @Transactional
    @PostMapping(value = "/saveClient", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveClient(@RequestBody Client client){
        return clientService.saveClient(client);
    }

    //To update client record by id
    @Transactional
    @PutMapping(value = "/updateClient/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateClientByName(@PathVariable Long id, @RequestBody Client client) throws ResourceNotFoundException{
        return clientService.updateClient(id, client);
    }

    //To save car record for client by client id and car details
    @Transactional
    @PostMapping(value = "/saveClientCar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveClientCar(@PathVariable Long id, @RequestBody Car car) throws ResourceNotFoundException{
        return clientService.saveClientCar(id, car);
    }

    //To delete car record for client by client id and car details
    @Transactional
    @DeleteMapping(value = "/deleteClientCar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteClientCar(@PathVariable Long id, @RequestBody Car car) throws ResourceNotFoundException{
        return clientService.deleteClientCar(id, car);

    }

    //3rd Party API integration
    @Transactional(readOnly = true)
    @GetMapping(value = "/album")
    public List<?> getAlbum(){
        return clientService.getAllAlbums();
    }
}
