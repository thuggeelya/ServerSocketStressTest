
public class ServerProxy implements ClientData {

    private Integer[] usersCash = null;
    private ClientData clientData = new Server();

    @Override
    public Integer[] setClientData(){
        if (usersCash == null) {
            usersCash = clientData.setClientData();
        }
        return usersCash;
    }

    public void clear() {
        usersCash = null;
    }
}