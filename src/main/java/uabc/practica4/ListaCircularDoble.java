package uabc.practica4;

import org.json.JSONObject;

public class ListaCircularDoble {
    private Nodo head = null;
    private Nodo tail = null;
    private int size = 0;

    // Add a node to the end of the list
    public void add(JSONObject data) {
        Nodo newNode = new Nodo();
        newNode.info = data;

        if (head == null) {
            head = newNode;
            tail = newNode;
            head.next = head;
            head.prev = head;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
            tail = newNode;
        }
        size++;
    }


    public Nodo getNext(Nodo current) {
        return current.next;
    }

    public Nodo getPrev(Nodo current) {
        return current.prev;
    }

    public Nodo getHead() {
        return head;
    }
    
    // Remove a node at a particular index
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Nodo current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        if (current == head) {
            tail.next = current.next;
            current.next.prev = tail;
            head = current.next;
        } else if (current == tail) {
            current.prev.next = head;
            head.prev = current.prev;
            tail = current.prev;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }

        size--;
    }

    // Get data at a particular index
    public JSONObject get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Nodo current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.info;
    }

    // Get size of the list
    public int size() {
        return size;
    }
    
    // Clear the list
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
    
    // Other necessary methods can be added as needed
}
