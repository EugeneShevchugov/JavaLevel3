package task3;

public class BasicAuthService implements AuthService {
    private ClientRecord bd;

    public BasicAuthService() {
    }

    @Override
    public Record findRecord(String login, String password) {
        bd = new ClientRecord();
        Record possibleClient = bd.getRecord(login);
        if (possibleClient != null) {
            if (possibleClient.getLogin().equals(login) && possibleClient.getPassword().equals(password))
                return possibleClient;
        }
        return null;
    }

    @Override
    public String findRecordLogin(String login) {
        bd = new ClientRecord();
        Record possibleClient = bd.getRecord(login);
        if (possibleClient.getLogin().equals(login)) return login;
        return null;
    }

}
