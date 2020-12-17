package bean;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable{


	private static final long serialVersionUID = 1L;

	private int customers_id;
	private String name;
	private String mobilephone;
	private String phone;
	private Date birthday;
	private int age;
	private String photo;
	private String like;
	private String hate;
	private String text;
	private int numbervisit;




	public Customer(int customers_id, String name, String mobilephone, String phone, Date birthday, int age,
			String photo, String like, String hate, String text, int numbervisit) {
		this.customers_id = customers_id;
		this.name = name;
		this.mobilephone = mobilephone;
		this.phone = phone;
		this.birthday = birthday;
		this.age = age;
		this.photo = photo;
		this.like = like;
		this.hate = hate;
		this.text = text;
		this.numbervisit = numbervisit;
	}


	public int getCustomers_id() {
		return customers_id;
	}

	public String getName() {
		return name;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public String getPhone() {
		return phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public int getAge() {
		return age;
	}

	public String getPhoto() {
		return photo;
	}

	public String getLike() {
		return like;
	}

	public String getHate() {
		return hate;
	}

	public String getText() {
		return text;
	}

	public int getNumbervisit() {
		return numbervisit;
	}

}