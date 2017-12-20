package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A list optimized for small numbers of elements
 * @param <E> elements type
 */
public class SmartList<E> extends AbstractList<E> implements List<E> {

    private static final int SMALL_SIZE = 5;
    private int listSize;
    private Object data;

    /**
     * Empty constructor
     */
    public SmartList() {
        listSize = 0;
        data = null;
    }

    /**
     * Empty which takes a collection and creates a list of its elements
     */
    public SmartList(@NotNull Collection<? extends E> collection) {
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

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     */
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public E set(int index, @Nullable E element) {
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

    /**
     * Returns the element at the specified position in this list.
     * @param index index of the element to return
     * @return the element at the specified position in this list
     */
    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public E get(int index) {
        checkIndex(index, true);
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

    /**
     * Appends the specified element to the end of this list.
     * @param element element to be appended to this list
     * @return true
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean add(@Nullable E element) {
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

    private void checkIndex(int index, boolean checkSize) {
        if ((checkSize && listSize == 0)
                || index < 0 || index >= listSize) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent elements
     * to the right (adds one to their indices).
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @SuppressWarnings("unchecked")
    @Override
    public void add(int index, @Nullable E element) {
        checkIndex(index, false);
        if (listSize == 0) {
            data = element;
        } else if (listSize == 1) {
            if (index == 0) {
                data = new Object[]{data, element, null, null, null};
            } else {
                data = new Object[]{element, data, null, null, null};
            }
        } else if (listSize < SMALL_SIZE) {
            for (int i = listSize - 1; i > index; i--) {
                ((E[])data)[i] = ((E[])data)[i - 1];
            }
            ((E[])data)[index] = element;
        } else if (listSize == SMALL_SIZE) {
            List newData = new ArrayList();
            newData.addAll(Arrays.asList((E[]) data));
            newData.add(index, element);
            data = newData;
        } else {
            ((ArrayList) data).add(index, element);
        }
        listSize++;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     */
    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public E remove(int index) {
        E result = get(index);
        if (listSize == 1) {
            data = null;
        } else if (listSize == 2) {
            data = ((E[]) data)[1 - index];
        } else if (listSize <= SMALL_SIZE) {
            System.arraycopy(((E[]) data), index + 1, ((E[]) data), index, listSize - 1 - index);
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
