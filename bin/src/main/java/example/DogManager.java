package example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DogManager {

    private List<Dog> lst = new ArrayList<Dog>(){{
        add(new Dog("1", "Grayhound", "Grey"));
        add(new Dog("2", "Puddle", "White"));
        add(new Dog("3", "Lab", "Black"));
    }};

    private ConcurrentMap<String, Dog> inMemoryStore = new ConcurrentHashMap<>();

    public DogManager() {
        for(Iterator<Dog> itr = lst.iterator(); itr.hasNext();){
            Dog dog = itr.next();
            inMemoryStore.put(dog.getId(), dog);
        }
    }

    public Dog get(String id) {
        return inMemoryStore.get(id);
    }

    public List<Dog> getAll(){
        return lst;
    }
}