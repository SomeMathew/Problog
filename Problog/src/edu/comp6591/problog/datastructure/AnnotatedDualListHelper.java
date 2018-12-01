package edu.comp6591.problog.datastructure;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.*;

/**
 * This class is used to keep a joined list of two Annotated types of elements.
 * 
 * <p>
 * This class also includes convenience methods to iterate through the list in a
 * free form manner without the constrain of an {@code Iterator}. A Listener is
 * available to be set with {@link #setTypeChangeListener(BiConsumer)} to track
 * a change of type in the elements during iteration.
 * 
 * <p>
 * The primary goal of this class is to control iteration over multiple
 * {@code AnnotatedDualListHelper} when generating a restricted Power set on
 * some positional elements.
 * 
 * <p>
 * The list is ordered in terms of annotated type.
 * <p>
 * <b>Example:</b><br>
 * Given Type A and Type B annotated elements they will be ordered by type A
 * then Type B.
 */

public class AnnotatedDualListHelper<T> {
    public enum Annotation {
        TYPE_A, TYPE_B
    }

    private final List<T> typeAList;
    private final List<T> typeBList;
    private final int combinedSize;

    private int currentIndex;
    private BiConsumer<Annotation, Annotation> changeListener;

    public AnnotatedDualListHelper(ImmutableList<T> typeA, ImmutableList<T> typeB) {
        this.typeAList = checkNotNull(typeA);
        this.typeBList = checkNotNull(typeB);
        this.combinedSize = typeA.size() + typeB.size();
        this.currentIndex = combinedSize > 0 ? 0 : -1;
        this.changeListener = null;
    }

    public int size() {
        return combinedSize;
    }

    public int size(Annotation type) {
        if (type == Annotation.TYPE_A) {
            return typeAList.size();
        } else {
            return typeBList.size();
        }
    }

    /**
     * Returns true if the collection is empty of any types of elements.
     * 
     * @return {@code true} if empty of any types of elements.
     */
    public boolean isEmpty() {
        return combinedSize <= 0;
    }

    /**
     * Returns true if the collection is empty of the given {@code type} of
     * elements.
     * 
     * @return {@code true} if empty of the given type of elements.
     */
    public boolean isEmpty(Annotation type) {
        return size(type) <= 0;
    }

    /**
     * Returns the {@link Annotation} of the element at the given {@code index}.
     * 
     * @param index Index to consider
     * @return The {@link Annotation} of the element at {@code index}.
     */
    public Annotation getAnnotation(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        if (index < typeAList.size()) {
            return Annotation.TYPE_A;
        } else {
            return Annotation.TYPE_B;
        }
    }

    /**
     * Returns the current element pointed to by the iteration.
     * 
     * @return the current element.
     */
    public T getCurrent() {
        if (isEmpty() || currentIndex >= size()) {
            throw new NoSuchElementException();
        }
        return get(currentIndex);
    }

    /**
     * Resets the index iterator to the first element in the list, if it exists.
     * 
     * This method is safe and doesn't throw {@link NoSuchElementException}.
     * 
     * @return {@code true} on success, {@code false} if there is no element in this
     *         list.
     */
    public boolean reset() {
        if (isEmpty()) {
            return false;
        } else {
            setNextIndex(0);
            return true;
        }
    }

    /**
     * Increment to the next index if possible.
     * 
     * This method is safe and doesn't throw {@link NoSuchElementException}
     * 
     * @return {@code true} if successful, {@code false} if we're at the last index.
     */
    public boolean incrementIfHasNext() {
        if (currentIndex + 1 >= size()) {
            return false;
        }
        setNextIndex(currentIndex + 1);
        return true;
    }

    /**
     * Get the {@link Annotation} for the currently pointed to object by the
     * iterator.
     * 
     * @return The {@link Annotation} of the current element
     */
    public Annotation getCurrentAnnotation() {
        if (currentIndex >= size()) {
            throw new NoSuchElementException();
        }

        return getAnnotation(currentIndex);
    }

    /**
     * Retrieve the object at the given {@code index}.
     * 
     * @param index
     * @return The object for the given {@code index}.
     * 
     * @throws IndexOutOfBoundsException
     */
    private T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        if (index < typeAList.size()) {
            return typeAList.get(index);
        } else {
            return typeBList.get(index - typeAList.size());
        }
    }

    /**
     * Sets the index to retrieve with the next call to {@link #getCurrent()} and
     * {@link #getCurrentAnnotation()};
     * 
     * @param index Next index to retrieve.
     * @throws IndexOutOfBoundsException
     */
    private void setNextIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        Annotation oldAnnotation = getAnnotation(currentIndex);
        Annotation newAnnotation = getAnnotation(index);
        currentIndex = index;
        if (changeListener != null && oldAnnotation != newAnnotation) {
            changeListener.accept(oldAnnotation, newAnnotation);
        }
    }

    /**
     * Sets a listener that will be called when a change of annotation is caused by
     * a call to the iterator type methods.
     * 
     * <p>
     * Calls to {@link #incrementIfHasNext()}, {@link #reset()} or {@link #setNextIndex(int)}
     * are able to cause a trigger of this listener.
     * 
     * @param changeListener {@link BiConsumer} with argument (oldAnnotation,
     *                       newAnnotation).
     */
    public void setTypeChangeListener(BiConsumer<Annotation, Annotation> changeListener) {
        this.changeListener = checkNotNull(changeListener);
    }

}
