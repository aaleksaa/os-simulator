package memory;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private int size;
    private List<String> content;

    public Page(List<String> content) {
        this.size = content.size() * 16;
        this.content = content;
    }

    public Page() {
        this.content = new ArrayList<>();
    }

    public void add(String input) {
        content.add(input);
        size += 16;
    }

    public int getSize() {
        return size;
    }

    public List<String> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
