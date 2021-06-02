package goa.systems.qrcode;

public class Transfer {

	private String bic;
	private String recipientname;
	private String iban;
	private String sum;
	private String usage;

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getRecipientname() {
		return recipientname;
	}

	public void setRecipientname(String recipientname) {
		this.recipientname = recipientname;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

}
