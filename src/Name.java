public class Name
{
    private String lastName;
    private String firstName;

    Name(String lastName, String firstName)
    {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    @Override
    public String toString()
    {
        String totalName = firstName + lastName;
        return totalName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
