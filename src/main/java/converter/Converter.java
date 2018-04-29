package converter;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class Converter {
    private String sell;
    private String buy;
    private double value;
    private double howMuch;
    private String wallet;
    private DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public Converter() {
        df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
    }

    private String identifyWallet(String sell) {
        String cash = "";

        switch (sell) {
            case ("QIWI RUB"):
                cash = Wallet.QIWI.toString();
                break;
            case ("WM RUB"):
                cash = Wallet.WM.toString();
                break;
            case ("Yandex RUB"):
                cash = Wallet.YANDEX.toString();
                break;
            case ("Payeer RUB"):
                cash = Wallet.PAYEER.toString();
                break;
            case ("BTC"):
                cash = Wallet.BTC.toString();
                break;
            case ("LTC"):
                cash = Wallet.LTC.toString();
                break;
            case ("BCC"):
                cash = Wallet.BCC.toString();
                break;
            case ("ETH"):
                cash = Wallet.ETH.toString();
                break;

        }
        return cash;
    }

    public String convertValue(String buyString, String sellString) throws Exception {

        wallet = identifyWallet(sellString);

        if (sellString.contains("RUB"))
            sell = "RUB";
        else
            sell = sellString;

        if (buyString.contains("RUB"))
            buy = "RUB";
        else
            buy = buyString;

        String url = "https://min-api.cryptocompare.com/data/price?fsym=" + sell + "&tsyms=" + buy;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String result = response.toString();
        value = Double.parseDouble(result.substring(result.indexOf(":") + 1, result.indexOf("}")));

        //System.out.println(result);

        return df.format(value);
    }

    public String makeAnswer(double count) {
        howMuch = count;
        String answer = "";
        answer += "<b>" + df.format(howMuch) + " " + sell + " = " + df.format(value * howMuch) + " " + buy + "</b>\nВы уверены, что хотите сделать обмен?";
        return answer;
    }

    public String getMessage() {
        String message = "Внесите <b>" + df.format(howMuch) + " " + sell + "</b> на кошелек <b>" + wallet + "</b> и нажмите  «Я ОПЛАТИЛ»";
        return message;
    }
}
