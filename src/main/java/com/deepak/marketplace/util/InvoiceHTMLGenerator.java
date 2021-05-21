package com.deepak.marketplace.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import com.deepak.marketplace.model.Address;
import com.deepak.marketplace.model.CartItem;

public class InvoiceHTMLGenerator {
	
	private static String generateInvoiceHeading(String heading) {
		String invoiceHeadingTable = String.format("<table class=\"invoice_heading\">\r\n"
				+ "      <tr>\r\n"
				+ "        <td class=\"invoice_heading___title\">%s</td>\r\n"
				+ "      </tr>\r\n"
				+ "    </table>", heading);
		return invoiceHeadingTable;
	}
	
	private static String generateInvoiceDateAndIdHTML(String invoiceDate,String invoiceID) {
		String invoiceDateIdHTML = "<td width=\"50%\">\r\n";
				
		invoiceDateIdHTML += String.format("<section class=\"invoice_date_id\">\r\n"
				+ "            <p class=\"invoice_date\" >%s</p>\r\n"
				+ "            <p class=\"invoice_id\">%s</p>\r\n"
				+ "          </section>\r\n"
				+ "        </td>",invoiceDate,invoiceID);
		return invoiceDateIdHTML;
	}
	
	private static String generateClientAddressHTML(Address clientAddress) {
		return generateAddressHTML(clientAddress, "CLIENT", "client_address");
	}
	
	private static String generateClientAddressAndInvoiceDateIDHTML(Address clientAddress,String invoiceDate,String invoiceID) {
		String addressInvoiceMetadataHTML = "<table class=\"client_address_invoice_metadata_table\">\r\n"
				+ "      <tr>";
		addressInvoiceMetadataHTML += generateClientAddressHTML(clientAddress);
		addressInvoiceMetadataHTML += generateInvoiceDateAndIdHTML(invoiceDate, invoiceID);
		addressInvoiceMetadataHTML += "</tr>\r\n"
				+ "    </table>";
		
		return addressInvoiceMetadataHTML;
	}
	
	private static String generateAddressHTML(Address address,String heading,String htmlClass) {

		System.out.println(heading+htmlClass);
		String addressHTMLString = "<td width=\"50%\">\r\n"
				+ String.format("<section class=\"%s\">\r\n"
				+ "            <p class=\"address_title\">%s</p>\r\n"
				+ "            <p>%s</p>\r\n"
				+ "            <p>%s</p>\r\n"
				+ "            <p>%s</p>\r\n"
				+ "            <p>%s</p>\r\n"
				+ "            <p>%s</p>\r\n"
				+ "          </section>\r\n"
				+ "        </td>", htmlClass,heading,address.getContactName(),address.getCompanyName(),address.getCompanyAddress(),address.getPhoneNumber(),address.getEmailAddress());
		return addressHTMLString;
	}
	
