package net.netau.vasyoid;

import java.util.*;

public class SmartList<E> extends AbstractList<E> implements List<E> {

    private static final int SMALL_SIZE = 5;
    private int listSize;
    private Object data;

    public SmartList() {
        listSize = 0;
        data = null;
    }

    public SmartList(Collection<? extends E> collection) {
        listSize = collection.size();
        if (listSize > SMALL_SIZE) {
            data = new ArrayList<>(collection);
        } else if (listSize > 1) {
            data = collection.toArray();
        } else if (listSize == 1) {
            data = collection.toArray()[0];
        } else {
            data = null;
        }
    }

    @Override
    public int size() {
        return listSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        E result = get(index);
        if (listSize <= 1) {
            data = element;
        } else if (listSize <= SMALL_SIZE) {
            ((E[]) data)[index] = element;
        } else {
            ((ArrayList<E>) data).set(index, element);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (listSize == 0 || index < 0 || index >= listSize) {
            throw new IndexOutOfBoundsException();
        }
        E result;
        if (listSize <= 1) {
            result = (E) data;
        } else if (listSize <= SMALL_SIZE) {
            result = ((E[]) data)[index];
        } else {
            result = ((ArrayList<E>) data).get(index);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E element) {
        if (listSize == 0) {
            data = element;
        } else if (listSize == 1) {
            data = new Object[]{data, element, null, null, null};
        } else if (listSize < SMALL_SIZE) {
            ((E[])data)[listSize] = element;
        } else if (listSize == SMALL_SIZE) {
            List newData = new ArrayList();
            newData.addAll(Arrays.asList((E[]) data));
            newData.add(element);
            data = newData;
        } else {
            ((ArrayList) data).add(element);
        }
        listSize++;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        E result = get(index);
        if (listSize == 1) {
            data = null;
        } else if (listSize == 2) {
            data = ((E[]) data)[1 - index];
        } else if (listSize <= SMALL_SIZE) {
            for (int i = index; i < listSize - 1; i++) {
                ((E[]) data)[i] = ((E[]) data)[i + 1];
            }
        } else if (listSize == SMALL_SIZE + 1) {
            ((ArrayList<E>) data).remove(index);
            data = ((ArrayList<E>) data).toArray();
        } else {
            ((ArrayList<E>) data).remove(index);
        }
        listSize--;
        return result;
    }
}
