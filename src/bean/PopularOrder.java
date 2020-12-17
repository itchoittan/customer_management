package bean;

import java.io.Serializable;

public class PopularOrder implements Serializable{

	private static final long serialVersionUID = 1L;

	private String order_id;
	private int order_price;
	private int sumprice;
	private int sumquantity;


	public PopularOrder(String order_id, int order_price, int sumprice, int sumquantity) {

		this.order_id = order_id;
		this.order_price = order_price;
		this.sumprice = sumprice;
		this.sumquantity = sumquantity;
	}


	public String getOrder_id() {
		return order_id;
	}

	public int getOrder_price() {
		return order_price;
	}

	public int getSumprice() {
		return sumprice;
	}

	public int getSumquantity() {
		return sumquantity;
	}


}
