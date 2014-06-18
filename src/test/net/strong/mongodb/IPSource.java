package test.net.strong.mongodb;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class IPSource {
	@Id private String start_ip;

	private String end_ip;

	private String country;
	private String city;


	public String getStart_ip() {
		return start_ip;
	}
	public void setStart_ip(String startIp) {
		start_ip = startIp;
	}
	public String getEnd_ip() {
		return end_ip;
	}
	public void setEnd_ip(String endIp) {
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
