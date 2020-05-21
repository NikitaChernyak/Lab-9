package helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.servlet.ServletContext;
import entity.AdList;

public abstract class AdListHelper {
	
	// Логический путь к файлу, в котором хранятся данные об объявлениях
	private static final String ADS_FILENAME = "WEB-INF/ads.dat";
	// Полный путь к файлу, в котором хранятся данные об объявлениях
	private static String ADS_PATH = null;

	// Читает данные объявлениях из файла хранилища и формирует на их основе объект AdList.
	public static AdList readAdList(ServletContext context) {
		try {
			ADS_PATH = context.getRealPath(ADS_FILENAME);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(ADS_PATH));
			return (AdList) in.readObject();
		} catch (Exception e) {
			return new AdList();
		}
	}
	
	// Сохраняет в файле хранилища содержимое списка объявлений
	public static void saveAdList(AdList ads) {
		synchronized (ads) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ADS_PATH));
				out.writeObject(ads);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}