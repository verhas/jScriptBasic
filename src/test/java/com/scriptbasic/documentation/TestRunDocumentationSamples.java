package com.scriptbasic.documentation;

import com.scriptbasic.Engine;
import com.scriptbasic.api.ScriptBasicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestRunDocumentationSamples {

    @Test
    @DisplayName("Run all the basic code in the resources/documentation directory and create the output")
    void testDocumentationCodeExecution() throws Exception {
        final var documentationDirectory = new File("src/test/resources/documentation").getPath();
        Files.list(Paths.get(documentationDirectory)).filter(p -> p.toFile().getAbsolutePath().endsWith(".bas"))
                .forEach(
                        p -> {
                            final var fn = p.toFile().getAbsolutePath();
                            final var out = Paths.get(fn.replaceAll(".bas$", ".out"));
                            final var err = Paths.get(fn.replaceAll(".bas$", ".err"));
                            final var engine = new Engine();
                            try {
                                final var sw = new StringWriter();
                                engine.setOutput(sw);
                                final var input = p.toFile();
                                engine.eval(input);
                                sw.close();
                                Files.writeString(out, sw.toString(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
                            } catch (IOException ignore) {
                                // should not happen with a string writer
                            } catch (ScriptBasicException e) {
                                try {
                                    Files.writeString(err, e.toString(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                );
    }

}
