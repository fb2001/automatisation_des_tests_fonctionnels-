package hai702.tp4.model;
public class User {
    private java.lang.Integer Id;

    private java.lang.String Name;

    private java.lang.String Email;

    private java.lang.String Password;

    private java.lang.Integer Age;

    public User(java.lang.Integer Id, java.lang.String Name, java.lang.String Email, java.lang.String Password, java.lang.Integer Age) {
        this.Id = Id;
        this.Name = Name;
        this.Email = Email;
        this.Password = Password;
        this.Age = Age;
    }

    public java.lang.Integer getId() {
        return Id;
    }

    public void setId(java.lang.Integer id) {
        Id = id;
    }

    public java.lang.String getName() {
        return Name;
    }

    public void setName(java.lang.String name) {
        Name = name;
    }

    public java.lang.String getEmail() {
        return Email;
    }

    public void setEmail(java.lang.String email) {
        Email = email;
    }

    public java.lang.Integer getAge() {
        return Age;
    }

    public void setAge(java.lang.Integer age) {
        Age = age;
    }

    public java.lang.String getPassword() {
        return Password;
    }

    public void setPassword(java.lang.String password) {
        Password = password;
    }
}