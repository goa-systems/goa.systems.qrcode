package goa.systems.qrcode;

import goa.systems.commons.validity.Validity;
import goa.systems.qrcode.exceptions.WrongDataException;

/**
 * This class represents a bank transfer with the required data to create a QR
 * code.
 * 
 * Hint: Set either reference or text. Not both.
 * 
 * @author ago
 * @since 2021-06-05
 */
public class Transfer {

	private String bic;
	private String creditor;
	private String iban;
	private String amount;
	private String reason;
	private String reference;
	private String text;
	private String message;

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getCreditor() {
		return creditor;
	}

	public void setCreditor(String creditor) {
		this.creditor = creditor;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTemplate() {
		//@formatter:off
		return "BCD\n"
			+ "001\n"
			+ "1\n"
			+ "SCT\n"
			+ "%s\n"
			+ "%s\n"
			+ "%s\n"
			+ "%s\n"
			+ "%s\n"
			+ "%s\n"
			+ "%s\n"
			+ "%s";
		//@formatter:on
	}

	public String toQRContent() throws WrongDataException {

		if (!Validity.isOnlyOneSet(this.reference, this.text)) {
			throw new WrongDataException("Reference and text are set. Choose only one.");
		}

		//@formatter:off
		return String.format(getTemplate(),
				this.bic == null ? "" : this.bic,
				this.creditor == null ? "" : this.creditor,
				this.iban == null ? "" : this.iban,
				this.amount == null ? "" : this.amount,
				this.reason == null ? "" : this.reason,
				this.reference == null ? "" : this.reference,
				this.text == null ? "" : this.text,
				this.message == null ? "" : this.message
				);
		//@formatter:on
	}

	/**
	 * 
	 * @return Empty template.
	 */
	public String toEmptyQRContent() {
		return String.format(getTemplate(), "", "", "", "", "", "", "", "");
	}
}
