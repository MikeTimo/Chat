package client;

import server.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotClient extends Client {
    public class BotSocketThread extends SocketThread {
        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            SimpleDateFormat simpleDateFormat;
            if(message != null & message.contains(":")){
                String[] messagesSplit = message.split(": ");
                if (messagesSplit[1].equals("дата")) {
                    simpleDateFormat = new SimpleDateFormat("d.MM.YYYY");
                    String answerMessage = "Информация для " + messagesSplit[0] + ": " + simpleDateFormat.format(Calendar.getInstance().getTime());
                    sendTextMessage(answerMessage);
                }else if (messagesSplit[1].equals("день")) {
                    simpleDateFormat = new SimpleDateFormat("d");
                    String answerMessage = "Информация для " + messagesSplit[0] + ": " + simpleDateFormat.format(Calendar.getInstance().getTime());
                    sendTextMessage(answerMessage);
                }else if (messagesSplit[1].equals("месяц")) {
                    simpleDateFormat = new SimpleDateFormat("MMMM");
                    String answerMessage = "Информация для " + messagesSplit[0] + ": " + simpleDateFormat.format(Calendar.getInstance().getTime());
                    sendTextMessage(answerMessage);
                }else if (messagesSplit[1].equals("год")) {
                    simpleDateFormat = new SimpleDateFormat("YYYY");
                    String answerMessage = "Информация для " + messagesSplit[0] + ": " + simpleDateFormat.format(Calendar.getInstance().getTime());
                    sendTextMessage(answerMessage);
                }else if (messagesSplit[1].equals("время")) {
                    simpleDateFormat = new SimpleDateFormat("H:mm:ss");
                    String answerMessage = "Информация для " + messagesSplit[0] + ": " + simpleDateFormat.format(Calendar.getInstance().getTime());
                    sendTextMessage(answerMessage);
                }else if (messagesSplit[1].equals("час")) {
                    simpleDateFormat = new SimpleDateFormat("H");
                    String answerMessage = "Информация для " + messagesSplit[0] + ": " + simpleDateFormat.format(Calendar.getInstance().getTime());
                    sendTextMessage(answerMessage);
                }else if (messagesSplit[1].equals("минуты")) {
                    simpleDateFormat = new SimpleDateFormat("m");
                    String answerMessage = "Информация для " + messagesSplit[0] + ": " + simpleDateFormat.format(Calendar.getInstance().getTime());
                    sendTextMessage(answerMessage);
                }else if (messagesSplit[1].equals("секунды")) {
                    simpleDateFormat = new SimpleDateFormat("s");
                    String answerMessage = "Информация для " + messagesSplit[0] + ": " + simpleDateFormat.format(Calendar.getInstance().getTime());
                    sendTextMessage(answerMessage);
                }else {
                }
            }
        }

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }
    }

    @Override
    protected String getUserName() {
        int x = (int) (Math.random()* 100);
        String boteName ="date_bot_" + x;
        return boteName;
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
