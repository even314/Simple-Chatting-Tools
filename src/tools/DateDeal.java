
package tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ���ڴ����ࡣ ������������Դ��
 */
public class DateDeal {

	/**
	 * ����ǰ���ڷ���"yyyy-MM-dd"���ַ���������ʽ��
	 * 
	 * @return ���ص�ǰ���ڵ�"yyyy-MM-dd"���ַ���������ʽ��
	 */
	public static String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}

	/**
	 * ����ǰ���ڷ���"yyyy��MM��dd�� HH:mm:ss"���ַ���������ʽ��
	 * 
	 * @return ���ص�ǰ���ڵ�"yyyy��MM��dd�� HH:mm:ss"���ַ���������ʽ��
	 */
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy'��'MM'��'dd'��' HH:mm:ss");
		return df.format(new Date());
	}

	/**
	 * ��Date�����ڷ���"yyyy-MM-dd HH:mm:ss"���ַ���������ʽ��
	 * 
	 * @param date
	 *            Date����
	 * @return ����"yyyy-MM-dd HH:mm:ss"���ַ���������ʽ��
	 */
	public static String getDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	/**
	 * ���ݵ�ǰ���ڷ��ز�ͬ��ʽ���ַ�����ʽ��
	 * 
	 * @param date
	 *            Date����
	 * @return ����뵱ǰʱ��������������ͬ���򷵻�"HH:mm:ss"��ʽ�����򷵻�"yyyy-MM-dd HH:mm:ss"��
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