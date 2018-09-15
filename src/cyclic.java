// cyclic
// Jerred Shepherd

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class cyclic {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("cyclic.in");
        File outputFile = new File("cyclic.out");

        Scanner scanner = new Scanner(inputFile);
        PrintWriter printWriter = new PrintWriter(outputFile);

        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.equals("00")) {
                break;
            }

            int[] values = line.chars()
                    .map(Character::getNumericValue)
                    .toArray();

//            System.out.println(Arrays.toString(values));

            int length = values.length;
            Node head = null;
            Node last = null;
            for (int value : values) {
                Node node = new Node(value, last);
                last = node;
                head = node;
            }

            Node n = head;
            while (n != null) {
                n.head = head;
                n = n.next;
            }

//            System.out.println(nodeToString(head));

            boolean isCyclic = true;
            Node multipliedHead;
            for (int i = 1; i < length + 1; i++) {
                multipliedHead = head.multiply(i, 0);
                System.out.println(String.format("%s x %s = %s", nodeToString(head), i, nodeToString(multipliedHead)));

                boolean canCycle = false;

                Node h = head;
                Node mh = multipliedHead;
                while (h != null) {
                    if (h.isCyclic(mh, 1, length)) {
                        canCycle = true;
                        break;
                    }
                    h = h.next;
                }

                if (!canCycle) {
                    isCyclic = false;
                    break;
                }
            }

            String output;
            if (isCyclic) {
                output = String.format("%s is cyclic", nodeToString(head));
            } else {
                output = String.format("%s is not cyclic", nodeToString(head));
            }
            printWriter.println(output);
            System.out.println(output);
        }
        printWriter.close();
    }

    private static String nodeToString(Node n) {
        return new StringBuilder(n.toString()).reverse().toString();
    }

    private static class Node {
        int value;
        Node next;
        Node head;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
            this.head = null;
        }

        public Node(int value, Node next, Node head) {
            this.value = value;
            this.next = next;
            this.head = head;
        }

        boolean isCyclic(Node curr, int i, int size) {
            System.out.println(String.format("Checking %s %s %s", this.value, curr.value, i));
            if (i == size) {
                return true;
            } else {
                if (value == curr.value) {
                    if (next != null) {
                        return next.isCyclic(curr.next, i + 1, size);
                    } else {
                        System.out.println("End of list; Wrapping to head");
                        return head.isCyclic(curr.head, i + 1, size);
                    }
                } else {
                    return false;
                }
            }
        }

        Node multiply(int n, int carry) {
            int newValue = this.value * n;
            newValue += carry;
            carry = 0;
//            System.out.println(String.format("%s x %s = %s", n, this.value, newValue));
            if (newValue > 9) {
                carry = newValue / 10;
                newValue = newValue % 10;
            }
            if (this.next == null) {
                if (carry != 0) {
                    this.next = new Node(1, null, this.head);
                    return this.next;
                } else {
                    return new Node(newValue, null, this.head);
                }
            }
            return new Node(newValue, next.multiply(n, carry), this.head);
        }

        @Override
        public String toString() {
            return value + "" + (next != null ? next.toString() : "");
        }
    }
}

