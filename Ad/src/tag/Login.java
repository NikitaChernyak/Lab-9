package tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import entity.User;
import entity.UserList;
import java.io.IOException;

public class Login extends SimpleTagSupport {
	
	private String login;
	private String password;

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void doTag() throws JspException, IOException {
		String errorMessage = null;
		// Извлечь из контекста приложения общий список пользователей
		UserList userList = (UserList) getJspContext().getAttribute("users", PageContext.APPLICATION_SCOPE);
		if (login == null || login.equals("")) {
			errorMessage = "Логин не может быть пустым!";
		} else {
			User user = userList.findUser(login);
			if (user == null || !user.getPassword().equals(password)) {
				errorMessage = "Такой пользователь не существует или указанная комбинация логин/пароль неверна!";
			} else {
				// Логин и пароль верны, аутентифицировать пользователя сохранив user в сессии
				getJspContext().setAttribute("authUser", user, PageContext.SESSION_SCOPE);
			}
		}
		// Сохранить описание ошибки (текст или null) в сессии
		getJspContext().setAttribute("errorMessage", errorMessage, PageContext.SESSION_SCOPE);
	}
}