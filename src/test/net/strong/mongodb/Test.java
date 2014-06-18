package test.net.strong.mongodb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.strong.mongodb.BaseMongodb;

public class Test extends BaseMongodb{

	protected Test(String host, int port, String database) {
		super(host, port, database);
	}

	public static <T> void main(String[] args) throws Exception{

		if(args == null || args.length<=0)
			throw new IllegalArgumentException("\nargument: add [mongodb/oracle] pathToIPlib.txt;\n          search [mongodb/oracle] ipaddress;");

		String op = args[0];
		String media = args[1];
		String arg = args[2];
		Test t = new Test("127.0.0.1",27017,"my_database");

		if("add".equalsIgnoreCase(op)){
			if("mongodb".equalsIgnoreCase(media)){

				File f = new File(arg);

				try {
					InputStream fi = new FileInputStream(f);
					InputStreamReader in = new InputStreamReader(fi, "utf-8");
					BufferedReader br = new BufferedReader(in);
					String line = null;
					IP ip = new IP();
					long ti = System.currentTimeMillis();
					while((line = br.readLine())!=null){
						String[] lines = line.split(" ");
						int serial = 0;
						String[] fields = new String[4];
						for(int i=0;i<lines.length;i++){
							String tmp = lines[i];
							if(tmp.trim().length()>0){
								if(serial>3){
									fields[3]+=" " + tmp;
								}else{
									fields[serial] = tmp;
									serial ++ ;
								}
							}
						}
						ip.setId(null);
						ip.setStart_ip(calc_ip(fields[0]));
						ip.setEnd_ip(calc_ip(fields[1]));
						ip.setCountry(fields[2]);
						ip.setCity(fields[3]);
						t.save(ip);
						//stmt.addBatch("insert intp iptables set start_ip='"+fields[0]+"',end_ip='"+fields[1]+"',provice='"+fields[2]+"',city='"+fields[3]+"'");
						fields=null;
					}

					System.out.println((System.currentTimeMillis()-ti));
				} catch (Exception e) {
					e.printStackTrace();
				}



			}else{

			}
		}else{
			if("mongodb".equalsIgnoreCase(media)){

			}else{

			}
		}


		//String ips = "219.128.51.229";
		//int ip_int = calc_ip(ips);

		//MongoDAO<IP> ipDAO = t.getDAO(IP.class);

		//DAO<IP, String> ipdao = t.getDAO(IP.class);
		//ipdao.get(ip)



		//long ti = System.currentTimeMillis();
		//Key<T> condition = new Key<T>("stars >=",4);
		/*Query<IP> q = t.find(IP.class).filter("start_ip <=", ip_int).filter("end_ip >=",ip_int);
		QueryResults<IP> a = ipdao.find(q);
		for(IP ip : a){
			System.out.println(ip.getCity());
			System.out.println(ip.getCountry());
		}*/
		/*
		for(IP ip : q){
			System.out.println(ip.getCity());
			System.out.println(ip.getCountry());
		}
		*/
		//System.out.println((System.currentTimeMillis()-ti));





		/*
	Hotel hotel = new Hotel();
	hotel.setName("My Hotel");
	hotel.setStars(4);
	hotel.setId(new ObjectId("4c7e7094cd966e02b02d174e"));

	Address address = new Address();
	address.setStreet("123 Some street");
	address.setCity("Some city");
	address.setPostCode("123 456");
	address.setCountry("Some country");
	hotel.setAddress(address);
	Key<Hotel> ht = t.save(hotel);

	System.out.println(ht.getId());
	hotel.setId(new ObjectId(ht.getId().toString()));
	hotel = (Hotel) t.get(hotel);
	System.out.print(hotel);

		 */
	}

	private static int calc_ip(String ip){
		if(ip==null)return 0;
		String[] ips = ip.split("\\.");
		return Integer.parseInt(ips[0])*256*256*256+Integer.parseInt(ips[1])*256*256+Integer.parseInt(ips[2])*256+Integer.parseInt(ips[3]);
	}}
