package task3;

import java.util.Objects;

public interface AuthService {
    Record findRecord(String login, String password);

    String findRecordLogin(String login);

    class Record {
        private long id;
        private String name;
        private String login;
        private String password;

        public Record(long id, String name, String login, String password) {
            this.id = id;
            this.name = name;
            this.login = login;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public void setName(String newName) {
            name = newName;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Record record = (Record) o;
            return id == record.id &&
                    name.equals(record.name) &&
                    login.equals(record.login) &&
                    password.equals(record.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, login, password);
        }

        @Override
        public String toString() {
            return "Record{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", login='" + login + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
