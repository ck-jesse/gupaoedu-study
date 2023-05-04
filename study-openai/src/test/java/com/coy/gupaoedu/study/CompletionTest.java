package com.coy.gupaoedu.study;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionChunk;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author chenck
 * @date 2023/5/4 11:56
 */
public class CompletionTest {

    String openAiKey = "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";

    OpenAiService service = new OpenAiService(openAiKey);

    @Test
    void createCompletion() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")//要使用的模型的ID
                .prompt("Somebody once told me the world is gonna roll me")//待补充的语句
                .echo(true)
                .n(5)//为每个提示生成多少完成结果
                .maxTokens(50)
                .user("testing")
                .logitBias(new HashMap<>())
                .logprobs(5)
                .build();

        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        assertEquals(5, choices.size());
        assertNotNull(choices.get(0).getLogprobs());
        String text = choices.get(0).getText();
        System.out.println("补全结果=" + text);
    }

    @Test
    void streamCompletion() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .n(1)
                .maxTokens(25)
                .user("testing")
                .logitBias(new HashMap<>())
                .logprobs(5)
                .stream(true)
                .build();

        List<CompletionChunk> chunks = new ArrayList<>();
        service.streamCompletion(completionRequest).blockingForEach(chunks::add);
        assertTrue(chunks.size() > 0);
        assertNotNull(chunks.get(0).getChoices().get(0));
        System.out.println(chunks.get(0).getChoices().get(0).getText());
    }
}
