package com.evan.chat.domain.usecase;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.Chat.ChatRepository;
import com.evan.chat.data.source.model.Chat;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/19
 * Time: 1:41
 */
public class SendMessage extends UseCase<SendMessage.RequestValues, SendMessage.ResponseValue> {

    private  final ChatRepository chatRepository;

    public SendMessage(@NonNull ChatRepository chatRepository) {
        this.chatRepository = checkNotNull(chatRepository, "chatRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Chat chat = requestValues.getChat();
        chatRepository.saveChat(chat);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final Chat chat;

        public RequestValues(@NonNull Chat chat) {
            this.chat = checkNotNull(chat,"chat cannot be null!");
        }

        public Chat getChat() {
            return chat;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
