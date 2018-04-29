package bot;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    private static String BOT_TOKEN = "578626150:AAEco0VJhjLCbEcVk-fPaB4j5uGQGgBaTKs";	//BOT_TOKEN
    private static String BOT_USERNAME = "NetEx24 Official";	//BOT_USERNAME
    private static List<UserThread> threadList = new ArrayList<>();

    public void onUpdateReceived(Update update) {
        Long id;
        boolean isFound = false;
        if (update.hasMessage() && update.getMessage().hasText()) {
            id = update.getMessage().getChatId();
            for (UserThread thread : threadList)

                if (thread.getName().equals(id.toString())) {
                    if (thread.isAlive())
                        thread.handle(update);
                    else {
                        threadList.remove(thread);
                        threadList.add(new UserThread(update, this, id));
                    }
                    isFound = true;
                }

            if (isFound == false)
                threadList.add(new UserThread(update, this, id));

        } else if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getMessage().getChatId();
            for (UserThread thread : threadList)

                if (thread.getName().equals(id.toString())) {
                    isFound = true;
                    if (thread.isAlive())
                        thread.handle(update);
                    else {
                        threadList.remove(thread);
                        threadList.add(new UserThread(update, this, id));
                    }
                }

            if (isFound == false)
                threadList.add(new UserThread(update, this, id));

        }
    }

    public String getBotUsername() {
        return BOT_USERNAME;
    }


    public String getBotToken() {
        return BOT_TOKEN;
    }
}
