package example;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import java.util.List;

public class DogResource implements Service {

    private DogManager dogManager = new DogManager();

    @Override
    public void update(Routing.Rules rules) {
        rules
                .get("/", this::dogs)
                .get("/{id}", this::dogsById);
    }

    private void dogsById(ServerRequest serverRequest, ServerResponse serverResponse) {
        //get the dog with the given id
        String id = serverRequest.path().param("id");
        Dog dog = dogManager.get(id);
        JsonObject jsonObject = from(dog);
        serverResponse.send(jsonObject);
    }

    private void dogs(ServerRequest serverRequest, ServerResponse serverResponse) {
        //get all dogs
        List<Dog> dogs = dogManager.getAll();
        JsonArray jsonArray = from(dogs);
        serverResponse.send(jsonArray);
    }

    private JsonObject from(Dog dog) {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", dog.getId())
                .add("breed", dog.getBreed())
                .add("color", dog.getColor())
                .build();
        return jsonObject;
    }

    private JsonArray from(List<Dog> dogs) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        dogs.forEach(dog -> jsonArrayBuilder.add(from(dog)));
        return jsonArrayBuilder.build();
    }
}