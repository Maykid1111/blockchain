package org.fisco.bcos.asset.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

import org.fisco.bcos.asset.contract.Test_v1;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class Web3Contoller {
	private Web3j web3j;
	private Credentials credentials;
	private static BigInteger gasPrice = new BigInteger("40000000");
	private static BigInteger gasLimit = new BigInteger("40000000");
	
	public void setWeb3j(Web3j web3j) {
		this.web3j = web3j;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	public String change(String string) {
		String string_=new String();
		Boolean state=false;
		if(string=="") 
			return "";
		for(int i=2;i<string.length();i++) {	
			if(state==false) {
				if(string.charAt(i)!='0') {
					state=true;
					string_+=string.charAt(i);
				}
			} 
			else {
				string_+=string.charAt(i);
			}
		}
		return string_;
	}
	
	public String tostring(String string) {
		int x;
		string=change(string);
		String string_ = "0123456789abcdef";
		char[] chars = string.toCharArray();
		byte[] bytes = new byte[string.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			x = string_.indexOf(chars[2 * i]) * 16;
			x += string_.indexOf(chars[2 * i + 1]);
			bytes[i] = (byte) (x & 0xff);
		}
		return new String(bytes);
	}

	public void recordAddress(String address) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.setProperty("address", address);
		final Resource contractResource = new ClassPathResource("contract.properties");
		FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
		properties.store(fileOutputStream, "contract address");
	}

	public String loadAddress() throws Exception {
		
		Properties properties = new Properties();
		final Resource contractResource = new ClassPathResource("contract.properties");
		properties.load(contractResource.getInputStream());

		String contractAddress = properties.getProperty("address");
		if (contractAddress == null || contractAddress.trim().equals("")) {
			throw new Exception(" load Asset contract address failed, please deploy it first. ");
		}
		return contractAddress;
	}

	
	
	public void init() throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext applictioncontext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		Service service = applictioncontext.getBean(Service.class);
		service.run();

		ChannelEthereumService channelEthereumService = new ChannelEthereumService();
		channelEthereumService.setChannelService(service);
		Web3j web3j = Web3j.build(channelEthereumService, 1);
		setWeb3j(web3j);

		Credentials credentials = Credentials.create(Keys.createEcKeyPair());
		setCredentials(credentials);	
	}

	
	public void deployAsset() {
		try {
			Test_v1 asset = Test_v1.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
			System.out.println(" deploy success, contract address is " + asset.getContractAddress());
			recordAddress(asset.getContractAddress());
		} catch (Exception e) {
			System.out.println(" deploy failed, error message is  " + e.getMessage());
		}
	}

	
	
	public void payback(String user) {
		try {
			String contractAddress = loadAddress();
			Test_v1 asset = Test_v1.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			asset.huanqian(user).send();
		} catch(Exception e) {
			System.out.println("error: "+e.getMessage());
		}
	}
	
	public String getUserBillMoney(String user) {
		try {
			String string = loadAddress();
			Test_v1 asset = Test_v1.load(string, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			Tuple2<BigInteger,BigInteger> result = asset.get_bills_money(user).send();
			System.out.println("money: ");
			if(result.getValue1().compareTo(new BigInteger("0"))==0){
				return new String("-"+result.getValue2());
			}
			 System.out.println("outmoney: "+result.getValue2());
			 return result.getValue2().toString();
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
		}
		return "0";
	}
	
	public String getUserBillFromUser(String user) {
		try {
			String contractAddress = loadAddress();
			Test_v1 asset = Test_v1.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			 TransactionReceipt res = asset.get_bills_from(user).send();
			
			 System.out.println("outuser: "+tostring(res.getOutput()));
			 return tostring(res.getOutput());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
		
	public void tranBillsFromToNumber(String from,String to,BigInteger number) {
		try {
			String contractAddress = loadAddress();
			Test_v1 asset = Test_v1.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			asset.tran_bills(from, to, number).send();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}