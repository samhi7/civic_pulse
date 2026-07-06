class Employee {
    String name;
    int id;

    Employee(String n, int i) {
        name = n;
        id = i;
    }

    void display() {
        System.out.println(name);
        System.out.println(id);
    }
}

public class Const {
    public static void main(String[] args) {
        Employee e = new Employee("Rahul", 201);

        e.display();
    }
}