	private static String generateShippingBillingAddressHTML(Address shippingAddress,Address billingAddress) {
		String billingShippingAddressString = "<table class=\"bill_ship_address_table\">\r\n"
				+ "      <tr>";
		
		billingShippingAddressString += generateAddressHTML(billingAddress,"BILL TO","bill_to_address");
		billingShippingAddressString += generateAddressHTML(shippingAddress,"SHIP TO","ship_to_address");
		billingShippingAddressString += "</tr>\r\n"
				+ "    </table>";
		return billingShippingAddressString;
	}
	
	
	private static String generateInvoiceGoodsTableHTML(Vector<CartItem> cartItems) {

		String goodsHTMLString = "<table class=\"invoice_goods_table\">\r\n" + "      <!-- invoice items header -->\r\n"
				+ "      <tr>\r\n" + "        <th width=\"50%\" class=\"invoiceitems_header\">Product</th>\r\n"
				+ "        <th width=\"10%\" class=\"invoiceitems_header\">Quantity</th>\r\n"
				+ "        <th width=\"20%\" class=\"invoiceitems_header\">Unit Price</th>\r\n"
				+ "        <th width=\"20%\" class=\"invoiceitems_header\">Total</th>\r\n" + "</tr>";

		for (CartItem cartItem : cartItems) {
			goodsHTMLString +=
					"<tr>\r\n" + "     <td width=\"50%\" class=\"invoiceitems_value\">"+cartItem.getName()+"</td>\r\n"
							+ "        <td width=\"10%\" class=\"invoiceitems_value\">"+cartItem.getQuantity()+"</td>\r\n"
							+ "        <td width=\"20%\" class=\"invoiceitems_value\">"+cartItem.getPrice()+"</td>\r\n"
							+ "        <td width=\"20%\" class=\"invoiceitems_value\">"+cartItem.getPrice() * cartItem.getQuantity()+"</td>\r\n" + "</tr>";
		}
		
		goodsHTMLString += "</table>";

		return goodsHTMLString;
	}
	
	
//	table row : tax rate,discount
	private static String generateAddlInvoiceDataRow(String heading,double value) {
		
		String strValue = String.format("%.2f", value);

		String addlDataRow = ("<tr>\r\n"
				+ "        <td width=\"80%\" class=\"invoiceitems_addl_header\">"+heading+"</td>\r\n"
				+ "        <td width=\"20%\" class=\"invoiceitems_value\">"+strValue+"</td>\r\n"
				+ "      </tr>");
		
		return addlDataRow;

	}
	
	
	private static String generateAddlInvoiceDataTableHTML(Map<String,Double> totalsMap) {
		
		double subTotal = totalsMap.get("subTotal");
		double tax = totalsMap.get("tax");
		double shipping = totalsMap.get("shipping");
		double grandTotal = totalsMap.get("grandTotal");

		
		String addlInvoiceDataString = "<table class=\"invoice_addl_table\">";
		addlInvoiceDataString += generateAddlInvoiceDataRow("Sub Total",subTotal);
		addlInvoiceDataString += generateAddlInvoiceDataRow("TAX RATE",5);
		addlInvoiceDataString += generateAddlInvoiceDataRow("Total Tax",tax);
		addlInvoiceDataString += generateAddlInvoiceDataRow("Shipping",shipping);
		addlInvoiceDataString += generateAddlInvoiceDataRow("GrandTotal", grandTotal);
		addlInvoiceDataString += "</table>";

		return addlInvoiceDataString;
	}

	
//	TODO:need to change later
	public static String generateInvoiceHTML(Vector<CartItem> cartItems,String cssPath,Map<String,Double> totalsMap, Address billingAddress, Address shippingAddress) {
		;
		String invoiceHeadingHTML = String.format("<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "  <head>\r\n"
				+ "    <link rel=\"stylesheet\" href=\"%s\" />\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
				+ "  </head>\r\n"
				+ "  <body>", cssPath);

        String frescoLocation = "30 --//, Ramnagarxroads, Hyderabad, Pin : 50048";
        Address frescoAddress = new Address("Tammali Deepak","Fresco",frescoLocation,"04027645559","deepak@fresco.com");
		

		String invoiceHeading = generateInvoiceHeading("INVOICE");

        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String invoiceCreatedAt = simpleDateFormat.format(new Date(System.currentTimeMillis()));


		String clientAddressInvoiceMetadata = generateClientAddressAndInvoiceDateIDHTML(frescoAddress,invoiceCreatedAt, "14135325213");
		String shippingAndBillingAddress = generateShippingBillingAddressHTML(shippingAddress, billingAddress);
		String invoiceGoods = generateInvoiceGoodsTableHTML(cartItems);
		String invoiceAddlData = generateAddlInvoiceDataTableHTML(totalsMap);
		
		String invoiceBodyHTML = invoiceHeading
										+ clientAddressInvoiceMetadata
										+ shippingAndBillingAddress
										+ invoiceGoods
										+ invoiceAddlData;
		
		String invoiceFooterHTML = "</body>\r\n"
				+ "</html>";
		
		return (invoiceHeadingHTML + invoiceBodyHTML + invoiceFooterHTML);
	}
	
}
