package de.eldritch.Anura.util.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.LinkedList;

class FileScanner {
    private final File file;

    public FileScanner(@NotNull File file) throws IllegalConfigException, IOException {
        this.file = file;
    }

    private void scan() throws IOException, IllegalConfigException {
        LinkedList<String> lines = new LinkedList<>(Files.readAllLines(file.toPath()));
        LinkedList<Node> nodes = new LinkedList<>();

        // special case: file is empty
        if (lines.isEmpty()) return;

        while (!lines.isEmpty()) {
            String line = lines.removeFirst();

            // matches comments
            if (line.stripIndent().matches("^#")) {
                // ignore
                continue;
            }

            // matches keys with value in the same line
            if (line.stripIndent().matches("^([^:]+\\.)*([^{.:}]+:)\\s*[^\\s]+")) {
                // TODO: assign value
                continue;
            }

            // matches keys without value (in the same line)
            if (line.stripIndent().matches("^([^:]+\\.)*([^{.:}]+:)\\s*$")) {
                String path = line.stripIndent().substring(0, line.stripIndent().indexOf(":") - 1);
                path = path.substring(path.lastIndexOf("."), path.length() - 1);

                // check whether the next line is indented lower (further left) or equal -> null value
                String nextLine = lines.peekFirst();
                if (nextLine != null && line.stripIndent().length() <= nextLine.stripIndent().length()) {
                    // TODO
                    continue;
                }

                continue;
            }

            // line cannot be processed
            throw new IllegalConfigException("Encountered unexpected line \"" + line + "\" when parsing.");
        }
    }

    private class Node {
        String key;
        Object value;

        Node parent;
        HashSet<Node> children = new HashSet<>();

        public Node(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public void setParent(Node parent) {
            parent.children.add(this);
            this.parent = parent;
        }
    }
}
