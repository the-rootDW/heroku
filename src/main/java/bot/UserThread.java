package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class UserThread extends Thread {
    private CommandHandler commandHandler;
    private TelegramLongPollingBot bot;
    private boolean needSleep = false;
    private SendMessage message;

    public UserThread(Update update, TelegramLongPollingBot myBot, Long id) {
        this.setName(id.toString());
        this.start();
        bot = myBot;
        commandHandler = new CommandHandler();
        handle(update);
    }

    public void handle(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            message = commandHandler.processText(update);
        } else if (update.hasCallbackQuery()) {
            message = commandHandler.processQuery(update);
        } else {
            message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("Бот Вас не понимает!");
        }
        createMessage(message);
    }

    private void createMessage(SendMessage message) {
        try {
            bot.execute(message); // Call method to send the message
            if (message.getText().equals("Проверяется оплата…"))
                needSleep = true;

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            if (needSleep == true) {
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                createMessage(message.setText("Ваш платеж не найден! Повторите попытку.\n" +
                        "Для того чтобы начать нажмите /start"));
                needSleep = false;
                break;
            } else
                System.out.println(this.getName() + " is running!");
        }
    }
}
