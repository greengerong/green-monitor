package green.monitor;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class ContextLogger {

    private Writer writer;

    public ContextLogger(Writer writer) {

        this.writer = writer;
    }

    public ContextLogger() {
        this(new StringWriter());
    }

    public Writer getWriter() {
        return writer;
    }

    public void append(String text) {
        try {
            writer.write(text);
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    public void append(String text, Object... args) {
        append(String.format(text, args));
    }

    @Override
    public String toString() {
        return writer.toString();
    }
}
