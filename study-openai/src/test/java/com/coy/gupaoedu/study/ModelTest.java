package com.coy.gupaoedu.study;

import com.theokanning.openai.model.Model;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author chenck
 * @date 2023/5/4 12:03
 */
public class ModelTest {

    String openAiKey = "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";

    OpenAiService service = new OpenAiService(openAiKey);

    @Test
    void listModels() {
        List<Model> models = service.listModels();

        System.out.println(models);
        assertFalse(models.isEmpty());
    }

    @Test
    void getModel() {
        Model ada = service.getModel("ada");

        assertEquals("ada", ada.id);
        assertEquals("openai", ada.ownedBy);
        assertFalse(ada.permission.isEmpty());
    }
}
