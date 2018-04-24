package application;

import java.util.Date;

/**  ласс, который ждет окончани€ суток,
 * чтобы восстановить ежедневный лимит
 * добавлен€и файлов 
 * @author Eugene
 */
public class Timer implements Runnable {
	
	private Account account;
	Timer(Account account) {
		this.account = account;
		Thread timer = new Thread(this);
		timer.start();
	}
	
	/** ¬ методе получаютс€ данные об оставшемс€ кол-ве
	 * секунд до наступлени€ нового дн€. ѕоток ждет это врем€*/
	@Override
	public void run() {
		while(true) {
			Date date = new Date();
			int hoursLeft = 23 - date.getHours();
			int minutesLeft = 59 - date.getMinutes();
			int secondsLeft = 60 - date.getSeconds() + minutesLeft * 60 + hoursLeft * 3600;
	    //    System.out.println(hoursLeft + " " + minutesLeft);
			try {
				Thread.sleep(secondsLeft * 1000);
				account.setLimit(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
}
