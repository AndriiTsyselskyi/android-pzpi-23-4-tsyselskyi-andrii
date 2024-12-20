package tsyselskyi.andrii.nure;

public class User {
    Integer id;
    String name;
    Integer age;

    public User(Integer id, String name, Integer age) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }


}