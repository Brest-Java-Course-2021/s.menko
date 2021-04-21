package com.epam.brest.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;




@Entity
@Table(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@NotNull(message="Name is required field!")
	@Column(name="name")
	private String name;
	
	
	@Column(name="price")
	private double price;
	
	@Column(name="availability")
	private int availability;
	
	@NotNull(message="Brand is required field!")
	@Column(name="brand")
	private String brand;
	
	@Column(name="image")
	private String image;
	
	@NotNull(message="Description is required field!")
	@Column(name="description")
	private String description;
	
	@Column(name="is_new")
	private int is_new;
	
	@Column(name="status")
	private int status;
	
	@Column(name="category_id",unique=false,updatable=true)
	private int category_id;
	
	
	public Product() {
		
	}
	
	

	public Product(String name, double price, int availability, String brand,
	String description, int category_id,String image) {
		this.name = name;
		this.price = price;
		this.availability = availability;
		this.brand = brand;
		this.description = description;
		this.category_id = category_id;
		this.image=image;
	}



	@Column(name="code")
	private int code;
	
	/*@ManyToMany(fetch=FetchType.EAGER,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE,
			 CascadeType.DETACH})
	@JoinTable(
			name="OrderDetail",
			joinColumns= @JoinColumn(name="product_id"),
			inverseJoinColumns= @JoinColumn(name="order_id"))
	private List<Order> orders;
	
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}*/

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIs_new() {
		return is_new;
	}

	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + availability;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + category_id;
		result = prime * result + code;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + is_new;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + status;
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (availability != other.availability)
			return false;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (category_id != other.category_id)
			return false;
		if (code != other.code)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (is_new != other.is_new)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	
	
	
}
