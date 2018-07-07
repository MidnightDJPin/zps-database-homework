package 票务管理;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class TicketInterface {

	public static boolean menu(CustomService customService, TicketService ticketService) {
		System.out.println("1.用户注册");
		System.out.println("2.查询班次");
		System.out.println("3.车票订购");
		System.out.println("4.订单查询");
		System.out.println("5.用户退票");
		System.out.println("6.退出系统");
		System.out.print("输入序号以选择下一步操作：");
		String pattern = "[1-6]";
		Scanner input = new Scanner(System.in);
		String select = input.nextLine();
		while (!Pattern.matches(pattern, select)) {
			System.out.print("输入错误，请重新输入：");
			select = input.nextLine();
		}
		int choose = Integer.parseInt(select);
		switch (choose) {
		case 1: {
			customerRegister(customService);
			break;
		}
		case 2: {
			busesQuery(ticketService);
			break;
		}
		case 3: {
			
		}
		case 4: {
			
		}
		case 5: {
			
		}
		case 6: {
			input.close();
			return false;
		}
		}
		input.close();
		return true;
	}
	
	
	public static void customerRegister(CustomService customService) {
		Scanner input = new Scanner(System.in);
		System.out.print("请输入注册用户名（长度不得超过30个字符）：");
		String namePatten = "[\u4e00-\u9fa5\\w]{0,30}";
		String cname = input.nextLine();
		while (!Pattern.matches(namePatten, cname)) {
			System.out.print("输入用户名不合法，请重新输入：");
			cname = input.nextLine();
		}
		System.out.print("请输入注册用手机号码：");
		String phonePatten = "^1[0-9]{10}$";
		String phone = input.nextLine();
		while (!Pattern.matches(phonePatten, phone)) {
			System.out.print("输入手机号码不合法，请重新输入：");
			phone = input.nextLine();
		}
		Customer customer = customService.register(cname, phone);
		if (customer == null)
			System.out.println("用户注册失败！");
		else
			System.out.println("用户注册成功，cid=" + customer.getCid()
			+ " cname=" + customer.getCname() + " phone=" + customer.getPhone());
		input.close();
	}
	
	
	public static int checkTimeStampType(String string) {
		String timeStampPatten = "^(\\d{4}-\\d{2}-\\d{2})";
		if (string == null) return 0;
		else if (!Pattern.matches(timeStampPatten, string)) return 1;
		else {
			string += " 00:00:00";
			Timestamp timestamp = Timestamp.valueOf(string);
			Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
			timestamp.setHours(currentTimeStamp.getHours());
			timestamp.setMinutes(currentTimeStamp.getMinutes() + 5);
			if (timestamp.after(currentTimeStamp)) return 2;
			else return 3;
		}
	}
	public static void printBuses(ArrayList<Bus> buses) {
		if (buses == null)
			System.out.println("没有找到符合查询条件的班次");
		else {
			System.out.println("班次编号\t出发地\t目的地\t出发时间\t余座\t票价\t");
			for (Bus bus : buses) {
				System.out.println(bus.getBid() + "\t" + bus.getOrigin() + "\t" + bus.getDestination()
				+ "\t" + bus.getStart_time().toString().substring(0, 15)
				+ "\t" + bus.getRest_seats() + "\t" + bus.getPrice() + "\t");
			}
		}
	}
	public static void busesQuery(TicketService ticketService) {
		Scanner input = new Scanner(System.in);
		System.out.print("请输入出发地：");
		String origin = input.nextLine();
		System.out.print("请输入出发地：");
		String destination = input.nextLine();
		System.out.print("请输入出发日期（日期格式:yy-MM-dd,若无需求可直接enter跳过）：");
		String start_timeString = input.nextLine();
		while (checkTimeStampType(start_timeString) % 2 == 1) {
			System.out.print("输入格式不正确或输入日期早于当前");
			start_timeString = input.nextLine();
		}
		Timestamp start_time;
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		if (start_timeString == null)
			start_time = Timestamp.valueOf(currentTimeStamp.toString());
		else {
			start_timeString += " 00:00:00";
			start_time = Timestamp.valueOf(start_timeString);
			start_time.setHours(currentTimeStamp.getHours());
			start_time.setMinutes(currentTimeStamp.getMinutes() + 5);
		}
		ArrayList<Bus> buses = ticketService.busesQuery(origin, destination, start_time);
		printBuses(buses);
		input.close();
	}
}
