package test.net.strong.mongodb;

import net.strong.mongodb.BaseEntity;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;


@Entity(value="iptables" ,noClassnameStored= true)
public class IP  extends BaseEntity{


	private static final long serialVersionUID = 2785221734096173952L;

	@Indexed(value=IndexDirection.ASC, name="upc", unique=true, dropDups=true)
	private int start_ip;

	private int end_ip;

	private String country;
	private String city;
	public int getStart_ip() {
		return start_ip;
	}
	public void setStart_ip(int startIp) {
		start_ip = startIp;
	}
	public int getEnd_ip() {
		return end_ip;
	}
	public void setEnd_ip(int endIp) {
		end_ip = endIp;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}


}
