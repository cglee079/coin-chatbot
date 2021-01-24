package com.podo.coinchatbot.app.telegram;

import com.podo.coinchatbot.app.job.TargetAlarmEachTargetContext;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.app.telegram.exception.TelegramApiRuntimeException;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramMessageSender extends DefaultAbsSender {

    private final Coin coin;
    private final String botToken;

    public TelegramMessageSender(Coin coin, String botToken) {
        super(ApiContext.getInstance(DefaultBotOptions.class));
        this.coin = coin;
        this.botToken = botToken;
    }

    public Coin getCoin() {
        return coin;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendMessage(SendMessageVo sendmessageVo) {
        MessageContext.putSendMessage(sendmessageVo.getMessage());
        send(sendmessageVo);
    }

    public void sendTargetAlarm(SendMessageVo sendMessageVo){
        TargetAlarmEachTargetContext.putSendMessage(sendMessageVo.getMessage());
        send(sendMessageVo);
    }

    private void send(SendMessageVo sendmessageVo) {
        final SendMessage sendMessage = new SendMessage(sendmessageVo.getChatId(), sendmessageVo.getMessage());

        sendMessage.setReplyMarkup(sendmessageVo.getKeyboard());
        sendMessage.setReplyToMessageId(sendmessageVo.getMessageId());
        sendMessage.enableHtml(true);
        sendMessage.enableMarkdown(false);


        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(sendmessageVo.getChatId(), sendmessageVo.getMessage());
        }
    }

//
//    /* 목표가 알림 */

//
//    /* 시간 알림 */
//    public void sendTimelyMessage(List<ClientVo> clients, Market marketId, TimelyInfoVo coinCurrent, TimelyInfoVo coinBefore) {
//        ClientVo client = null;
//
//        JSONObject coinCurrentMoney = null;
//        JSONObject coinBeforeMoney = null;
//        if (inBtcs.get(marketId)) {
//            coinCurrentMoney = coinManager.getMoney(coinCurrent, marketId);
//            coinBeforeMoney = coinManager.getMoney(coinBefore, marketId);
//        }
//
//        int clientLength = clients.size();
//        for (int i = 0; i < clientLength; i++) {
//            client = clients.get(i);
//            String msg = msgMaker.msgSendTimelyMessage(client, coinCurrent, coinBefore, coinCurrentMoney, coinBeforeMoney);
//            sendMessage(client.getUserId(), null, msg, null);
//        }
//    }
//
//    /* 일일 알림 */
//    public void sendDailyMessage(List<ClientVo> clients, Market marketId, TimelyInfoVo coinCurrent, TimelyInfoVo coinBefore) {
//        ClientVo client = null;
//        int clientLength = clients.size();
//
//        JSONObject coinCurrentMoney = null;
//        JSONObject coinBeforeMoney = null;
//        if (inBtcs.get(marketId)) {
//            coinCurrentMoney = coinManager.getMoney(coinCurrent, marketId);
//            coinBeforeMoney = coinManager.getMoney(coinBefore, marketId);
//        }
//
//        String msg = null;
//        for (int i = 0; i < clientLength; i++) {
//            client = clients.get(i);
//            msg = msgMaker.msgSendDailyMessage(client, coinCurrent, coinBefore, coinCurrentMoney, coinBeforeMoney);
//            sendMessage(client.getUserId(), null, msg, null);
//
//            if (client.getCoinCnt() != null && client.getInvest() != null) {
//                sendMessage(client.getUserId(), null, calcResult(client, coinCurrent.getLast()), null);
//            }
//        }
//    }
}
