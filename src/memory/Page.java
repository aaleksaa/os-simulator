package memory;

import java.util.ArrayList;
import java.util.List;

public class Page {
    public static final int PAGE_SIZE = 16;
    private int totalSize;
    private final List<String> content;

    public Page() {
        this.content = new ArrayList<>();
    }

    public void add(String input) {
        content.add(input);
        totalSize += PAGE_SIZE;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public List<String> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
