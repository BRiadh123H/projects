package cabinet.model;

public class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int remainingMoney;
    private int whatsPaid;

    public Patient(int id, String firstName, String lastName, String phone, String email, int remainingMoney, int whatsPaid) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.remainingMoney = remainingMoney;
        this.whatsPaid = whatsPaid;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getRemainingMoney() { return remainingMoney; }
    public void setRemainingMoney(int remainingMoney) { this.remainingMoney = remainingMoney; }

    public int getWhatsPaid() { return whatsPaid; }
    public void setWhatsPaid(int whatsPaid) { this.whatsPaid = whatsPaid; }
}
