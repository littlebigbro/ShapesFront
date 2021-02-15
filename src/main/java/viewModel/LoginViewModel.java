package viewModel;

import Utils.HttpConnector;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

public class LoginViewModel {
    private String password;
    private String username;
    private boolean isLogin = false;

    @Command
    public void login() {
        if (HttpConnector.authentication(password)) {
            isLogin = true;
            Executions.sendRedirect("/main.zul");
        } else {
            isLogin = false;
            Messagebox.show("Wrong username or password", null, 0, Messagebox.ERROR);
        }
    }

    public boolean isLogin() { return isLogin; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
