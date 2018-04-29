package bot;

import converter.Converter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardCreator {

    public void createSellMenu(SendMessage message) {
        message.setText("Выберите валюту, которую вы хотите обменять!");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("QIWI RUB").setCallbackData("Sell=QIWI RUB"));
        rowInline.add(new InlineKeyboardButton().setText("WM RUB").setCallbackData("Sell=WM RUB"));
        rowInline.add(new InlineKeyboardButton().setText("Yandex RUB").setCallbackData("Sell=Yandex RUB"));
        rowInline.add(new InlineKeyboardButton().setText("Payeer  RUB").setCallbackData("Sell=Payeer RUB"));
        rowsInline.add(rowInline);
        rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("BTC").setCallbackData("Sell=BTC"));
        rowInline.add(new InlineKeyboardButton().setText("LTC").setCallbackData("Sell=LTC"));
        rowInline.add(new InlineKeyboardButton().setText("BCC").setCallbackData("Sell=BCC"));
        rowInline.add(new InlineKeyboardButton().setText("ETH").setCallbackData("Sell=ETH"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
    }

    public void createBuyMenu(SendMessage message, String sell) {
        message.setText("На какую валюту Вы хотите обменять <b>" + sell + "</b>?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("QIWI RUB").setCallbackData("Buy=QIWI RUB"));
        rowInline.add(new InlineKeyboardButton().setText("WM RUB").setCallbackData("Buy=WM RUB"));
        rowInline.add(new InlineKeyboardButton().setText("Yandex RUB").setCallbackData("Buy=Yandex RUB"));
        rowInline.add(new InlineKeyboardButton().setText("Payeer RUB").setCallbackData("Buy=Payeer RUB"));
        rowsInline.add(rowInline);
        rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("BTC").setCallbackData("Buy=BTC"));
        rowInline.add(new InlineKeyboardButton().setText("LTC").setCallbackData("Buy=LTC"));
        rowInline.add(new InlineKeyboardButton().setText("BCC").setCallbackData("Buy=BCC"));
        rowInline.add(new InlineKeyboardButton().setText("ETH").setCallbackData("Buy=ETH"));
        rowsInline.add(rowInline);
        rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Назад").setCallbackData("back=1"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

    }

    public boolean buyCreate(SendMessage message, String sell, String buy, Boolean isTransaction, Converter converter) {
        if (buy.equals(sell)) message.setText("Вы выбрали одну и ту же валюту для обмена. Выберите разные!");
        else {
            try {
                message.setText("Ваш выбор : Обмен <b>" + sell + "</b> на <b>" + buy + "</b> по курсу <b>1 " + sell + " = " + converter.convertValue(buy, sell) + " " + buy + "</b>\n" +
                        "Введите количество <b>" + sell + "</b>, которое вы хотите обменять?");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Назад").setCallbackData("back=2"));
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                isTransaction = true;
            } catch (Exception e) {
                e.printStackTrace();
                isTransaction = false;
            }
        }
        return isTransaction;
    }

    public void yesCreate(SendMessage message, Converter converter) {
        message.setText(converter.getMessage());
        InlineKeyboardMarkup markupInline;
        List<List<InlineKeyboardButton>> rowsInline;
        List<InlineKeyboardButton> rowInline;
        markupInline = new InlineKeyboardMarkup();
        rowsInline = new ArrayList<>();
        rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Я ОПЛАТИЛ").setCallbackData("payment"));
        rowsInline.add(rowInline);
        rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Назад").setCallbackData("back=4"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
    }

    public void createSellWallet(SendMessage message, String sell) {
        message.setText("Введите ваш кошелек <b>" + sell + "</b>. ( Оплата должна производиться именно с указаного кошелька!)");
    }

    public void createBuyWallet(SendMessage message, String buy) {
        message.setText("Введите ваш кошелек <b>" + buy + "</b>.\n" +
                "На этот кошелек вам поступят денежные средства!");
    }
}
