
package tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期处理类。 （来自网络资源）
 */
public class DateDeal {

	/**
	 * 将当前日期返回"yyyy-MM-dd"的字符串表现形式。
	 * 
	 * @return 返回当前日期的"yyyy-MM-dd"的字符串表现形式。
	 */
	public static String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}

	/**
	 * 将当前日期返回"yyyy年MM月dd日 HH:mm:ss"的字符串表现形式。
	 * 
	 * @return 返回当前日期的"yyyy年MM月dd日 HH:mm:ss"的字符串表现形式。
	 */
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy'年'MM'月'dd'日' HH:mm:ss");
		return df.format(new Date());
	}

	/**
	 * 将Date的日期返回"yyyy-MM-dd HH:mm:ss"的字符串表现形式。
	 * 
	 * @param date
	 *            Date对象。
	 * @return 返回"yyyy-MM-dd HH:mm:ss"的字符串表现形式。
	 */
	public static String getDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	/**
	 * 根据当前日期返回不同形式的字符串形式。
	 * 
	 * @param date
	 *            Date对象。
	 * @return 如果与当前时间所在年月日相同，则返回"HH:mm:ss"形式，否则返回"yyyy-MM-dd HH:mm:ss"。
	 */
	public static String getDate2(Date date) {
		GregorianCalendar g1 = new GregorianCalendar();
		g1.setTime(date);
		GregorianCalendar g2 = new GregorianCalendar();
		g2.setTime(new Date());
		SimpleDateFormat df = null;
		if (g1.get(Calendar.YEAR) == g2.get(Calendar.YEAR)
				&& g1.get(Calendar.MONTH) == g2.get(Calendar.MONTH)
				&& g1.get(Calendar.DAY_OF_MONTH) == g2
						.get(Calendar.DAY_OF_MONTH))
			df = new SimpleDateFormat("HH:mm:ss");
		else
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
}
