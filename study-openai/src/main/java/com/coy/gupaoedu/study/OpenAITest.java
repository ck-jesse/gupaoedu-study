package com.coy.gupaoedu.study;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

/**
 * https://platform.openai.com/docs/guides/rate-limits
 *
 * https://platform.openai.com/docs/api-reference/introduction
 *
 * @author chenck
 * @date 2023/5/4 14:09
 */
public class OpenAITest {
    public static void main(String[] args) {
        String openAiKey = "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";
        OpenAiService service = new OpenAiService(openAiKey);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .model("ada")
                .echo(true)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }
}
