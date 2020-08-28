package task2.client;

public interface AuthorizationChecker {
    boolean checkAuthorization(String login, String password);
}
