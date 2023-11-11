package example;

public class Dog{
    String id, breed, color;

    public Dog(String a, String b, String c){
        id = a;
        breed = b;
        color = c;
    }

    public String getId(){ return id;}
    public String getBreed(){ return breed;}
    public String getColor(){ return color;}
}