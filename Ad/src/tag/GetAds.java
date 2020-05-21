package tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import entity.Ad;
import entity.AdList;
import entity.User;

public class GetAds extends SimpleTagSupport {
	
	private int id = 0;
	// Поле данных для атрибута range (диапазон объявлений)
	private String range;
	// Поле данных для атрибута sort (основание сортировки)
	private String sort;
	// Поле данных для атрибута dir (направление сортировки)
	private char dir;
	// Поле данных для атрибута var (контейнер результата)
	private String var;

	public void setId(int id) {
		this.id = id;
	}

	public void setRange(String range) {
		this.range = range.toLowerCase();
	}

	public void setSort(String sort) {
		this.sort = sort.toLowerCase();
	}

	public void setDir(char dir) {
		this.dir = Character.toLowerCase(dir);
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void doTag() throws JspException, IOException {
		// Извлечь из контекста приложения общий список объявлений
		final AdList adList = (AdList) getJspContext().getAttribute("ads",
				PageContext.APPLICATION_SCOPE);
		if (id > 0) {
			// Если требуется извлечь данные только 1 объявления
			for (Ad ad : adList.getAds()) {
				if (ad.getId() == id) {
					getJspContext().setAttribute(GetAds.this.var, ad,
							PageContext.PAGE_SCOPE);
				}
			}
		} else {
			// Необходимо построение выборки объявлений
			// Извлечь из сессии bean аутентифицированного пользователя
			final User authUser = (User) getJspContext().getAttribute("authUser", PageContext.SESSION_SCOPE);
			ArrayList<Ad> sortedList = new ArrayList<Ad>();
			for (Ad ad : adList.getAds()) {
				if (!"my".equals(range) || (authUser != null && authUser.getId() == ad.getAuthorId())) {
					sortedList.add(ad);
				}
			}
			
			// Как анонимный класс определить и создать экземпляр компаратора
			// Именно компаратор будет отвечать за сортировку объявлений заданным способом
			Comparator<Ad> comparator = new Comparator<Ad>() {
				public int compare(Ad ad1, Ad ad2) {
					int result;
					// Если выбрана сортировка по дате последнего изменения объявления
					if (GetAds.this.sort != null
							&& GetAds.this.sort.equals("date")) {

						result = ad1.getLastModified().compareTo(
								ad2.getLastModified());
						if (GetAds.this.dir == 'd') {
							result = -result;
						}
					} 
					// Если выбрана сортировка по теме объявления
					else if (GetAds.this.sort != null
							&& GetAds.this.sort.equals("subject")) {
						result = ad1.getSubject().compareTo(ad2.getSubject());
						if (GetAds.this.dir == 'd') {
							result = -result;
						}
					} 
					// Иначе сортируем по автору объявления по значениям поля name автора
					else {
						result = ad1.getAuthor().getName()
								.compareTo(ad2.getAuthor().getName());
						if (GetAds.this.dir == 'd') {
							result = -result;
						}
					}
					return result;
				}
			};
			if (sortedList.size() == 0) {
				sortedList = null;
			} else {
				sortedList.sort(comparator);
			}
			// Сохранить отсортированный список в переменной с именем varName
			// В контексте страницы
			getJspContext().setAttribute(GetAds.this.var, sortedList, PageContext.PAGE_SCOPE);
		}
	}
}