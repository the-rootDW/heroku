package bot;

import converter.Converter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


public class CommandHandler {
    private static Converter converter = new Converter();
    private static KeyboardCreator creator = new KeyboardCreator();
    /**
     * COMMANDS
     **/
    private static final String START = "/start";
    /**
     * TEMPLATES
     **/
    private static String NAME = "Netex24";
    private static String SITE = "https://www.netex24.net";
    private static String SUPPORT = "@ilusha_a";
    private static String NAME_BOT = "Netex24_BOT";
    private static String FEEDBACKS = "https://www.bestchange.ru/netex24-exchanger.html";

    /**
     * TEXT
     **/
    private static String START_MESSAGE = "<b>/name/</b> предназначен для тех, кто хочет быстро, безопасно и по выгодному курсу сделать ввод/вывод или обмен  электронных валют. \n" +
            "В Telegram обменивать деньги намного удобнее и быстрее, Вы только попробуйте!\n" +
            "А также у нас самый выгодный курс, проверьте!\n" +
            "Работаем 24/7 !\n" +
            "<b>Наш сайт работает с 2015 : /site/</b>\n" +
            "\n" +
            " Telegram-Бот содержит весь набор необходимых функций для удобной и безопасной конвертации наиболее распространенных видов электронных денег.\n" +
            "\n" +
            "Удобства и доверие клиентов является для нас первоочередной задачей.\n" +
            "Наша служба техподдержки готова оказать вам максимальную помощь и консультации по интересующим вопросам ( Онлайн 24/7)  - /name_support/\n" +
            "\n" +
            "За время работы мы приобрели репутацию проверенного партнера и делаем все возможное,чтобы ваши впечатления от нашего сервиса были только благоприятными. \n" +
            "\n" +
            "<b>Отзывы наших клиентов : /name_channel/</b>\n" +
            "\n" +
            "<b>На сайте действует программа лояльности :</b>\n" +
            "Накопительная скидка и партнерская программа, воспользовавшись преимуществами которых,\n" +
            "вы сможете совершать обмен электронных валют на более выгодных условиях. \n" +
            "Подробнее можно узнать, нажав на кнопку «Реферальная система».\n" +
            "\n" +
            "/name_bot/ пользуется  13023 человек. \n" +
            "\n" +
            "Для того чтобы воспользоваться нашими услугами, нажмите  кнопку «Обменять»";

    private static String REFERAL = "Ваш реферальный код : <b>OEKFDLK</b>\n" +
            "\n" +
            "Приглашено рефералов : <b>0</b>";

    /**
     * VALUES
     **/
    private String sell;
    private String buy;


    /**
     * FLAGS
     **/
    private boolean isTransaction;
    private int levelOfTransaction;
    private int stage;

    /**
     * METHODS
     **/

    public CommandHandler() {
        START_MESSAGE = START_MESSAGE.replaceAll("/name/", NAME);
        START_MESSAGE = START_MESSAGE.replaceAll("/site/", SITE);
        START_MESSAGE = START_MESSAGE.replaceAll("/name_support/", SUPPORT);
        START_MESSAGE = START_MESSAGE.replaceAll("/name_bot/", NAME_BOT);
        START_MESSAGE = START_MESSAGE.replaceAll("/name_channel/", FEEDBACKS);
    }

    public SendMessage processText(Update update) {
        String text = update.getMessage().getText();


        SendMessage message = new SendMessage().setText("Бот вас не понимает!") // Create a message object object
                .setChatId(update.getMessage().getChatId());


        InlineKeyboardMarkup markupInline;
        List<List<InlineKeyboardButton>> rowsInline;
        List<InlineKeyboardButton> rowInline;

        switch (text) {
            case (START):
                isTransaction = false;
                levelOfTransaction = 0;
                stage = -1;
                message.setText(START_MESSAGE);
                markupInline = new InlineKeyboardMarkup();
                rowsInline = new ArrayList<>();
                rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Реферальная система").setCallbackData("reff"));
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Обменять").setCallbackData("trade"));
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                break;
            default:
                switch (levelOfTransaction) {
                    case 1:
                        creator.createBuyWallet(message, buy);
                        levelOfTransaction++;
                        break;
                    case 2:
                        creator.yesCreate(message, converter);
                        levelOfTransaction++;
                        break;
                }

                if (isTransaction == true) {
                    try {
                        double value = Double.parseDouble(text);
                        isTransaction = false;
                        message.setText(converter.makeAnswer(value));
                        markupInline = new InlineKeyboardMarkup();
                        rowsInline = new ArrayList<>();
                        rowInline = new ArrayList<>();
                        rowInline.add(new InlineKeyboardButton().setText("Да").setCallbackData("yes"));
                        rowsInline.add(rowInline);
                        rowInline = new ArrayList<>();
                        rowInline.add(new InlineKeyboardButton().setText("Назад").setCallbackData("back=3"));
                        rowsInline.add(rowInline);
                        markupInline.setKeyboard(rowsInline);
                        message.setReplyMarkup(markupInline);
                        stage++;
                    } catch (Exception e) {
                        message.setText("Вы ввели запрещенный символ,повторите попытку!");
                    }
                }

        }

        return message.setParseMode("HTML");
    }

    public SendMessage processQuery(Update update) {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(update.getCallbackQuery().getMessage().getChatId());
        String[] call_data = update.getCallbackQuery().getData().split("=");

        switch (call_data[0]) {
            case ("back"):
                if (Integer.parseInt(call_data[1]) == stage) {
                    switch (stage) {
                        case 1:
                            creator.createSellMenu(message);
                            stage--;
                            break;
                        case 2:
                            creator.createBuyMenu(message, sell);
                            stage--;
                            isTransaction = false;
                            break;
                        case 3:
                            isTransaction = true;
                            isTransaction = creator.buyCreate(message, sell, buy, isTransaction, converter);
                            stage--;
                            break;
                        case 4:
                            levelOfTransaction = 1;
                            creator.createSellWallet(message, sell);
                            stage--;
                            break;
                    }
                }
                break;
            case ("trade"):
                if (stage == -1) {
                    creator.createSellMenu(message);
                    stage++;
                }
                break;
            case ("reff"):
                message.setText(REFERAL);
                break;
            case ("yes"):
                if (stage == 3) {
                    levelOfTransaction++;
                    stage++;
                    creator.createSellWallet(message, sell);
                }
                break;
            case ("Sell"):
                if (stage == 0) {
                    sell = call_data[1];
                    stage++;
                    creator.createBuyMenu(message, sell);
                }
                break;
            case ("Buy"):
                if (stage == 1) {
                    buy = call_data[1];
                    isTransaction = creator.buyCreate(message, sell, buy, isTransaction, converter);
                    if (isTransaction == true)
                        stage++;
                }
                break;
            case ("payment"):
                message.setText("Проверяется оплата…");
                stage++;
                break;
        }

        return message.setParseMode("HTML");
    }
}

