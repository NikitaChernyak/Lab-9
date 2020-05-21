package tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import entity.Ad;
import entity.AdList;
import entity.User;
import helper.AdListHelper;

public class DeleteAd extends SimpleTagSupport {
	
	private Ad ad;

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public void doTag() throws JspException, IOException {
		
		String errorMessage = null;
		// Извлечь из контекста приложения общий список объявлений
		AdList adList = (AdList) getJspContext().getAttribute("ads", PageContext.APPLICATION_SCOPE);
		// Извлечь из сессии описание текущего пользователя
		User currentUser = (User) getJspContext().getAttribute("authUser", PageContext.SESSION_SCOPE);
		if (currentUser == null || (ad.getId() > 0 && ad.getAuthorId() != currentUser.getId())) {
			errorMessage = "Вы пытаетесь изменить сообщение, к которому не обладаете правами доступа!";
		}
		if (errorMessage == null) {
			adList.deleteAd(ad);
			AdListHelper.saveAdList(adList);
		}
		// Сохранить описание ошибки (текст или null) в сессии
		getJspContext().setAttribute("errorMessage", errorMessage, PageContext.SESSION_SCOPE);
	}
}