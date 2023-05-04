package com.coy.gupaoedu.study;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author chenck
 * @date 2023/5/4 12:00
 */
public class ChatCompletionTest {

    String openAiKey = "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";

    OpenAiService service = new OpenAiService(openAiKey);

    // 保存上下文
    List<ChatMessage> messages = new ArrayList<>();
    // 连续对话
    @Test
    public void createChatCompletion() {
        System.out.println("OpenAI Chat: Send a message");
        Scanner in = new Scanner(System.in);
        String input = in.next();

        while (!"exit".equals(input)) {
            ChatMessage systemMessage = new ChatMessage(ChatMessageRole.USER.value(), input);
            messages.add(systemMessage);

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo-0301")
                    .messages(messages)
                    .user("testing")
                    .maxTokens(50)
                    .temperature(1.0)
                    .logitBias(new HashMap<>())
                    .build();
            List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
            System.out.println(choices.get(0).getMessage().getContent());

            ChatMessage context = new ChatMessage(choices.get(0).getMessage().getRole(), choices.get(0).getMessage().getContent());
            messages.add(context);
            System.out.println("OpenAI Chat: Send a message");
            in = new Scanner(System.in);
            input = in.next();
        }

    }

    // 单轮对话
    @Test
    void createChatCompletion1() {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "请对中国做一下描述");
        final ChatMessage systemMessage1 = new ChatMessage(ChatMessageRole.SYSTEM.value(), "继续");
        messages.add(systemMessage);
        messages.add(systemMessage1);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(5)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
        assertEquals(5, choices.size());
        for (ChatCompletionChoice choice : choices) {
            System.out.println(choice.getIndex() + " " + choice.getMessage().getRole() + " " + choice.getMessage().getContent());
        }
    }

    @Test
    void streamChatCompletion() {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model("gpt-3.5-turbo").messages(messages).n(1).maxTokens(50).logitBias(new HashMap<>()).stream(true).build();

        List<ChatCompletionChunk> chunks = new ArrayList<>();
        service.streamChatCompletion(chatCompletionRequest).blockingForEach(chunks::add);
        assertTrue(chunks.size() > 0);
        assertNotNull(chunks.get(0).getChoices().get(0));
    }
}
