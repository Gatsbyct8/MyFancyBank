public class Manager extends Person
{
    private String passcode;
    private String managerID;

    Manager(Name name, Address address, String phoneNumber, String managerID, String passcode)
    {
        super(name, address, phoneNumber);
        this.managerID = managerID;
        this.passcode = passcode;
    }

    public String getManagerID()
    {
        return managerID;
    }
    public boolean add();
    public boolean update();
    public boolean delete();
}
