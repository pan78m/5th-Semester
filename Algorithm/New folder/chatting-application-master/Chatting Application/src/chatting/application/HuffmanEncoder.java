package chatting.application;

import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanEncoder {
    private HashMap<Character, String> encodingMap;
    private HashMap<String, Character> decodingMap;

    HuffmanEncoder() {
        encodingMap = new HashMap<>();
        decodingMap = new HashMap<>();
    }

    String compress(String message) {
        // Build Huffman tree and encoding map
        buildEncodingMap(message);

        // Compress the message
        StringBuilder compressedMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            compressedMessage.append(encodingMap.get(c));
        }
        return compressedMessage.toString();
    }

    String decompress(String compressedMessage) {
        // Build decoding map using encoding map
        buildDecodingMap();

        // Decompress the message
        StringBuilder decompressedMessage = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();
        for (char c : compressedMessage.toCharArray()) {
            currentCode.append(c);
            if (decodingMap.containsKey(currentCode.toString())) {
                decompressedMessage.append(decodingMap.get(currentCode.toString()));
                currentCode.setLength(0); // Clear currentCode
            }
        }
        return decompressedMessage.toString();
    }

    private void buildEncodingMap(String message) {
        // Calculate frequency of each character in the message
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : message.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Build Huffman tree
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (char c : frequencyMap.keySet()) {
            pq.offer(new HuffmanNode(c, frequencyMap.get(c)));
        }

        while (pq.size() > 1) {
            HuffmanNode leftChild = pq.poll();
            HuffmanNode rightChild = pq.poll();
            HuffmanNode parent = new HuffmanNode('\0', leftChild.frequency + rightChild.frequency);
            parent.left = leftChild;
            parent.right = rightChild;
            pq.offer(parent);
        }

        // Generate encoding map using the Huffman tree
        generateEncodingMap(pq.poll(), new StringBuilder());
    }

    private void generateEncodingMap(HuffmanNode node, StringBuilder code) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            encodingMap.put(node.character, code.toString());
            decodingMap.put(code.toString(), node.character);
            return;
        }

        code.append('0');
        generateEncodingMap(node.left, code);
        code.setLength(code.length() - 1);

        code.append('1');
        generateEncodingMap(node.right, code);
        code.setLength(code.length() - 1);
    }

    private void buildDecodingMap() {
        // Generate decoding map from encoding map
        for (Character c : encodingMap.keySet()) {
            decodingMap.put(encodingMap.get(c), c);
        }
    }

    private static class HuffmanNode implements Comparable<HuffmanNode> {
        char character;
        int frequency;
        HuffmanNode left, right;

        HuffmanNode(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }
}
