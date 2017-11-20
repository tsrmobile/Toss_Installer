package th.co.thiensurat.toss_installer.api.request;

import java.util.List;

/**
 * Created by teerayut.k on 10/17/2017.
 */

public class RequestAuth {

    private List<authenBody> body;

    public List<authenBody> getBody() {
        return body;
    }

    public RequestAuth setBody(List<authenBody> body) {
        this.body = body;
        return this;
    }

    public static class authenBody {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public authenBody setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public authenBody setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
